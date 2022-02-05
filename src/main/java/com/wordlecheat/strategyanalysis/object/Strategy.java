package com.wordlecheat.strategyanalysis.object;

import java.util.ArrayList;
import java.util.List;

import com.wordlecheat.strategyanalysis.game.GameState;
import com.wordlecheat.strategyanalysis.game.Guess;
import com.wordlecheat.strategyanalysis.service.GuessService;

public enum Strategy {

    RANDOM_GUESS(GuessStrategy.PREFER_WORD_FREQUENCY, (GuessService guessService, GameState gameState) -> guessService.getRandomGuess(gameState, gameState.getKnownLetterPlacements().length)),
    ONE_EXPLORATION(GuessStrategy.PREFER_WORD_FREQUENCY, "Irate"),
    TWO_EXPLORATIONS(GuessStrategy.PREFER_WORD_FREQUENCY, "Irate", "Sound"),
    THREE_EXPLORATIONS(GuessStrategy.PREFER_WORD_FREQUENCY, "Irate", "Sound", "Lymph"),
    ONE_EXPLORATION_SOARE(GuessStrategy.PREFER_WORD_FREQUENCY, "Soare");

    private List<GuessMethod> guessMethods;

    Strategy(GuessStrategy remainingGuessMethodStrategy, String... words) {
        this.guessMethods = new ArrayList<>();
        for (String word : words) {
            this.guessMethods.add((GuessService guessService, GameState gameState) -> guessService.getDictionaryEntryForWord(gameState, word));
        }
        fillRemainingGuessMethods(remainingGuessMethodStrategy);
    }

    Strategy(GuessStrategy remainingGuessMethodStrategy, GuessMethod... guessMethods) {
        this.guessMethods = new ArrayList<>();
        for (GuessMethod guessMethod : guessMethods) {
            this.guessMethods.add(guessMethod);
        }
        fillRemainingGuessMethods(remainingGuessMethodStrategy);
    }

    private void fillRemainingGuessMethods(GuessStrategy remainingGuessMethodStrategy) {
        while (guessMethods.size() < 6) {
            if (remainingGuessMethodStrategy == GuessStrategy.PREFER_WORD_FREQUENCY) {
                guessMethods.add((GuessService guessService, GameState gameState) -> guessService.getHighestWordFrequencyWord(gameState));
            } else if (remainingGuessMethodStrategy == GuessStrategy.PREFER_LETTER_FREQUENCY) {
                guessMethods.add((GuessService guessService, GameState gameState) -> guessService.getHighestLetterFrequencyWord(gameState));
            }
        }
    }

    public Guess getGuess(GuessService guessService, GameState gameState) {
        return guessMethods.get(gameState.getGuessNumber() - 1).makeGuess(guessService, gameState);
    }

    private enum GuessStrategy {
        PREFER_LETTER_FREQUENCY, PREFER_WORD_FREQUENCY;
    }
}
