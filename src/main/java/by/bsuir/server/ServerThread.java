package by.bsuir.server;


import by.bsuir.database.DataBaseWorker;
import by.bsuir.entities.User;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
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

                boolean answer = chooseAction(whatToDo, object);
                TimeUnit.MILLISECONDS.sleep(50);

                if (answer) {
                    objectOutputStream.writeObject("true");
                } else {
                    objectOutputStream.writeObject("false");
                }
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

    private boolean chooseAction(String whatToDo, Object object) {
        switch (whatToDo) {
            case "authorisation":
                return isAuthorised(object);
            default:
                throw new IllegalArgumentException();
        }
    }

    private boolean isAuthorised(Object object) {
        User user = (User) object;
        dataBaseWorker = DataBaseWorker.getInstance();
        return dataBaseWorker.authorisation(user);
    }

    private void addUser(Object object) {
        User user = (User) object;
        // Add in BD ...
    }
}

