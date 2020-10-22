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
import org.junit.runner.RunWith;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 *
 * @author irabob
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class CommentDaoDBTest {
    
    @Autowired
    CommentDao comDao;
    
    @Autowired
    UserDao userDao;
    
    @Autowired
    PostDao postDao;
    
    @Autowired
    HashtagDao tagDao;
    
    public CommentDaoDBTest(){
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
    public void testCreateReadComment(){
        Comment comm = new Comment();
        Post post = new Post();
        User user = new User();
        Hashtag tag = new Hashtag();
        
        tag.setName("testtag");
        tag = tagDao.createHashtag(tag);
        
        Set<Hashtag> tags = new HashSet<>();
        
        tags.add(tag);
        
        user.setEmail("testemail");
        user.setUsername("testname");
        user.setRole("ROLE_USER");
        user.setPassword("password");
        user.setEnabled(true);
        
        user = userDao.createUser(user);
        
        
        post.setContent("testcontent");
        post.setTitle("testTitle");
        post.setPostDate(LocalDate.now());
        post.setExpDate(LocalDate.now());
        post.setIsStatic(true);
        post.setIsaApproved(true);
        post.setHashtags(tags);
        
        post = postDao.createContent(post);
        
        comm.setDate(LocalDate.now());
        comm.setTitle("title");
        comm.setText("some test text");
        comm.setEnabled(true);
        comm.setUser(user);
        comm.setPostId(post.getId());
        comm = comDao.createComment(comm);
        
        Comment fromDao = comDao.readCommentById(comm.getId());
        assertEquals(fromDao, comm);
    }
    
    @Test
    public void testReadComments(){
        Comment comm = new Comment();
        Comment comm1 = new Comment();
        Post post = new Post();
        User user = new User();
        Hashtag tag = new Hashtag();
        
        tag.setName("testtag");
        tag = tagDao.createHashtag(tag);
        
        Set<Hashtag> tags = new HashSet<>();
        
        tags.add(tag);
        
        user.setEmail("testemail");
        user.setUsername("testname");
        user.setRole("ROLE_USER");
        user.setPassword("password");
        user.setEnabled(true);
        
        user = userDao.createUser(user);
        
        
        post.setContent("testcontent");
        post.setTitle("testTitle");
        post.setPostDate(LocalDate.now());
        post.setExpDate(LocalDate.now());
        post.setIsStatic(true);
        post.setIsaApproved(true);
        post.setHashtags(tags);
        
        post = postDao.createContent(post);
        
        comm.setDate(LocalDate.now());
        comm.setTitle("title");
        comm.setText("some test text");
        comm.setEnabled(true);
        comm.setUser(user);
        comm.setPostId(post.getId());
        comm = comDao.createComment(comm);
        
        comm1.setDate(LocalDate.now());
        comm1.setTitle("title");
        comm1.setText("some test text");
        comm1.setEnabled(true);
        comm1.setUser(user);
        comm1.setPostId(post.getId());
        comm1 = comDao.createComment(comm1);
        
        List<Comment> comments = comDao.readAllComments();
        
        assertEquals(2, comments.size());
    }
    
    @Test
    public void testUpdateComment(){
        Comment comm = new Comment();
        Post post = new Post();
        User user = new User();
        Hashtag tag = new Hashtag();
        
        tag.setName("testtag");
        tag = tagDao.createHashtag(tag);
        
        Set<Hashtag> tags = new HashSet<>();
        
        tags.add(tag);
        
        user.setEmail("testemail");
        user.setUsername("testname");
        user.setRole("ROLE_USER");
        user.setPassword("password");
        user.setEnabled(true);
        
        user = userDao.createUser(user);
        
        
        post.setContent("testcontent");
        post.setTitle("testTitle");
        post.setPostDate(LocalDate.now());
        post.setExpDate(LocalDate.now());
        post.setIsStatic(true);
        post.setIsaApproved(true);
        post.setHashtags(tags);
        
        post = postDao.createContent(post);
        
        comm.setDate(LocalDate.now());
        comm.setTitle("title");
        comm.setText("some test text");
        comm.setEnabled(true);
        comm.setUser(user);
        comm.setPostId(post.getId());
        comm = comDao.createComment(comm);
        
        Comment fromDao = comDao.readCommentById(comm.getId());
        
        comm.setEnabled(false);
        comDao.updateComment(comm);
        Comment updatedComm = comDao.readCommentById(comm.getId());
        
        assertNotEquals(fromDao, updatedComm);
    }
}
