package com.example.prpjectfx1.Post;

import com.example.prpjectfx1.entity.User;
import com.example.prpjectfx1.repository.ConnectToTheDatabase;
import com.example.prpjectfx1.repository.PostComRepos;
import com.example.prpjectfx1.repository.UserRepository;

import java.sql.Connection;
import java.sql.SQLException;

public class AppContext{
    private static User user;
    private static final PostComRepos postComRepos = new PostComRepos();

    public AppContext() {
        user = new User();
    }

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        AppContext.user = user;
    }

    public static PostComRepos getPostComRepos() {
        return postComRepos;
    }

    public static Connection getConnection() throws SQLException, ClassNotFoundException {
        return ConnectToTheDatabase.ConnectDatabase();
    }
}

