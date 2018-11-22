package by.bsuir.course.server;


import by.bsuir.course.database.DataBaseWorker;
import by.bsuir.course.entities.Referee;
import by.bsuir.course.entities.Sportsman;
import by.bsuir.course.entities.User;

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
                System.out.println(whatToDo);
                TimeUnit.MILLISECONDS.sleep(50);
                Object object = objectInputStream.readObject();

                chooseAction(whatToDo, object, objectOutputStream);
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

    private void chooseAction(String whatToDo, Object object, ObjectOutputStream objectOutputStream) throws IOException {
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

}

