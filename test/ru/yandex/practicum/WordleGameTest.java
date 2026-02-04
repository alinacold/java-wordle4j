package ru.yandex.practicum;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.PrintWriter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WordleGameTest {

    private PrintWriter log;
    private WordleDictionary dictionary;

    @BeforeEach
    void setUp() {
        log = new PrintWriter(System.out);
        dictionary = WordleDictionary.getUniqueWords(List.of(
                "кошка", "мошка", "вилка", "давка", "атака", "фаска"), log);
    }

    @Test
    void makeMove_nullInput_shouldNotSpendStep() {
        WordleGame game = new WordleGame(dictionary, 6, log, "кошка");
        int before = game.getStepsLeft();

        boolean expectedThrown = false;
        boolean wrongThrown = false;

        try {
            game.makeMove(null);
        } catch (InvalidWordFormatException e) {
            expectedThrown = true;
        } catch (WordleGameException e) {
            wrongThrown = true;
        }

        assertTrue(expectedThrown);
        assertFalse(wrongThrown);
        assertEquals(before, game.getStepsLeft());
    }

    @Test
    void makeMove_invalidFormat_shouldNotSpendStep() {
        WordleGame game = new WordleGame(dictionary, 6, log, "кошка");
        int before = game.getStepsLeft();

        boolean expectedThrown = false;
        boolean wrongThrown = false;

        try {
            game.makeMove("кот"); // 3 буквы
        } catch (InvalidWordFormatException e) {
            expectedThrown = true;
        } catch (WordleGameException e) {
            wrongThrown = true;
        }

        assertTrue(expectedThrown);
        assertFalse(wrongThrown);
        assertEquals(before, game.getStepsLeft());
    }

    @Test
    void makeMove_notInDictionary_shouldNotSpendStep() {
        WordleGame game = new WordleGame(dictionary, 6, log, "кошка");
        int before = game.getStepsLeft();

        boolean expectedThrown = false;
        boolean wrongThrown = false;

        try {
            game.makeMove("пчела");
        } catch (WordNotFoundInDictionaryException e) {
            expectedThrown = true;
        } catch (WordleGameException e) {
            wrongThrown = true;
        }

        assertTrue(expectedThrown);
        assertFalse(wrongThrown);
        assertEquals(before, game.getStepsLeft());
    }

    @Test
    void makeMove_valid_shouldSpendStepAndStoreHistory() throws Exception {
        WordleGame game = new WordleGame(dictionary, 6, log, "кошка");

        String hint = game.makeMove("мошка");
        assertEquals("-++++", hint);
        assertEquals(5, game.getStepsLeft());
        assertEquals(1, game.getHistory().size());
        assertTrue(game.getHistory().containsKey("мошка"));
    }

    @Test
    void win_shouldSetGameOver() throws Exception {
        WordleGame game = new WordleGame(dictionary, 6, log, "давка");

        String hint = game.makeMove("давка");

        assertEquals("+++++", hint);
        assertTrue(game.isWin());
        assertTrue(game.isGameOver());
    }

    @Test
    void suggestWord_shouldNotReturnAlreadyUsedWord() throws Exception {
        WordleGame game = new WordleGame(dictionary, 6, log, "давка");

        game.makeMove("атака"); // сохраняем историю

        String s = game.suggestWord();
        assertNotEquals("атака", s);
    }

    @Test
    void suggestWord_shouldReturnCandidateConsistentWithHistory() throws Exception {
        WordleGame game = new WordleGame(dictionary, 6, log, "фаска");

        String hint = game.makeMove("атака");
        assertEquals("^--++", hint);

        String suggestion = game.suggestWord();

        assertEquals(hint, WordleDictionary.buildHint("атака", suggestion));
    }
}
