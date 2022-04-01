package com.wordlecheat.builddb.job;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wordlecheat.dictionary.object.DictionaryEntry;
import com.wordlecheat.dictionary.repository.DictionaryEntryRepository;
import com.wordlecheat.dictionary.service.DictionaryEntryService;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.data.domain.Sort.Direction;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    private DictionaryEntryRepository dictionaryEntryRepository;
    private DictionaryEntryService dictionaryEntryService;
    private JobBuilderFactory jobBuilderFactory;
    private StepBuilderFactory stepBuilderFactory;
    private JobRepository jobRepository;

    @Autowired
    public BatchConfiguration(DictionaryEntryRepository dictionaryEntryRepository, DictionaryEntryService dictionaryEntryService, JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory, JobRepository jobRepository) {
        this.dictionaryEntryRepository = dictionaryEntryRepository;
        this.dictionaryEntryService = dictionaryEntryService;
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.jobRepository = jobRepository;
    }
    
    @Bean
    public RepositoryItemReader<DictionaryEntry> reader() {
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
    public RepositoryItemWriter<DictionaryEntry> writer() {
        RepositoryItemWriter<DictionaryEntry> writer = new RepositoryItemWriter<>();
        writer.setRepository(dictionaryEntryRepository);
        writer.setMethodName("save");
        return writer;
    }

    @Bean
    public DictionaryEntryProcessor processor() {
        return new DictionaryEntryProcessor(dictionaryEntryService);
    }

    @Bean
    public Step step1(ItemReader<DictionaryEntry> itemReader, ItemWriter<DictionaryEntry> itemWriter)
            throws Exception {

        return this.stepBuilderFactory.get("step1").<DictionaryEntry, DictionaryEntry>chunk(5).reader(itemReader)
                .processor(processor()).writer(itemWriter).build();
    }

    @Bean
    public Job dictionaryEntryFillJob(JobCompletionNotificationListener listener, Step step1)
            throws Exception {

        return this.jobBuilderFactory.get("dictionaryEntryFillJob").incrementer(new RunIdIncrementer())
                .listener(listener).start(step1).build();
    }

    @Bean 
    public JobLauncher asynchJobLauncher() throws Exception {
        SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
        jobLauncher.setJobRepository(jobRepository);
        jobLauncher.setTaskExecutor(new SimpleAsyncTaskExecutor());
        jobLauncher.afterPropertiesSet();
        return jobLauncher;
    }
}
