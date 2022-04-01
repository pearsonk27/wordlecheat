package com.wordlecheat.dictionary.service;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.wordlecheat.dictionary.object.DictionaryEntry;
import com.wordlecheat.dictionary.repository.DictionaryEntryRepository;
import com.wordlecheat.logging.LogEntryExit;
import com.wordlecheat.strategyanalysis.game.LetterPlacement;
import com.wordlecheat.strategyanalysis.service.LetterPlacementService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DictionaryEntryService {

    private static final Logger log = LoggerFactory.getLogger(DictionaryEntryService.class);

    private LetterPlacementService letterPlacementService;
    private DictionaryEntryRepository dictionaryEntryRepository;

    @Autowired
    public DictionaryEntryService(LetterPlacementService letterPlacementService, DictionaryEntryRepository dictionaryEntryRepository) {
        this.letterPlacementService = letterPlacementService;
        this.dictionaryEntryRepository = dictionaryEntryRepository;
    }
    
    public void setLetterPlacements(DictionaryEntry dictionaryEntry) {
        String word = dictionaryEntry.getWord();
        log.debug("Setting LetterPlacements for \"{}\"", word);
        Set<LetterPlacement> letterPlacements = IntStream
            .range(0, word.length())
            .mapToObj(i -> (LetterPlacement) letterPlacementService.getLetterPlacement(Character.toString(word.charAt(i)), i))
            .collect(Collectors.toSet());
        dictionaryEntry.setLetterPlacements(letterPlacements);
    }

    @LogEntryExit(showArgs = true, showResult = false, unit = ChronoUnit.MILLIS)
    public List<DictionaryEntry> findAllPossibleWords(String[] knownLetterPlacementsArray, Set<String> containedLetters,
            Set<String> notContainedLetters, Set<String[]> knownNonLetterPlacementsArraySet) {
        return dictionaryEntryRepository.findAllPossibleWords(knownLetterPlacementsArray, containedLetters, notContainedLetters, knownNonLetterPlacementsArraySet);
    }

    // @LogEntryExit(showArgs = true, showResult = false, unit = ChronoUnit.MILLIS)
    // public List<DictionaryEntry> findAllPossibleWordsWithJpql(String[] knownLetterPlacementsArray, Set<String> containedLetters,
    //         Set<String> notContainedLetters, Set<String[]> knownNonLetterPlacementsArraySet) {
    //             List<LetterPlacement> knownLetterPlacements = letterPlacementService.convertToLetterPlacements(knownLetterPlacementsArray);
    //             List<Integer> knownLetterPlacementIds = toIdList(knownLetterPlacements);
    //             List<LetterPlacement> knownNonLetterPlacements = letterPlacementService.convertToLetterPlacements(knownNonLetterPlacementsArraySet);
    //             List<Integer> knownNonLetterPlacementIds = toIdList(knownNonLetterPlacements);
    //             return dictionaryEntryRepository.findByHints(knownLetterPlacementIds, knownNonLetterPlacementIds, containedLetters, notContainedLetters);
    // }

    @LogEntryExit(showArgs = true, showResult = false, unit = ChronoUnit.MILLIS)
    public List<DictionaryEntry> findAllPossibleWordsWithNativeSql(String[] knownLetterPlacementsArray, Set<String> containedLetters,
            Set<String> notContainedLetters, Set<String[]> knownNonLetterPlacementsArraySet) {
                List<LetterPlacement> knownLetterPlacements = letterPlacementService.convertToLetterPlacements(knownLetterPlacementsArray);
                List<Integer> knownLetterPlacementIds = toIdList(knownLetterPlacements);
                List<LetterPlacement> knownNonLetterPlacements = letterPlacementService.convertToLetterPlacements(knownNonLetterPlacementsArraySet);
                List<Integer> knownNonLetterPlacementIds = toIdList(knownNonLetterPlacements);
                return dictionaryEntryRepository.findByHintsWithNativeQuery(knownLetterPlacementIds, knownNonLetterPlacementIds, containedLetters, notContainedLetters, containedLetters.size());
    }

    private List<Integer> toIdList(List<LetterPlacement> knownLetterPlacements) {
        return knownLetterPlacements.stream().map(LetterPlacement::getId).collect(Collectors.toList());
    }
}
