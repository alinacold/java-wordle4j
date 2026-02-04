package ru.yandex.practicum;

import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class WordleGame {

    private final String answer;
    private int stepsLeft;

    private final WordleDictionary dictionary;
    private final LinkedHashMap<String, String> history = new LinkedHashMap<>();
    private final PrintWriter log;

    public WordleGame(WordleDictionary dictionary, PrintWriter log) {
        this(dictionary, 6, log);
    }

    public WordleGame(WordleDictionary dictionary, int stepsLeft, PrintWriter log) {
        if (log == null) {
            throw new IllegalArgumentException("Файл логирования не передан");
        }
        if (dictionary == null) {
            throw new IllegalArgumentException("Словарь не передан");
        }
        if (stepsLeft <= 0) {
            throw new IllegalArgumentException("Значение шага должно быть положительным числом");
        }

        this.dictionary = dictionary;
        this.stepsLeft = stepsLeft;

        // ответ выбираем из словаря
        this.answer = dictionary.getRandomWord();
        this.log = log;
        this.log.println("[WordleGame] Игра началась. Количество попыток: " + stepsLeft);

    }

    // Создание конструктора с указанным ответом для тестирования
    public WordleGame(WordleDictionary dictionary, int stepsLeft, PrintWriter log, String fixedAnswer) {
        if (log == null) {
            throw new IllegalArgumentException("Файл логирования не передан");
        }
        if (dictionary == null) {
            throw new IllegalArgumentException("Словарь не передан");
        }
        if (stepsLeft <= 0) {
            throw new IllegalArgumentException("Значение шага должно быть положительным числом");
        }
        if (fixedAnswer == null) {
            throw new IllegalArgumentException("fixedAnswer == null");
        }

        String normalized = WordleDictionary.normalize(fixedAnswer);
        if (!WordleDictionary.isValidGameWord(normalized)) {
            throw new IllegalArgumentException("fixedAnswer invalid format");
        }
        if (!dictionary.contains(normalized)) {
            throw new IllegalArgumentException("fixedAnswer not in dictionary");
        }

        this.dictionary = dictionary;
        this.stepsLeft = stepsLeft;
        this.log = log;
        this.answer = normalized;

        this.log.println("[WordleGame] Игра началась: " + stepsLeft);
    }

    public int getStepsLeft() {
        return stepsLeft;
    }

    //package-private

    String getAnswer() {
        return answer;
    }

    public boolean isWin() {
        return history.containsKey(answer);
    }

    public boolean isGameOver() {
        return isWin() || stepsLeft <= 0;
    }

    public Map<String, String> getHistory() {
        return new LinkedHashMap<>(history);
    }

    // Обработка попытки игры
    public String makeMove(String userInput) throws WordleGameException {
        if (isGameOver()) {
            log.println("[WordleGame] Игра завершена");
            throw new WordleGameOverException("Игра завершена");
        }

        if (userInput == null) {
            log.println("[WordleGame] Пустой ввод");
            throw new InvalidWordFormatException("Пустой ввод");
        }

        String guess = WordleDictionary.normalize(userInput);

        if (guess.isEmpty()) {
            log.println("[WordleGame] Догадка - пустое значение");
            throw new InvalidWordFormatException("Необходимо ввести слово из 5 букв");
        }

        if (!WordleDictionary.isValidGameWord(guess)) {
            log.println("[WordleGame] Догадка невалидна: " + guess);
            throw new InvalidWordFormatException("Необходимо ввести слово из 5 русских букв.");
        }

        if (!dictionary.contains(guess)) {
            log.println("[WordleGame] Слова нет в словаре: " + guess);
            throw new WordNotFoundInDictionaryException("Слова нет в словаре: " + guess);
        }
        if (history.containsKey(guess)) {
            log.println("[WordleGame] Слово уже использовалось: " + guess);
            throw new InvalidWordFormatException("Слово уже использовалось: " + guess);
        }

        stepsLeft--;

        String hint = WordleDictionary.buildHint(guess, answer);
        history.put(guess, hint);

        log.println("[WordleGame] Догадка = " + guess + "; Подсказка = " + hint + "; Оставшееся количество попыток = " + stepsLeft);

        return hint;
    }

    // Создание слова-подсказки
    public String suggestWord() throws WordleGameException {
        if (isGameOver()) {
            log.println("[WordleGame] Игра завершена");
            throw new WordleGameOverException("Игра завершена");
        }

        List<String> all = dictionary.getWords();

        // Проверяем слово на участие раннее в игре
        for (String candidate : all) {
            if (candidate.equals(answer)) {
                continue;
            }
            if (history.containsKey(candidate)) {
                continue;
            }
            if (isCandidateValid(candidate)) {
                log.println("[WordleGame] Подсказка = " + candidate);

                return candidate;
            }
        }

        log.println("[WordleGame] Не удалось подобрать подсказку: нет подходящих слов");
        throw new WordleGameException("Не удалось подобрать подсказку: нет подходящих слов");
    }

    // Проверка, что слово-подсказка подходит по условиям маски-подсказки
    private boolean isCandidateValid(String candidate) {
        for (Map.Entry<String, String> entry : history.entrySet()) {
            String previousGuess = entry.getKey();
            String expectedHint = entry.getValue();

            String actualHint = WordleDictionary.buildHint(previousGuess, candidate);
            if (!actualHint.equals(expectedHint)) {
                return false;
            }
        }

        return true;
    }
}
