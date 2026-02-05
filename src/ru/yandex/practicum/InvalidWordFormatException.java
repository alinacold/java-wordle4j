package ru.yandex.practicum;

public class InvalidWordFormatException extends WordleGameException {
    public InvalidWordFormatException(String message) {
        super(message);
    }
}
