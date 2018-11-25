package by.bsuir.course.database;

import by.bsuir.course.entities.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    public synchronized String addSportsmenAndReferees(List<Sportsman> sportsmen, List<Referee> referees) {
        try (Statement statement = connection.createStatement()) {
            if (!connection.isClosed()) {

                statement.addBatch("SET FOREIGN_KEY_CHECKS = 0;");
                statement.addBatch("TRUNCATE TABLE marks");
                statement.addBatch("SET FOREIGN_KEY_CHECKS = 1;");
                statement.executeBatch();
                statement.clearBatch();

                statement.addBatch("SET FOREIGN_KEY_CHECKS = 0;");
                statement.addBatch("TRUNCATE TABLE sportsman");
                statement.addBatch("SET FOREIGN_KEY_CHECKS = 1;");
                statement.executeBatch();
                statement.clearBatch();


                statement.addBatch("SET FOREIGN_KEY_CHECKS = 0;");
                statement.addBatch("TRUNCATE TABLE referee");
                statement.addBatch("SET FOREIGN_KEY_CHECKS = 1;");
                statement.executeBatch();
                statement.clearBatch();


                statement.addBatch("SET FOREIGN_KEY_CHECKS = 0;");
                statement.addBatch("TRUNCATE TABLE human");
                statement.addBatch("SET FOREIGN_KEY_CHECKS = 1;");
                statement.executeBatch();

                statement.addBatch("SET FOREIGN_KEY_CHECKS = 0;");
                statement.addBatch("TRUNCATE TABLE address");
                statement.addBatch("SET FOREIGN_KEY_CHECKS = 1;");
                statement.executeBatch();
                statement.clearBatch();


                ResultSet resultSet;

                ////////////////////////////////////////////////////////////insert Sportsmen
                for (Sportsman sportsman : sportsmen) {
                    statement.execute("INSERT into human (name, surname, age)\n" +
                            "VALUES ('" + sportsman.getName() + "',\n" +
                            "        '" + sportsman.getSurname() + "', \n" +
                            "        '" + sportsman.getAge() + "');");

                    resultSet = statement.executeQuery("SELECT LAST_INSERT_ID() as lastHumanId;");
                    int humanId = 0;
                    while (resultSet.next()) {
                        humanId = Integer.valueOf(resultSet.getString("lastHumanId"));
                    }

                    statement.execute("INSERT into address (country, city)\n" +
                            "VALUES ('" + sportsman.getAddress().getCountry() + "',\n" +
                            "        '" + sportsman.getAddress().getCity() + "');");

                    resultSet = statement.executeQuery("SELECT LAST_INSERT_ID() as lastAddressId;");
                    int addressId = 0;
                    while (resultSet.next()) {
                        addressId = Integer.valueOf(resultSet.getString("lastAddressId"));
                    }

                    statement.execute("INSERT into sportsman (sport, address_id, human_id)\n" +
                            "VALUES ('" + sportsman.getPerformance().getName() + "', \n" +
                            "        '" + addressId + "', \n" +
                            "        '" + humanId + "');");
                }
                ////////////////////////////////////////////////////////////insert Referees
                for (Referee referee : referees) {
                    statement.execute("INSERT into human (name, surname, age)\n" +
                            "VALUES ('" + referee.getName() + "',\n" +
                            "        '" + referee.getSurname() + "', \n" +
                            "        '" + referee.getAge() + "');");

                    resultSet = statement.executeQuery("SELECT LAST_INSERT_ID() as lastHumanId;");
                    int humanId = 0;
                    while (resultSet.next()) {
                        humanId = Integer.valueOf(resultSet.getString("lastHumanId"));
                    }
                    statement.execute("INSERT into address (country, city)\n" +
                            "VALUES ('" + referee.getAddress().getCountry() + "',\n" +
                            "        '" + referee.getAddress().getCity() + "');");

                    resultSet = statement.executeQuery("SELECT LAST_INSERT_ID() as lastAddressId;");
                    int addressId = 0;
                    while (resultSet.next()) {
                        addressId = Integer.valueOf(resultSet.getString("lastAddressId"));
                    }

                    statement.execute("INSERT into referee (sport, login, password, address_id, human_id) \n" +
                            "VALUES ('" + referee.getSport() + "', \n" +
                            "        '" + referee.getLogin() + "', \n" +
                            "        '" + referee.getPassword() + "',\n" +
                            "         " + addressId + ",\n" +
                            "         " + humanId + ");");
                }
                //////////////////////////////////////////////////////////// insert into Marks
                for (Sportsman sportsman : sportsmen) {
                    if (sportsman.getPerformance().getMarks().isEmpty()) {
                        continue;
                    }
                    /////////////////////// find sportsman ID
                    resultSet = statement.executeQuery("SELECT sportsman.id as sportsmanId\n" +
                            "FROM sportsman\n" +
                            "       JOIN human h on sportsman.human_id = h.id\n" +
                            "WHERE h.name = '" + sportsman.getName() + "'\n" +
                            "  AND h.surname = '" + sportsman.getSurname() + "';");
                    int sportsmanId = 0;
                    while (resultSet.next()) {
                        sportsmanId = Integer.valueOf(resultSet.getString("sportsmanId"));
                    }
                    //////////////////////
                    Map<Referee, Mark> markMap = sportsman.getPerformance().getMarks();


                    for (Map.Entry<Referee, Mark> entry : markMap.entrySet()) {
                        Referee referee = entry.getKey();
                        Mark mark = entry.getValue();

                        /////////////////////// find referee ID
                        resultSet = statement.executeQuery("SELECT id as refereeId\n" +
                                "from referee\n" +
                                "where login = '" + referee.getLogin() + "'\n" +
                                "  and password = '" + referee.getPassword() + "' ");
                        int refereeId = 0;
                        while (resultSet.next()) {
                            refereeId = Integer.valueOf(resultSet.getString("refereeId"));
                        }
                        //////////////////////

                        switch (sportsman.getPerformance().getName()) {
                            case "Фигурное катание":
                                statement.execute("INSERT INTO marks (ski_jumping_mark, \n" +
                                        "                   diving_mark, \n" +
                                        "                   skating_mark_1, \n" +
                                        "                   skating_mark_2, \n" +
                                        "                   sportsman_id, \n" +
                                        "                   referee_id)\n" +
                                        "VALUES (0, 0, \n" +
                                        "        " + ((FigureSkatingMark) mark).getPresentationMark() + ", \n" +
                                        "        " + ((FigureSkatingMark) mark).getTechnicalMark() + ", \n" +
                                        "        " + sportsmanId + ", \n" +
                                        "        " + refereeId + ");");
                                break;
                            case "Дайвинг":
                                statement.execute("INSERT INTO marks (ski_jumping_mark, \n" +
                                        "                   diving_mark, \n" +
                                        "                   skating_mark_1, \n" +
                                        "                   skating_mark_2, \n" +
                                        "                   sportsman_id, \n" +
                                        "                   referee_id)\n" +
                                        "VALUES (0,\n" +
                                        "        " + ((DivingMark) mark).getMark() + ", \n" +
                                        "        0, \n" +
                                        "        0, \n" +
                                        "        " + sportsmanId + ", \n" +
                                        "        " + refereeId + ");");
                                break;
                            case "Прыжки с трамплина":
                                statement.execute("INSERT INTO marks (ski_jumping_mark, \n" +
                                        "                   diving_mark, \n" +
                                        "                   skating_mark_1, \n" +
                                        "                   skating_mark_2, \n" +
                                        "                   sportsman_id, \n" +
                                        "                   referee_id)\n" +
                                        "VALUES (" + ((SkiJumpingMark) mark).getMark() + ",\n" +
                                        "        0, \n" +
                                        "        0, \n" +
                                        "        0, \n" +
                                        "        " + sportsmanId + ", \n" +
                                        "        " + refereeId + ");");
                                break;
                            default:
                                throw new UnsupportedOperationException();
                        }
                    }
                }
                return "successful inserting all";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "false";
    }
}
