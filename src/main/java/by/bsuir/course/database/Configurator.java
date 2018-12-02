package by.bsuir.course.database;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Configurator {
    private static final String FILE_PATH = "src/main/resources/properties.json";

    public DataBaseProperties getProperties() {
        String json = "";
        try {

            List<String> strings = Files.readAllLines(Paths.get(FILE_PATH));

            for (String string : strings) {
                json += string;
            }
           /* byte[] bytes = Files.readAllBytes(Paths.get(FILE_PATH));
            json = String.valueOf(bytes);*/

        } catch (IOException e) {
            e.printStackTrace();
        }
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        DataBaseProperties dataBaseProperties = gson.fromJson(json, DataBaseProperties.class);
        return dataBaseProperties;
    }
}