package com.wordlecheat.builddb.job;

import java.io.IOException;

import com.wordlecheat.builddb.BuildDbService;
import com.wordlecheat.dictionary.object.DictionaryEntry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

public class DictionaryEntryFieldSetMapper implements FieldSetMapper<DictionaryEntry> {

    private static final Logger log = LoggerFactory.getLogger(DictionaryEntryFieldSetMapper.class);

    private BuildDbService buildDbService;

    public DictionaryEntryFieldSetMapper(BuildDbService buildDbService) {
        this.buildDbService = buildDbService;
    }

    @Override
    public DictionaryEntry mapFieldSet(FieldSet fieldSet) throws BindException {
        String entry = fieldSet.readString("entry");
        if ("\"".equals(entry.substring(0, 1))) {
            entry = entry.substring(1, entry.length() - 1);
        }
        DictionaryEntry dictionaryEntry = null;
        try {
            dictionaryEntry = buildDbService.buildDictionaryEntryFromDefinitionLine(entry);
        } catch (IOException | StringIndexOutOfBoundsException e) {
            log.warn("Entry could not be processed: {}", entry, e);
        }
        log.info("Mapped '{}' with {} definitions (id: {}).", dictionaryEntry.getWord(), dictionaryEntry.getDefinitions().size(), dictionaryEntry.getId());
        return dictionaryEntry;
    }
    
}
