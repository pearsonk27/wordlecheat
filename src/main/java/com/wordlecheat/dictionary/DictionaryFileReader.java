package com.wordlecheat.dictionary;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DictionaryFileReader {

    private static final Logger log = LoggerFactory.getLogger(DictionaryFileReader.class);

    private static String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private int currentLetterIndex;
    private BufferedReader dictionaryReader;

    public DictionaryFileReader() {
        currentLetterIndex = 0;
        setDictionaryReader();
    }

    private void setDictionaryReader() {
        InputStream inputStream = getCurrentLetterDictionary();
        InputStreamReader streamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        dictionaryReader = new BufferedReader(streamReader);
    }

    public String getNextDictionaryEntry() throws IOException {
        String entry = dictionaryReader.readLine();
        if (entry == null) {
            if (currentLetterIndex++ >= ALPHABET.length() - 1) {
                return entry;
            } else {
                setDictionaryReader();
                return getNextDictionaryEntry();
            }
        }
        if (entry.isBlank()) {
            return getNextDictionaryEntry();
        }
        if ("\"".equals(entry.substring(0, 1))) {
            return entry.substring(1, entry.length() - 1);
        }
        return entry;
    }
    
    private InputStream getCurrentLetterDictionary() {
        String currentLetter = ALPHABET.substring(currentLetterIndex, currentLetterIndex + 1);
        String resourceFile = String.format("dictionary/%s.csv", currentLetter);
        log.info("Pulling words from dictionary partition {}", currentLetter);
        return getFileFromResourceAsStream(resourceFile);
    }

    // get a file from the resources folder
    // works everywhere, IDEA, unit test and JAR file.
    private InputStream getFileFromResourceAsStream(String fileName) {

        // The class loader that loaded the class
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(fileName);

        // the stream holding the file content
        if (inputStream == null) {
            throw new IllegalArgumentException("file not found! " + fileName);
        } else {
            return inputStream;
        }

    }
}
