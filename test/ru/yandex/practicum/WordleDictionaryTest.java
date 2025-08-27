package ru.yandex.practicum;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;

class WordleDictionaryTest {

    @Test
    void wordHasAnyLetter() {
        assertEquals(0, WordleDictionary.wordHasAnyLetter("стоик", new HashSet<>(Arrays.asList('в', 'ч', 'е'))));
        assertEquals(1, WordleDictionary.wordHasAnyLetter("стоик", new HashSet<>(Arrays.asList('в', 'т', 'е'))));
    }
}
