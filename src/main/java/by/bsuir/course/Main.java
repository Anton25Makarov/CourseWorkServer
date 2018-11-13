package by.bsuir.course;

import by.bsuir.course.database.DataBaseWorker;
import by.bsuir.course.entities.MainWindow;

public class Main {

    public static void main(String[] args) {
        MainWindow mainWindow = new MainWindow();
        mainWindow.setLocationRelativeTo(null);
        mainWindow.setVisible(true);

       /* DataBaseWorker dataBaseWorker = new DataBaseWorker();
        dataBaseWorker.connect();*/

      /*  Server server = new Server();
        server.start();*/
    }
}