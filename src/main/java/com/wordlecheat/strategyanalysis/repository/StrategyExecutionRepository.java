package com.wordlecheat.strategyanalysis.repository;

import com.wordlecheat.strategyanalysis.object.StrategyExecution;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;

@Repository
public interface StrategyExecutionRepository extends PagingAndSortingRepository<StrategyExecution, Integer> {
    
    @Override
    @RestResource(exported = false)
    <S extends StrategyExecution> S save(S s);

    @Override
    @RestResource(exported = false)
    void deleteById(Integer s);
}
