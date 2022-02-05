package com.wordlecheat.dictionary.repository;

import java.util.List;
import java.util.Set;

import com.wordlecheat.dictionary.object.DictionaryEntry;

public interface DictionaryEntryRepositoryCustom {
    DictionaryEntry findNextGuess(String[] knownLetterPlacements, Set<String> containedLetters, Set<String> notContainedLetters, Set<String[]> knownNonLetterPlacements);

    DictionaryEntry getRandomNLetterWord(int n);

    List<DictionaryEntry> findAllPossibleWords(String[] knownLetterPlacements, Set<String> containedLetters, Set<String> notContainedLetters, Set<String[]> knownNonLetterPlacements);

    long getCountOfPossibleWords(String[] knownLetterPlacements, Set<String> containedLetters, Set<String> notContainedLetters, Set<String[]> knownNonLetterPlacements);
}
