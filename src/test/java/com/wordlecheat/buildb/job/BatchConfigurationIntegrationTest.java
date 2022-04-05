package com.wordlecheat.buildb.job;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.wordlecheat.dictionary.object.DictionaryEntry;
import com.wordlecheat.dictionary.repository.DictionaryEntryRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("h2")
@SpringBootTest
public class BatchConfigurationIntegrationTest {

    @Autowired
    private DictionaryEntryRepository dictionaryEntryRepository;

    @Test
    void testCountWords() {
        DictionaryEntry dictionaryEntry = new DictionaryEntry();
        dictionaryEntry.setWord("Cable");
        dictionaryEntryRepository.save(dictionaryEntry);
        assertThat(dictionaryEntryRepository.count()).isEqualTo(1);
    }
}
