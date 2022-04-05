package com.wordlecheat.builddb.job;

import com.wordlecheat.dictionary.object.DictionaryEntry;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.stereotype.Component;

@Component
@StepScope
public class CustomDictionaryEntryReader extends FlatFileItemReader<DictionaryEntry> {

    @Override
    public void afterPropertiesSet() throws Exception {
        setRecordSeparatorPolicy(new BlankLineRecordSeparatorPolicy());
        super.afterPropertiesSet();
    }
}
