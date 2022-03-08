package com.codegym.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private final static String jdbcURL = "jdbc:mysql://localhost:3306/demo?useSSL=false";
    private final static String jdbcUsername = "root";
    private final static String jdbcPassword = "123456";
    public static Connection getConnection() {
            Connection connection = null;
            try {
                Class.forName("com.mysql.jdbc.Driver");
                connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
            return connection;
        }
    }
