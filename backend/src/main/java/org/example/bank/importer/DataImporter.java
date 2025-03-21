package org.example.bank.importer;

import java.util.List;

public abstract class DataImporter<T> {
    // Шаблонный метод для импорта данных
    public final List<T> importData(String filePath) {
        String fileContent = readFile(filePath);
        List<T> data = parseData(fileContent);
        // Дополнительная валидация данных может быть выполнена здесь
        return data;
    }

    protected String readFile(String filePath) {
        // Реализация чтения файла (например, Files.readString(Paths.get(filePath)))
        // Здесь для упрощения возвращаем пустую строку
        return "";
    }

    protected abstract List<T> parseData(String fileContent);
}
