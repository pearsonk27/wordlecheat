package com.wordlecheat.strategyanalysis.repository;

import com.wordlecheat.strategyanalysis.object.StrategySuccessRate;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;

@Repository
public interface StrategySuccessRateRepository extends CrudRepository<StrategySuccessRate, Integer> {
    
    @Override
    @RestResource(exported = false)
    <S extends StrategySuccessRate> S save(S s);

    @Override
    @RestResource(exported = false)
    void deleteById(Integer s);
}
