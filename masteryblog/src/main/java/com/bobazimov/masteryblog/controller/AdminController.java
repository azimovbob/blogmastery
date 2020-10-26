/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bobazimov.masteryblog.controller;

import com.bobazimov.masteryblog.dto.Hashtag;
import com.bobazimov.masteryblog.dto.Post;
import com.bobazimov.masteryblog.dto.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import com.bobazimov.masteryblog.service.AdminService;
import com.bobazimov.masteryblog.service.CommentService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author irabob
 */

@Controller
public class AdminController {
    
    @Autowired
    AdminService service;
    
    @Autowired
    CommentService commService;
    
    @Autowired
    PasswordEncoder encoder;
    
    final String BLOG_UPLOAD_DIR = "/blogs";
    final String USER_UPLOAD_DIR = "/users";
    
    Set<ConstraintViolation<User>> violations = new HashSet<>();
    Set<ConstraintViolation<Post>> postViolations = new HashSet<>();
    @GetMapping("/admin")
    public String displayAdminPage(Model model){
        List<User> users = service.getUsers();
        model.addAttribute("users", users);
        model.addAttribute("errors", violations);
        return "/admin";
    }
    
    @GetMapping("/adminContent")
    public String adminManageContent(Model model){
        List<Post> blogs = service.readAllContetns();
        model.addAttribute("blogs", blogs);
        model.addAttribute("errors", postViolations);
        return "/adminContent";
    }
    
    @PostMapping("/createContent")
    public String createContent(Post post, String tags, String postdate, 
        String expdate, Boolean isaApproved, Boolean isStatic, @RequestParam("file") MultipartFile file){
        
        String fileLocation = service.saveImage(file, Long.toString(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)), BLOG_UPLOAD_DIR);
  
        LocalDate postDate;
        LocalDate expLd;
        try{
            postDate = LocalDate.parse(postdate);
        }catch(Exception ex){
            postDate = LocalDate.now();
        }
        try {
            expLd = LocalDate.parse(expdate);
        } catch (Exception e) {
            expLd = null;
        }
        post.setPostDate(postDate);
        post.setExpDate(expLd);
        post.setFileName(fileLocation);
        if(isaApproved != null){
            post.setIsaApproved(true);
        }else{
            post.setIsaApproved(false);
        }
        if(isStatic != null){
            post.setIsStatic(true);
        }else{
            post.setIsStatic(false);
        }
        Validator validate = Validation.buildDefaultValidatorFactory().getValidator();
        postViolations.clear();
        postViolations = validate.validate(post);
        if(postViolations.isEmpty()){
            service.createPost(post, tags);
        }
        return "redirect:/adminContent";
    }
    
    @GetMapping("/editContent")
    public String editContent(Integer id, Model model){
        Post post = service.getPostById(id);
        String tagStr = "";
        for(Hashtag tag: post.getHashtags()){
            tagStr+="#" + tag.getName(); 
        }
        model.addAttribute("hashtags", tagStr);
        model.addAttribute("errors", postViolations);
        model.addAttribute("blog",post);
        
        return "/editContent";
    }
    
    @PostMapping("editContent")
    public String editContent(Model model, Post post, String tags, String postdate, String expdate, 
            Boolean isaApproved, Boolean isStatic, @RequestParam("file") MultipartFile file){

        String fileLocation = service.updateImage(file, Long.toString(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)), BLOG_UPLOAD_DIR);
        LocalDate postDate;
        LocalDate expLd;
        try{
            postDate = LocalDate.parse(postdate);
        }catch(Exception ex){
            postDate = LocalDate.now();
        }
        try {
            expLd = LocalDate.parse(expdate);
        } catch (Exception e) {
            expLd = null;
        }
        post.setFileName(fileLocation);
        post.setPostDate(postDate);
        post.setExpDate(expLd);
        if(isaApproved != null){
            post.setIsaApproved(true);
        }else{
            post.setIsaApproved(false);
        }
        if(isStatic != null){
            post.setIsStatic(true);
        }else{
            post.setIsStatic(false);
        }
        Validator validate = Validation.buildDefaultValidatorFactory().getValidator();
        postViolations.clear();
        postViolations = validate.validate(post);
        if(postViolations.isEmpty()){
            service.updatePost(post, tags);
            return "redirect:/adminContent";
        }
        else{
            model.addAttribute("hashtags", tags);
            model.addAttribute("blog", post);
            model.addAttribute("errors", postViolations);
            return "editContent";
        }
        
    }
    
    @PostMapping("/deleteBlog")
    public String deleteBlog(Integer id){
        service.deletePost(id);
        return "redirect:/adminContent";
    }
    
    @PostMapping("/addContentEditor")
    public String addContentEditor(User user, @RequestParam("file") MultipartFile file){
        String fileLocation = service.saveImage(file, Long.toString(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)), USER_UPLOAD_DIR);
        user.setEnabled(true);
        user.setRole("ROLE_EDITOR");
        user.setFileName(fileLocation);
        user.setPassword(encoder.encode(user.getPassword()));
        Validator validate = Validation.buildDefaultValidatorFactory().getValidator();
        violations = validate.validate(user);
        if(violations.isEmpty()){
            service.addContetnEditor(user);
        }
        return "redirect:/admin";
    }
    
    @PostMapping("/deleteUser")
    public String deleteUser(Integer id){
        service.deleteUser(id);
        return "redirect:/admin";
    }
    
    @GetMapping("/editUser")
    public String editUserDisplay(Model model, Integer id, Integer error){
        User user = service.getUserById(id);
        model.addAttribute("user", user);
        
        if(error != null){
            if(error == 1){
                model.addAttribute("error", "Password did not match, password was not updated.");
            }else if(error == 2){
                model.addAttribute("error", "Password cannot be blank");
            }
        }
        
        return "editUser";
    }
    
    @PostMapping(value="/editUser")
    public String editUserAction(String[] roleIdList, Boolean enabled, Integer id){
        User user = service.getUserById(id);
        if(enabled != null){
            user.setEnabled(enabled);
        }else{
            user.setEnabled(false);
        }

        service.updateUser(user);
        
        return "redirect:/admin";
    }
    
    @PostMapping("/editPassword")
    public String editPassword(Integer id, String password, String confirmPassword){
        User user = service.getUserById(id);
        if(password.isBlank()){
            return "redirect:/editUser?id=" + id + "&error=2";
        }
        if(password.equals(confirmPassword)){
            user.setPassword(encoder.encode(password));
            service.updateUser(user);
            return "redirect:/admin";
        }else{
            return "redirect:/editUser?id=" + id + "&error=1";
        }
    }
    
    @PostMapping("deleteComment")
    public String deleteComment(Integer id){
        commService.deleteComment(id);
        return "redirect:/adminContent";
    }
}
