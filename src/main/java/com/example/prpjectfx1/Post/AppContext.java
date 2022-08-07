package com.example.prpjectfx1.Post;

import com.example.prpjectfx1.entity.User;
import com.example.prpjectfx1.repository.ConnectToTheDatabase;
import com.example.prpjectfx1.repository.PostComRepos;
import com.example.prpjectfx1.repository.UserRepository;
import lombok.Getter;
import lombok.Setter;

import java.sql.Connection;
import java.sql.SQLException;
@Getter
@Setter
public class AppContext{
    @Getter @Setter
    private static User user;
    @Getter
    private static final PostComRepos postComRepos = new PostComRepos();

    public AppContext() {
        user = new User();
    }

    public static Connection getConnection() throws SQLException, ClassNotFoundException {
        return ConnectToTheDatabase.ConnectDatabase();
    }
}

