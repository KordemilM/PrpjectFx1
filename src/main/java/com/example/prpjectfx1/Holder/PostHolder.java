package com.example.prpjectfx1.Holder;

import com.example.prpjectfx1.entity.PostCom;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostHolder {
    private PostCom postCom;
    @Getter
    private static final PostHolder INSTANCE = new PostHolder();

}
