package com.example.prpjectfx1.Holder;

import com.example.prpjectfx1.entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserHolder {
    private User user;
    private static final UserHolder INSTANCE = new UserHolder();

}
