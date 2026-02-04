package ru.yandex.practicum;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.PrintWriter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WordleDictionaryTest {

    private PrintWriter log;

    @BeforeEach
    void setUp() {
        log = new PrintWriter(System.out);
    }

    @Test
    void normalize_shouldTrimLowerCaseAndReplaceLetter() {
        assertEquals("елка", WordleDictionary.normalize(" ЁлКа "));
    }

    @Test
    void isValidGameWord_shouldReturnTrueFor5RussianLetters() {
        assertTrue(WordleDictionary.isValidGameWord("кошка"));
        assertFalse(WordleDictionary.isValidGameWord("кот"));
        assertFalse(WordleDictionary.isValidGameWord("кошкa"));
        assertFalse(WordleDictionary.isValidGameWord("кошка!"));
    }

    @Test
    void getUniqueWords_shouldNormalizeFilterAndDeduplicate() {
        List<String> raw = List.of(
                "КОШКА",
                "кошка",
                " ёлка ",
                "елка",
                "кот",
                "кошка!",
                "актёр"
        );

        WordleDictionary dictionary = WordleDictionary.getUniqueWords(raw, log);

        assertTrue(dictionary.contains("кошка"));
        assertTrue(dictionary.contains("КОШКА"));
        assertFalse(dictionary.contains("ёлка"));
        assertTrue(dictionary.contains("актер"));
        assertEquals(2, dictionary.size()); // кошка, елка
    }

    @Test
    void buildHint_allCorrect_shouldReturnPlus5() {
        assertEquals("+++++", WordleDictionary.buildHint("давка", "давка"));
    }

    @Test
    void buildHint_exampleFromTask_shouldMatch() {
        assertEquals("+-+^-", WordleDictionary.buildHint("балет", "белое"));
    }

    @Test
    void buildHint_withRepeatingLetters_shouldNotOvercount() {
        assertEquals("++-+^", WordleDictionary.buildHint("комок", "кокос"));
    }
}
