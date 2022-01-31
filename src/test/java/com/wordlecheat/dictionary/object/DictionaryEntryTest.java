package com.wordlecheat.dictionary.object;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class DictionaryEntryTest {

    private static String WORD_A_ENTRY = "A (prep.) In process of; in the act of; into; to; -- used with verbal substantives in -ing which begin with a consonant. This is a shortened form of the preposition an (which was used before the vowel sound); as in a hunting, a building, a begging.";
    private static String WORD_A = "A";

    private DictionaryEntry dictionaryEntry;

    @Test
    void testConstructor() {
        dictionaryEntry = new DictionaryEntry(WORD_A_ENTRY);
        assertThat(dictionaryEntry.getWord()).isEqualTo(WORD_A);
    }
}
