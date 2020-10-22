/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bobazimov.masteryblog.controller;

import com.bobazimov.masteryblog.dto.Hashtag;
import com.bobazimov.masteryblog.dto.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import com.bobazimov.masteryblog.service.AdminService;
import java.time.LocalDate;
import java.util.List;
import org.springframework.ui.Model;

/**
 *
 * @author irabob
 */

@Controller
public class AdminController {
    
    @Autowired
    AdminService service;
    
    @GetMapping("/admin")
    public String displayAdminPage(Model model){
        List<Post> blogs = service.readAllContetns();
        model.addAttribute("blogs", blogs);
        return "/admin";
    }
    
    @PostMapping("/createContent")
    public String createContent(String title, String content, String hashtags, String postdate, String expdate, Boolean isaApproved, Boolean isStatic){
        Post post = new Post();
        LocalDate postLd = LocalDate.parse(postdate);
        LocalDate expLd = LocalDate.parse(expdate);
        post.setTitle(title);
        post.setContent(content);
        post.setPostDate(postLd);
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
        service.createPost(post, hashtags);
        return "redirect:/admin";
    }
    
    @GetMapping("/editContent")
    public String editContent(Integer id, Model model){
        Post post = service.getPostById(id);
        String tagStr = "";
        for(Hashtag tag: post.getHashtags()){
            tagStr+="#" + tag.getName(); 
        }
        model.addAttribute("hashtags", tagStr);
        model.addAttribute("blog",post);
        return "/editContent";
    }
    
    @PostMapping("editContent")
    public String editContent(String title, String content, String hashtags, String postdate, String expdate, Boolean isaApproved, Boolean isStatic, Integer id){
        Post post = new Post();
        post.setId(id);
        LocalDate postLd = LocalDate.parse(postdate);
        LocalDate expLd = LocalDate.parse(expdate);
        post.setTitle(title);
        post.setContent(content);
        post.setPostDate(postLd);
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
        service.updatePost(post, hashtags);
        return "redirect:/admin";
    }
}
