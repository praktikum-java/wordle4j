package ru.yandex.practicum;

public class WordleGameWrongWordException extends WordleGameException {

    public WordleGameWrongWordException(String word) {
        super("неправильное слово", word);
    }
}
