package ru.yandex.practicum;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class WordleDictionaryLoader {
    public WordleDictionary loadWordleDictionary(String filename, PrintWriter log) throws WordleDictionaryLoadException {

        List<String> lines = new ArrayList<>();

        try (FileReader fileReader = new FileReader(filename, StandardCharsets.UTF_8); BufferedReader br = new BufferedReader(fileReader)) {

            while (br.ready()) {
                String line = br.readLine();
                if (line != null) {
                    lines.add(line);
                }
            }

        } catch (FileNotFoundException e) {
            log.println("[WordleDictionaryLoader] " + e.getMessage());
            e.printStackTrace(log);
            throw new WordleDictionaryFileNotFoundException("Файл словаря не найден: " + filename, e);

        } catch (IOException e) {
            log.println("[WordleDictionaryLoader] " + e.getMessage());
            e.printStackTrace(log);
            throw new WordleDictionaryReadException("Ошибка чтения файла словаря: " + filename, e);
        }

        if (lines.isEmpty()) {
            log.println("[WordleDictionaryLoader] Файл словаря пуст: " + filename);
            throw new WordleDictionaryIsEmptyException("Файл словаря пуст: " + filename);
        }

        WordleDictionary wordleDictionary = WordleDictionary.getUniqueWords(lines, log);

        if (wordleDictionary.size() == 0) {
            log.println("[WordleDictionaryLoader] После отбора подходящих слов словарь пуст: " + filename);
            throw new WordleDictionaryIsEmptyException("После отбора подходящих слов словарь пуст");
        }

        log.println("[WordleDictionaryLoader] Файл словаря обработан: " + filename);
        return wordleDictionary;
    }
}

