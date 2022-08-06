package com.example.prpjectfx1.repository;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {

    public void createTables(Connection connection) throws SQLException {
        userTable(connection.createStatement());
        followTable(connection.createStatement());
        initPostComTable(connection.createStatement());
        initLikesTable(connection.createStatement());
        initViewsTable(connection.createStatement());
    }

    private void userTable(Statement statement) throws SQLException {
        statement.executeUpdate(
                "CREATE TABLE IF NOT EXISTS " +
                        "my_user (name varchar(255)," +
                        "username varchar(255) NOT NULL unique," +
                        "password varchar(255) NOT NULL," +
                        "security_response varchar(255) NOT NULL," +
                        "email varchar(255) NOT NULL," +
                        "business_account int NOT NULL," +
                        "bio varchar(255)," +
                        "photo varchar(255),"+
                        "PRIMARY KEY (username))");
        statement.close();
    }

    private void followTable(Statement statement) throws SQLException {
        statement.executeUpdate("CREATE TABLE IF NOT EXISTS " +
                "follow (id int NOT NULL AUTO_INCREMENT," +
                "fromId varchar(255) NOT NULL," +
                "toId varchar(255) NOT NULL," +
                "PRIMARY KEY (id))");
        statement.close();
    }

    public void initPostComTable(Statement statement) throws SQLException {
        statement.executeUpdate(
                "CREATE TABLE IF NOT EXISTS " +
                        "PostCom(id int NOT NULL AUTO_INCREMENT, " +
                        "subject varchar(255), " +
                        "content varchar(255), " +
                        "username varchar(255), " +
                        "likes int, " +
                        "views int, " +
                        "parent int, " +
                        "DateTime datetime, " +
                        "image varchar(255), " +
                        "isAds TINYINT, " +
                        "PRIMARY KEY (id))");
        statement.close();
    }

    public void initLikesTable(Statement statement) throws SQLException {
        statement.executeUpdate(
                "CREATE TABLE IF NOT EXISTS " +
                        "likes(id int NOT NULL AUTO_INCREMENT, " +
                        "post_id int, " +
                        "assigndate Date, " +
                        "username varchar(255), " +
                        "PRIMARY KEY (id))");
        statement.close();
    }

    public void initViewsTable(Statement statement) throws SQLException {
        statement.executeUpdate(
                "CREATE TABLE IF NOT EXISTS " +
                        "views(id int NOT NULL AUTO_INCREMENT, " +
                        "post_id int, " +
                        "assigndate Date, " +
                        "username varchar(255), " +
                        "PRIMARY KEY (id))");
        statement.close();
    }
}
