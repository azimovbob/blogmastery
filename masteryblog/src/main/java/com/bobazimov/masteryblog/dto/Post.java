/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bobazimov.masteryblog.dto;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

/**
 *
 * @author irabob
 */
public class Post {
    private int id;
    private String content;
    private String title;
    private boolean isaApproved;
    private boolean isStatic;
    private LocalDate postDate;
    private LocalDate expDate;
    private Set<Comment> comments;
    private Set<Hashtag> hashtags;

    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    public Set<Hashtag> getHashtags() {
        return hashtags;
    }

    public void setHashtags(Set<Hashtag> hashtags) {
        this.hashtags = hashtags;
    }

    public LocalDate getPostDate() {
        return postDate;
    }

    public void setPostDate(LocalDate postDate) {
        this.postDate = postDate;
    }

    public LocalDate getExpDate() {
        return expDate;
    }

    public void setExpDate(LocalDate expDate) {
        this.expDate = expDate;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Post{" + "id=" + id + ", content=" + content + ", title=" + title + ", isaApproved=" + isaApproved + ", isStatic=" + isStatic + ", postDate=" + postDate + ", expDate=" + expDate + ", comments=" + comments + ", hashtags=" + hashtags + '}';
    }

  

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 13 * hash + this.id;
        hash = 13 * hash + Objects.hashCode(this.content);
        hash = 13 * hash + Objects.hashCode(this.title);
        hash = 13 * hash + (this.isaApproved ? 1 : 0);
        hash = 13 * hash + (this.isStatic ? 1 : 0);
        hash = 13 * hash + Objects.hashCode(this.postDate);
        hash = 13 * hash + Objects.hashCode(this.expDate);
        hash = 13 * hash + Objects.hashCode(this.comments);
        hash = 13 * hash + Objects.hashCode(this.hashtags);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Post other = (Post) obj;
        if (this.id != other.id) {
            return false;
        }
        if (this.isaApproved != other.isaApproved) {
            return false;
        }
        if (this.isStatic != other.isStatic) {
            return false;
        }
        if (!Objects.equals(this.content, other.content)) {
            return false;
        }
        if (!Objects.equals(this.title, other.title)) {
            return false;
        }
        if (!Objects.equals(this.postDate, other.postDate)) {
            return false;
        }
        if (!Objects.equals(this.expDate, other.expDate)) {
            return false;
        }
        if (!Objects.equals(this.comments, other.comments)) {
            return false;
        }
        if (!Objects.equals(this.hashtags, other.hashtags)) {
            return false;
        }
        return true;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isIsaApproved() {
        return isaApproved;
    }

    public void setIsaApproved(boolean isaApproved) {
        this.isaApproved = isaApproved;
    }

    public boolean isIsStatic() {
        return isStatic;
    }


    public void setIsStatic(boolean isStatic) {
        this.isStatic = isStatic;
    }
 
}
