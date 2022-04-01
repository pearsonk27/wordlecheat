package com.wordlecheat.strategyanalysis.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.wordlecheat.strategyanalysis.game.LetterPlacement;
import com.wordlecheat.strategyanalysis.repository.LetterPlacementRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LetterPlacementService {
    
    private LetterPlacementRepository letterPlacementRepository;

    @Autowired
    public LetterPlacementService(LetterPlacementRepository letterPlacementRepository) {
        this.letterPlacementRepository = letterPlacementRepository;
    }

    public LetterPlacement getLetterPlacement(String letter, int index) {
        List<LetterPlacement> letterPlacementsList = letterPlacementRepository.findByLetterAndStringIndex(letter, index);
        LetterPlacement letterPlacement;
        if (letterPlacementsList.isEmpty()) {
            letterPlacement = new LetterPlacement();
            letterPlacement.setLetter(letter);
            letterPlacement.setStringIndex(index);
            letterPlacementRepository.save(letterPlacement);
        } else {
            letterPlacement = letterPlacementsList.get(0);
        }
        return letterPlacement;
    }

    public List<LetterPlacement> convertToLetterPlacements(String[] letterPlacementsArray) {
        List<LetterPlacement> letterPlacements = new ArrayList<>();
        for (int i = 0; i < letterPlacementsArray.length; i++) {
            if (letterPlacementsArray[i] != null) {
                letterPlacements.add(getLetterPlacement(letterPlacementsArray[i], i));
            }
        }
        return letterPlacements;
    }

    public List<LetterPlacement> convertToLetterPlacements(Set<String[]> letterPlacementsArraySet) {
        List<LetterPlacement> letterPlacements = new ArrayList<>();
        for (String[] letterPlacementArray : letterPlacementsArraySet) {
            letterPlacements.addAll(convertToLetterPlacements(letterPlacementArray).stream().filter(e -> letterPlacements.stream()
                .noneMatch(p -> p.getId() == e.getId()))
                .collect(Collectors.toList()));
        }
        return letterPlacements;
    }
}
