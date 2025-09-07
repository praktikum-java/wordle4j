package ru.yandex.practicum;

import java.io.PrintWriter;
import java.util.*;

import static ru.yandex.practicum.WordleDictionary.wordHasAllLetters;
import static ru.yandex.practicum.WordleDictionary.wordHasAnyLetter;

public class WordleGame {
    public static final int WORD_LENGTH = 5;
    public static final int MAX_STEPS = 6;
    private final WordleDictionary wholeDictionary = new WordleDictionary();
    private final WordleDictionary gameDictionary = new WordleDictionary();
    private final PrintWriter log;
    private final Set<Character> skipLetters = new HashSet<>();
    private final Set<Character> maybeLetters = new HashSet<>();
    private final List<Character> okLetters = new ArrayList<>();
    private String answer;
    private int steps = -1;

    public WordleGame(PrintWriter log, WordleDictionary someDictionary) throws WordleGameWrongWordException {
        this.log = log;
        initDictionaryFrom(someDictionary);
        wholeDictionary.addAll(gameDictionary.getAll());
        log.println(String.format("В словарь игры загружено %d слов", wholeDictionary.size()));
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) throws WordleGameNoSuchWordException {
        if (gameDictionary.isEmpty() || !gameDictionary.contains(answer)) {
            throw new WordleGameNoSuchWordException(answer);
        }
        this.answer = answer;
    }

    private void initDictionaryFrom(WordleDictionary someDictionary) throws WordleGameWrongWordException {
        for (String word : someDictionary.getAll()) {
            addWord(word);
        }
    }

    public void addWord(String rawWord) throws WordleGameWrongWordException {
        String word = WordleDictionary.normalizeWord(rawWord);
        if (!word.isBlank() && word.length() == WORD_LENGTH) {
            gameDictionary.add(word);
        }
    }

    public boolean isEnd() {
        return steps <= 0;
    }

    public void reset() {
        steps = MAX_STEPS;
        skipLetters.clear();
        maybeLetters.clear();
        okLetters.clear();
        okLetters.addAll(Arrays.asList(new Character[WORD_LENGTH]));

        answer = gameDictionary.getRandomWord();
    }

    private void validateWord(String candidate) throws WordleGameException {
        if (candidate == null || candidate.isBlank() || candidate.contains(" ")) {
            throw new WordleGameWrongWordException(candidate);
        }
        if (candidate.length() != WORD_LENGTH) {
            throw new WordleGameWrongWordLengthException(candidate);
        }
        if (!wholeDictionary.contains(candidate)) {
            throw new WordleGameNoSuchWordException(candidate);
        }
    }

    private String makeResume(String candidate) {
        StringBuilder sb = new StringBuilder();
        for (int c = 0; c < candidate.length(); c++) {
            if (answer.charAt(c) == candidate.charAt(c)) {
                sb.append("+");
            } else if (answer.contains(String.valueOf(candidate.charAt(c)))) {
                sb.append("^");
            } else {
                sb.append("-");
            }
        }
        return sb.toString();
    }

    public String checkWord(String rawCandidate) throws WordleGameException {
        String candidate = WordleDictionary.normalizeWord(rawCandidate);
        validateWord(candidate);
        steps--;
        if (candidate.equalsIgnoreCase(answer)) {
            steps *= -1;
            return "+++++ " + " слово отгадано";
        }
        return makeResume(candidate);
    }

    public String getState() {
        return String.format("попыток %d/%d", Math.abs(steps), MAX_STEPS);
    }

    public String guessWord(String rawCandidate, String resume) throws WordleGameException {
        String candidate = WordleDictionary.normalizeWord(rawCandidate);
        if (resume.isEmpty()) {
            return gameDictionary.getRandomWord(true);
        }
        for (int c = 0; c < candidate.length(); c++) {
            switch (resume.charAt(c)) {
                case '-':
                    skipLetters.add(candidate.charAt(c));
                    break;
                case '^':
                    maybeLetters.add(candidate.charAt(c));
                    break;
                case '+':
                    okLetters.set(c, candidate.charAt(c));
                    break;
                default:
                    throw new RuntimeException("неизвестный символ в коде слова: " + resume.charAt(c));
            }
        }
        Set<Character> allLetters = new HashSet<>(maybeLetters);
        for (Character c : okLetters) {
            if (c != null) {
                allLetters.add(c);
            }
        }

        List<String> nextWords = new ArrayList<>();
        for (String word : gameDictionary.getAll()) {
            if (!skipLetters.isEmpty() && wordHasAnyLetter(word, skipLetters) > 0) { //не содержит неправильных букв
                if (word.equals(answer)) {
                    throw new RuntimeException("ошибочно удален верный ответ " + answer);
                }
                continue;
            }
            if (!allLetters.isEmpty() && !wordHasAllLetters(word, allLetters)) { //содержит правильные буквы по максимуму
                if (word.equals(answer)) {
                    throw new RuntimeException("ошибочно удален верный ответ " + answer);
                }
                continue;
            }
            if (!okLetters.isEmpty() && !WordleDictionary.wordHasLettersInPlace(word, okLetters)) {
                if (word.equals(answer)) {
                    throw new RuntimeException("ошибочно удален верный ответ " + answer);
                }
                continue;
            }
            nextWords.add(word);
        }
        gameDictionary.clear();
        gameDictionary.addAll(nextWords);
        if (nextWords.isEmpty()) {
            throw new WordleEmptyCandidatesException(skipLetters, maybeLetters, okLetters);
        }
        return gameDictionary.getRandomWord(true); //в идеале будет 1 из 1
    }

    public void setOkLetters(Character[] characters) {
        for (int idx = 0; idx < characters.length; idx++) {
            okLetters.set(idx, characters[idx]);
        }
    }

    public void setMaybeLetters(Character[] chars) {
        maybeLetters.addAll(Arrays.asList(chars));
    }

    public void setSkipLetters(Character[] chars) {
        skipLetters.addAll(Arrays.asList(chars));
    }
}
