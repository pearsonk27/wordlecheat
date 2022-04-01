package com.wordlecheat.dictionary.repository;

import java.util.Date;
import java.util.List;
import java.util.Set;

import com.wordlecheat.dictionary.object.DictionaryEntry;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;

@Repository
public interface DictionaryEntryRepository extends PagingAndSortingRepository<DictionaryEntry, Integer>, DictionaryEntryRepositoryCustom {
    
    List<DictionaryEntry> findByWordIgnoreCase(String word);

    long countByLastCheckedFrequencyBetween(Date startLastCheckedFrequency, Date endLastCheckedFrequency);

    @RestResource(exported = false)
    Page<DictionaryEntry> findByFrequencyGreaterThan(Double frequency, Pageable pageable);

    @RestResource(exported = false)
    List<DictionaryEntry> findByFrequencyGreaterThan(Double frequency);

    List<DictionaryEntry> findByLetterPlacements_IdInAndLetterPlacements_IdNotInAndLetterPlacements_LetterInAndLetterPlacements_LetterNotIn(List<Integer> knownLetterPlacements, List<Integer> knownNonLetterPlacements, Set<String> containedLetters, Set<String> notContainedLetters);

    // @Query("select distinct entry from DictionaryEntry entry " +
    //             " where entry.id in (select distinct delp.dictionary_id " +
    //                                 " from DictionaryEntry subEntry " +
    //                                 " join DictionaryEntry.letterPlacements delp " +
    //                                 " join LetterPlacement lp " +
    //                                 " where subEntry.id = delp.dictionary_id " +
    //                                 "   and lp.id = delp.letter_placement_id " +
    //                                 "   and lp.id in :knownLetterPlacements) " +
    //                 " and entry.id in (select distinct delp.dictionary_id " +
    //                                 " from DictionaryEntry subEntry " +
    //                                 " join DictionaryEntry.letterPlacements delp " +
    //                                 " join LetterPlacement lp " +
    //                                 " where subEntry.id = delp.dictionary_id " +
    //                                 "   and lp.id = delp.letter_placement_id " +
    //                                 "   and lp.letter in :containedLetters) " +
    //                 " and entry.id not in (select distinct delp.dictionary_id " +
    //                                 " from DictionaryEntry subEntry " +
    //                                 " join DictionaryEntry.letterPlacements delp " +
    //                                 " join LetterPlacement lp " +
    //                                 " where subEntry.id = delp.dictionary_id " +
    //                                 "   and lp.id = delp.letter_placement_id " +
    //                                     "   and (lp.id in :knownNonLetterPlacements " +
    //                                             " or lp.letter in :notContainedLetters)) ")
    // List<DictionaryEntry> findByHints(@Param("knownLetterPlacements") List<Integer> knownLetterPlacements, 
    //     @Param("knownNonLetterPlacements") List<Integer> knownNonLetterPlacements, 
    //     @Param("containedLetters") Set<String> containedLetters, 
    //     @Param("notContainedLetters") Set<String> notContainedLetters);

    @Query(nativeQuery = true, value = "select " +
"    d.* " +
"from " +
"    dictionary as d " +
"join ( " +
"    select distinct dictionary_id " +
"    from dictionary_entry_letter_placements delp " +
"    join letter_placement lp on delp.letter_placement_id = lp.id " +
"    where lp.id in :knownLetterPlacements " +
") lp1 on lp1.dictionary_id = d.id " +
"join ( " +
"    select dictionary_id " +
"    from dictionary_entry_letter_placements delp " +
"    join letter_placement lp on delp.letter_placement_id = lp.id " +
"    where lower(lp.letter) in :containedLetters " +
"    group by dictionary_id " +
"    having count(distinct(lower(lp.letter))) = :numberContainedLetters " +
") lp2 on lp2.dictionary_id = d.id " +
"left join ( " +
"    select distinct dictionary_id " +
"    from dictionary_entry_letter_placements delp " +
"    join letter_placement lp on delp.letter_placement_id = lp.id " +
"    where lp.id in :knownNonLetterPlacements " +
"        or lower(lp.letter) in :notContainedLetters " +
") lp3 on lp3.dictionary_id = d.id " +
"where lp3.dictionary_id is null;")
    List<DictionaryEntry> findByHintsWithNativeQuery(@Param("knownLetterPlacements") List<Integer> knownLetterPlacements, 
        @Param("knownNonLetterPlacements") List<Integer> knownNonLetterPlacements, 
        @Param("containedLetters") Set<String> containedLetters, 
        @Param("notContainedLetters") Set<String> notContainedLetters,
        @Param("numberContainedLetters") int numberContainedLetters);
}
