/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bobazimov.masteryblog.dao;

import com.bobazimov.masteryblog.dto.Hashtag;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class HashtagDaoDB implements HashtagDao {

    @Autowired
    JdbcTemplate jdbc;
    
    @Override
    @Transactional
    public Hashtag createHashtag(Hashtag hashtag) {
        final String creatQuery = "INSERT INTO hashtag(name) VALUES(?)";
        jdbc.update(creatQuery, hashtag.getName());
        int id = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        hashtag.setId(id);
        return hashtag;
    }

    @Override
    public Hashtag readHashtagById(int id) {
        try{
            final String readQuery = "SELECT * FROM hashtag WHERE id = ?";
            return jdbc.queryForObject(readQuery, new HashtagMapper(), id);
        }catch(DataAccessException ex){
            return null;
        }        
    }

    @Override
    public Set<Hashtag> readHashtags() {
        final String readAllQuery = "SELECT * FROM hashtag";
        List<Hashtag> tags = jdbc.query(readAllQuery, new HashtagMapper());
        Set<Hashtag> setOfHashtags = new HashSet<>();
        for(Hashtag tag: tags){
            setOfHashtags.add(tag);
        }
        return setOfHashtags;
    }

    @Override
    @Transactional
    public void updateHashtag(Hashtag hashtag) {
        final String updateQuery = "UPDATE hashtag SET name = ?";
        jdbc.update(updateQuery, hashtag.getName());
    }

    @Override
    @Transactional
    public void deleteHashtag(int id) {
        final String deleteFromParentQuery = "DELETE FROM post_hashtag WHERE hashtagId = ?";
        jdbc.update(deleteFromParentQuery, id);
        
        final String deleteQuery = "DELETE FROM hashtag WHERE id = ?";
        jdbc.update(deleteQuery, id);
    }

    @Override
    public Hashtag readHashtagByName(String name) {
        try{
            final String readHashtagByName = "SELECT * FROM hashtag WHERE name = ?";
            return jdbc.queryForObject(readHashtagByName, new HashtagMapper(), name);
        }catch(DataAccessException ex){
            return null;
        }
    }
    
    
    
    public final static class HashtagMapper implements RowMapper<Hashtag>{

        @Override
        public Hashtag mapRow(ResultSet rs, int i) throws SQLException {
           Hashtag tag = new Hashtag();
           tag.setId(rs.getInt("id"));
           tag.setName(rs.getString("name"));
           return tag;
        }
        
    }
    
}
