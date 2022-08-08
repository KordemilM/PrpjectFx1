package com.example.prpjectfx1.repository;


import com.example.prpjectfx1.entity.PostCom;
import com.example.prpjectfx1.entity.User;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class PostComRepos {




    public void addPost(PostCom post, Connection connection) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "insert into PostCom(subject, content, username, likes, views, parent,datetime,image,isAds) values(?,?,?,?,?,?,?,?,?)"
        );
        preparedStatement.setString(1, post.getSubject());
        preparedStatement.setString(2, post.getContent());
        preparedStatement.setString(3, post.getUserName());
        preparedStatement.setInt(4, post.getLikes());
        preparedStatement.setInt(5, 0);
        preparedStatement.setInt(6, 0);
        preparedStatement.setTimestamp(7, post.getDate());
        preparedStatement.setString(8, post.getImage());
        preparedStatement.setInt(9, post.isAds()?1:0);
        preparedStatement.executeUpdate();
    }

    public ArrayList<PostCom> getAllPost(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(
                "select * from PostCom WHERE parent = 0"
        );
        return getPostCom(statement, resultSet);
    }

    private ArrayList<PostCom> getPostCom(Statement statement, ResultSet resultSet) throws SQLException {
        ArrayList<PostCom> postComArrayList = new ArrayList<>();
        while (resultSet.next()) {
            PostCom postCom = new PostCom();
            postCom.setId(resultSet.getInt("id"));
            postCom.setSubject(resultSet.getString("subject"));
            postCom.setContent(resultSet.getString("content"));
            postCom.setUserName(resultSet.getString("username"));
            postCom.setLikes(resultSet.getInt("likes"));
            postCom.setViews(resultSet.getInt("views"));
            postCom.setParent(0);
            postCom.setDate(resultSet.getTimestamp("datetime"));
            postCom.setImage(resultSet.getString("image"));
            postCom.setAds(resultSet.getInt("isAds") == 1);
            postComArrayList.add(postCom);
        }
        statement.close();
        return postComArrayList;
    }

    public ArrayList<PostCom> getAllPostsByUser(String username, Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(
                "select * from PostCom WHERE parent = 0 AND username = '"+username+"'"
        );
        return getPostCom(statement, resultSet);
    }

    // get children of a post

    public ArrayList<PostCom> getChildren(int id, Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(
                "select * from PostCom WHERE parent = "+id
        );
        ArrayList<PostCom> postComArrayList = new ArrayList<>();
        while (resultSet.next()) {
            PostCom postCom = new PostCom();
            postCom.setId(resultSet.getInt("id"));
            postCom.setSubject(resultSet.getString("subject"));
            postCom.setContent(resultSet.getString("content"));
            postCom.setUserName(resultSet.getString("username"));
            postCom.setLikes(resultSet.getInt("likes"));
            postCom.setViews(resultSet.getInt("views"));
            postCom.setParent(id);
            postCom.setDate(resultSet.getTimestamp("datetime"));
            postCom.setImage(resultSet.getString("image"));
            postComArrayList.add(postCom);
        }
        statement.close();
        return postComArrayList;
    }

    // get all post other than the user's post

    public ArrayList<PostCom> getAllPostOtherThanUser(String username, Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(
                "select * from PostCom WHERE parent = 0 AND username != '"+username+"'"
        );
        return getPostCom(statement, resultSet);
    }

    // add Children

    public void addChildren(PostCom post, Connection connection) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "insert into PostCom(subject, content, username, likes, views, parent,datetime,image,isAds) values(?,?,?,?,?,?,?,?,?)"
        );
        preparedStatement.setString(1, post.getSubject());
        preparedStatement.setString(2, post.getContent());
        preparedStatement.setString(3, post.getUserName());
        preparedStatement.setInt(4, post.getLikes());
        preparedStatement.setInt(5, 0);
        preparedStatement.setInt(6, post.getParent());
        preparedStatement.setTimestamp(7, post.getDate());
        preparedStatement.setString(8, post.getImage());
        preparedStatement.setInt(9, post.isAds()?1:0);
        preparedStatement.executeUpdate();
    }

    // like a post

    public void likePost(PostCom post,User user, Connection connection) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "update PostCom set likes = likes + 1 where id = "+ post.getId()
        );
        preparedStatement.executeUpdate();
        preparedStatement.close();
        PreparedStatement preparedStatement1 = connection.prepareStatement(
                "insert into likes(post_id, assigndate,username) values(?,?,?)"
        );
        preparedStatement1.setInt(1, post.getId());
        preparedStatement1.setDate(2, Date.valueOf(LocalDate.now()));
        preparedStatement1.setString(3, user.getUserName());
        preparedStatement1.executeUpdate();
    }

    // add view to a post if not already viewed by the user

    public void addView(PostCom post, User user, Connection connection) throws SQLException {
        if(!isViewed(post,user,connection)){
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "update PostCom set views = views + 1 where id = "+ post.getId()
            );
            preparedStatement.executeUpdate();
            preparedStatement.close();
            PreparedStatement preparedStatement1 = connection.prepareStatement(
                    "insert into views(post_id, assigndate,username) values(?,?,?)"
            );
            preparedStatement1.setInt(1, post.getId());
            preparedStatement1.setDate(2, Date.valueOf(LocalDate.now()));
            preparedStatement1.setString(3, user.getUserName());
            preparedStatement1.executeUpdate();
        }
    }

    // get likes of a post by date

    public int getLikesByDate(PostCom post,Date date, Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(
                "select * from likes where " +
                        "post_id = " + post.getId() +
                        " and assigndate = '" + date + "'"
        );
        int likes = 0;
        while (resultSet.next()) {
            likes++;
        }
        statement.close();
        return likes;
    }

    public LinkedHashMap<Date, Integer> getLikes(PostCom post, Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(
                "select * from likes where post_id = " + post.getId() +
                        " ORDER by assigndate"
        );

        LinkedHashMap<Date, Integer> likes = new LinkedHashMap<>();
        while (resultSet.next()) {
            likes.put(resultSet.getDate("assigndate"), getLikesByDate(post, resultSet.getDate("assigndate"), connection));
        }
        statement.close();
        return likes;
    }

    // get views of a post order by date

    public int getViewsByDate(PostCom post, Date date, Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(
                "select * from views where " +
                        "post_id = " + post.getId() + " and assigndate = '" + date + "'"
        );
        int views = 0;
        while (resultSet.next()) {
            views++;
        }
        statement.close();
        return views;
    }

    public LinkedHashMap<Date, Integer> getViews(PostCom post, Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(
                "select * from views where post_id = " + post.getId() +
                        " ORDER by assigndate"
        );
        LinkedHashMap<Date, Integer> views = new LinkedHashMap<>();
        while (resultSet.next()) {
            views.put(resultSet.getDate("assigndate"), getViewsByDate(post, resultSet.getDate("assigndate"), connection));
        }
        statement.close();
        return views;
    }

    // get last 10 post of followers and the user

    public ArrayList<PostCom> getLast10Post(String username, Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(
                "select * from PostCom where username = '"+username+"' or username in (select toId from follow where fromId = '"+username+"') order by datetime desc limit 10"
        );
        return getPostCom(statement, resultSet);
    }

    // get 1 random Ads post hasnt viewed by user

    public PostCom getRandomAdsPost(String username, Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(
                "select * from PostCom where isAds = true and id not in (select post_id from views where username = '"+username+"') order by rand() limit 1"
        );
        ArrayList<PostCom> postComArrayList = getPostCom(statement, resultSet);
        statement.close();
        return postComArrayList.get(0);
    }

    // get All posts other than ads and hasnt viewed other than user

    public ArrayList<PostCom> getAllPosts(String username, Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(
                "select * from PostCom where isAds = false and id not in (select post_id from views where username = '"+username+"') and username != '" + username + "' order by datetime desc"
        );
        return getPostCom(statement, resultSet);
    }

    // get username of likes of a post

    public ArrayList<String> getUsernameOfLikes(PostCom post, Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(
                "select * from likes where post_id = " + post.getId()
        );
        ArrayList<String> username = new ArrayList<>();
        while (resultSet.next()) {
            username.add(resultSet.getString("username"));
        }
        statement.close();
        return username;
    }

    // check if user liked a post

    public boolean isLiked(PostCom post, User user, Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(
                "select * from likes where post_id = " + post.getId() + " and username = '" + user.getUserName() + "'"
        );
        boolean isLiked = false;
        while (resultSet.next()) {
            isLiked = true;
        }
        statement.close();
        return isLiked;
    }

    public boolean isViewed(PostCom post, User user, Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(
                "select * from views where post_id = " + post.getId() + " and username = '" + user.getUserName() + "'"
        );
        boolean isViewed = false;
        while (resultSet.next()) {
            isViewed = true;
        }
        statement.close();
        return isViewed;
    }

    //get number of posts of a user

    public int getNumberOfPosts(String username, Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(
                "select * from PostCom where username = '"+username+"' order by datetime desc"
        );
        int numberOfPosts = 0;
        while (resultSet.next()) {
            numberOfPosts++;
        }
        statement.close();
        return numberOfPosts;
    }

    //last post of a user

    public PostCom getLastPost(String username, Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(
                "select * from PostCom where username = '"+username+"' order by datetime desc limit 1"
        );
        ArrayList<PostCom> postComArrayList = getPostCom(statement, resultSet);
        statement.close();
        return postComArrayList.get(0);
    }
}
