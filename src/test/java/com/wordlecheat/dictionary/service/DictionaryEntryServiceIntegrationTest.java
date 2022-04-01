package com.wordlecheat.dictionary.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.wordlecheat.dictionary.object.DictionaryEntry;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class DictionaryEntryServiceIntegrationTest {
    
    @Autowired
    private DictionaryEntryService dictionaryEntryService;

    private String[] knownLetterPlacementsArray;

    private Set<String> containedLetters;

    private Set<String> notContainedLetters;

    private Set<String[]> knownNonLetterPlacementsArraySet;

    @BeforeEach
    void setUp() {
        knownLetterPlacementsArray = new String[] {null, null, "a", null, null};
        containedLetters = new HashSet<>();
        containedLetters.add("r");
        containedLetters.add("t");
        containedLetters.add("e");
        containedLetters.add("a");
        notContainedLetters = new HashSet<>();
        notContainedLetters.add("i");
        notContainedLetters.add("s");
        notContainedLetters.add("o");
        notContainedLetters.add("u");
        notContainedLetters.add("n");
        notContainedLetters.add("d");
        notContainedLetters.add("l");
        notContainedLetters.add("y");
        notContainedLetters.add("m");
        notContainedLetters.add("p");
        notContainedLetters.add("h");
        knownNonLetterPlacementsArraySet = new HashSet<>();
        knownNonLetterPlacementsArraySet.add(new String[] {null, "r", null, "t", "e"});
    }

    @Test
    void testFindAllPossibleWords() {
        List<DictionaryEntry> dictionaryEntries = dictionaryEntryService.findAllPossibleWords(knownLetterPlacementsArray, containedLetters, notContainedLetters, knownNonLetterPlacementsArraySet);
        assertThat(dictionaryEntries).isNotNull();
        for (DictionaryEntry dictionaryEntry : dictionaryEntries) {
            System.out.println(dictionaryEntry);
        }
    }

    // @Test
    // void testFindAllPossibleWordsWithJpql() {
    //     List<DictionaryEntry> dictionaryEntries = dictionaryEntryService.findAllPossibleWordsWithJpql(knownLetterPlacementsArray, containedLetters, notContainedLetters, knownNonLetterPlacementsArraySet);
    //     assertThat(dictionaryEntries).isNotNull();
    // }

    @Test
    void testFindAllPossibleWordsWithNativeSql() {
        List<DictionaryEntry> dictionaryEntries = dictionaryEntryService.findAllPossibleWordsWithNativeSql(knownLetterPlacementsArray, containedLetters, notContainedLetters, knownNonLetterPlacementsArraySet);
        assertThat(dictionaryEntries).isNotNull();
        for (DictionaryEntry dictionaryEntry : dictionaryEntries) {
            System.out.println(dictionaryEntry);
        }
    }
}
