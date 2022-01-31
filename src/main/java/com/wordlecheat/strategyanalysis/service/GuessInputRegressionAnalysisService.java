package com.wordlecheat.strategyanalysis.service;

import java.util.List;

import com.wordlecheat.strategyanalysis.object.GuessInputSuccessRate;
import com.wordlecheat.strategyanalysis.object.MultipleLinearRegression;
import com.wordlecheat.strategyanalysis.repository.GuessInputSuccessRateRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@Service
public class GuessInputRegressionAnalysisService {
    
    private GuessInputSuccessRateRepository guessInputSuccessRateRepository;

    @Autowired
    public GuessInputRegressionAnalysisService(GuessInputSuccessRateRepository guessInputSuccessRateRepository) {
        this.guessInputSuccessRateRepository = guessInputSuccessRateRepository;
    }

    public MultipleLinearRegression getMultipleLinearRegression() {
        List<GuessInputSuccessRate> guessInputSuccessRates = (List<GuessInputSuccessRate>) guessInputSuccessRateRepository.findAll();
        int guessInputSuccessRatesCount = guessInputSuccessRates.size();
        double[][] independentVariables = new double[guessInputSuccessRatesCount][5];
        double[] dependentVariables = new double[guessInputSuccessRatesCount];
        for (int i = 0; i < guessInputSuccessRatesCount; i++) {
            double[] independentVariableSet = new double[5];
            independentVariableSet[0] = 1;
            independentVariableSet[1] = guessInputSuccessRates.get(i).getCountContainedLettersGuessInputs();
            independentVariableSet[2] = guessInputSuccessRates.get(i).getCountKnownLetterPlacementsInputs();
            independentVariableSet[3] = guessInputSuccessRates.get(i).getCountKnownNonLetterPlacementsInputs();
            independentVariableSet[4] = guessInputSuccessRates.get(i).getCountNotContainedLettersGuessInputs();
            independentVariables[i] = independentVariableSet;
            dependentVariables[i] = guessInputSuccessRates.get(i).getSuccessRate();
        }
        return new MultipleLinearRegression(independentVariables, dependentVariables);
    }
}
