package com.wordlecheat.strategyanalysis.job;

import com.wordlecheat.dictionary.object.DictionaryEntry;
import com.wordlecheat.strategyanalysis.object.Strategy;
import com.wordlecheat.strategyanalysis.object.StrategyExecution;
import com.wordlecheat.strategyanalysis.service.StrategyAnalysisService;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;

@StepScope
public class StrategyExecutionProcessor implements ItemProcessor<DictionaryEntry, StrategyExecution> {

    private StrategyAnalysisService strategyAnalysisService;

    public StrategyExecutionProcessor(StrategyAnalysisService strategyAnalysisService) {
        this.strategyAnalysisService = strategyAnalysisService;
    }

    @Value("#{jobParameters['strategy']}")
    private String strategyName;

    @Override
    public StrategyExecution process(DictionaryEntry dictionaryEntry) throws Exception {
        Strategy strategy = Strategy.valueOf(strategyName);
        return strategyAnalysisService.playGame(strategy, dictionaryEntry);
    }
    
}
