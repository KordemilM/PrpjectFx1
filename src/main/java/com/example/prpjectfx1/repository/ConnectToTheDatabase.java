package com.example.prpjectfx1.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectToTheDatabase {

    public static Connection ConnectDatabase() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager
                .getConnection("jdbc:mysql://localhost:3306/project","root","123456890");
        return connection;
    }
    //"jdbc:mysql://localhost:3306/project","root","M78fF52Kwa1"
    //"jdbc:mysql://localhost:3306/project","root","maziar.gohar123"
}
