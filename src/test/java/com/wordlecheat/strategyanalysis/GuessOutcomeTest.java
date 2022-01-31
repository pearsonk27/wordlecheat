package com.wordlecheat.strategyanalysis;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import com.wordlecheat.strategyanalysis.game.GuessOutcome;

import org.junit.jupiter.api.Test;

public class GuessOutcomeTest {
    
    @Test
    void testConstructor() {
        GuessOutcome guessOutcome = new GuessOutcome("Panic", "Oaths");
        List<String> notContainedLetters = new ArrayList<>();
        notContainedLetters.add("O");
        notContainedLetters.add("t");
        notContainedLetters.add("h");
        notContainedLetters.add("s");
        String[] knownLetterPlacements = new String[] { null, "a", null, null, null };
        assertThat(guessOutcome.getNotContainedLetters()).isEqualTo(notContainedLetters);
        assertThat(guessOutcome.getKnownLetterPlacements()).isEqualTo(knownLetterPlacements);
        assertThat(guessOutcome.getContainedLetters()).isEmpty();
    }
}
