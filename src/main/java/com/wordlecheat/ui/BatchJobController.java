package com.wordlecheat.ui;

import com.wordlecheat.builddb.job.BatchConfiguration;
import com.wordlecheat.builddb.job.UiJobExecution;
import com.wordlecheat.dictionary.object.LetterEnum;

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
    
    @Autowired
    @Qualifier("asynchJobLauncher")
    private JobLauncher jobLauncher;
    
    @Autowired
    @Qualifier("fullBuildDbJob")
    private Job fullBuildDbJob;
    
    @Autowired
    @Qualifier(BatchConfiguration.BUILD_DB_SINGLE_LETTER_STRING)
    private Job buildDbSingleLetterJob;
    
    @Autowired
    @Qualifier("buildDbAddWordleWordsJob")
    private Job buildDbAddWordleWordsJob;

    @Autowired
    private JobRepository jobRepository;

    @PostMapping(path = "/" + BatchConfiguration.BUILD_DB_SINGLE_LETTER_STRING + "/start/{letter}")
    public UiJobExecution startBuildDbSingleLetterJob(@PathVariable LetterEnum letter) {
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("startAt", System.currentTimeMillis())
                .addString("letter", letter.name())
                .toJobParameters();
        try {
            jobLauncher.run(buildDbSingleLetterJob, jobParameters);
        } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException | JobParametersInvalidException e) {
            e.printStackTrace();
        }
        return new UiJobExecution(jobRepository.getLastJobExecution(BatchConfiguration.BUILD_DB_SINGLE_LETTER_STRING, jobParameters));
    }

    @GetMapping(path = "/" + BatchConfiguration.BUILD_DB_SINGLE_LETTER_STRING + "/{letter}/{startAt}")
    public UiJobExecution getCurrentBuildDbSingleLetterJob(@PathVariable LetterEnum letter, @PathVariable long startAt) {
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("startAt", startAt)
                .addString("letter", letter.name())
                .toJobParameters();
        UiJobExecution jobExecution = new UiJobExecution(jobRepository.getLastJobExecution(BatchConfiguration.BUILD_DB_SINGLE_LETTER_STRING, jobParameters));
        return jobExecution;
    }

    @PostMapping(path = "/fullBuildDbJob/start")
    public void startFullBuildDbJob() {
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("startAt", System.currentTimeMillis())
                .toJobParameters(); 
        try {
            jobLauncher.run(fullBuildDbJob, jobParameters);
        } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException | JobParametersInvalidException e) {
            e.printStackTrace();
        }
    }

    @PostMapping(path = "/buildDbAddWordleWordsJob/start")
    public void startBuildDbAddWordleWordsJob() {
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("startAt", System.currentTimeMillis())
                .toJobParameters(); 
        try {
            jobLauncher.run(buildDbAddWordleWordsJob, jobParameters);
        } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException | JobParametersInvalidException e) {
            e.printStackTrace();
        }
    }
}
