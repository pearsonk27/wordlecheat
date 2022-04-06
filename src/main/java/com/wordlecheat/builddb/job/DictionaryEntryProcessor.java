package com.wordlecheat.builddb.job;

import java.io.IOException;

import com.wordlecheat.builddb.BuildDbService;
import com.wordlecheat.dictionary.object.DictionaryEntry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

public class DictionaryEntryProcessor implements ItemProcessor<String, DictionaryEntry> {

    private static final Logger log = LoggerFactory.getLogger(DictionaryEntryProcessor.class);

    private BuildDbService buildDbService;

    @Autowired
    public DictionaryEntryProcessor(BuildDbService buildDbService) {
        this.buildDbService = buildDbService;
    }

    @Override
    public DictionaryEntry process(String entry) throws Exception {
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
