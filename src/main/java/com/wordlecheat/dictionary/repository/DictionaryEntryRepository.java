package com.wordlecheat.dictionary.repository;

import java.util.Date;
import java.util.List;

import com.wordlecheat.dictionary.object.DictionaryEntry;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;

@Repository
public interface DictionaryEntryRepository extends PagingAndSortingRepository<DictionaryEntry, Integer>, DictionaryEntryRepositoryCustom {
    
    List<DictionaryEntry> findByWordIgnoreCase(String word);

    @RestResource(exported = false)
    long countByLastCheckedFrequencyBetween(Date startLastCheckedFrequency, Date endLastCheckedFrequency);

    @RestResource(exported = false)
    Page<DictionaryEntry> findByFrequencyGreaterThan(Double frequency, Pageable pageable);

    @RestResource(exported = false)
    List<DictionaryEntry> findByFrequencyGreaterThan(Double frequency);
}
