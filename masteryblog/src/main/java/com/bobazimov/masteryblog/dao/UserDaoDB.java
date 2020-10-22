/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bobazimov.masteryblog.dao;

import com.bobazimov.masteryblog.dto.User;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class UserDaoDB implements UserDao {

    @Autowired
    JdbcTemplate jdbc;
    
    @Override
    @Transactional
    public User createUser(User user) {
        final String createQuery = "INSERT INTO user(username, email, password, role, enable) "
                + "VALUES(?,?,?,?,?)";
        jdbc.update(createQuery, user.getUsername(), user.getEmail(), user.getPassword(), 
                user.getRole(), user.isEnabled());
        int id = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        user.setId(id);
        return user;
    }

    @Override
    public User readUserById(int id) {
        try{
            final String readQuery = "SELECT * FROM user WHERE id = ?";
            return jdbc.queryForObject(readQuery, new UserMapper(), id);
        }catch(DataAccessException ex){
            return null;
        }
    }

    @Override
    public User readUserByName(String username) {
        try{
            final String readQuery = "SELECT * FROM user WHERE username = ?";
            return jdbc.queryForObject(readQuery, new UserMapper(), username);
        }catch(DataAccessException ex){
            return null;
        }
    }

    @Override
    public User readUserByEmail(String email) {
        try{
            final String readQuery = "SELECT * FROM user WHERE email = ?";
            return jdbc.queryForObject(readQuery, new UserMapper(), email);
        }catch(DataAccessException ex){
            return null;
        }
    }

    @Override
    public List<User> readAllUsers() {
        final String readAllQuery = "SELECT * FROM user";
        return jdbc.query(readAllQuery, new UserMapper());
    }

    @Override
    @Transactional
    public void deleteUser(int id) {
        final String deleteCommentQuery = "DELETE FROM comment WHERE userId = ?";
        jdbc.update(deleteCommentQuery, id);
        
        final String deleteQuery = "DELETE FROM user WHERE id = ?";
        jdbc.update(deleteQuery, id);
    }

    @Override
    @Transactional
    public void updateUser(User user) {
        final String updateQuery = "UPDATE user SET username = ?, "
                + "email = ?, password = ?, enable = ? WHERE id = ?";
        jdbc.update(updateQuery, user.getUsername(), user.getEmail(), 
                user.getPassword(), user.isEnabled(), user.getId());
    }
    
    public final static class UserMapper implements RowMapper<User>{

        @Override
        public User mapRow(ResultSet rs, int i) throws SQLException {
           User user = new User();
           user.setId(rs.getInt("id"));
           user.setUsername(rs.getString("username"));
           user.setEmail(rs.getString("email"));
           user.setPassword(rs.getString("password"));
           user.setRole(rs.getString("role"));
           user.setEnabled(rs.getBoolean("enable"));
           return user;
        }
    }
    
}
