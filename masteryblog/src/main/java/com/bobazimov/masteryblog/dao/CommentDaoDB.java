/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bobazimov.masteryblog.dao;

import com.bobazimov.masteryblog.dao.UserDaoDB.UserMapper;
import com.bobazimov.masteryblog.dto.Comment;
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
public class CommentDaoDB implements CommentDao {

    @Autowired
    JdbcTemplate jdbc;
    
    @Override
    @Transactional
    public Comment createComment(Comment comment) {
        final String createQuery = "INSERT INTO comment(title, text, date, enabled, postId, userId) "
                + "VALUES(?,?,?,?,?,?)";
        jdbc.update(createQuery, comment.getTitle(), comment.getText(), comment.getDate(), 
                comment.isEnabled(), comment.getPostId(), comment.getUser().getId());
        int id = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        comment.setId(id);
        return comment;
    }

    @Override
    public Comment readCommentById(int id) {
        try{
            final String readCommentQuery = "SELECT * FROM comment WHERE id = ?";
            Comment comment = jdbc.queryForObject(readCommentQuery, new CommentMapper(), id);
            comment.setUser(getUserForComment(id));
            return comment;
        }catch(DataAccessException ex){
            return null;
        }
    }

    @Override
    public List<Comment> readAllComments() {
        final String readAllCommentQuery = "SELECT * FROM comment";
        List<Comment> comments = jdbc.query(readAllCommentQuery, new CommentMapper());
        assosiateUserForComment(comments);
        return comments;
    }

    @Override
    @Transactional
    public void deleteComment(int id) {
        final String deleteQuery = "DELETE FROM comment WHERE id = ?";
        jdbc.update(deleteQuery, id);
    }

    @Override
    @Transactional
    public void updateComment(Comment comment) {
        final String updateQuery = "UPDATE comment SET title = ?, text = ?, date = ? , "
                + "enabled = ? WHERE id = ?";
        jdbc.update(updateQuery, comment.getTitle(), comment.getText(), comment.getDate(), 
                comment.isEnabled(), comment.getId());
    }

    private User getUserForComment(int id) {
        final String query = "SELECT u.* FROM user u "
                + "JOIN comment c ON c.userId = u.id "
                + "WHERE c.id = ?";
        return jdbc.queryForObject(query, new UserMapper(), id);
    }


    private void assosiateUserForComment(List<Comment> comments) {
        for(Comment comment: comments){
            comment.setUser(getUserForComment(comment.getId()));
        }
    }
    
    public final static class CommentMapper implements RowMapper<Comment>{

        @Override
        public Comment mapRow(ResultSet rs, int i) throws SQLException {
            Comment comment = new Comment();
            comment.setId(rs.getInt("id"));
            comment.setTitle(rs.getString("title"));
            comment.setText(rs.getString("text"));
            comment.setDate(rs.getDate("date").toLocalDate());
            comment.setEnabled(rs.getBoolean("enabled"));
            comment.setPostId(rs.getInt("postId"));
            return comment;
        }
        
    }
    
}
