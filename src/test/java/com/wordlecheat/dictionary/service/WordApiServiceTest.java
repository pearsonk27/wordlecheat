package com.wordlecheat.dictionary.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.wordlecheat.dictionary.repository.DictionaryEntryRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class WordApiServiceTest {
    
    private WordApiService wordApiService;

    @Mock
    private DictionaryEntryRepository dictionaryEntryRepository;

    @Test
    void testGetWordFrequency() {
        when(dictionaryEntryRepository.countByLastCheckedFrequencyBetween(any(), any())).thenReturn(0L);
        wordApiService = new WordApiService(dictionaryEntryRepository);
        Double frequency = wordApiService.getWordFrequency("word");
        // Purposely fail
        assertThat(frequency).isNull();
    }
}
