package com.example.prpjectfx1.entity;

import java.sql.Timestamp;
import java.util.ArrayList;

public class PostCom {
    private Integer id;

    private String subject;

    private String content;

    private String userName;

    private int likes;

    private int views;

    private int parent;

    private Timestamp date;

    private String image;

    private boolean isAds;

    private ArrayList<PostCom> children;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        return "Post{" +
                "user=" + userName +
                ", subject='" + subject + '\'' +
                ", content='" + content + '\'' +
                '}';
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public void addComment(PostCom comment) {
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Integer getParent() {
        return parent;
    }

    public void setParent(int parent) {
        this.parent = parent;
    }

    public ArrayList<PostCom> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<PostCom> children) {
        this.children = children;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean getIsAds() {
        return isAds;
    }

    public void setIsAds(boolean isAds) {
        this.isAds = isAds;
    }

}

