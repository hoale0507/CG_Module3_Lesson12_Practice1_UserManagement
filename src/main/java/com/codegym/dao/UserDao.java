package com.codegym.dao;

import com.codegym.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDao implements  IUserDao{
 Connection connection = DBConnection.getConnection();

    private static final String INSERT_USERS_SQL = "INSERT INTO users (name, email, country) VALUES (?, ?, ?);";
    private static final String SELECT_USER_BY_ID = "select id,name,email,country from users where id =?";
    private static final String SELECT_ALL_USERS = "select * from users";
    private static final String DELETE_USERS_SQL = "delete from users where id = ?;";
    private static final String UPDATE_USERS_SQL = "update users set name = ?,email= ?, country =? where id = ?;";
    protected Connection getConnection(){
        Connection connection = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return connection;
    }
    @Override
    public void insertUser(User user) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USERS_SQL);
        preparedStatement.setString(1,user.getName());
        preparedStatement.setString(2,user.getEmail());
        preparedStatement.setString(3,user.getCountry());
        preparedStatement.executeUpdate();
    }

    @Override
    public User selectUser(int id) {
        User user = new User();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER_BY_ID);
            preparedStatement.setInt(1,id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                String country = resultSet.getString("country");
                user = new User(id,name,email,country);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public List<User> selectAllUsers() {
        List<User> users = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_USERS);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                String country = resultSet.getString("country");
                User user = new User(id,name,email,country);
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public boolean deleteUser(int id) throws SQLException {
        boolean isDeleted;
        PreparedStatement preparedStatement = connection.prepareStatement(DELETE_USERS_SQL);
        preparedStatement.setInt(1,id);
        isDeleted = preparedStatement.executeUpdate()>0;
        return isDeleted;
    }

    @Override
    public boolean updateUser(User user) throws SQLException {
        boolean isUpdated;
        PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_USERS_SQL);
        preparedStatement.setString(1,user.getName());
        preparedStatement.setString(2,user.getEmail());
        preparedStatement.setString(3,user.getCountry());
        preparedStatement.setInt(4,user.getId());
        isUpdated = preparedStatement.executeUpdate() > 0;
        return isUpdated;
    }

    @Override
    public User getUserById(int id) {
        User user = new User();
        try {
            CallableStatement callableStatement = connection.prepareCall("call get_user_by_id(?)");
            callableStatement.setInt(1,id);
            ResultSet resultSet = callableStatement.executeQuery();
            while(resultSet.next()){
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                String country = resultSet.getString("country");
                user = new User(id,name,email,country);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public void insertUserStore(User user) throws SQLException {
        CallableStatement callableStatement = connection.prepareCall("call insert_user(?,?,?)");
        callableStatement.setString(1,user.getName());
        callableStatement.setString(2,user.getEmail());
        callableStatement.setString(3,user.getCountry());
        callableStatement.executeUpdate();
    }

    private void printSQLException(SQLException ex) {
        for (Throwable e : ex) {
            if (e instanceof SQLException) {
                e.printStackTrace(System.err);
                System.err.println("SQLState: " + ((SQLException) e).getSQLState());
                System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
                System.err.println("Message: " + e.getMessage());
                Throwable t = ex.getCause();
                while (t != null) {
                    System.out.println("Cause: " + t);
                    t = t.getCause();
                }
            }
        }
    }
}
