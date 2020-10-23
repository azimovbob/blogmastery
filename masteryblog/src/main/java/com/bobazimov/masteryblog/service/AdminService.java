/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bobazimov.masteryblog.service;

import com.bobazimov.masteryblog.dao.CommentDao;
import com.bobazimov.masteryblog.dao.HashtagDao;
import com.bobazimov.masteryblog.dao.PostDao;
import com.bobazimov.masteryblog.dao.UserDao;
import com.bobazimov.masteryblog.dto.Hashtag;
import com.bobazimov.masteryblog.dto.Post;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
