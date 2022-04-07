package com.wordlecheat.strategyanalysis.service;

import com.wordlecheat.dictionary.object.DictionaryEntry;
import com.wordlecheat.dictionary.repository.DictionaryEntryRepository;
import com.wordlecheat.strategyanalysis.game.GameState;
import com.wordlecheat.strategyanalysis.game.Guess;
import com.wordlecheat.strategyanalysis.game.GuessOutcome;
import com.wordlecheat.strategyanalysis.object.Strategy;
import com.wordlecheat.strategyanalysis.object.StrategyExecution;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

@Service
public class StrategyAnalysisService {

    private static final Logger log = LoggerFactory.getLogger(StrategyAnalysisService.class);

    private DictionaryEntryRepository dictionaryEntryRepository;
    private GuessService guessService;
    
    @Autowired
    public StrategyAnalysisService(DictionaryEntryRepository dictionaryEntryRepository,
            GuessService guessService) {
        this.dictionaryEntryRepository = dictionaryEntryRepository;
        this.guessService = guessService;
    }

    public StrategyExecution playGame(Strategy strategy, String wordleWord) {
        DictionaryEntry dictionaryEntry = dictionaryEntryRepository.findByWordIgnoreCase(wordleWord).get(0);
        return playGame(strategy, dictionaryEntry);
    }

    public StrategyExecution playGame(Strategy strategy, DictionaryEntry dictionaryEntry) {
        StrategyExecution strategyExecution = new StrategyExecution(strategy, dictionaryEntry);
        GameState gameState = new GameState(dictionaryEntry.getWord().length());
        while (strategyExecution.isInProgress()) {
            try {
                makeGuess(strategyExecution, gameState);
            } catch (EmptyResultDataAccessException ex) {
                log.warn("No word found ({})", gameState);
            }
            GuessOutcome guessOutcome = new GuessOutcome(dictionaryEntry, strategyExecution.getCurrentGuess());
            gameState.updateGameState(guessOutcome);
            strategyExecution.setSuccess(guessOutcome.isCorrectGuess());
        }
        return strategyExecution;
    }

    private void makeGuess(StrategyExecution strategyExecution, GameState gameState) {
        gameState.setGuessNumber(strategyExecution.getGuesses().size() + 1);
        Guess guess = strategyExecution.getStrategy().getGuess(guessService, gameState);
        strategyExecution.addGuess(guess);
    }
}
