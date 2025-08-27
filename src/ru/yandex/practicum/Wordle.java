package ru.yandex.practicum;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Scanner;

public class Wordle {

    public static final String WORDS_FILE = "words_ru.txt";

    private final PrintWriter log;

    Wordle(PrintWriter log) {
        this.log = log;
    }

    public static void main(String[] args) {
        try (FileOutputStream fos = new FileOutputStream("log." + Instant.now().getEpochSecond() + ".txt"); Writer writer = new OutputStreamWriter(fos, StandardCharsets.UTF_8)) {
            PrintWriter log = new PrintWriter(writer, true);
            try {
                WordleDictionary wordleDictionary = new WordleDictionaryLoader(log).loadDictionaryFromFile(WORDS_FILE);
                WordleGame wordleGame = new WordleGame(log, wordleDictionary);
                new Wordle(log).playGame(wordleGame);
            } catch (Exception e) {
                e.printStackTrace(log);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void playGame(WordleGame wordleGame) throws WordleGameException {
        log.println("начало игры");
        Scanner scanner = new Scanner(System.in);
        wordleGame.reset();
        String lastResume = "";
        String lastCandidate = "";
        System.out.println("Угадайте слово из пяти букв, у вас шесть попыток \nEnter - ввод слова или подсказка");
        while (!wordleGame.isEnd()) {
            log.println("ждём ввода слова");
            String candidate = scanner.nextLine();
            String guess = wordleGame.guessWord(lastCandidate, lastResume);
            if (candidate.isBlank()) {
                log.println("пользователь воспользовался подсказкой: " + guess);
                candidate = guess;
                System.out.println(candidate);
            } else {
                log.println("пользователь ввёл слово: " + candidate);
            }

            String resume;
            try {
                resume = wordleGame.checkWord(candidate);
                String state = wordleGame.getState();
                log.println("результат проверки слова получен");
                System.out.println(resume + " " + state);
                lastResume = resume;
                lastCandidate = candidate;
            } catch (WordleGameException e) {
                System.out.println(e.getMessage());
                log.println("ошибка при проверке слова");
                e.printStackTrace(log);
            }
        }
        System.out.println("Правильный ответ: " + wordleGame.getAnswer());
        log.println("игра окончена");
    }

}
