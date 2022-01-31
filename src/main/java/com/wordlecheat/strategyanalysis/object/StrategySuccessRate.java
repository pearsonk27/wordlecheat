package com.wordlecheat.strategyanalysis.object;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;
import org.hibernate.annotations.Synchronize;

@Entity
@Immutable
@Subselect(value = "select row_number() over() as id, strategy, " +
"sum(case when is_success = true then 1.0 else 0.0 end) / cast(count(*) as double precision) as success_rate " +
"from strategy_execution " +
"group by strategy")
@Synchronize("Strategy")
public class StrategySuccessRate {

    @Id
    @Column(name = "id", nullable = false)
    private int id;
    
    @Column(name = "strategy", nullable = false)
    @Enumerated(EnumType.STRING)
    private Strategy strategy;

    @Column(name = "success_rate", nullable = false)
    private double successRate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Strategy getStrategy() {
        return strategy;
    }

    public void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }

    public double getSuccessRate() {
        return successRate;
    }

    public void setSuccessRate(double successRate) {
        this.successRate = successRate;
    }
}
