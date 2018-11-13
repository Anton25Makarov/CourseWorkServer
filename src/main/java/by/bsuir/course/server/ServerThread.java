package by.bsuir.course.server;


import by.bsuir.course.entities.User;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ServerThread extends Thread {
    private Socket incoming;
    private String methodToCall;

    public ServerThread(Socket incomingSocket) {
        incoming = incomingSocket;
    }

    @Override
    public void run() {
        try (ObjectInputStream objectInputStream = new ObjectInputStream(incoming.getInputStream());
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(incoming.getOutputStream())) {


            String whatToDo = (String) objectInputStream.readObject();

            System.out.println(whatToDo);

           /* String confirm = switcher(whatToDo);

            objectOutputStream.writeObject(confirm);

            Object object = objectInputStream.readObject();

            switch (methodToCall) {
                case "addUser":
                    addUser(object);
                    break;
                default:
                    throw new IllegalArgumentException();
            }*/

            String[] strings = {"qwert", "asdfg", "zxcv"};
            objectOutputStream.writeObject(strings);


        } catch (IOException e) {
            System.out.println("Disconnect");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            System.out.println("Disconnect");
        }
    }

    private String switcher(String whatToDo) {
        switch (whatToDo) {
            case "addUser":
                methodToCall = whatToDo;
                return "ok";
            default:
                throw new IllegalArgumentException();
        }
    }

    private void addUser(Object object) {
        User user = (User) object;
        // Add in BD ...
    }
}

