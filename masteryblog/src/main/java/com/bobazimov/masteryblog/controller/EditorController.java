/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bobazimov.masteryblog.controller;

import com.bobazimov.masteryblog.dto.Hashtag;
import com.bobazimov.masteryblog.dto.Post;
import com.bobazimov.masteryblog.service.EditorService;
import com.bobazimov.masteryblog.service.HomeService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
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
public class EditorController {
    
    @Autowired
    EditorService service;

    @Autowired
    HomeService homeService;
    
    final String BLOG_UPLOAD_DIR = "/blogs";
    Set<ConstraintViolation<Post>> postViolations = new HashSet<>();
    
    @GetMapping("/editor")
    public String displayAllPosts(Model model){
        List<Post> blogs = service.getPosts();
        model.addAttribute("blogs", blogs);
        model.addAttribute("errors", postViolations);
        return "editor";
    }
    
    @PostMapping("/createEditorContent")
    public String createContent(Post post, String tags, String postdate, 
        @RequestParam("file") MultipartFile file, String expdate, Boolean isStatic){
        String fileLocation = homeService.saveImage(file, Long.toString(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)), BLOG_UPLOAD_DIR);
       
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
        post.setIsaApproved(false);
        post.setFileName(fileLocation);
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
        return "redirect:/editor";
    }
    
    @GetMapping("/editEditorContent")
    public String editContent(Integer id, Model model){
        Post post = service.getPostById(id);
        String tagStr = "";
        for(Hashtag tag: post.getHashtags()){
            tagStr+="#" + tag.getName(); 
        }
        model.addAttribute("hashtags", tagStr);
        model.addAttribute("blog",post);
        model.addAttribute("errors", postViolations);
        postViolations.clear();
        return "/editEditorContent";
    }
    
    @PostMapping("editEditorContent")
    public String editContent(Model model, Post post, String tags, String postdate, String expdate,
            @RequestParam("file") MultipartFile file, Boolean isStatic){
        
        String fileLocation = homeService.updateImage(file, Long.toString(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)), BLOG_UPLOAD_DIR);
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
        post.setIsaApproved(false);
        post.setFileName(fileLocation);
        if(isStatic != null){
            post.setIsStatic(true);
        }else{
            post.setIsStatic(false);
        }
        Validator validate = Validation.buildDefaultValidatorFactory().getValidator();
        
        postViolations = validate.validate(post);
        if(postViolations.isEmpty()){
            service.updatePost(post,tags);
            return "redirect:/editor";
        }else{
            model.addAttribute("hashtags", tags);
            model.addAttribute("blog",post);
            model.addAttribute("errors", postViolations);
            return "editEditorContent";
        }
        
        
    }
}
