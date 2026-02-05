package ru.yandex.practicum;

public class WordleDictionaryReadException extends WordleDictionaryLoadException {
    public WordleDictionaryReadException(String message, Throwable cause) {
        super(message, cause);
    }
}
