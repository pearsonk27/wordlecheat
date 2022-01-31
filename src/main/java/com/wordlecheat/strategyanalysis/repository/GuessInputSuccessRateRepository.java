package com.wordlecheat.strategyanalysis.repository;

import com.wordlecheat.strategyanalysis.object.GuessInputSuccessRate;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GuessInputSuccessRateRepository extends CrudRepository<GuessInputSuccessRate, Integer> {
    
}
