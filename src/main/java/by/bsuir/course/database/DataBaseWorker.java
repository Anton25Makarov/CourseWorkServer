package by.bsuir.course.database;

import by.bsuir.course.entities.Address;
import by.bsuir.course.entities.Referee;
import by.bsuir.course.entities.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataBaseWorker {
    private static final String URL = "jdbc:mysql://localhost:3306/course_work_judging_system?autoReconnect=true&useSSL=false";
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

    public synchronized String isAuthorised(Referee referee) {
        try (Statement statement = connection.createStatement()) {
            if (!connection.isClosed()) {

                // check for admin
                ResultSet resultSetAdmin = statement.executeQuery("SELECT login\n" +
                        "FROM admin\n" +
                        "WHERE login like '" + referee.getLogin() + "' and password like '" + referee.getPassword() + "';");

                while (resultSetAdmin.next()) {
                    return "Admin";
                }

                // check for referee
                ResultSet resultSet = statement.executeQuery("SELECT sport\n" +
                        "FROM referee\n" +
                        "WHERE login like '" + referee.getLogin() + "' and password like '" + referee.getPassword() + "';");
//                ResultSet resultSet = statement.executeQuery("SELECT * FROM users");
                String refereeSport = null;
                while (resultSet.next()) {
                    refereeSport = resultSet.getString("sport");
                }
                if (refereeSport != null) {
                    return refereeSport;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public synchronized List<Referee> readReferees() {
        List<Referee> referees = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
            if (!connection.isClosed()) {

                ResultSet resultSet = statement.executeQuery("SELECT name,\n" +
                        "       surname,\n" +
                        "       age,\n" +
                        "       login,\n" +
                        "       password,\n" +
                        "       sport,\n" +
                        "       a.city    as 'city',\n" +
                        "       a.country as 'country'\n" +
                        "from referee\n" +
                        "       inner join address a on referee.address_id = a.id" + ";");

                while (resultSet.next()) {
                    Referee referee = new Referee();
                    referee.setName(resultSet.getString("name"));
                    referee.setSurname(resultSet.getString("surname"));
                    referee.setAge(Integer.valueOf(resultSet.getString("age")));
                    referee.setLogin(resultSet.getString("login"));
                    referee.setPassword(resultSet.getString("password"));
                    referee.setSport(resultSet.getString("sport"));
                    Address address = new Address();
                    address.setCity(resultSet.getString("city"));
                    address.setCountry(resultSet.getString("country"));
                    referee.setAddress(address);

                    referees.add(referee);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return referees;
    }
}
