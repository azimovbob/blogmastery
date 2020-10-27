/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bobazimov.masteryblog.dao;

import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author irabob
 */
public interface ImageDao {
    String saveImage(MultipartFile file, String fileName, String directory);
    
    String updateImage(MultipartFile file, String fileName, String directory);
    
    boolean deleteImage(String oldFile);
}
