package by.bsuir.course.database;

import java.sql.*;

public class DataBaseWorker {
    private static final String URL = "jdbc:mysql://localhost:3306/test_db?autoReconnect=true&useSSL=false";
    private static final String LOGIN = "root";
    private static final String PASSWORD = "MySQL18662507";
    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public DataBaseWorker(/*Connection connection*/) {
        /*this.connection = connection;*/
    }

    public void connect() {

        try {
            connection = DriverManager.getConnection(URL, LOGIN, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
