/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bobazimov.masteryblog.service;

import com.bobazimov.masteryblog.dao.ImageDao;
import com.bobazimov.masteryblog.dao.PostDao;
import com.bobazimov.masteryblog.dto.Post;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author irabob
 */

@Service
public class HomeService {
    @Autowired
    PostDao dao;
    
    @Autowired
    ImageDao imgDao;
    
    public List<Post> getStaticPosts(){
        List<Post> posts = dao.readAllcontents();
        try {
            posts = posts.stream()
                .filter(p-> p.isIsaApproved() && p.isIsStatic())
                .filter(p-> p.getPostDate().compareTo(LocalDate.now()) <= 0)
                .filter(p-> p.getExpDate().compareTo(LocalDate.now()) >= 0)
                .collect(Collectors.toList());
        } catch (Exception e) {
            posts = posts.stream()
                .filter(p-> p.isIsaApproved() && p.isIsStatic())
                .filter(p-> p.getPostDate().compareTo(LocalDate.now()) <= 0)
                .collect(Collectors.toList());
        }
        return posts;
    }
    
    public Post getPostById(int id){
        return dao.readContentById(id);
    }
    
    public List<Post> getSimilarBlogs(String tagName){
        List<Post> posts = dao.readPostByCategory(tagName);
        try {
            posts = posts.stream()
                .filter(p-> p.isIsaApproved())
                .filter(p-> p.getPostDate().compareTo(LocalDate.now()) <= 0)
                .filter(p-> p.getExpDate().compareTo(LocalDate.now()) >= 0)
                .collect(Collectors.toList());
        } catch (Exception e) {
            posts = posts.stream()
                .filter(p-> p.isIsaApproved())
                .filter(p-> p.getPostDate().compareTo(LocalDate.now()) <= 0)
                .collect(Collectors.toList());
        }
        return posts;
    }
    
    public List<Post> getAllBlogs(){
        List<Post> posts = dao.readAllcontents();
        try {
           posts =  posts.stream()
                .filter(p-> p.isIsaApproved())
                .filter(p-> p.getPostDate().compareTo(LocalDate.now()) <= 0)
                .filter(p-> p.getExpDate().compareTo(LocalDate.now()) >= 0)
                .collect(Collectors.toList()); 
        } catch (Exception e) {
            posts =  posts.stream()
                .filter(p-> p.isIsaApproved())
                .filter(p-> p.getPostDate().compareTo(LocalDate.now()) <= 0)
                .collect(Collectors.toList());
        }
        return posts;
    }
    
    public String saveImage(MultipartFile file, String fileName, String directory){
       return imgDao.saveImage(file, fileName, directory);
    }
    
    public String updateImage(MultipartFile file, String fileName, String directory){
       return imgDao.updateImage(file, fileName, directory);
    }
    
    public void deleteImage(String filename){
        imgDao.deleteImage(filename);
    }
    
}

    

//@Service
//public class UserDetailsServiceImpl implements UserDetailsService {
//    @Autowired
//    UserDao users;
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        User user = users.getUserByUsername(username);
//        if (!user.isEnabled()) {
//            return null;
//        }
//        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
//        for(Role role : user.getRoles()) {
//            grantedAuthorities.add(new SimpleGrantedAuthority(role.getRole()));
//        }
//        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), grantedAuthorities);
//    }
//}