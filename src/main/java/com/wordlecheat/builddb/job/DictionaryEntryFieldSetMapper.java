package com.wordlecheat.builddb.job;

import java.io.IOException;

import com.wordlecheat.builddb.BuildDbService;
import com.wordlecheat.dictionary.object.DictionaryEntry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

public class DictionaryEntryFieldSetMapper implements FieldSetMapper<String> {

    @Override
    public String mapFieldSet(FieldSet fieldSet) throws BindException {
        return fieldSet.readString("entry");
    }
    
}
