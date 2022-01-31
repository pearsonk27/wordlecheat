select ROW_NUMBER() over() as id,
    strategy,
    sum(case when is_success = true then 1.0 else 0.0 end) / cast(count(*) as double precision) as success_rate
from strategy_execution
group by strategy;

select row_number() over() as id,
    coalesce(klpi.count_known_letter_placements_inputs, 0) as count_known_letter_placements_inputs,
    coalesce(knlpi.count_known_non_letter_placements_inputs, 0) as count_known_non_letter_placements_inputs,
    coalesce(clgi.count_contained_letters_guess_inputs, 0) as count_contained_letters_guess_inputs,
    coalesce(nclgi.not_count_contained_letters_guess_inputs, 0) as not_count_contained_letters_guess_inputs,
    sum(case when se.is_success and g.guess_number = mg.last_guess_number then 1.0
            else 0.0 end) / cast(count(*) as double precision) as success_rate
from strategy_execution se
join guess g on se.id = g.strategy_execution_id
join (
    select strategy_execution_id,
        max(guess_number) as last_guess_number
    from guess
    group by strategy_execution_id
) mg on se.id = mg.strategy_execution_id
left join (
    select guess_id,
        count(*) as count_known_letter_placements_inputs
    from known_letter_placements_input
    group by guess_id
) klpi on g.id = klpi.guess_id
left join (
    select guess_id,
        count(*) as count_known_non_letter_placements_inputs
    from known_non_letter_placements_input
    group by guess_id
) knlpi on g.id = knlpi.guess_id
left join (
    select guess_id,
        count(*) as count_contained_letters_guess_inputs
    from contained_letters_guess_input
    group by guess_id
) clgi on g.id = clgi.guess_id
left join (
    select guess_id,
        count(*) as not_count_contained_letters_guess_inputs
    from not_contained_letters_guess_input
    group by guess_id
) nclgi on g.id = nclgi.guess_id
group by klpi.count_known_letter_placements_inputs,
    knlpi.count_known_non_letter_placements_inputs,
    clgi.count_contained_letters_guess_inputs,
    nclgi.not_count_contained_letters_guess_inputs;