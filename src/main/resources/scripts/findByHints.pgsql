select
    d.*
from
    dictionary as d
join (
    select distinct dictionary_id
    from dictionary_entry_letter_placements delp
    join letter_placement lp on delp.letter_placement_id = lp.id
    where lp.id in :knownLetterPlacements
) lp1 on lp1.dictionary_id = d.id
join (
    select distinct dictionary_id
    from dictionary_entry_letter_placements delp
    join letter_placement lp on delp.letter_placement_id = lp.id
    where lp.letter in :containedLetters
) lp2 on lp2.dictionary_id = d.id
left join (
    select distinct dictionary_id
    from dictionary_entry_letter_placements delp
    join letter_placement lp on delp.letter_placement_id = lp.id
    where lp.id in :knownNonLetterPlacements
        or lp.letter in :notContainedLetters
) lp3 on lp3.dictionary_id = d.id
where lp2.dictionary_id is null;