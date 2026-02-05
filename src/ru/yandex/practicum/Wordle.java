package ru.yandex.practicum;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Scanner;

public class Wordle {

    private static final String DICTIONARY_FILE = "words_ru.txt";
    private static final String LOG_FILE = "wordle.log";
    private static final int MAX_STEPS = 6;

    public static void main(String[] args) {
        // Создание лог-файла для хранения ошибок / системных сообщений
        try (PrintWriter log = new PrintWriter(new FileWriter(LOG_FILE, true));
             Scanner scanner = new Scanner(System.in)) {

            // Загрузка словаря
            WordleDictionary dictionary;
            try {
                WordleDictionaryLoader loader = new WordleDictionaryLoader();
                dictionary = loader.loadWordleDictionary(DICTIONARY_FILE, log);
            } catch (WordleDictionaryLoadException e) {
                log.println(e.getMessage());
                e.printStackTrace(log);

                System.out.println("Не удалось загрузить словарь. Подробности записаны в " + LOG_FILE);
                return;
            }

            // Игра
            WordleGame game = new WordleGame(dictionary, MAX_STEPS, log);
            System.out.println("Игра началась! Введите догадку");

            while (!game.isGameOver()) {
                System.out.print("> ");
                String input = scanner.nextLine();

                // Если пустой ввод - вернуть подсказку
                if (input.trim().isEmpty()) {
                    try {
                        String suggestion = game.suggestWord();
                        System.out.println(suggestion);
                    } catch (WordleGameException e) {
                        // Игровая ошибка: показываем игроку
                        System.out.println(e.getMessage());
                    }
                    continue;
                }

                // Обычный ход
                try {
                    String hint = game.makeMove(input);
                    System.out.println(hint);
                    if (!game.isGameOver()) {
                        System.out.println("Осталось попыток: " + game.getStepsLeft());
                    }
                } catch (WordleGameException e) {
                    // Игровая ошибка: показываем игроку, попытку не тратим
                    System.out.println(e.getMessage());
                }
            }

            if (game.isWin()) {
                System.out.println("Победа!");
            } else {
                System.out.println("Попытки закончились. Вы проиграли.");
                System.out.println("Загаданное слово: " + game.getAnswer());
            }
        } catch (Exception e) {
            // Если не удалось даже создать лог-файл или что-то совсем неожиданное
            e.printStackTrace();
        }
    }
}
