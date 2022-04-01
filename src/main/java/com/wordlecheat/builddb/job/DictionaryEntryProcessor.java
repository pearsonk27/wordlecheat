package com.wordlecheat.builddb.job;

import com.wordlecheat.dictionary.object.DictionaryEntry;
import com.wordlecheat.dictionary.service.DictionaryEntryService;

import org.springframework.batch.item.ItemProcessor;

public class DictionaryEntryProcessor implements ItemProcessor<DictionaryEntry, DictionaryEntry> {

    private DictionaryEntryService dictionaryEntryService;

    public DictionaryEntryProcessor(DictionaryEntryService dictionaryEntryService) {
        this.dictionaryEntryService = dictionaryEntryService;
    }

    @Override
    public DictionaryEntry process(DictionaryEntry dictionaryEntry) throws Exception {
        dictionaryEntryService.setLetterPlacements(dictionaryEntry);
        return dictionaryEntry;
    }
    
}
