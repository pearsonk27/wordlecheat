package com.wordlecheat.builddb.job;

import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;

/**
 * Needed to avoid the circular reference issues in JobExecution
 */
public class UiJobExecution {
    
    private String jobName;
    
    private long instanceId;

    private Map<String, Object> jobParameters;

    private BatchStatus batchStatus;

    public UiJobExecution() {}

    public UiJobExecution(JobExecution jobExecution) {
        this.jobName = jobExecution.getJobInstance().getJobName();
        this.instanceId = jobExecution.getJobInstance().getInstanceId();
        this.jobParameters = new HashMap<>();
        for (Map.Entry<String, JobParameter> jobParameter : jobExecution.getJobParameters().getParameters().entrySet()) {
            this.jobParameters.put(jobParameter.getKey(), jobParameter.getValue().getValue());
        }
        this.batchStatus = jobExecution.getStatus();
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public long getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(long instanceId) {
        this.instanceId = instanceId;
    }

    public Map<String, Object> getJobParameters() {
        return jobParameters;
    }

    public void setJobParameters(Map<String, Object> jobParameters) {
        this.jobParameters = jobParameters;
    }

    public BatchStatus getBatchStatus() {
        return batchStatus;
    }

    public void setBatchStatus(BatchStatus batchStatus) {
        this.batchStatus = batchStatus;
    }
}
