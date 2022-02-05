package com.wordlecheat.strategyanalysis.object;

import com.wordlecheat.strategyanalysis.game.GameState;
import com.wordlecheat.strategyanalysis.game.Guess;
import com.wordlecheat.strategyanalysis.service.GuessService;

public interface GuessMethod {
    
    Guess makeGuess(GuessService guessService, GameState gameState);
}
