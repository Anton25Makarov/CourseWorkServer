package by.bsuir.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Serv implements Runnable {

    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(8071)) {
            System.out.println("initialized");
            while (true) {

                Socket sock = serverSocket.accept();
                System.out.println(sock.getInetAddress().getHostName() + " connected");

                ServerThread server = new ServerThread(sock);
                server.start();//запуск потока
            }
        } catch (IOException e) {
            System.err.println(e);
        }
    }
}
