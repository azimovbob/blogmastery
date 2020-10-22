package com.bobazimov.masteryblog.dao;


import com.bobazimov.masteryblog.dto.Hashtag;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import static org.junit.jupiter.api.Assertions.*;
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
public class HashtagDaoDBTest {
    
    @Autowired
    HashtagDao tagDao;
    
    public HashtagDaoDBTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
        List<Hashtag> tags = tagDao.readHashtags();
        for(Hashtag tag: tags){
            tagDao.deleteHashtag(tag.getId());
        }
    }
    
    @AfterEach
    public void tearDown() {
    }
    
    @Test
    public void testCreateReadHashtag(){
        Hashtag tag = new Hashtag();
        tag.setName("testtag");
        tag = tagDao.createHashtag(tag);
        
        Hashtag fromDao = tagDao.readHashtagById(tag.getId());
        assertEquals(fromDao, tag);
    }
    
    @Test
    public void testReadHashtags(){
        Hashtag tag = new Hashtag();
        tag.setName("testtag");
        tag = tagDao.createHashtag(tag);
        
        Hashtag tag1 = new Hashtag();
        tag1.setName("testtag");
        tag1 = tagDao.createHashtag(tag1);
        
        List<Hashtag> tags = tagDao.readHashtags();
        assertEquals(2, tags.size());
        assertTrue(tags.contains(tag));
        assertTrue(tags.contains(tag1));
    }
    
    @Test void testDeleteHashtag(){
        Hashtag tag = new Hashtag();
        tag.setName("testtag");
        tag = tagDao.createHashtag(tag);
        
        tagDao.deleteHashtag(tag.getId());
        Hashtag fromDao = tagDao.readHashtagById(tag.getId());
        assertNull(fromDao);
    }
    
    @Test
    public void testUpdateHashtag(){
       Hashtag tag = new Hashtag();
       tag.setName("testtag");
       tag = tagDao.createHashtag(tag);
       
       Hashtag fromDao = tagDao.readHashtagById(tag.getId());
       
       tag.setName("updatedtesttag");
       tagDao.updateHashtag(tag);
       Hashtag updatedTag = tagDao.readHashtagById(tag.getId());
       
       assertNotEquals(fromDao, updatedTag);
    }
}