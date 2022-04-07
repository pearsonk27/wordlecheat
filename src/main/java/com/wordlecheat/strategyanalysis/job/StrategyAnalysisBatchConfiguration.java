package com.wordlecheat.strategyanalysis.job;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wordlecheat.dictionary.object.DictionaryEntry;
import com.wordlecheat.dictionary.repository.DictionaryEntryRepository;
import com.wordlecheat.strategyanalysis.object.StrategyExecution;
import com.wordlecheat.strategyanalysis.repository.StrategyExecutionRepository;
import com.wordlecheat.strategyanalysis.service.StrategyAnalysisService;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort.Direction;

@Configuration
@EnableBatchProcessing
public class StrategyAnalysisBatchConfiguration {

    public static final String STRATEGY_ANALYSIS_JOB_NAME = "strategyAnalysisJob";

    private DictionaryEntryRepository dictionaryEntryRepository;
    private StrategyAnalysisService strategyAnalysisService;
    private StrategyExecutionRepository strategyExecutionRepository;
    private JobBuilderFactory jobBuilderFactory;
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    public StrategyAnalysisBatchConfiguration(DictionaryEntryRepository dictionaryEntryRepository, StrategyAnalysisService strategyAnalysisService, StrategyExecutionRepository strategyExecutionRepository, JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
        this.dictionaryEntryRepository = dictionaryEntryRepository;
        this.strategyAnalysisService = strategyAnalysisService;
        this.strategyExecutionRepository = strategyExecutionRepository;
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
    }
    
    @Bean
    public RepositoryItemReader<DictionaryEntry> dictionaryEntryReader() {
        RepositoryItemReader<DictionaryEntry> reader = new RepositoryItemReader<>();
        reader.setRepository(dictionaryEntryRepository);
        reader.setMethodName("findByFrequencyGreaterThan");
        List<Object> queryMethodArguments = new ArrayList<>();
        queryMethodArguments.add(0.0);
        reader.setArguments(queryMethodArguments);
        reader.setPageSize(100);
        Map<String, Direction> sorts = new HashMap<>();
        sorts.put("id", Direction.ASC);
        reader.setSort(sorts);
        return reader;
    }

    @Bean
    @StepScope
    public StrategyExecutionProcessor strategyExecutionProcessor() {
        return new StrategyExecutionProcessor(strategyAnalysisService);
    }

    @Bean
    public RepositoryItemWriter<StrategyExecution> strategyExecutionWriter() {
        RepositoryItemWriter<StrategyExecution> writer = new RepositoryItemWriter<>();
        writer.setRepository(strategyExecutionRepository);
        writer.setMethodName("save");
        return writer;
    }

    @Bean
    public Step strategyAnalysisStep(ItemReader<DictionaryEntry> dictionaryEntryReader, ItemWriter<StrategyExecution> strategyExecutionWriter)
            throws Exception {
        return this.stepBuilderFactory.get("runStrategyAnalysisStep").<DictionaryEntry, StrategyExecution>chunk(10).reader(dictionaryEntryReader)
                .processor(strategyExecutionProcessor()).writer(strategyExecutionWriter).build();
    }

    @Bean
    public Job strategyAnalysisJob(Step strategyAnalysisStep)
            throws Exception {
        return this.jobBuilderFactory.get(STRATEGY_ANALYSIS_JOB_NAME).incrementer(new RunIdIncrementer()).start(strategyAnalysisStep).build();
    }
}
