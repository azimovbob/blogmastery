/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bobazimov.masteryblog.dao;

import com.bobazimov.masteryblog.dao.CommentDaoDB.CommentMapper;
import com.bobazimov.masteryblog.dao.HashtagDaoDB.HashtagMapper;
import com.bobazimov.masteryblog.dao.UserDaoDB.UserMapper;
import com.bobazimov.masteryblog.dto.Comment;
import com.bobazimov.masteryblog.dto.Hashtag;
import com.bobazimov.masteryblog.dto.Post;
import com.bobazimov.masteryblog.dto.User;
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
public class PostDaoDB implements PostDao {

    @Autowired
    JdbcTemplate jdbc;

    @Override
    @Transactional
    public Post createContent(Post post) {
        final String createQuery = "INSERT INTO post(title, content, photofilename, description, date, exp_date, static, approved) "
                + "VALUES(?,?,?,?,?,?,?,?)";
        jdbc.update(createQuery,
                    post.getTitle(), post.getContent(), post.getFileName(), post.getDescription(), post.getPostDate(), post.getExpDate(), 
                    post.isIsStatic(), post.isIsaApproved());
        int id = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        post.setId(id);
        insertPostHashtags(post);
        return post;
    }

    @Override
    public Post readContentById(int id) {
        try{
           final String readQuery = "SELECT * FROM post WHERE id = ?";
           Post post = jdbc.queryForObject(readQuery, new PostMapper(), id);
           post.setComments(getCommentsForPost(id));
           post.setHashtags(getHashtagsForPost(id));
           return post;
        }catch(DataAccessException ex){
            return null;
        }
    }

    @Override
    public List<Post> readAllcontents() {
        final String readAllQuery = "SELECT * FROM post";
        List<Post> posts = jdbc.query(readAllQuery, new PostMapper());
        assosiateUserCommentsHashtags(posts);
        return posts;
    }

    @Override
    @Transactional
    public void updatePost(Post post) {
        final String updateQuery = "UPDATE post SET title = ?, content= ?, date = ?, "
                + "exp_date = ?, static = ?, approved = ? WHERE id = ?";
        jdbc.update(updateQuery, post.getTitle(), post.getContent(), post.getPostDate(), 
                    post.getExpDate(), post.isIsStatic(), post.isIsaApproved(), 
                    post.getId());
        final String deleteHashtagQuery = "DELETE FROM post_hashtag WHERE postId = ?";
        jdbc.update(deleteHashtagQuery, post.getId());
        insertPostHashtags(post);    
    }

    @Override
    @Transactional
    public void deletePost(int id) {
        final String deleteQueryForComment = "DELETE FROM comment WHERE postId = ?";
        jdbc.update(deleteQueryForComment, id);
        
        final String deleteQueryForPostHashTag = "DELETE FROM post_hashtag WHERE postId = ?";
        jdbc.update(deleteQueryForPostHashTag, id);
        
        final String deleteQuery = "DELETE FROM post WHERE id= ? ";
        jdbc.update(deleteQuery, id);
    }

    @Override
    public List<Comment> readAllComments(Post post) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Hashtag> readAllHashtags(Post post) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    @Override
    public List<Post> readPostByCategory(String tagName) {
        final String query = "select p.* from post p " +
                              "join post_hashtag ph on p.id = ph.postId " +
                              "join hashtag h on h.id = ph.hashtagId " +
                              "where h.name = ?";
        List<Post> posts = jdbc.query(query, new PostMapper(), tagName);
        assosiateUserCommentsHashtags(posts);
        return posts;
    }
    

    private void insertPostHashtags(Post post) {
        final String insertPostHastagsQuery = "INSERT INTO post_hashtag(postId, hashtagId) VALUES(?,?)";
        for(Hashtag tag: post.getHashtags()){
            jdbc.update(insertPostHastagsQuery, post.getId(), tag.getId());
        }
    }

    private User getUserForPost(int id) {
        final String readUserQuery = "SELECT u.* FROM user u "
                + "JOIN post p ON u.id = p.userId "
                + "WHERE p.id = ?";
        return jdbc.queryForObject(readUserQuery, new UserMapper(), id);
    }

    private Set<Comment> getCommentsForPost(int id) {
        final String readCommentsQuery = "SELECT * FROM comment WHERE postId = ?";
        List<Comment> comments = jdbc.query(readCommentsQuery, new CommentMapper(), id);
        Set<Comment> commentsSet = new HashSet<>();
        for(Comment comment: comments){
            comment.setUser(getUserForComment(comment.getId()));
            commentsSet.add(comment);
        }
        return commentsSet;
    }

    private Set<Hashtag> getHashtagsForPost(int id) {
        final String readHashtagsQuery = "SELECT h.* FROM hashtag h "
                + "JOIN post_hashtag ph ON ph.hashtagId = h.id "
                + "WHERE ph.postId = ? ";
        List<Hashtag> tags = jdbc.query(readHashtagsQuery, new HashtagMapper(), id);
        Set<Hashtag> tagsSet = new HashSet<>();
        for(Hashtag tag: tags){
            tagsSet.add(tag);
        }
        return tagsSet;
    }

    private void assosiateUserCommentsHashtags(List<Post> posts) {
        for(Post post: posts){
            post.setComments(getCommentsForPost(post.getId()));
            post.setHashtags(getHashtagsForPost(post.getId()));
        }
    }

    private User getUserForComment(int id) {
        final String query = "SELECT u.* FROM user u "
                + "JOIN comment c ON c.userId = u.id "
                + "WHERE c.id = ?";
        return jdbc.queryForObject(query, new UserMapper(), id);
    }
    
    public final static class PostMapper implements RowMapper<Post>{

        @Override
        public Post mapRow(ResultSet rs, int i) throws SQLException {
            Post post = new Post();
            post.setId(rs.getInt("id"));
            post.setTitle(rs.getString("title"));
            post.setContent(rs.getString("content"));
            post.setIsStatic(rs.getBoolean("static"));
            if(rs.getDate("exp_date")!=null){
                post.setExpDate(rs.getDate("exp_date").toLocalDate());
            }
            post.setIsaApproved(rs.getBoolean("approved"));
            post.setPostDate(rs.getDate("date").toLocalDate());
            post.setFileName(rs.getString("photofilename"));
            post.setDescription(rs.getString("description"));
            return post;
        }
        
    }

}
