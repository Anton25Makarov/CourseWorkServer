package by.bsuir.course.database;

import by.bsuir.course.entities.*;

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

                ResultSet resultSet = statement.executeQuery("SELECT sport,\n" +
                        "       h.name, \n" +
                        "       h.surname,\n" +
                        "       h.age,\n" +
                        "       login,\n" +
                        "       password,\n" +
                        "       a.city    as 'city',\n" +
                        "       a.country as 'country'\n" +
                        "from referee\n" +
                        "       join address a on referee.address_id = a.id " +
                        "       join human h on referee.human_id = h.id" + ";");

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

    public synchronized List<Sportsman> readSportsmen(List<Referee> referees) {
        List<Sportsman> sportsmen = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
            if (!connection.isClosed()) {
                ResultSet resultSet = statement.executeQuery("SELECT " +
                        "       h.name,\n" +
                        "       h.surname,\n" +
                        "       h.age,\n" +
                        "       a.country,\n" +
                        "       a.city,\n" +
                        "       sport \n" +
                        "from sportsman\n" +
                        "       join human h on sportsman.human_id = h.id " +
                        "       join address a on sportsman.address_id = a.id" + ";");

                while (resultSet.next()) {
                    Sportsman sportsman = new Sportsman();

                    sportsman.setName(resultSet.getString("name"));
                    sportsman.setSurname(resultSet.getString("surname"));
                    sportsman.setAge(Integer.valueOf(resultSet.getString("age")));
                    Address address = new Address();
                    address.setCity(resultSet.getString("city"));
                    address.setCountry(resultSet.getString("country"));
                    sportsman.setAddress(address);


                    String sportsmanSport = resultSet.getString("sport");
                    Sport sport;
                    switch (sportsmanSport) {
                        case "Фигурное катание":
                            sport = new FigureSkating(sportsmanSport);
                            break;
                        case "Дайвинг":
                            sport = new Diving(sportsmanSport);
                            break;
                        case "Прыжки с трамплина":
                            sport = new SkiJumping(sportsmanSport);
                            break;
                        default:
                            throw new UnsupportedOperationException();
                    }
                    sportsman.setPerformance(sport);

                    sportsmen.add(sportsman);
                }


                ResultSet resultSetForMarks = statement.executeQuery("SELECT " +
                        "       r.login,\n" +
                        "       r.password,\n" +
                        "       m.ski_jumping_mark,\n" +
                        "       m.diving_mark,\n" +
                        "       m.skating_mark_1,\n" +
                        "       m.skating_mark_2,\n" +
                        "       h.name,\n" +
                        "       h.surname \n" +
                        "from sportsman\n" +
                        "       join marks m on sportsman.id = m.sportsman_id" +
                        "       join referee r on m.referee_id = r.id " +
                        "       join human h on sportsman.human_id = h.id" + ";");

                while (resultSetForMarks.next()) {

                    String refereeLogin = resultSetForMarks.getString("login");
                    String refereePassword = resultSetForMarks.getString("password");

                    String sportsmanName = resultSetForMarks.getString("name");
                    String sportsmanSurname = resultSetForMarks.getString("surname");

                    double ski_jumping_mark = Double.valueOf(resultSetForMarks.getString("ski_jumping_mark"));
                    double diving_mark = Double.valueOf(resultSetForMarks.getString("diving_mark"));
                    double skating_mark_1 = Double.valueOf(resultSetForMarks.getString("skating_mark_1"));
                    double skating_mark_2 = Double.valueOf(resultSetForMarks.getString("skating_mark_2"));

                    Mark mark = null;

                    if (ski_jumping_mark != 0) {
                        mark = new SkiJumpingMark(ski_jumping_mark);
                    } else if (diving_mark != 0) {
                        mark = new DivingMark(diving_mark);
                    } else if (skating_mark_1 != 0 || skating_mark_2 != 0) {
                        mark = new FigureSkatingMark(skating_mark_1, skating_mark_2);
                    }



                    outer:
                    for (Sportsman sportsman : sportsmen) {
                        if (sportsman.getName().equals(sportsmanName)
                                && sportsman.getSurname().equals(sportsmanSurname)) {
                            for (Referee referee : referees) {
                                if (referee.getLogin().equals(refereeLogin)
                                        && referee.getPassword().equals(refereePassword)) {
                                    sportsman.getPerformance().addResult(referee, mark);
                                    break outer;
                                }
                            }
                        }
                    }

                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sportsmen;
    }
}
