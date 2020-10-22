/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bobazimov.masteryblog.controller;

import com.bobazimov.masteryblog.dto.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import com.bobazimov.masteryblog.dao.PostDao;

/**
 *
 * @author irabob
 */

@Controller
public class AdminController {
    
    @Autowired
    PostDao contents;
    
    @GetMapping("/admin")
    public String displayAdminPage(){
        return "/admin";
    }
    
    @PostMapping("/createContent")
    public String createContent(String title, String content){
        Post contentObj = new Post();
        contentObj.setTitle(title);
        contentObj.setContent(content);
        contents.createContent(contentObj);
        return "redirect:/admin";
    }
}
