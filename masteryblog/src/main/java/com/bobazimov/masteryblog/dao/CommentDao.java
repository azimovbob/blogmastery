/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bobazimov.masteryblog.dao;

import com.bobazimov.masteryblog.dto.Comment;
import java.util.List;

/**
 *
 * @author irabob
 */
public interface CommentDao {
    Comment createComment(Comment comment);
    Comment readCommentById(int id);
    List<Comment> readAllComments();
    void deleteComment(int id);
    void updateComment(Comment comment);
}
