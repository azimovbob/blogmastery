/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bobazimov.masteryblog.dao;

import com.bobazimov.masteryblog.dto.Comment;
import com.bobazimov.masteryblog.dto.Hashtag;
import com.bobazimov.masteryblog.dto.Post;
import java.util.List;

/**
 *
 * @author irabob
 */
public interface PostDao {
    Post createContent(Post post);
    Post readContentById(int id);
    List<Post> readAllcontents();
    void updatePost(Post post);
    void deletePost(int id);
    List<Comment> readAllComments(Post post);
    List<Hashtag> readAllHashtags(Post post);
}
