package com.wordlecheat.dictionary.repository;

import java.util.Date;
import java.util.List;

import com.wordlecheat.dictionary.object.DictionaryEntry;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DictionaryEntryRepository extends CrudRepository<DictionaryEntry, Integer>, DictionaryEntryRepositoryCustom {
    
    List<DictionaryEntry> findByWordIgnoreCase(String word);

    long countByLastCheckedFrequencyBetween(Date startLastCheckedFrequency, Date endLastCheckedFrequency);

    List<DictionaryEntry> findByFrequencyGreaterThan(Double frequency);
}
