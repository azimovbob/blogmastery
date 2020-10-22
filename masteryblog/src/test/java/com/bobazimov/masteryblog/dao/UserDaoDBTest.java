/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bobazimov.masteryblog.dao;

import com.bobazimov.masteryblog.dto.Comment;
import com.bobazimov.masteryblog.dto.User;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
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
public class UserDaoDBTest {
    
    @Autowired
    UserDao userDao;
    
    @Autowired
    CommentDao commentDao;
    
    public UserDaoDBTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
        
        List<Comment> comments = commentDao.readAllComments();
        for(Comment comment: comments){
            commentDao.deleteComment(comment.getId());
        }
        
        List<User> users = userDao.readAllUsers();
        for(User user: users){
            userDao.deleteUser(user.getId());
        }
    }
    
    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of createUser method, of class UserDaoDB.
     */
    @Test
    public void testCreateGetUser() {
        User user = new User();
        user.setUsername("testUser");
        user.setEmail("testEmail");
        user.setRole("ROLE_USER");
        user.setEnabled(true);
        user.setPassword("password");
        user = userDao.createUser(user);
        
        User fromDao = userDao.readUserById(user.getId());
        assertEquals(user, fromDao);
        
        fromDao = userDao.readUserByName(user.getUsername());
        assertEquals(fromDao, user);
        
        fromDao = userDao.readUserByEmail(user.getEmail());
        assertEquals(fromDao, user);
    }

    /**
     * Test of readAllUsers method, of class UserDaoDB.
     */
    @Test
    public void testReadAllUsers() {
        User user = new User();
        user.setUsername("testUser");
        user.setEmail("testEmail");
        user.setRole("ROLE_USER");
        user.setEnabled(true);
        user.setPassword("password");
        user = userDao.createUser(user);
        
        User user1 = new User();
        user1.setUsername("testUser1");
        user1.setEmail("testEmail1");
        user1.setRole("ROLE_USER");
        user1.setEnabled(true);
        user1.setPassword("password");
        user1 = userDao.createUser(user1);
        
        List<User> users = userDao.readAllUsers();
        
        assertEquals(2, users.size());
        assertTrue(users.contains(user));
        assertTrue(users.contains(user1));
    }

    /**
     * Test of deleteUser method, of class UserDaoDB.
     */
    @Test
    public void testDeleteUser() {
        User user = new User();
        user.setUsername("testUser");
        user.setEmail("testEmail");
        user.setRole("ROLE_USER");
        user.setEnabled(true);
        user.setPassword("password");
        user = userDao.createUser(user);
        
        User fromDao = userDao.readUserById(user.getId());
        assertEquals(user, fromDao);
        
        userDao.deleteUser(user.getId());
        
        fromDao = userDao.readUserById(user.getId());
        assertEquals(fromDao, null);
    }

    /**
     * Test of updateUser method, of class UserDaoDB.
     */
    @Test
    public void testUpdateUser() {
        User user = new User();
        user.setUsername("testUser");
        user.setEmail("testEmail");
        user.setRole("ROLE_USER");
        user.setEnabled(true);
        user.setPassword("password");
        user = userDao.createUser(user);
        
        User fromDao = userDao.readUserById(user.getId());
        user.setEmail("testEmail1");
        userDao.updateUser(user);
        
        User updatedUser = userDao.readUserById(user.getId());
        assertNotEquals(fromDao, updatedUser);
        
    }
    
}
