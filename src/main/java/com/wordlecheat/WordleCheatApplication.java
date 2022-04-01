package com.wordlecheat;

import java.util.Arrays;
import java.util.List;

import com.wordlecheat.builddb.BuildDbService;
import com.wordlecheat.dictionary.object.DictionaryEntry;
import com.wordlecheat.dictionary.object.WordleWords;
import com.wordlecheat.dictionary.repository.DictionaryEntryRepository;
import com.wordlecheat.strategyanalysis.object.MultipleLinearRegression;
import com.wordlecheat.strategyanalysis.object.Strategy;
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
    // private static final String[] wordleWords = WordleWords.WORDLE_WORDS;
    private static final String[] wordleWords = Arrays.copyOfRange(WordleWords.WORDLE_WORDS, 0, 2);

    public static void main(String[] args) {
		SpringApplication.run(WordleCheatApplication.class, args);
	}

    @Bean
    public CommandLineRunner run(StrategyAnalysisService strategyAnalysisService, GuessInputRegressionAnalysisService guessInputRegressionAnalysisService, DictionaryEntryRepository dictionaryEntryRepository, StrategySuccessRateRepository strategySuccessRateRepository, BuildDbService buildDbService) {
        return (args) -> {
            // compileStrategyData(strategyAnalysisService, dictionaryEntryRepository);
            // compileStrategyDataUsingWordleWordsOnly(strategyAnalysisService, dictionaryEntryRepository);
            // printStrategySuccessRates(strategySuccessRateRepository);
            // runRegression(guessInputRegressionAnalysisService);
            // buildDbService.buildDictionary();
            
            // System.out.println("Profiles");
            // for (String profile : env.getActiveProfiles()) {
            //     System.out.println(profile);
            // }
            // System.out.println(dictionaryEntryRepository.getRandomNLetterWord(5));
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

    private void compileStrategyData(StrategyAnalysisService strategyAnalysisService, DictionaryEntryRepository dictionaryEntryRepository) {
        List<DictionaryEntry> dictionaryEntries = dictionaryEntryRepository.findByFrequencyGreaterThan(0.0);
        log.info("Starting analysis of {} strategies, each on {} words", Strategy.values().length, dictionaryEntries.size());
        for (Strategy strategy : Strategy.values()) {
            for (DictionaryEntry wordleWord : dictionaryEntries) {
                strategyAnalysisService.playGame(strategy, wordleWord);
            }
        }
    }

    private void compileStrategyDataUsingWordleWordsOnly(StrategyAnalysisService strategyAnalysisService, DictionaryEntryRepository dictionaryEntryRepository) {
        log.info("Starting analysis of {} strategies, each on {} words", Strategy.values().length, wordleWords.length);
        for (Strategy strategy : Strategy.values()) {
            for (String wordleWord : wordleWords) {
                strategyAnalysisService.playGame(strategy, wordleWord);
            }
        }
    }

    private void printStrategySuccessRates(StrategySuccessRateRepository strategySuccessRateRepository) {
        for (StrategySuccessRate strategySuccessRate : strategySuccessRateRepository.findAll()) {
            log.info("Strategy: {} Success Rate: {}", strategySuccessRate.getStrategy(), strategySuccessRate.getSuccessRate());
        }
    }
}
