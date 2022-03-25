package com.wordlecheat.dictionary.repository;

import com.wordlecheat.dictionary.object.DictionaryEntry;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureDataJpa
public class DictionaryEntryRepositoryIntegrationTest {

    @Autowired
    DictionaryEntryRepository dictionaryEntryRepository;
    
    @Test
    void test() {
        DictionaryEntry dictionaryEntry = dictionaryEntryRepository.findByWordIgnoreCase("toast").get(0);
        System.out.println(dictionaryEntry);
    }
}
