/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bobazimov.masteryblog.controller;

import com.bobazimov.masteryblog.dto.Post;
import com.bobazimov.masteryblog.dto.User;
import com.bobazimov.masteryblog.service.HomeService;
import com.bobazimov.masteryblog.service.UserService;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author irabob
 */

@Controller
public class ResgisterController {
    
    @Autowired
    UserService service;
    
    @Autowired
    HomeService homeService;
    
    @Autowired
    PasswordEncoder encoder;
    
    Set<ConstraintViolation<User>> violations = new HashSet<>();
    
    final String USER_UPLOAD_DIR = "/users";
    
    @GetMapping("/register")
    public String register(Model model, Integer error){
        List<Post> blogs = homeService.getStaticPosts();
        model.addAttribute("blogs", blogs);
        model.addAttribute("errors", violations);
        if(error != null){
            if(error == 1){
                model.addAttribute("error", "A user with that email address already exists");
            }else if(error == 2){
                model.addAttribute("error", "Username already taken"); 
            }else if(error == 3){
                model.addAttribute("error", "Password cannot be empty"); 
            }
        }
        return "register";
    }
    
    @PostMapping("signup")
    public String signup(User user, @RequestParam("file") MultipartFile file){
        String fileLocation = service.saveImage(file, Long.toString(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)), USER_UPLOAD_DIR);
        User userByEmail = service.getUserByEmail(user.getEmail());
        User userByName = service.getUserByName(user.getUsername());
        if(userByEmail != null){
            return "redirect:/register?id=" + user.getId() + "&error=1";
        }
        if(userByName != null){
            return "redirect:/register?id=" + user.getId() + "&error=2";
        }
        if(user.getPassword().isBlank()){
            return "redirect:/register?id=" + user.getId() + "&error=3";
        }
        user.setEnabled(true);
        user.setRole("ROLE_USER");
        user.setPassword(encoder.encode(user.getPassword()));
        if(file.isEmpty()){
            user.setFileName("/uploads/users/avatar.jpg");
        }else{
            user.setFileName(fileLocation);
        }
        Validator validate = Validation.buildDefaultValidatorFactory().getValidator();
        violations = validate.validate(user);
        if(violations.isEmpty()){
            service.createUser(user);
        }else{
            return "redirect:/register";
        }
        return "redirect:/home";
    }
}
