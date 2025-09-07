package ru.yandex.practicum;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class WordleDictionary {

    private final List<String> words = new ArrayList<>();

    private final Random random = new Random();

    public static int wordHasAnyLetter(String word, Collection<Character> letters) {
        int count = 0;
        for (int i = 0; i < word.length(); i++) {
            if (letters.contains(word.charAt(i))) {
                count++;
            }
        }
        return count;
    }

    public static boolean wordHasAllLetters(String word, Collection<Character> letters) {
        for (Character c : letters) {
            if (!word.contains(String.valueOf(c))) {
                return false;
            }
        }
        return true;
    }

    public static boolean wordHasLettersInPlace(String word, List<Character> okLetters) {
        for (int c = 0; c < word.length(); c++) {
            if (okLetters.get(c) != null && okLetters.get(c) != word.charAt(c)) {
                return false;
            }
        }
        return true;
    }

    public static String normalizeWord(String rawWord) throws WordleGameWrongWordException {
        if (rawWord == null) {
            throw new WordleGameWrongWordException(rawWord);
        }
        return rawWord.toLowerCase().replaceAll("ё", "е");
    }

    public void add(String word) {
        words.add(word);
    }

    public Collection<String> getAll() {
        return words;
    }

    public boolean contains(String candidate) {
        return words.contains(candidate);
    }

    public boolean isEmpty() {
        return words.isEmpty();
    }

    public void addAll(Collection<String> words) {
        this.words.addAll(words);
    }

    public int size() {
        return words.size();
    }

    public void clear() {
        words.clear();
    }

    public String getRandomWord() {
        return getRandomWord(false);
    }

    public String getRandomWord(boolean remove) {
        int index = random.nextInt(words.size());
        if (remove) {
            return words.remove(index);
        } else {
            return words.get(index);
        }
    }
}
