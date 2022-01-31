package com.wordlecheat.strategyanalysis.game;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.wordlecheat.dictionary.object.DictionaryEntry;

public class GuessOutcome {
    
    private String wordleWord;
    private String guess;
    private String[] knownLetterPlacements;
    private Set<String> containedLetters;
    private Set<String> notContainedLetters;
    private String[] knownNonLetterPlacements;

    public GuessOutcome(DictionaryEntry wordleWord, Guess guess) {
        this(wordleWord.getWord(), guess.getDictionaryEntry().getWord());
    }

    public GuessOutcome(String wordleWord, String guess) {
        this.wordleWord = wordleWord;
        this.guess = guess;
        knownLetterPlacements = new String[wordleWord.length()];
        containedLetters = new HashSet<>();
        notContainedLetters = new HashSet<>();
        knownNonLetterPlacements = new String[wordleWord.length()];
        for (int i = 0; i < wordleWord.length(); i++) {
            char guessedLetter = guess.charAt(i);
            if (lettersAreEqualIgnoringCase(guessedLetter, wordleWord.charAt(i))) {
                knownLetterPlacements[i] = String.valueOf(guessedLetter);
            } else if (wordContainsLetterIgnoringCase(wordleWord, guessedLetter)) {
                containedLetters.add(String.valueOf(guessedLetter));
                knownNonLetterPlacements[i] = String.valueOf(guessedLetter);
            } else {
                notContainedLetters.add(String.valueOf(guessedLetter));
            }
        }
    }

    private boolean wordContainsLetterIgnoringCase(String word, char letter) {
        return word.toLowerCase().indexOf(Character.toLowerCase(letter)) != -1;
    }

    private boolean lettersAreEqualIgnoringCase(char guessedLetter, char wordleLetter) {
        return Character.toLowerCase(guessedLetter) == Character.toLowerCase(wordleLetter);
    }

    public boolean isCorrectGuess() {
        return wordleWord.equalsIgnoreCase(guess);
    }

    public String[] getKnownLetterPlacements() {
        return knownLetterPlacements;
    }

    public Set<String> getContainedLetters() {
        return containedLetters;
    }

    public Set<String> getNotContainedLetters() {
        return notContainedLetters;
    }

    public String[] getKnownNonLetterPlacements() {
        if (containedLetters.isEmpty()) {
            return null;
        }
        return knownNonLetterPlacements;
    }

    @Override
    public String toString() {
        return "GuessOutcome [containedLetters=" + containedLetters.toString() + ", guess=" + guess + ", knownLetterPlacements="
                + Arrays.toString(knownLetterPlacements) + ", notContainedLetters=" + notContainedLetters.toString()
                + ", wordleWord=" + wordleWord + "]";
    }
}
