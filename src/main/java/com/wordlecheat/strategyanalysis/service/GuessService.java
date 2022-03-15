package com.wordlecheat.strategyanalysis.service;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.wordlecheat.dictionary.object.DictionaryEntry;
import com.wordlecheat.dictionary.repository.DictionaryEntryRepository;
import com.wordlecheat.strategyanalysis.game.GameState;
import com.wordlecheat.strategyanalysis.game.Guess;
import com.wordlecheat.strategyanalysis.game.LetterPlacement;
import com.wordlecheat.strategyanalysis.repository.LetterPlacementRepository;

import org.hibernate.service.spi.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@Service
public class GuessService {

    private static final Logger log = LoggerFactory.getLogger(GuessService.class);

    private DictionaryEntryRepository dictionaryEntryRepository;
    private LetterPlacementRepository letterPlacementRepository;

    @Autowired
    public GuessService(DictionaryEntryRepository dictionaryEntryRepository, LetterPlacementRepository letterPlacementRepository) {
        this.dictionaryEntryRepository = dictionaryEntryRepository;
        this.letterPlacementRepository = letterPlacementRepository;
    }
    
    public Guess getDictionaryEntryForWord(GameState gameState, String word) {
        long startTime = System.currentTimeMillis();
        DictionaryEntry dictionaryEntry;
        try {
            dictionaryEntry = dictionaryEntryRepository.findByWordIgnoreCase(word).get(0);
        } catch (IndexOutOfBoundsException e) {
            throw new ServiceException("Cannot find word '" + word + "' in database", e);
        }
        log.info("{} found in {} ms", word, System.currentTimeMillis() - startTime);

        startTime = System.currentTimeMillis();
        int countPossibleWords = (int) dictionaryEntryRepository.getCountOfPossibleWords(gameState.getKnownLetterPlacements(), gameState.getContainedLetters(), gameState.getNotContainedLetters(), gameState.getKnownNonLetterPlacements());
        log.info("Count of remaining possible words found in {} ms", System.currentTimeMillis() - startTime);

        Guess guess = new Guess(dictionaryEntry, gameState.getGuessNumber(), countPossibleWords);
        setGuessInputs(guess, gameState);
        return guess;
    }

    public Guess getRandomGuess(GameState gameState, int wordLength) {
        long startTime = System.currentTimeMillis();
        Guess guess = new Guess(dictionaryEntryRepository.getRandomNLetterWord(wordLength), gameState.getGuessNumber(), (int) dictionaryEntryRepository.getCountOfPossibleWords(gameState.getKnownLetterPlacements(), gameState.getContainedLetters(), gameState.getNotContainedLetters(), gameState.getKnownNonLetterPlacements()));
        log.info("Random {} letter word found in {}", wordLength, System.currentTimeMillis() - startTime);
        setGuessInputs(guess, gameState);
        return guess;
    }

    public Guess getHighestWordFrequencyWord(GameState gameState) {
        long startTime = System.currentTimeMillis();
        List<DictionaryEntry> dictionaryEntries = dictionaryEntryRepository.findAllPossibleWords(gameState.getKnownLetterPlacements(), gameState.getContainedLetters(), gameState.getNotContainedLetters(), gameState.getKnownNonLetterPlacements());
        log.info("Found list of all possible words ({}) in {} ms", dictionaryEntries.size(), System.currentTimeMillis() - startTime);

        startTime = System.currentTimeMillis();
        DictionaryEntry dictionaryEntry = Collections.max(dictionaryEntries, Comparator.comparing(c -> c.getFrequency()));
        log.info("Found max frequency word in {} ms", System.currentTimeMillis() - startTime);

        Guess guess = new Guess(dictionaryEntry, gameState.getGuessNumber(), dictionaryEntries.size());
        setGuessInputs(guess, gameState);
        return guess;
    }

    private void setGuessInputs(Guess guess, GameState gameState) {
        long startTime = System.currentTimeMillis();
        Set<String> containedLettersInput = new HashSet<>();
        containedLettersInput.addAll(gameState.getContainedLetters());
        guess.setContainedLettersInput(containedLettersInput);
        Set<String> notContainedLettersInput = new HashSet<>();
        notContainedLettersInput.addAll(gameState.getNotContainedLetters());
        guess.setNotContainedLettersInput(notContainedLettersInput);
        Set<LetterPlacement> knownLetterPlacementsInput = convertToLetterPlacements(gameState.getKnownLetterPlacements());
        guess.setKnownLetterPlacementsInput(knownLetterPlacementsInput);
        Set<LetterPlacement> knownNonLetterPlacementsInput = new HashSet<>();
        for (String[] knownLetterNonPlacementInput : gameState.getKnownNonLetterPlacements()) {
            addNewOnly(knownNonLetterPlacementsInput, convertToLetterPlacements(knownLetterNonPlacementInput));
        }
        guess.setKnownNonLetterPlacementsInput(knownNonLetterPlacementsInput);
        log.info("Guess Inputs built in {} ms", System.currentTimeMillis() - startTime);
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

    public Guess getHighestLetterFrequencyWord(GameState gameState) {
        long startTime = System.currentTimeMillis();
        List<DictionaryEntry> dictionaryEntries = dictionaryEntryRepository.findAllPossibleWords(gameState.getKnownLetterPlacements(), gameState.getContainedLetters(), gameState.getNotContainedLetters(), gameState.getKnownNonLetterPlacements());
        log.info("Found list of all possible words ({}) in {} ms", dictionaryEntries.size(), System.currentTimeMillis() - startTime);

        startTime = System.currentTimeMillis();
        DictionaryEntry dictionaryEntry = Collections.max(dictionaryEntries, Comparator.comparing(c -> c.getLetterFrequency()));
        log.info("Found max letterFrequency word in {} ms", System.currentTimeMillis() - startTime);

        Guess guess = new Guess(dictionaryEntry, gameState.getGuessNumber(), dictionaryEntries.size());
        setGuessInputs(guess, gameState);
        return guess;
    }
}
