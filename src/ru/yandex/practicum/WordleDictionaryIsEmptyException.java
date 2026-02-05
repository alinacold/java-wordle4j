package ru.yandex.practicum;

public class WordleDictionaryIsEmptyException extends WordleDictionaryLoadException {
    public WordleDictionaryIsEmptyException(String message) {
        super(message);
    }
}
