package com.example.prpjectfx1.Holder;

import com.example.prpjectfx1.entity.PostCom;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
@NoArgsConstructor
public class ManyPostsHolder {
    public static ArrayList<ManyPostsHolder> instances = new ArrayList<>();
    private ArrayList<PostCom> posts = new ArrayList<>();

    public static ManyPostsHolder getInstance(int index) {
        if (instances.size() <= index) {
            instances.add(new ManyPostsHolder());
        }
        return instances.get(index);
    }
}
