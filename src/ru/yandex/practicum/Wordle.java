package ru.yandex.practicum;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Scanner;

public class Wordle {

    public static void main(String[] args) {
        // Создание лог-файла для хранения ошибок / системных сообщений
        try (PrintWriter log = new PrintWriter(new FileWriter("wordle.log", true));
             Scanner scanner = new Scanner(System.in)) {

            // Загрузка словаря
            WordleDictionary dictionary;
            try {
                WordleDictionaryLoader loader = new WordleDictionaryLoader();
                dictionary = loader.loadWordleDictionary("words_ru.txt", log);
            } catch (WordleDictionaryLoadException e) {
                log.println(e.getMessage());
                e.printStackTrace(log);

                System.out.println("Не удалось загрузить словарь. Подробности записаны в wordle.log");
                return;
            }

            // Игра
            WordleGame game = new WordleGame(dictionary, 6, log);
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
                    if (!game.isWin()) {
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
            }

            System.out.println("Загаданное слово: " + game.getAnswer());

        } catch (Exception e) {
            // если не удалось даже создать лог-файл или что-то совсем неожиданное
            e.printStackTrace();
        }
    }
}
