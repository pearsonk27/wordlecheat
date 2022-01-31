package com.wordlecheat.strategyanalysis.repository;

import com.wordlecheat.strategyanalysis.object.StrategySuccessRate;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StrategySuccessRateRepository extends CrudRepository<StrategySuccessRate, Integer> {
    
}
