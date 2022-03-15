explain select dictionary0_.id as id1_2_, 
    dictionary0_.frequency as frequenc2_2_, 
    dictionary0_.last_checked_frequency as last_che3_2_, 
    dictionary0_.letter_frequency as letter_f4_2_, 
    dictionary0_.word as word5_2_ 
from dictionary dictionary0_ 
where (lower(dictionary0_.word) like '%c%') 
    and (lower(dictionary0_.word) not like '___c_') 
    and (lower(dictionary0_.word) not like '%p%') 
    and (lower(dictionary0_.word) not like '%u%') 
    and (lower(dictionary0_.word) not like '%h%') 
    and (lower(dictionary0_.word) not like '%n%')
    and (dictionary0_.frequency is not null) 
    and length(dictionary0_.word)=5;

explain select *
from dictionary
where word like '%cig%';

select se.creation_date, g.*
from guess g
join strategy_execution se on g.strategy_execution_id = se.id
order by id DESC
limit 100;

select d.word, g.*
from guess g
join dictionary d on g.guess_dictionary_id = d.id
where strategy_execution_id = 720706;

select *
from contained_letters_guess_input
where guess_id = 720708;

select lp.letter, lp.string_index
from known_letter_placements_input klpi
join letter_placement lp on klpi.letter_placement_id = lp.id
where guess_id = 720708;

select lp.letter, lp.string_index
from known_non_letter_placements_input knlpi
join letter_placement lp on knlpi.letter_placement_id = lp.id
where guess_id = 720708;

select *
from not_contained_letters_guess_input
where guess_id = 720708;

select *
from pg_indexes
where tablename = 'dictionary';

alter table dictionary
drop constraint uk_fkpwk50033iswvbmm0kifo1ub;

drop index uk_fkpwk50033iswvbmm0kifo1ub;

drop index idxfkpwk50033iswvbmm0kifo1ub;

CREATE UNIQUE INDEX uk_fkpwk50033iswvbmm0kifo1ub ON public.dictionary USING btree (word)

CREATE EXTENSION pg_trgm;
CREATE EXTENSION btree_gin;

CREATE INDEX idx_gin_word ON public.dictionary USING gin (word);

select *
from pg_indexes
where indexname = 'uk_fkpwk50033iswvbmm0kifo1ub';