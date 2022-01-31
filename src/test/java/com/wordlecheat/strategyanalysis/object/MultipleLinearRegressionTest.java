package com.wordlecheat.strategyanalysis.object;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MultipleLinearRegressionTest {

    private static final Logger log = LoggerFactory.getLogger(MultipleLinearRegressionTest.class);
    
    /**
     * Unit tests the {@code MultipleLinearRegression} data type.
     *
     * @param args the command-line arguments
     */
    @Test
    public void testConstructor() {
        double[][] x = { {  1,  10,  20 },
                         {  1,  20,  40 },
                         {  1,  40,  15 },
                         {  1,  80, 100 },
                         {  1, 160,  23 },
                         {  1, 200,  18 } };
        double[] y = { 243, 483, 508, 1503, 1764, 2129 };
        MultipleLinearRegression regression = new MultipleLinearRegression(x, y);

        log.info("{} + {} beta1 + {} beta2  (R^2 = {})\n",
                      String.format("%.2f", regression.beta(0)), String.format("%.2f", regression.beta(1)), String.format("%.2f", regression.beta(2)), String.format("%.2f", regression.R2()));

        assertThat(regression.beta(0)).isEqualTo(3.0, within(0.01));
        assertThat(regression.beta(1)).isEqualTo(10.0, within(0.01));
        assertThat(regression.beta(2)).isEqualTo(7.0, within(0.01));
        assertThat(regression.R2()).isEqualTo(1.0, within(0.01));
    }

    @Test
    public void test() {
        String query = "select row_number() over() as id, strategy, " +
        "sum(case when is_success = true then 1.0 else 0.0 end) / cast(count(*) as double precision) as success_rate " +
        "from strategy_execution " +
        "group by strategy;";

        log.info(query);
    }

    @Test
    public void test1() {
        String query = "select row_number() over() as id, " +
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
        "    nclgi.count_not_contained_letters_guess_inputs;";

        log.info(query);
    }
}
