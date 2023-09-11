package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class ReadTextFile {
    public static void main(String[] args) {
        String fileName = "sample.txt"; // Имя файла

        // Создаем несколько строк текста
        String[] lines = {
                "Это первая строка текста.",
                "Это вторая строка текста.",
                "Это третья строка текста."
        };

        // Записываем строки в файл
        writeLinesToFile(fileName, lines);

        // Читаем содержимое файла и выводим его в стандартный поток вывода
        readAndPrintFileContent(fileName);
    }

    private static void writeLinesToFile(String fileName, String[] lines) {
        Path filePath = Paths.get(fileName);
        try {
            Files.write(filePath, List.of(lines));
            System.out.println("Файл успешно создан: " + fileName);
        } catch (IOException e) {
            System.err.println("Ошибка при записи в файл: " + e.getMessage());
        }
    }

    private static void readAndPrintFileContent(String fileName) {
        Path filePath = Paths.get(fileName);
        try {
            List<String> fileLines = Files.readAllLines(filePath);
            System.out.println("Содержимое файла " + fileName + ":");
            for (String line : fileLines) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.err.println("Ошибка при чтении файла: " + e.getMessage());
        }
    }
}
