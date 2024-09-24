package org.example.server.system;

import org.example.server.manager.CollectionManager;

import java.io.FileWriter;
import java.io.IOException;

public class WriteToFile {
    public static void writeToFile(String data) {
        try (FileWriter fileWriter = new FileWriter("C:\\lab5\\src\\main\\java\\org\\example\\system\\data.xml")) {

            fileWriter.write(data);
            System.out.println("Текст записан в файл data.xml");
        } catch (IOException e) {
            System.out.println("Ошибка записи в файл: " + e.getMessage());
        }
    }

    public static void write(CollectionManager collectionManager) {
    }
}
