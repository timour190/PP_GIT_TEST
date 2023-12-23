package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao  {
    Connection connection;

    private static final String createUsersQuery = "CREATE TABLE IF NOT EXISTS users(id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT, name VARCHAR (50), lastName VARCHAR (50), age INT(3))";
    private static final String dropUsersTable = "DROP TABLE users";
    private static final String insertIntoUsers = "INSERT INTO users (name, lastName, age) values (?, ?, ?)";
    private static final String deleteFromUsersId = "DELETE FROM users WHERE id = ?";
    private static final String selectFromUsers = "select * FROM users";
    private static final String deleteFromUsers = "DELETE FROM users";
    public UserDaoJDBCImpl() {
        try {
            connection = new Util().getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void createUsersTable() {
        try (Statement statement = connection.createStatement()) {
            statement.execute(createUsersQuery);
        } catch (SQLException e) {
            throw new IllegalStateException("Invalid createUsersQuery: " + e.getMessage());
        }
    }

    @Override
    public void dropUsersTable() {
        try (Statement statement = connection.createStatement()) {
            statement.execute(dropUsersTable);
        } catch (SQLException e) {
            throw new IllegalStateException("Invalid dropUsersTable: " + e.getMessage());
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        try (PreparedStatement statement = connection.prepareStatement(insertIntoUsers)) {
            statement.setString(1, name);
            statement.setString(2, lastName);
            statement.setInt(3, age);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Invalid saveUser: " + e.getMessage());
        }
    }

    @Override
    public void removeUserById(long id) {
        try (PreparedStatement statement = connection.prepareStatement(deleteFromUsersId)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Invalid removeUserById: " + e.getMessage());
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();
        try (ResultSet resultSet = connection.createStatement().executeQuery(selectFromUsers)) {
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getLong(1));
                user.setName(resultSet.getString(2));
                user.setLastName(resultSet.getString(3));
                user.setAge(resultSet.getByte(4));
                userList.add(user);
            }
        } catch (SQLException e) {
            System.out.println("Данные не считаны");
        }
        return userList;
    }

    @Override
    public void cleanUsersTable() {
        try (Statement statement = connection.createStatement()) {
            statement.execute(deleteFromUsers);
        } catch (SQLException e) {
            throw new IllegalStateException("Invalid cleanUsersTable: " + e.getMessage());
        }
    }
}
