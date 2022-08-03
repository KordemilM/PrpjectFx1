package com.example.prpjectfx1;

import java.io.FileNotFoundException;
import java.sql.*;

public class UserRepository {

    static Connection connection;

    static {
        try {
            connection = ConnectToTheDatabase.ConnectDatabase();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected static boolean searchUserByUsername(String username) throws SQLException{

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM user WHERE username='" + username + "'");
        return !resultSet.next();
    }

    protected static boolean searchPassword(String username,String password) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM user WHERE username=?");
        preparedStatement.setString(1, username);
        ResultSet resultSet = preparedStatement.executeQuery();
        if(resultSet.next()){
            if(password.equals(resultSet.getString(3))){
                return true;
            }
        }
        return false;
    }

    protected static void addUser(User user) throws SQLException {

        PreparedStatement preparedStatement = connection.prepareStatement("insert into user " +
                "(username,password,security_response,email,business_account) VALUES (?,?,?,?,?)");
        preparedStatement.setString(1,user.getUserName());
        preparedStatement.setString(2,user.getPassword());
        preparedStatement.setString(3,user.getSecurityResponse());
        preparedStatement.setString(4,user.getEmail());
        preparedStatement.setInt(5,user.getAccount());
        preparedStatement.executeUpdate();
    }

    protected static String numberOfFollowers(String username) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM follow WHERE toId=?");
        preparedStatement.setString(1, username);
        ResultSet resultSet = preparedStatement.executeQuery();
        int c = 0;
        while (resultSet.next()) {
            c++;
        }
        return Integer.toString(c);
    }

    protected static String numberOfFollowing(String username) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM follow WHERE fromId=?");
        preparedStatement.setString(1, username);
        ResultSet resultSet = preparedStatement.executeQuery();
        int c = 0;
        while (resultSet.next()) {
            c++;
        }
        return Integer.toString(c);
    }

    protected static boolean findFollow(String fromId,String toId) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM follow WHERE fromId=? " +
                "AND toId=?");
        preparedStatement.setString(1, fromId);
        preparedStatement.setString(2, toId);
        ResultSet resultSet = preparedStatement.executeQuery();
        if(resultSet.next()){
            return false;
        }
        return true;
    }

    protected static User searchUser(String username) throws SQLException {
        User user = new User();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM user WHERE username=?");
        preparedStatement.setString(1, username);
        ResultSet resultSet = preparedStatement.executeQuery();
        if(resultSet.next()){
            user.setName(resultSet.getString(1));  user.setUserName(resultSet.getString(2));
            user.setBio(resultSet.getString(7));   user.setPhoto(resultSet.getString(8));
            user.setAccount(resultSet.getInt(6));
            return user;
        }
        return null;
    }

    protected static void addFollower(String fromId , String toId) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("insert into follow (fromId,toId)" +
                "VALUES (?,?)");
        preparedStatement.setString(1,fromId);
        preparedStatement.setString(2,toId);
        preparedStatement.executeUpdate();
    }

    protected static void updateProfile(User user) throws SQLException {
        PreparedStatement preparedStatement1 = connection.prepareStatement("update user set " +
                "name=?,business_account=?,bio=?,photo=? WHERE username=?");
        preparedStatement1.setString(1, user.getName());
        preparedStatement1.setInt(2, user.getAccount());
        preparedStatement1.setString(3, user.getBio());
        preparedStatement1.setString(4, user.getPhoto());
        preparedStatement1.setString(5, user.getUserName());
        preparedStatement1.executeUpdate();
    }

    protected static void addPhoto() throws SQLException, FileNotFoundException {
        PreparedStatement preparedStatement = connection.prepareStatement("update user set " +
                "photo=? WHERE username=?");
        //InputStream in = new FileInputStream("H:\\B612.jpg");
       // preparedStatement.setBlob(1,in);
        preparedStatement.setString(1,"H:\\B612.jpg");
        preparedStatement.setString(2,"ali");
        preparedStatement.executeUpdate();
    }
}
