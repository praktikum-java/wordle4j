package ru.yandex.practicum;

public class WordleGameWrongWordLengthException extends WordleGameException {

    public WordleGameWrongWordLengthException(String word) {
        super("слово не подходит", word);
    }
}
