package com.example.prpjectfx1.Holder;

import com.example.prpjectfx1.entity.PostCom;

import java.util.ArrayList;

public class PostsHolder {
    private static PostsHolder instance;
    private ArrayList<PostCom> posts;

    private PostsHolder() {
        posts = new ArrayList<>();
    }

    public static PostsHolder getInstance() {
        if (instance == null) {
            instance = new PostsHolder();
        }
        return instance;
    }

    public ArrayList<PostCom> getPosts() {
        return posts;
    }

    public void setPosts(ArrayList<PostCom> posts) {
        this.posts = posts;
    }
}
