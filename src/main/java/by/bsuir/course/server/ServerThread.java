package by.bsuir.course.server;


import by.bsuir.course.database.DataBaseWorker;
import by.bsuir.course.entities.Referee;
import by.bsuir.course.entities.Sportsman;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ServerThread extends Thread {
    private Socket incoming;
    private String methodToCall;
    private DataBaseWorker dataBaseWorker;

    public ServerThread(Socket incomingSocket) {
        incoming = incomingSocket;
    }

    @Override
    public void run() {
        try (ObjectInputStream objectInputStream = new ObjectInputStream(incoming.getInputStream());
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(incoming.getOutputStream())) {

            while (!incoming.isClosed()) {
                String whatToDo = (String) objectInputStream.readObject();
                System.out.println("User choose: " + whatToDo);
                TimeUnit.MILLISECONDS.sleep(50);
                Object object = objectInputStream.readObject();

                chooseAction(whatToDo, object, objectOutputStream, objectInputStream);
                TimeUnit.MILLISECONDS.sleep(50);


            }
        } catch (SocketException e) {
            System.out.println("Клиент отсоединился");
        } catch (ClassNotFoundException e) {
            System.out.println("Такой класс не найден");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Disconnect");
            e.printStackTrace();
        } finally {
            System.out.println("Disconnect");
        }
    }

    private void chooseAction(String whatToDo, Object object,
                              ObjectOutputStream objectOutputStream,
                              ObjectInputStream objectInputStream)
            throws IOException {
        switch (whatToDo) {
            case "authorisation":
                String answer = isAuthorised(object);
                if (answer != null) {
                    objectOutputStream.writeObject(answer);
                } else {
                    objectOutputStream.writeObject("false");
                }
                break;
            case "getAll":
                List<Referee> referees = readRefereesFromBd();
                List<Sportsman> sportsmen = readSportsmanFromBd(referees);

                objectOutputStream.writeObject(referees);
                objectOutputStream.writeObject(sportsmen);
                break;
            case "setAll":
                try {
                    List<Sportsman> sportsmenForAdd = (List<Sportsman>) objectInputStream.readObject();

                    List<Referee> refereesForAdd = (List<Referee>) objectInputStream.readObject();

                    objectOutputStream.writeObject(addSportsmenAndRefereesInBd(sportsmenForAdd, refereesForAdd));

                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            default:
                throw new IllegalArgumentException();
        }
    }

    private String isAuthorised(Object object) {
        Referee referee = (Referee) object;
        dataBaseWorker = DataBaseWorker.getInstance();
        return dataBaseWorker.isAuthorised(referee);
    }

    private List<Referee> readRefereesFromBd() {
        dataBaseWorker = DataBaseWorker.getInstance();
        return dataBaseWorker.readReferees();
    }

    private List<Sportsman> readSportsmanFromBd(List<Referee> referees) {
        dataBaseWorker = DataBaseWorker.getInstance();
        return dataBaseWorker.readSportsmen(referees);
    }

    private String addSportsmenAndRefereesInBd(List<Sportsman> sportsmen, List<Referee> referees) {
        dataBaseWorker = DataBaseWorker.getInstance();
        return dataBaseWorker.addSportsmenAndReferees(sportsmen, referees);
    }
}

