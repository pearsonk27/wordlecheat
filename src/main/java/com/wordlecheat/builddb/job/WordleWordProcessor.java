package com.wordlecheat.builddb.job;

import com.wordlecheat.builddb.BuildDbService;
import com.wordlecheat.dictionary.object.DictionaryEntry;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

public class WordleWordProcessor implements ItemProcessor<String, DictionaryEntry> {

    private BuildDbService buildDbService;

    @Autowired
    public WordleWordProcessor(BuildDbService buildDbService) {
        this.buildDbService = buildDbService;
    }

    @Override
    public DictionaryEntry process(String entry) throws Exception {
        return buildDbService.buildDictionaryEntryFromWord(entry);
    }
    
}
