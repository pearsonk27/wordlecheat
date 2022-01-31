package com.wordlecheat.strategyanalysis.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.wordlecheat.dictionary.object.DictionaryEntry;
import com.wordlecheat.dictionary.repository.DictionaryEntryRepository;
import com.wordlecheat.strategyanalysis.game.GameState;
import com.wordlecheat.strategyanalysis.game.Guess;
import com.wordlecheat.strategyanalysis.game.GuessOutcome;
import com.wordlecheat.strategyanalysis.game.LetterPlacement;
import com.wordlecheat.strategyanalysis.object.Strategy;
import com.wordlecheat.strategyanalysis.object.StrategyExecution;
import com.wordlecheat.strategyanalysis.repository.LetterPlacementRepository;
import com.wordlecheat.strategyanalysis.repository.StrategyExecutionRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@Service
public class StrategyAnalysisService {

    private static final Logger log = LoggerFactory.getLogger(StrategyAnalysisService.class);

    private DictionaryEntryRepository dictionaryEntryRepository;
    private StrategyExecutionRepository strategyExecutionRepository;
    private LetterPlacementRepository letterPlacementRepository;
    private GuessService guessService;
    
    @Autowired
    public StrategyAnalysisService(DictionaryEntryRepository dictionaryEntryRepository,
    StrategyExecutionRepository strategyExecutionRepository, LetterPlacementRepository letterPlacementRepository, GuessService guessService) {
        this.dictionaryEntryRepository = dictionaryEntryRepository;
        this.strategyExecutionRepository = strategyExecutionRepository;
        this.letterPlacementRepository = letterPlacementRepository;
        this.guessService = guessService;
    }

    public void playGame(Strategy strategy, String wordleWord) {
        DictionaryEntry dictionaryEntry = dictionaryEntryRepository.findByWordIgnoreCase(wordleWord).get(0);
        playGame(strategy, dictionaryEntry);
    }

    public void playGame(Strategy strategy, DictionaryEntry dictionaryEntry) {
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
        strategyExecutionRepository.save(strategyExecution);
    }

    private void makeGuess(StrategyExecution strategyExecution, GameState gameState) {
        int guessNumber = strategyExecution.getGuesses().size() + 1;
        DictionaryEntry dictionaryEntry = strategyExecution.getStrategy().getGuess(guessNumber, guessService, gameState);
        Guess guess = new Guess(dictionaryEntry, guessNumber);
        setGuessInputs(guess, strategyExecution, gameState.getKnownLetterPlacements(), gameState.getContainedLetters(), gameState.getNotContainedLetters(), gameState.getKnownNonLetterPlacements());
        strategyExecution.addGuess(guess);
    }

    private void setGuessInputs(Guess guess, StrategyExecution strategyExecution, String[] knownLetterPlacements, Set<String> containedLetters, Set<String> notContainedLetters, Set<String[]> knownNonLetterPlacements) {
        Set<String> containedLettersInput = new HashSet<>();
        containedLettersInput.addAll(containedLetters);
        guess.setContainedLettersInput(containedLettersInput);
        Set<String> notContainedLettersInput = new HashSet<>();
        notContainedLettersInput.addAll(notContainedLetters);
        guess.setNotContainedLettersInput(notContainedLettersInput);
        Set<LetterPlacement> knownLetterPlacementsInput = convertToLetterPlacements(knownLetterPlacements);
        guess.setKnownLetterPlacementsInput(knownLetterPlacementsInput);
        Set<LetterPlacement> knownNonLetterPlacementsInput = new HashSet<>();
        for (String[] knownLetterNonPlacementInput : knownNonLetterPlacements) {
            addNewOnly(knownNonLetterPlacementsInput, convertToLetterPlacements(knownLetterNonPlacementInput));
        }
        guess.setKnownNonLetterPlacementsInput(knownNonLetterPlacementsInput);
    }

    private void addNewOnly(Set<LetterPlacement> gameStateKnownNonLetterPlacements, Set<LetterPlacement> addedKnownNonLetterPlacements) {
        for (LetterPlacement addedLetterPlacement : addedKnownNonLetterPlacements) {
            boolean addLetterPlacement = true;
            for (LetterPlacement gameStateLetterPlacement : gameStateKnownNonLetterPlacements) {
                if (gameStateLetterPlacement.getLetter().equalsIgnoreCase(addedLetterPlacement.getLetter()) && gameStateLetterPlacement.getStringIndex() == addedLetterPlacement.getStringIndex()) {
                    addLetterPlacement = false;
                }
            }
            if (addLetterPlacement) {
                gameStateKnownNonLetterPlacements.add(addedLetterPlacement);
            }
        }
    }

    private Set<LetterPlacement> convertToLetterPlacements(String[] letters) {
        Set<LetterPlacement> letterPlacements = new HashSet<>();
        for (int i = 0; i < letters.length; i++) {
            String letter = letters[i];
            if (letter != null) {
                List<LetterPlacement> letterPlacementsList = letterPlacementRepository.findByLetterAndStringIndex(letter, i);
                LetterPlacement letterPlacement;
                if (letterPlacementsList.isEmpty()) {
                    letterPlacement = new LetterPlacement();
                    letterPlacement.setLetter(letter);
                    letterPlacement.setStringIndex(i);
                    letterPlacementRepository.save(letterPlacement);
                } else {
                    letterPlacement = letterPlacementsList.get(0);
                }
                letterPlacements.add(letterPlacement);
            }
        }
        return letterPlacements;
    }
}
