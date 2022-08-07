package com.example.prpjectfx1.repository;

import com.example.prpjectfx1.entity.PostCom;
import com.example.prpjectfx1.entity.User;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

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

    public static boolean searchUserByUsername(String username) throws SQLException{

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM my_user WHERE username='" + username + "'");
        return !resultSet.next();
    }

    public static boolean searchUserByUsernameAndPassword(String username, String password) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM my_user WHERE username=?");
        preparedStatement.setString(1, username);
        ResultSet resultSet = preparedStatement.executeQuery();
        if(resultSet.next()){
            if(password.equals(resultSet.getString(3))){
                return true;
            }
        }
        return false;
    }

    public static void addUser(User user) throws SQLException {

        PreparedStatement preparedStatement = connection.prepareStatement("insert into my_user " +
                "(username,password,security_response,email,business_account) VALUES (?,?,?,?,?)");
        preparedStatement.setString(1,user.getUserName());
        preparedStatement.setString(2,user.getPassword());
        preparedStatement.setString(3,user.getSecurityResponse());
        preparedStatement.setString(4,user.getEmail());
        preparedStatement.setInt(5,user.getAccount());
        preparedStatement.executeUpdate();
    }

    public static String numberOfFollowers(String username) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM follow WHERE toId=?");
        preparedStatement.setString(1, username);
        ResultSet resultSet = preparedStatement.executeQuery();
        int c = 0;
        while (resultSet.next()) {
            c++;
        }
        return Integer.toString(c);
    }

    public static String numberOfFollowing(String username) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM follow WHERE fromId=?");
        preparedStatement.setString(1, username);
        ResultSet resultSet = preparedStatement.executeQuery();
        int c = 0;
        while (resultSet.next()) {
            c++;
        }
        return Integer.toString(c);
    }

    public static boolean findFollowedOrNO(String fromId, String toId) throws SQLException {
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

    public static User searchUser(String username) throws SQLException {
        User user = new User();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM my_user WHERE username=?");
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

    public static boolean addFollowing(String fromId, String toId) throws SQLException {
        if(!fromId.equals(toId)){
            PreparedStatement preparedStatement = connection.prepareStatement("insert into follow (fromId,toId)" +
                    "VALUES (?,?)");
            preparedStatement.setString(1,fromId);
            preparedStatement.setString(2,toId);
            preparedStatement.executeUpdate();
            return true;
        }
        return false;
    }

    public static void updateProfile(User user) throws SQLException {
        PreparedStatement preparedStatement1 = connection.prepareStatement("update my_user set " +
                "name=?,business_account=?,bio=?,photo=? WHERE username=?");
        preparedStatement1.setString(1, user.getName());
        preparedStatement1.setInt(2, user.getAccount());
        preparedStatement1.setString(3, user.getBio());
        preparedStatement1.setString(4, user.getPhoto());
        preparedStatement1.setString(5, user.getUserName());
        preparedStatement1.executeUpdate();
    }

    protected static void addPhoto() throws SQLException, FileNotFoundException {
        PreparedStatement preparedStatement = connection.prepareStatement("update my_user set " +
                "photo=? WHERE username=?");
        //InputStream in = new FileInputStream("H:\\B612.jpg");
       // preparedStatement.setBlob(1,in);
        preparedStatement.setString(1,"H:\\B612.jpg");
        preparedStatement.setString(2,"ali");
        preparedStatement.executeUpdate();
    }

    public static void deleteUser(String username) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM my_user WHERE username=?");
        PreparedStatement preparedStatement1 = connection.prepareStatement("DELETE FROM follow WHERE fromId=? OR toId=?" );
        preparedStatement.setString(1,username);
        preparedStatement1.setString(1,username);
        preparedStatement1.setString(2,username);
        preparedStatement1.executeUpdate();
        preparedStatement.executeUpdate();
    }

    public static ArrayList userSuggestion(String username) throws SQLException {
        HashMap<String,Integer> map = new HashMap<>();

        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM follow WHERE fromId IN " +
                "(SELECT toId FROM follow WHERE fromId = ?)" +
                " AND toId NOT IN (SELECT toId FROM follow WHERE fromId = ?) AND toId!=?");
        preparedStatement.setString(1,username);
        preparedStatement.setString(2,username);
        preparedStatement.setString(3,username);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()){
            map.put(resultSet.getString(3),0);
        }

        for (String s : map.keySet()) {
            PreparedStatement preparedStatement1 = connection.prepareStatement("SELECT COUNT(*) FROM follow WHERE "+
                    "(fromId = ? AND toId IN (SELECT toId FROM follow WHERE fromId = ?)) OR " +
                    "(toId = ? AND fromId IN (SELECT toId FROM follow WHERE fromId = ?))");
            preparedStatement1.setString(1,s);
            preparedStatement1.setString(2,username);
            preparedStatement1.setString(3,s);
            preparedStatement1.setString(4,username);

            ResultSet resultSet1 = preparedStatement1.executeQuery();
            if(resultSet1.next()){
                map.put(s,resultSet1.getInt(1));
            }
        }
        ArrayList<String> list = new ArrayList<>(map.keySet());
        list.sort((o1, o2) -> Integer.compare(map.get(o2), map.get(o1)));
        return list;
    }

    public static boolean searchUserByUsernameAndQuestion(String username, String question) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM my_user WHERE username=?");
        preparedStatement.setString(1, username);
        ResultSet resultSet = preparedStatement.executeQuery();
        if(resultSet.next()){
            if(question.equalsIgnoreCase(resultSet.getString(4))){
                return true;
            }
        }
        return false;
    }

    public static void updatePassword(String username,String password) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("update my_user set " +
                "password=? WHERE username=?");
        preparedStatement.setString(1,password);
        preparedStatement.setString(2, username);
        preparedStatement.executeUpdate();
    }
}
