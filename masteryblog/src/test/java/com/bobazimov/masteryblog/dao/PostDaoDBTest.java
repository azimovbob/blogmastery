/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bobazimov.masteryblog.dao;

import com.bobazimov.masteryblog.dto.Comment;
import com.bobazimov.masteryblog.dto.Hashtag;
import com.bobazimov.masteryblog.dto.Post;
import com.bobazimov.masteryblog.dto.User;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 *
 * @author irabob
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class PostDaoDBTest {
     @Autowired
    CommentDao comDao;
    
    @Autowired
    UserDao userDao;
    
    @Autowired
    PostDao postDao;
    
    @Autowired
    HashtagDao tagDao;
    
    public PostDaoDBTest(){
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
        List<User> users = userDao.readAllUsers();
        for(User user: users){
            userDao.deleteUser(user.getId());
        }
        
        List<Post> posts = postDao.readAllcontents();
        for(Post post: posts){
            postDao.deletePost(post.getId());
        }
        
        
        List<Hashtag> tags = tagDao.readHashtags();
        for(Hashtag tag: tags){
            tagDao.deleteHashtag(tag.getId());
        }
        
        List<Comment> comments = comDao.readAllComments();
        for(Comment comm: comments){
            comDao.deleteComment(comm.getId());
        }
    }
    
    @AfterEach
    public void tearDown() {
    }
    
    @Test
    public void testCreadReadPost(){
        Post post = new Post();
        post.setContent("some content test");
        post.setTitle("test title");
        post.setPostDate(LocalDate.now());
        post.setExpDate(LocalDate.now());
        post.setIsaApproved(true);
        post.setIsStatic(true);
        post.setComments(new HashSet<>());
        Hashtag tag = new Hashtag();
        tag.setName("testtag");
        tag = tagDao.createHashtag(tag);
        
        Set<Hashtag> tags = new HashSet<>();
        tags.add(tag);
        
        post.setHashtags(tags);
        post = postDao.createContent(post);
        
        Post fromDao = postDao.readContentById(post.getId());
        
        assertEquals(fromDao, post);
    }
    
    @Test
    public void testReadAllPost(){
        Post post = new Post();
        post.setContent("some content test");
        post.setTitle("testtitle one");
        post.setPostDate(LocalDate.now());
        post.setExpDate(LocalDate.now());
        post.setIsaApproved(true);
        post.setIsStatic(true);
        post.setComments(new HashSet<>());
        Hashtag tag = new Hashtag();
        tag.setName("testtag");
        tag = tagDao.createHashtag(tag);
        
        Set<Hashtag> tags = new HashSet<>();
        tags.add(tag);
        
        post.setHashtags(tags);
        
        post = postDao.createContent(post);
        
        Post post1 = new Post();
        post1.setContent("some content test");
        post1.setTitle("test title2");
        post1.setPostDate(LocalDate.now());
        post1.setExpDate(LocalDate.now());
        post1.setIsaApproved(true);
        post1.setIsStatic(true);
        post1.setComments(new HashSet<>());
        post1.setHashtags(tags);
        
        post1 = postDao.createContent(post1);
        
        List<Post> posts = postDao.readAllcontents();
        
        assertEquals(2, posts.size());
        assertTrue(posts.contains(post));
        assertTrue(posts.contains(post1));
    }
    
    @Test
    public void testUpdateContent(){
        Post post = new Post();
        post.setContent("some content test");
        post.setTitle("testtitle one");
        post.setPostDate(LocalDate.now());
        post.setExpDate(LocalDate.now());
        post.setIsaApproved(true);
        post.setIsStatic(true);
        post.setComments(new HashSet<>());
        Hashtag tag = new Hashtag();
        tag.setName("testtag");
        tag = tagDao.createHashtag(tag);
        
        Set<Hashtag> tags = new HashSet<>();
        tags.add(tag);
        
        post.setHashtags(tags);
        
        post = postDao.createContent(post);
        
        Post fromDao = postDao.readContentById(post.getId());
        
        post.setIsStatic(false);
        postDao.updatePost(post);
        Post updated = postDao.readContentById(post.getId());
        assertNotEquals(fromDao, updated);
    }
}




