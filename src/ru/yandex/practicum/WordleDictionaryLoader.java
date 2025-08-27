package ru.yandex.practicum;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class WordleDictionaryLoader {

    private final PrintWriter log;

    public WordleDictionaryLoader(PrintWriter log) {
        this.log = log;
    }

    public File getDictionaryFile(String dictionaryFile) throws FileNotFoundException {
        Path wordsFilePath = Paths.get("", dictionaryFile);
        log.println("попытка открыть файл словаря: " + wordsFilePath.toAbsolutePath());
        File wordsFile = wordsFilePath.toFile();
        if (!wordsFile.exists()) {
            throw new FileNotFoundException();
        }
        return wordsFile;
    }

    public WordleDictionary loadDictionaryFromFile(String dictionaryFilename) throws IOException {
        WordleDictionary wordleDictionary = new WordleDictionary();
        File dictionaryFile = getDictionaryFile(dictionaryFilename);
        log.println("открыт файл словаря: " + dictionaryFile.getAbsolutePath());
        wordleDictionary.addAll(loadWordsToList(dictionaryFile));
        return wordleDictionary;
    }

    public List<String> loadWordsToList(File wordsFile) throws IOException {
        log.println("загружаем слова из файла");
        List<String> words = new ArrayList<>();
        try (FileReader fileReader = new FileReader(wordsFile, StandardCharsets.UTF_8)) {
            BufferedReader wordsReader = new BufferedReader(fileReader);
            while (wordsReader.ready()) {
                String line = wordsReader.readLine();
                words.add(line);
            }
            log.printf("загружено %d слов\n", words.size());
        } catch (IOException e) {
            log.println("Ошибка чтения файла");
            throw e;
        }
        return words;
    }

}
