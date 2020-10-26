/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bobazimov.masteryblog.service;

import com.bobazimov.masteryblog.dao.CommentDao;
import com.bobazimov.masteryblog.dao.HashtagDao;
import com.bobazimov.masteryblog.dao.ImageDao;
import com.bobazimov.masteryblog.dao.PostDao;
import com.bobazimov.masteryblog.dao.UserDao;
import com.bobazimov.masteryblog.dto.Hashtag;
import com.bobazimov.masteryblog.dto.Post;
import com.bobazimov.masteryblog.dto.User;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author irabob
 */

@Service
public class AdminService {
    
    @Autowired
    UserDao userDao;
    
    @Autowired
    PostDao postDao;
    
    @Autowired
    HashtagDao tagDao;
    
    @Autowired
    CommentDao comDao;
    
    @Autowired
    ImageDao imgDao;
    
    public void createPost(Post post, String hashtags){
        Set<Hashtag> postTags = convertHashtags(hashtags);
        post.setHashtags(postTags);
        postDao.createContent(post);
    }
    
    public List<Post> readAllContetns(){
        return postDao.readAllcontents();
    }
    
    public Post getPostById(int id){
        return postDao.readContentById(id);
    }
    
    public void updatePost(Post post, String tags){
        Set<Hashtag> postTags = convertHashtags(tags);
        post.setHashtags(postTags);
        postDao.updatePost(post);
    }
    
    public void deletePost(int id){
        postDao.deletePost(id);
    }
    
    private Set<Hashtag> convertHashtags(String hashtags){
        String[] hashtagsArr = hashtags.split("#");
        Set<Hashtag> tags = tagDao.readHashtags();
        Set<Hashtag> postTags = new HashSet<>();
        Set<String> tagsName = new HashSet<>();
        if(!tags.isEmpty()){
           for(Hashtag tag: tags){
            tagsName.add(tag.getName());
            } 
        }
        for(int i = 1; i<hashtagsArr.length; i++){
            if(!tagsName.contains(hashtagsArr[i])){
                Hashtag tag = new Hashtag();
                tag.setName(hashtagsArr[i]);
                tagDao.createHashtag(tag);
            }
            postTags.add(tagDao.readHashtagByName(hashtagsArr[i]));
        }
        return postTags;
    }
    
    public void addContetnEditor(User user){
        userDao.createUser(user);
    }
    
    public List<User> getUsers(){
        return userDao.readAllUsers();
    }
    
    public void deleteUser(Integer id){
        userDao.deleteUser(id);
    }
    
    public User getUserById(Integer id){
        return userDao.readUserById(id);
    }

    public void updateUser(User user) {
        userDao.updateUser(user);
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
