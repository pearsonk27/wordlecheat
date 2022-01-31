package com.wordlecheat.strategyanalysis.service;

import com.wordlecheat.dictionary.object.DictionaryEntry;
import com.wordlecheat.dictionary.repository.DictionaryEntryRepository;
import com.wordlecheat.strategyanalysis.game.GameState;

import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@Service
public class GuessService {

    private DictionaryEntryRepository dictionaryEntryRepository;

    @Autowired
    public GuessService(DictionaryEntryRepository dictionaryEntryRepository) {
        this.dictionaryEntryRepository = dictionaryEntryRepository;
    }
    
    public DictionaryEntry getDictionaryEntryForWord(String word) {
        return dictionaryEntryRepository.findByWordIgnoreCase(word).get(0);
    }

    public DictionaryEntry getRandomGuess(int wordLength) {
        return dictionaryEntryRepository.getRandomNLetterWord(wordLength);
    }

    public DictionaryEntry getHighestWordFrequencyWord(GameState gameState) {
        return dictionaryEntryRepository.findNextGuess(gameState.getKnownLetterPlacements(), gameState.getContainedLetters(), gameState.getNotContainedLetters(), gameState.getKnownNonLetterPlacements());
    }

    public DictionaryEntry getHighestLetterFrequencyWord(GameState gameState) {
        throw new NotImplementedException();
    }
}
