package com.wordlecheat.strategyanalysis.object;

import com.wordlecheat.dictionary.object.DictionaryEntry;
import com.wordlecheat.strategyanalysis.game.GameState;
import com.wordlecheat.strategyanalysis.service.GuessService;

public interface GuessMethod {
    
    DictionaryEntry makeGuess(GuessService guessService, GameState gameState);
}
