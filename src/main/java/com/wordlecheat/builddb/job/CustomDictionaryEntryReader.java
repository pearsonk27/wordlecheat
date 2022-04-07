package com.wordlecheat.builddb.job;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.stereotype.Component;

@Component
@StepScope
public class CustomDictionaryEntryReader extends FlatFileItemReader<String> {

    @Override
    public void afterPropertiesSet() throws Exception {
        setRecordSeparatorPolicy(new BlankLineRecordSeparatorPolicy());
        super.afterPropertiesSet();
    }
}
