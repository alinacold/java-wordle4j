package ru.yandex.practicum;

public class WordleDictionaryFileNotFoundException extends WordleDictionaryLoadException {
    public WordleDictionaryFileNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
