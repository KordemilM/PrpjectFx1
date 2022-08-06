package com.example.prpjectfx1.Holder;

import com.example.prpjectfx1.entity.PostCom;

public class PostHolder {
    private PostCom postCom;
    private static final PostHolder INSTANCE = new PostHolder();
    public static PostHolder getInstance() {
        return INSTANCE;
    }
    public PostCom getPostCom() {
        return postCom;
    }
    public void setPostCom(PostCom postCom) {
        this.postCom = postCom;
    }

}
