package com.wordlecheat.dictionary.object;

import org.junit.jupiter.api.Test;

public class LetterEnumTest {
    
    /**
     * Made just to build SQL Queries to populate the letter_frequency values in the dictionary table
     */
    @Test
    void testGetSqlUpdates() {
        for (LetterEnum letter : LetterEnum.values()) {
            String message = "update dictionary\n";
            message += String.format("set letter_frequency = letter_frequency + %s\n", letter.getFrequency());
            message += "where frequency is not null\n\t";
            message += "and word ilike '%" + letter.toString() + "%';\n";
            System.out.println(message);
        }
    }
}
