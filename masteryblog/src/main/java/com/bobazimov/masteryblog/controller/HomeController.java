/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bobazimov.masteryblog.controller;

import com.bobazimov.masteryblog.dto.Post;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.bobazimov.masteryblog.dao.PostDao;

/**
 *
 * @author irabob
 */

@Controller
public class HomeController {
    
    @Autowired
    PostDao contentDao;
    
    @GetMapping({"/", "home"})
    public String displayContent(Model model){
        return "home";
    }
}
