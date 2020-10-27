/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bobazimov.masteryblog.controller;

import com.bobazimov.masteryblog.dto.Comment;
import com.bobazimov.masteryblog.dto.Post;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.bobazimov.masteryblog.dto.User;
import com.bobazimov.masteryblog.service.CommentService;
import com.bobazimov.masteryblog.service.HomeService;
import com.bobazimov.masteryblog.service.UserService;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;

/**
 *
 * @author irabob
 */

@Controller
public class HomeController {
    
    @Autowired
    PasswordEncoder encoder;
    
    @Autowired
    HomeService service;
    
    @Autowired
    UserService userService;
    
    @Autowired
    CommentService commentService;
    
    Set<ConstraintViolation<Comment>> violations = new HashSet<>();
    @GetMapping({"/", "home"})
    public String displayContent(Model model){
        List<Post> posts = service.getAllBlogs();
        model.addAttribute("blogs", posts);
        List<Post> staticBlogs = service.getStaticPosts();
        model.addAttribute("staticBlogs", staticBlogs);
        return "home";
    }
    
    @GetMapping("/displayBlog")
    public String displayBlog(Integer id, Model model){
        Post post = service.getPostById(id);
        List<Post> staticBlogs = service.getStaticPosts();
        model.addAttribute("staticBlogs", staticBlogs);
        model.addAttribute("blog", post);
        model.addAttribute("errors", violations);
        return "displayBlog";
    }
    
    @GetMapping("/resetPassword")
    public String resetPassword(Model model, Integer error){
        List<Post> blogs = service.getStaticPosts();
        model.addAttribute("blogs", blogs);
        if(error != null){
            if(error == 1){
                model.addAttribute("error", "Password did not match, password was not updated.");
            }else if(error == 2){
                model.addAttribute("error", "Wrong email, password was not updated.");
            }
        }
        return "resetPassword";
    }
    
    @PostMapping("/editUserPassword")
    public String editPassword(String email, Integer id, String password, String confirmPassword){
        User user = userService.getUserByEmail(email);
        if(user == null){
             return "redirect:/resetPassword?id=" + id + "&error=2"; 
        }
        if(password.equals(confirmPassword)){
            user.setPassword(encoder.encode(password));
            userService.updateUser(user);
            
            return "redirect:/home";
        }else{
            return "redirect:/resetPassword?id=" + id + "&error=1";
        }
    }
       
    @PostMapping("createComment")
    public String createComment(Integer blogId, String username, String title, String text){
        User user = userService.getUserByName(username);
        Comment comment = new Comment();
        comment.setDate(LocalDate.now());
        comment.setEnabled(false);
        comment.setPostId(blogId);
        comment.setText(text);
        comment.setTitle(title);
        comment.setUser(user);
        Validator validate = Validation.buildDefaultValidatorFactory().getValidator();
        violations = validate.validate(comment);
        if(violations.isEmpty()){
            commentService.createComment(comment);
        }
        return "redirect:/displayBlog?id="+blogId;
    }
    
}
