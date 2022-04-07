package com.wordlecheat.dictionary.repository;

import java.util.Date;
import java.util.List;

import javax.persistence.Cacheable;

import com.wordlecheat.dictionary.object.DictionaryEntry;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;

@Repository
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public interface DictionaryEntryRepository extends PagingAndSortingRepository<DictionaryEntry, Integer>, DictionaryEntryRepositoryCustom {
    
    List<DictionaryEntry> findByWordIgnoreCase(String word);

    @RestResource(exported = false)
    long countByLastCheckedFrequencyBetween(Date startLastCheckedFrequency, Date endLastCheckedFrequency);

    @RestResource(exported = false)
    Page<DictionaryEntry> findByFrequencyGreaterThan(Double frequency, Pageable pageable);

    @RestResource(exported = false)
    List<DictionaryEntry> findByFrequencyGreaterThan(Double frequency);
}
