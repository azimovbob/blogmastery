/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bobazimov.masteryblog.dao;

import com.bobazimov.masteryblog.dto.Hashtag;
import java.util.List;

/**
 *
 * @author irabob
 */
public interface HashtagDao {
    Hashtag createHashtag(Hashtag hashtag);
    Hashtag readHashtagById(int id);
    List<Hashtag> readHashtags();
    void updateHashtag(Hashtag hashtag);
    void deleteHashtag(int id);
}
