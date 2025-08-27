package ru.yandex.practicum;

public class WordleGameNoSuchWordException extends WordleGameException {

    public WordleGameNoSuchWordException(String word) {
        super("слово не существует", word);
    }
}
