package com.wordlecheat.dictionary.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;
import static org.mockito.Mockito.when;

import com.wordlecheat.dictionary.object.DictionaryEntry;
import com.wordlecheat.dictionary.repository.DictionaryEntryRepository;
import com.wordlecheat.strategyanalysis.game.LetterPlacement;
import com.wordlecheat.strategyanalysis.service.LetterPlacementService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class DictionaryEntryServiceTest {

    @Mock
    private LetterPlacementService letterPlacementService;

    @Mock
    private DictionaryEntryRepository dictionaryEntryRepository;

    private LetterPlacement[] letterPlacements = {new LetterPlacement(1, "i", 0), new LetterPlacement(2, "r", 1), new LetterPlacement(3, "a", 2), new LetterPlacement(4, "t", 3), new LetterPlacement(5, "e", 4)};

    private DictionaryEntryService dictionaryEntryService;

    @BeforeEach
    void setUp() {
        dictionaryEntryService = new DictionaryEntryService(letterPlacementService, dictionaryEntryRepository);
        for (LetterPlacement letterPlacement : letterPlacements) {
            when(letterPlacementService.getLetterPlacement(letterPlacement.getLetter(), letterPlacement.getStringIndex())).thenReturn(letterPlacement);
        }
    }
    
    @Test
    public void testSetLetterPlacements() {
        DictionaryEntry dictionaryEntry = new DictionaryEntry();
        dictionaryEntry.setWord("irate");
        dictionaryEntryService.setLetterPlacements(dictionaryEntry);
        assertThat(dictionaryEntry.getLetterPlacements())
          .hasSize(5)
          .extracting(LetterPlacement::getId, LetterPlacement::getLetter, LetterPlacement::getStringIndex)
          .containsExactlyInAnyOrder(
               tuple(1, "i", 0),
               tuple(2, "r", 1),
               tuple(3, "a", 2),
               tuple(4, "t", 3),
               tuple(5, "e", 4)
           ); 
    }
}
