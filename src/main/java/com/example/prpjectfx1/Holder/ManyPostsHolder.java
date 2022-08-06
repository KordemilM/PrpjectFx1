package com.example.prpjectfx1.Holder;

import com.example.prpjectfx1.entity.PostCom;

import java.util.ArrayList;

public class ManyPostsHolder {
    public static ArrayList<ManyPostsHolder> instances = new ArrayList<>();
    private ArrayList<PostCom> posts;

    public ManyPostsHolder() {
        posts = new ArrayList<>();
    }

    public static ManyPostsHolder getInstance(int index) {
        if (instances.size() <= index) {
            instances.add(new ManyPostsHolder());
        }
        return instances.get(index);
    }

    public ArrayList<PostCom> getPosts() {
        return posts;
    }

    public void setPosts(ArrayList<PostCom> posts) {
        this.posts = posts;
    }
}
