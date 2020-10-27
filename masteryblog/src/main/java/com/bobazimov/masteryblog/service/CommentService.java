/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bobazimov.masteryblog.service;

import com.bobazimov.masteryblog.dao.CommentDao;
import com.bobazimov.masteryblog.dto.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author irabob
 */

@Service
public class CommentService {
    
    @Autowired
    CommentDao dao;
    
    public void createComment(Comment comment){
        dao.createComment(comment);
    }
    
    public void deleteComment(int id){
        dao.deleteComment(id);
    }
}
