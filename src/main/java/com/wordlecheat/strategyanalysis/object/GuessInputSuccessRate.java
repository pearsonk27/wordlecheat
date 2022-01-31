package com.wordlecheat.strategyanalysis.object;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

@Entity
@Immutable
@Subselect(value = "select row_number() over() as id, " +
"    coalesce(klpi.count_known_letter_placements_inputs, 0) as count_known_letter_placements_inputs, " +
"    coalesce(knlpi.count_known_non_letter_placements_inputs, 0) as count_known_non_letter_placements_inputs, " +
"    coalesce(clgi.count_contained_letters_guess_inputs, 0) as count_contained_letters_guess_inputs, " +
"    coalesce(nclgi.count_not_contained_letters_guess_inputs, 0) as count_not_contained_letters_guess_inputs, " +
"    sum(case when se.is_success and g.guess_number = mg.last_guess_number then 1.0 " +
"            else 0.0 end) / cast(count(*) as double precision) as success_rate " +
"from strategy_execution se " +
"join guess g on se.id = g.strategy_execution_id " +
"join ( " +
"    select strategy_execution_id, " +
"        max(guess_number) as last_guess_number " +
"    from guess " +
"    group by strategy_execution_id " +
") mg on se.id = mg.strategy_execution_id " +
"left join ( " +
"    select guess_id, " +
"        count(*) as count_known_letter_placements_inputs " +
"    from known_letter_placements_input " +
"    group by guess_id " +
") klpi on g.id = klpi.guess_id " +
"left join ( " +
"    select guess_id, " +
"        count(*) as count_known_non_letter_placements_inputs " +
"    from known_non_letter_placements_input " +
"    group by guess_id " +
") knlpi on g.id = knlpi.guess_id " +
"left join ( " +
"    select guess_id, " +
"        count(*) as count_contained_letters_guess_inputs " +
"    from contained_letters_guess_input " +
"    group by guess_id " +
") clgi on g.id = clgi.guess_id " +
"left join ( " +
"    select guess_id, " +
"        count(*) as count_not_contained_letters_guess_inputs " +
"    from not_contained_letters_guess_input " +
"    group by guess_id " +
") nclgi on g.id = nclgi.guess_id " +
"group by klpi.count_known_letter_placements_inputs, " +
"    knlpi.count_known_non_letter_placements_inputs, " +
"    clgi.count_contained_letters_guess_inputs, " +
"    nclgi.count_not_contained_letters_guess_inputs")
public class GuessInputSuccessRate {

    @Id
    @Column(name = "id")
    private int id;
    
    @Column(name = "count_known_letter_placements_inputs")
    private int countKnownLetterPlacementsInputs;
    
    @Column(name = "count_known_non_letter_placements_inputs")
    private int countKnownNonLetterPlacementsInputs;

    @Column(name = "count_contained_letters_guess_inputs")
    private int countContainedLettersGuessInputs;

    @Column(name = "count_not_contained_letters_guess_inputs")
    private int countNotContainedLettersGuessInputs;

    @Column(name = "success_rate")
    private double successRate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCountKnownLetterPlacementsInputs() {
        return countKnownLetterPlacementsInputs;
    }

    public void setCountKnownLetterPlacementsInputs(int countKnownLetterPlacementsInputs) {
        this.countKnownLetterPlacementsInputs = countKnownLetterPlacementsInputs;
    }

    public int getCountKnownNonLetterPlacementsInputs() {
        return countKnownNonLetterPlacementsInputs;
    }

    public void setCountKnownNonLetterPlacementsInputs(int countKnownNonLetterPlacementsInputs) {
        this.countKnownNonLetterPlacementsInputs = countKnownNonLetterPlacementsInputs;
    }

    public int getCountContainedLettersGuessInputs() {
        return countContainedLettersGuessInputs;
    }

    public void setCountContainedLettersGuessInputs(int countContainedLettersGuessInputs) {
        this.countContainedLettersGuessInputs = countContainedLettersGuessInputs;
    }

    public int getCountNotContainedLettersGuessInputs() {
        return countNotContainedLettersGuessInputs;
    }

    public void setCountNotContainedLettersGuessInputs(int countNotContainedLettersGuessInputs) {
        this.countNotContainedLettersGuessInputs = countNotContainedLettersGuessInputs;
    }

    public double getSuccessRate() {
        return successRate;
    }

    public void setSuccessRate(double successRate) {
        this.successRate = successRate;
    }
}
