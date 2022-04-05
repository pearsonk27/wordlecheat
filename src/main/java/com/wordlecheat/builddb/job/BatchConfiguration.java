package com.wordlecheat.builddb.job;

import com.wordlecheat.builddb.BuildDbService;
import com.wordlecheat.dictionary.object.DictionaryEntry;
import com.wordlecheat.dictionary.repository.DictionaryEntryRepository;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    public static final String BUILD_DB_SINGLE_LETTER_STRING = "buildDbSingleLetterJob";
    
	private JobBuilderFactory jobBuilderFactory;

	private StepBuilderFactory stepBuilderFactory;

    private BuildDbService buildDbService;

    private DictionaryEntryRepository dictionaryEntryRepository;
    
    private JobRepository jobRepository;
    
	@Value("classpath*:/dictionary/*.csv")
	private Resource[] inputFiles;

    @Value("classpath:/AllAcceptedWordleWords.csv")
    private Resource allAcceptedWordleWords;

    @Autowired
    public BatchConfiguration(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory, BuildDbService buildDbService, DictionaryEntryRepository dictionaryEntryRepository, JobRepository jobRepository) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.buildDbService = buildDbService;
        this.dictionaryEntryRepository = dictionaryEntryRepository;
        this.jobRepository = jobRepository;
    }
    
	@Bean
	public MultiResourceItemReader<DictionaryEntry> multiResourceItemreader() {
		MultiResourceItemReader<DictionaryEntry> reader = new MultiResourceItemReader<>();
		reader.setDelegate(flatFileItemReader(null));
		reader.setResources(inputFiles);
		return reader;
	}

    @StepScope
    @Bean
    public FlatFileItemReader<DictionaryEntry> singleDictionaryFileItemReader(@Value("#{jobParameters['letter']}") String letter) {
        String resourcePath = String.format("dictionary/%s.csv", letter);
        Resource chosenLetterResource = new ClassPathResource(resourcePath);
        return flatFileItemReader(chosenLetterResource);
    }

	public CustomDictionaryEntryReader flatFileItemReader(Resource resource) {
		DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
		tokenizer.setNames(new String[] { "entry" });
		DefaultLineMapper<DictionaryEntry> dictionaryCsvLineMapper = new DefaultLineMapper<>();
		dictionaryCsvLineMapper.setLineTokenizer(tokenizer);
		dictionaryCsvLineMapper.setFieldSetMapper(new DictionaryEntryFieldSetMapper(buildDbService));
		dictionaryCsvLineMapper.afterPropertiesSet();
		CustomDictionaryEntryReader reader = new CustomDictionaryEntryReader();
		reader.setLineMapper(dictionaryCsvLineMapper);
        if (resource != null) {
            reader.setResource(resource);
        }
		return reader;
	}

    @Bean
    public RepositoryItemWriter<DictionaryEntry> dictionaryEntryWriter() {
        RepositoryItemWriter<DictionaryEntry> writer = new RepositoryItemWriter<>();
        writer.setRepository(dictionaryEntryRepository);
        writer.setMethodName("save");
        return writer;
    }

    @Bean
    public Step allDictionaryCsvsStep() {
        return stepBuilderFactory.get("allDictionaryCsvs")
				.<DictionaryEntry, DictionaryEntry>chunk(1)
				.reader(multiResourceItemreader())
				.writer(dictionaryEntryWriter())
				.build();
    }

    @Bean
    public Step singleDictionaryCsvStep() {
        return stepBuilderFactory.get("singleDictionaryCsv")
				.<DictionaryEntry, DictionaryEntry>chunk(1)
				.reader(singleDictionaryFileItemReader("A"))
				.writer(dictionaryEntryWriter())
				.build();
    }

    @Bean
    public Step wordleWordsStep() {
        return stepBuilderFactory.get("wordleWordsStep")
				.<DictionaryEntry, DictionaryEntry>chunk(10)
				.reader(flatFileItemReader(allAcceptedWordleWords))
				.writer(dictionaryEntryWriter())
				.build();
    }

    @Bean
	public Job fullBuildDbJob() {
		return jobBuilderFactory.get("fullBuildDbJob")
				.start(allDictionaryCsvsStep())
                .next(wordleWordsStep())
				.build();
	}

    @Bean
    public Job buildDbAddWordleWordsJob() {
		return jobBuilderFactory.get("buildDbAddWordleWordsJob")
				.start(wordleWordsStep())
				.build();
    }

    @Bean
	public Job buildDbSingleLetterJob() {
		return jobBuilderFactory.get(BUILD_DB_SINGLE_LETTER_STRING)
                .start(singleDictionaryCsvStep())
				.build();
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
