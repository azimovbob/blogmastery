/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bobazimov.masteryblog.controller;

import com.bobazimov.masteryblog.dto.Post;
import com.bobazimov.masteryblog.service.HomeService;
import com.bobazimov.masteryblog.service.UserService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

/**
 *
 * @author irabob
 */

@Controller
public class BlogsController {
    
    @Autowired
    HomeService homeService;
 
    @Autowired
    UserService userService;
    
    @GetMapping("/blogs")
    public String displayBlogs(Model model){
        List<Post> posts = homeService.getAllBlogs();
        model.addAttribute("blogs", posts);
        return "/blogs";
    }

    @PostMapping("displayBlogsByHashtag")
    public String displayBlogsByHashtag(Model model, String name){
        List<Post> blogs = homeService.getSimilarBlogs(name);
        model.addAttribute("name", name);
        model.addAttribute("blogs", blogs);
        return "blogsByHashtag";
    }
    
    @GetMapping("displayBlogsByHashtag")
    public String displayBlogsBytag(Model model, String name){
        List<Post> blogs = homeService.getSimilarBlogs(name);
        model.addAttribute("name", name);
        model.addAttribute("blogs", blogs);
        return "blogsByHashtag";
    }
}
