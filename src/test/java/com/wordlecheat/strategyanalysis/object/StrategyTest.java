package com.wordlecheat.strategyanalysis.object;

import org.junit.jupiter.api.Test;

public class StrategyTest {
    
    @Test
    void testEnums() {
        Strategy[] strategies = Strategy.values();
        System.out.println(strategies);
    }
}
