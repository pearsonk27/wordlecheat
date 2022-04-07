package com.wordlecheat;

import com.wordlecheat.builddb.BuildDbService;
import com.wordlecheat.dictionary.repository.DictionaryEntryRepository;
import com.wordlecheat.strategyanalysis.object.MultipleLinearRegression;
import com.wordlecheat.strategyanalysis.object.StrategySuccessRate;
import com.wordlecheat.strategyanalysis.repository.StrategySuccessRateRepository;
import com.wordlecheat.strategyanalysis.service.GuessInputRegressionAnalysisService;
import com.wordlecheat.strategyanalysis.service.StrategyAnalysisService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class WordleCheatApplication {
    
    private static final Logger log = LoggerFactory.getLogger(WordleCheatApplication.class);

    public static void main(String[] args) {
		SpringApplication.run(WordleCheatApplication.class, args);
	}

    @Bean
    public CommandLineRunner run(StrategyAnalysisService strategyAnalysisService, GuessInputRegressionAnalysisService guessInputRegressionAnalysisService, DictionaryEntryRepository dictionaryEntryRepository, StrategySuccessRateRepository strategySuccessRateRepository, BuildDbService buildDbService) {
        return (args) -> {
            printStrategySuccessRates(strategySuccessRateRepository);
            // runRegression(guessInputRegressionAnalysisService);
        };
    }

    private void runRegression(GuessInputRegressionAnalysisService guessInputRegressionAnalysisService) {
        MultipleLinearRegression regression = guessInputRegressionAnalysisService.getMultipleLinearRegression();
        log.info("{} + {} CountContainedLettersGuessInputs + {} CountKnownLetterPlacementsInputs + {} CountKnownNonLetterPlacementsInputs + {} CountNotContainedLettersGuessInputs (R^2 = {})\n",
            String.format("%.2f", regression.beta(0)), 
            String.format("%.2f", regression.beta(1)), 
            String.format("%.2f", regression.beta(2)), 
            String.format("%.2f", regression.beta(3)), 
            String.format("%.2f", regression.beta(4)), 
            String.format("%.2f", regression.R2()));
    }

    private void printStrategySuccessRates(StrategySuccessRateRepository strategySuccessRateRepository) {
        for (StrategySuccessRate strategySuccessRate : strategySuccessRateRepository.findAll()) {
            log.info("Strategy: {} Success Rate: {}", strategySuccessRate.getStrategy(), strategySuccessRate.getSuccessRate());
        }
    }
}
