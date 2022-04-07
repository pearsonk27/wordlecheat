package com.wordlecheat.ui;

import com.wordlecheat.builddb.job.BuildDbBatchConfiguration;
import com.wordlecheat.builddb.job.UiJobExecution;
import com.wordlecheat.dictionary.object.LetterEnum;
import com.wordlecheat.strategyanalysis.job.StrategyAnalysisBatchConfiguration;
import com.wordlecheat.strategyanalysis.object.Strategy;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/batch")
public class BatchJobController {

    private static final String START_AT_PARAMETER_NAME = "startAt";
    
    @Autowired
    @Qualifier("asynchJobLauncher")
    private JobLauncher jobLauncher;
    
    @Autowired
    @Qualifier(BuildDbBatchConfiguration.FULL_BUILD_JOB_NAME)
    private Job fullBuildDbJob;
    
    @Autowired
    @Qualifier(BuildDbBatchConfiguration.SINGLE_LETTER_STRING_JOB_NAME)
    private Job buildDbSingleLetterJob;
    
    @Autowired
    @Qualifier(BuildDbBatchConfiguration.ADD_WORDLE_WORDS_JOB_NAME)
    private Job buildDbAddWordleWordsJob;

    @Autowired
    @Qualifier(StrategyAnalysisBatchConfiguration.STRATEGY_ANALYSIS_JOB_NAME)
    private Job strategyAnalysisJob;

    @Autowired
    private JobRepository jobRepository;

    @PostMapping(path = "/" + BuildDbBatchConfiguration.SINGLE_LETTER_STRING_JOB_NAME + "/start/{letter}")
    public UiJobExecution startBuildDbSingleLetterJob(@PathVariable LetterEnum letter) {
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong(START_AT_PARAMETER_NAME, System.currentTimeMillis())
                .addString("letter", letter.name())
                .toJobParameters();
        try {
            jobLauncher.run(buildDbSingleLetterJob, jobParameters);
        } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException | JobParametersInvalidException e) {
            e.printStackTrace();
        }
        return new UiJobExecution(jobRepository.getLastJobExecution(BuildDbBatchConfiguration.SINGLE_LETTER_STRING_JOB_NAME, jobParameters));
    }

    @GetMapping(path = "/" + BuildDbBatchConfiguration.SINGLE_LETTER_STRING_JOB_NAME + "/{letter}/{startAt}")
    public UiJobExecution getCurrentBuildDbSingleLetterJob(@PathVariable LetterEnum letter, @PathVariable long startAt) {
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong(START_AT_PARAMETER_NAME, startAt)
                .addString("letter", letter.name())
                .toJobParameters();
        UiJobExecution jobExecution = new UiJobExecution(jobRepository.getLastJobExecution(BuildDbBatchConfiguration.SINGLE_LETTER_STRING_JOB_NAME, jobParameters));
        return jobExecution;
    }

    @PostMapping(path = "/" + BuildDbBatchConfiguration.FULL_BUILD_JOB_NAME + "/start")
    public UiJobExecution startFullBuildDbJob() {
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong(START_AT_PARAMETER_NAME, System.currentTimeMillis())
                .toJobParameters(); 
        try {
            jobLauncher.run(fullBuildDbJob, jobParameters);
        } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException | JobParametersInvalidException e) {
            e.printStackTrace();
        }
        UiJobExecution jobExecution = new UiJobExecution(jobRepository.getLastJobExecution(BuildDbBatchConfiguration.FULL_BUILD_JOB_NAME, jobParameters));
        return jobExecution;
    }

    @GetMapping(path = "/" + BuildDbBatchConfiguration.FULL_BUILD_JOB_NAME + "/{startAt}")
    public UiJobExecution getFullBuildDbJob(@PathVariable long startAt) {
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong(START_AT_PARAMETER_NAME, startAt)
                .toJobParameters();
        UiJobExecution jobExecution = new UiJobExecution(jobRepository.getLastJobExecution(BuildDbBatchConfiguration.FULL_BUILD_JOB_NAME, jobParameters));
        return jobExecution;
    }

    @PostMapping(path = "/" + BuildDbBatchConfiguration.ADD_WORDLE_WORDS_JOB_NAME + "/start")
    public UiJobExecution startBuildDbAddWordleWordsJob() {
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong(START_AT_PARAMETER_NAME, System.currentTimeMillis())
                .toJobParameters(); 
        try {
            jobLauncher.run(buildDbAddWordleWordsJob, jobParameters);
        } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException | JobParametersInvalidException e) {
            e.printStackTrace();
        }
        return new UiJobExecution(jobRepository.getLastJobExecution(BuildDbBatchConfiguration.ADD_WORDLE_WORDS_JOB_NAME, jobParameters));
    }

    @GetMapping(path = "/" + BuildDbBatchConfiguration.ADD_WORDLE_WORDS_JOB_NAME + "/{startAt}")
    public UiJobExecution getBuildDbAddWordleWordsJob(@PathVariable long startAt) {
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong(START_AT_PARAMETER_NAME, startAt)
                .toJobParameters();
        UiJobExecution jobExecution = new UiJobExecution(jobRepository.getLastJobExecution(BuildDbBatchConfiguration.ADD_WORDLE_WORDS_JOB_NAME, jobParameters));
        return jobExecution;
    }

    @PostMapping(path = "/" + StrategyAnalysisBatchConfiguration.STRATEGY_ANALYSIS_JOB_NAME + "/start/{strategy}")
    public UiJobExecution startStrategyAnalysisJob(@PathVariable Strategy strategy) {
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong(START_AT_PARAMETER_NAME, System.currentTimeMillis())
                .addString("strategy", strategy.name())
                .toJobParameters();
        try {
            jobLauncher.run(strategyAnalysisJob, jobParameters);
        } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException | JobParametersInvalidException e) {
            e.printStackTrace();
        }
        UiJobExecution jobExecution = new UiJobExecution(jobRepository.getLastJobExecution(StrategyAnalysisBatchConfiguration.STRATEGY_ANALYSIS_JOB_NAME, jobParameters));
        return jobExecution;
    }

    @GetMapping(path = "/" + StrategyAnalysisBatchConfiguration.STRATEGY_ANALYSIS_JOB_NAME + "/{strategy}/{startAt}")
    public UiJobExecution getStrategyAnalysisJob(@PathVariable Strategy strategy, @PathVariable long startAt) {
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong(START_AT_PARAMETER_NAME, startAt)
                .addString("strategy", strategy.name())
                .toJobParameters();
        UiJobExecution jobExecution = new UiJobExecution(jobRepository.getLastJobExecution(StrategyAnalysisBatchConfiguration.STRATEGY_ANALYSIS_JOB_NAME, jobParameters));
        return jobExecution;
    }
}
