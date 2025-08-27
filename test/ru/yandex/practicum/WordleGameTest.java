package ru.yandex.practicum;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.PrintWriter;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class WordleGameTest {

    private static PrintWriter log;
    private static WordleDictionary dictionary;

    private WordleGame game;

    @BeforeAll
    static void loadWords() throws IOException {
        log = new PrintWriter(System.out, true);
        WordleDictionaryLoader loader = new WordleDictionaryLoader(log);
        dictionary = loader.loadDictionaryFromFile(Wordle.WORDS_FILE);
    }

    @BeforeEach
    void setUp() throws WordleGameWrongWordException {
        game = new WordleGame(log, dictionary);
        game.reset();
    }

    @Test
    void guessWordWithSomeStateTest() throws WordleGameException {
        game.setAnswer("стоик");
        game.setOkLetters(new Character[]{'с', null, null, 'и', null});
        game.setMaybeLetters(new Character[]{'т', 'с', 'и'});
        game.setSkipLetters(new Character[]{'у', 'ш', 'й', 'ы', 'ь', 'п'});
        assertNotNull(game.guessWord("совет", "+^--^"));
    }
}
