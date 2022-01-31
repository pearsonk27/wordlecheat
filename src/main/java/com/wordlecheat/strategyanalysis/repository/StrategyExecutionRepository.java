package com.wordlecheat.strategyanalysis.repository;

import com.wordlecheat.strategyanalysis.object.StrategyExecution;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StrategyExecutionRepository extends CrudRepository<StrategyExecution, Integer> {
    
}
