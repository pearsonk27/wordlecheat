package com.wordlecheat.builddb.job;

import com.wordlecheat.builddb.BuildDbService;
import com.wordlecheat.dictionary.object.DictionaryEntry;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

public class WordleWordFieldSetMapper implements FieldSetMapper<DictionaryEntry> {
    
    private BuildDbService buildDbService;

    public WordleWordFieldSetMapper(BuildDbService buildDbService) {
        this.buildDbService = buildDbService;
    }

    @Override
    public DictionaryEntry mapFieldSet(FieldSet fieldSet) throws BindException {
        String entry = fieldSet.readString("entry");
        DictionaryEntry dictionaryEntry = buildDbService.buildDictionaryEntryFromWord(entry);
        return dictionaryEntry;
    }
}
