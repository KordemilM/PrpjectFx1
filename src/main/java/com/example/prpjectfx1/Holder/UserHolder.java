package com.example.prpjectfx1.Holder;

import com.example.prpjectfx1.entity.User;

public class UserHolder {
    private User user;
    private static final UserHolder INSTANCE = new UserHolder();
    public static UserHolder getInstance() {
        return INSTANCE;
    }
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }

}
