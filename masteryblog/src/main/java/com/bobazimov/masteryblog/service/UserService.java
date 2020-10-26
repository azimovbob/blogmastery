/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bobazimov.masteryblog.service;

import com.bobazimov.masteryblog.dao.ImageDao;
import com.bobazimov.masteryblog.dao.UserDao;
import com.bobazimov.masteryblog.dto.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author irabob
 */

@Service
public class UserService {
 
   @Autowired
   UserDao userDao;
   
   @Autowired
   ImageDao imgDao;
   
   public void createUser(User user){
       userDao.createUser(user);
   }
   
   public User getUserByName(String name){
       return userDao.readUserByName(name);
   }
   
   public User getUserByEmail(String email){
       return userDao.readUserByEmail(email);
   }
   
   public void updateUser(User user){
       userDao.updateUser(user);
   }
   
   public String saveImage(MultipartFile file, String fileName, String directory){
       return imgDao.saveImage(file, fileName, directory);
   }
}
