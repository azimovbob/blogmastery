/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bobazimov.masteryblog.dao;

import com.bobazimov.masteryblog.dto.User;
import java.util.List;

/**
 *
 * @author irabob
 */
public interface UserDao {
    User createUser(User user);
    User readUserById(int id);
    User readUserByName(String username);
    User readUserByEmail(String email);
    List<User> readAllUsers();
    void deleteUser(int id);
    void updateUser(User user);
}
