package by.bsuir.database;

import by.bsuir.entities.User;

import java.sql.*;

public class DataBaseWorker {
    private static final String URL = "jdbc:mysql://localhost:3306/sportgames?autoReconnect=true&useSSL=false";
    private static final String LOGIN = "root";
    private static final String PASSWORD = "MySQL18662507";
    private static volatile DataBaseWorker instance;
    private static Connection connection;

    public static DataBaseWorker getInstance() {
        if (instance == null) {
            synchronized (DataBaseWorker.class) {
                if (instance == null) {
                    instance = new DataBaseWorker();
                    try {
                        connection = DriverManager.getConnection(URL, LOGIN, PASSWORD);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

    public synchronized boolean authorisation(User user) {
        try (Statement statement = connection.createStatement()) {
            if (!connection.isClosed()) {
                ResultSet resultSet = statement.executeQuery("SELECT *\n" +
                        "FROM users\n" +
                        "WHERE login like '" + user.getLogin() + "' and password like '" + user.getPassword() + "';");
//                ResultSet resultSet = statement.executeQuery("SELECT * FROM users");
                int count = 0;
                while (resultSet.next()) {
                    count++;
                }
                if (count != 0) {
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
