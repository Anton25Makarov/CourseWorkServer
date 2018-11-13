package by.bsuir.course.entities;

import by.bsuir.course.server.ServerThread;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainWindow extends JFrame {
    private static final int ACTIVE_PORT = 8071;

    private JPanel panel;
    private JButton startButton;
    private JButton endButton;
    private JLabel socketStatus;
    private JLabel activePort;
    private JTextArea connectedList;

    private boolean serverWork;
    private ExecutorService executorService;
    private ServerSocket serverSocket;

    public MainWindow() throws HeadlessException {
        super("Сервер");
        setSize(500, 500);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        panel = new JPanel();
        panel.setLayout(null);

        startButton = new JButton("Запуск");
        startButton.setLocation(100, 50);
        startButton.setSize(100, 50);

        endButton = new JButton("Стоп");
        endButton.setLocation(250, 50);
        endButton.setSize(100, 50);

        socketStatus = new JLabel("Сокет закрыт");
        socketStatus.setLocation(375, 400);
        socketStatus.setSize(100, 100);
        socketStatus.setBackground(Color.pink);

        activePort = new JLabel("Автивный порт: " + ACTIVE_PORT);
        activePort.setLocation(20, 400);
        activePort.setSize(150, 100);
        activePort.setBackground(Color.pink);

        connectedList = new JTextArea();
        connectedList.setLocation(50, 120);
        connectedList.setSize(380, 300);

        panel.add(startButton);
        panel.add(endButton);
        panel.add(socketStatus);
        panel.add(activePort);
        panel.add(connectedList);

        add(panel);

        Runnable startServer = () -> {
            try {
                serverSocket = new ServerSocket(ACTIVE_PORT);
                while (serverWork) {
                    System.out.println("wait connection ...");

                    Socket sock = serverSocket.accept();
                    System.out.println(sock.getInetAddress().getHostName() + " connected");
                    connectedList.append("Подключился: " + sock.getInetAddress() + "\n");

                    ServerThread server = new ServerThread(sock);
                    server.start();//запуск потока
                }
            } catch (SocketException e) {
                socketStatus.setText("Сокет закрыт");
                System.out.println("closed");
            } catch (IOException e) {
                e.getStackTrace();
            }
        };

        startButton.addActionListener(event -> {
            if (!serverWork) {
                executorService = Executors.newSingleThreadExecutor();
                serverWork = true;
                executorService.execute(startServer);
                socketStatus.setText("Сокет открыт");
            }
        });

        endButton.addActionListener(event -> {
            if (serverWork) {
                serverWork = false;
                executorService.shutdown();
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
