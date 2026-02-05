package ru.yandex.practicum;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class WordleDictionary {

    // Список уникальных слов
    private final List<String> words;
    // Map для подсчета количества повторений слов
    private final LinkedHashMap<String, Integer> wordsCount;
    private final PrintWriter log;

    private WordleDictionary(List<String> words, LinkedHashMap<String, Integer> wordsCount, PrintWriter log) {
        if (log == null) {
            throw new IllegalArgumentException("log == null");
        }
        if (words == null) {
            throw new IllegalArgumentException("words == null");
        }
        if (wordsCount == null) {
            throw new IllegalArgumentException("wordsCount == null");
        }

        this.words = words;
        this.wordsCount = wordsCount;
        this.log = log;
    }

    public List<String> getWords() {
        return new ArrayList<>(words);
    }

    // Получение количества всех уникальных слов
    public int size() {
        return words.size();
    }

    // Проверка на наличие слова в словаре
    public boolean contains(String word) {
        if (word == null) {
            return false;
        }

        String normalized = normalize(word);
        return wordsCount.containsKey(normalized);
    }

    // Подсчет повторений слов в словаре
    public static WordleDictionary getUniqueWords(List<String> allWords, PrintWriter log) {
        if (log == null) {
            throw new IllegalArgumentException("Файл логирования не задан");
        }
        if (allWords == null) {
            throw new IllegalArgumentException("Файл словаря не передан");
        }

        LinkedHashMap<String, Integer> wordsCount = new LinkedHashMap<>();

        for (String word : allWords) {
            if (word == null) {
                continue;
            }

            String normalized = normalize(word);
            if (!isValidGameWord(normalized)) {
                continue;
            }

            wordsCount.put(normalized, wordsCount.getOrDefault(normalized, 0) + 1);
        }

        List<String> uniqueWords = new ArrayList<>(wordsCount.keySet());
        log.println("[WordleDictionary] Получен словарь уникальных слов");

        return new WordleDictionary(uniqueWords, wordsCount, log);
    }

    // Приведение слова к формату слов в игре
    public static String normalize(String s) {
        if (s == null) {
            return null;
        }
        return s.trim().toLowerCase().replace('ё', 'е');
    }

    // Получение случайного слова из словаря
    public String getRandomWord() {
        if (words.isEmpty()) {
            log.println("[WordleDictionary] Словарь игры пуст");
            throw new IllegalStateException("Словарь игры пуст");
        }

        int idx = (int) (Math.random() * words.size());

        log.println("[WordleDictionary] Загаданное слово: " + words.get(idx));

        return words.get(idx);
    }

    // Проверка слова на валидность для игры
    public static boolean isValidGameWord(String word) {
        if (word == null) {
            return false;
        }
        if (word.length() != 5) {
            return false;
        }
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            if (c < 'а' || c > 'я') {
                return false;
            }
        }

        return true;
    }
}
