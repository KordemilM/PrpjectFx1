package com.example.prpjectfx1.Holder;

import com.example.prpjectfx1.entity.PostCom;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
@Getter
@Setter
@NoArgsConstructor
public class PostsHolder {
    @Getter
    private static PostsHolder instance;
    private ArrayList<PostCom> posts = new ArrayList<>();

    public static PostsHolder getInstance() {
        if (instance == null) {
            instance = new PostsHolder();
        }
        return instance;
    }

}
