package com.wordlecheat.strategyanalysis.repository;

import com.wordlecheat.strategyanalysis.object.GuessInputSuccessRate;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = false)
public interface GuessInputSuccessRateRepository extends CrudRepository<GuessInputSuccessRate, Integer> {
    
}
