package com.wordlecheat.strategyanalysis.repository;

import java.util.List;

import com.wordlecheat.strategyanalysis.game.LetterPlacement;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = false)
public interface LetterPlacementRepository extends CrudRepository<LetterPlacement, Integer> {
    
    List<LetterPlacement> findByLetterAndStringIndex(String letter, int stringIndex);
}
