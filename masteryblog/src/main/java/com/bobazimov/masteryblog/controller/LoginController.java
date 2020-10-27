/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bobazimov.masteryblog.controller;

import com.bobazimov.masteryblog.dto.Post;
import com.bobazimov.masteryblog.service.HomeService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 *
 * @author irabob
 */

@Controller
public class LoginController {
    
    @Autowired
    HomeService homeService;
    
    @GetMapping("/login")
    public String showLoginForm(Model model){
        List<Post> blogs = homeService.getStaticPosts();
        model.addAttribute("blogs", blogs);
        return "login";
    }
}
