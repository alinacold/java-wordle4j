package ru.yandex.practicum;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class WordleDictionaryLoaderTest {

    private PrintWriter log;
    private WordleDictionaryLoader loader;

    private final String testFileName = "test_words_ru.txt";

    @BeforeEach
    void setUp() {
        log = new PrintWriter(System.out);
        loader = new WordleDictionaryLoader();
        new File(testFileName).delete();
    }

    @AfterEach
    void tearDown() {
        new File(testFileName).delete();
    }

    @Test
    void load_fileNotFound_shouldThrowFileNotFoundException() {
        boolean expectedThrown = false;
        boolean wrongThrown = false;

        try {
            loader.loadWordleDictionary("no_such_file.txt", log);
        } catch (WordleDictionaryFileNotFoundException e) {
            expectedThrown = true;
            assertTrue(e.getMessage().contains("Файл словаря не найден"));
        } catch (WordleDictionaryLoadException e) {
            wrongThrown = true;
        }

        assertTrue(expectedThrown);
        assertFalse(wrongThrown);
    }

    @Test
    void load_emptyFile_shouldThrowIsEmptyException() throws Exception {

        try (FileWriter fw = new FileWriter(testFileName, StandardCharsets.UTF_8)) {}

        boolean expectedThrown = false;
        boolean wrongThrown = false;

        try {
            loader.loadWordleDictionary(testFileName, log);
        } catch (WordleDictionaryIsEmptyException e) {
            expectedThrown = true;
            assertTrue(e.getMessage().contains("Файл словаря пуст"));
        } catch (WordleDictionaryLoadException e) {
            wrongThrown = true;
        }

        assertTrue(expectedThrown);
        assertFalse(wrongThrown);
    }

    @Test
    void load_validFile_shouldLoadAndFilter() throws Exception {
        try (FileWriter fw = new FileWriter(testFileName, StandardCharsets.UTF_8)) {
            fw.write("КОШКА\n");
            fw.write(" коШКа \n");
            fw.write("давка\n");
            fw.write("кот\n");
            fw.write("кошка!\n");
        }

        WordleDictionary dictionary = null;
        boolean thrown = false;

        try {
            dictionary = loader.loadWordleDictionary(testFileName, log);
        } catch (WordleDictionaryLoadException e) {
            thrown = true;
            assertFalse(e.getMessage().isEmpty());
        }

        assertFalse(thrown);
        assertNotNull(dictionary);
        assertTrue(dictionary.contains("кошка"));
        assertTrue(dictionary.contains("давка"));
        assertEquals(2, dictionary.size());
    }
}
