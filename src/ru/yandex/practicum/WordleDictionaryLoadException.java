package ru.yandex.practicum;

public class WordleDictionaryLoadException extends Exception{
    public WordleDictionaryLoadException(String message) {
        super(message);
    }

    public WordleDictionaryLoadException(String message, Throwable cause) {
        super(message, cause);
    }
}
