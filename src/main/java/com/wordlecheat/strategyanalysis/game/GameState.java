package com.wordlecheat.strategyanalysis.game;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class GameState {

    private Set<String> containedLetters;
    private Set<String> notContainedLetters;
    private String[] knownLetterPlacements;
    private Set<String[]> knownNonLetterPlacements;
    private int guessNumber;
    
    public GameState(int wordLength) {
        containedLetters = new HashSet<>();
        notContainedLetters = new HashSet<>();
        knownLetterPlacements = new String[wordLength];
        knownNonLetterPlacements = new HashSet<>();
    }

    public void updateGameState(GuessOutcome guessOutcome) {
        for (int i = 0; i < knownLetterPlacements.length; i++) {
            if (knownLetterPlacements[i] == null) {
                knownLetterPlacements[i] = guessOutcome.getKnownLetterPlacements()[i];
            }
        }
        containedLetters.addAll(guessOutcome.getContainedLetters());
        notContainedLetters.addAll(guessOutcome.getNotContainedLetters());
        if (guessOutcome.getKnownNonLetterPlacements() != null) {
            knownNonLetterPlacements.add(guessOutcome.getKnownNonLetterPlacements());
        }
    }

    public Set<String> getContainedLetters() {
        return containedLetters;
    }

    public Set<String> getNotContainedLetters() {
        return notContainedLetters;
    }

    public String[] getKnownLetterPlacements() {
        return knownLetterPlacements;
    }

    public Set<String[]> getKnownNonLetterPlacements() {
        return knownNonLetterPlacements;
    }

    public int getGuessNumber() {
        return guessNumber;
    }

    public void setGuessNumber(int guessNumber) {
        this.guessNumber = guessNumber;
    }

    @Override
    public String toString() {
        return "GameState [containedLetters=" + containedLetters + ", knownLetterPlacements="
                + Arrays.toString(knownLetterPlacements) + ", knownNonLetterPlacements=" + knownNonLetterPlacements
                + ", notContainedLetters=" + notContainedLetters + "]";
    }
}
