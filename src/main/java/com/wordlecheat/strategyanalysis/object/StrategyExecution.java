package com.wordlecheat.strategyanalysis.object;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.wordlecheat.dictionary.object.DictionaryEntry;
import com.wordlecheat.strategyanalysis.game.Guess;

@Entity
@Table(name = "strategy_execution")
public class StrategyExecution {
    
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    
    @Column(name = "strategy", nullable = false)
    @Enumerated(EnumType.STRING)
    private Strategy strategy;

    @OneToMany(targetEntity = Guess.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "strategy_execution_id", referencedColumnName = "id")
    private List<Guess> guesses;

    @OneToOne
    @JoinColumn(name = "wordle_word_id", referencedColumnName = "id", nullable = false)
    private DictionaryEntry wordleWord;

    @Column(name = "is_success")
    private boolean isSuccess;

    @Column(name = "creation_date")
    private Date creationDate;

    public StrategyExecution() {
        this.creationDate = new Date();
    }

    public StrategyExecution(Strategy strategy, DictionaryEntry wordleWord) {
        this.strategy = strategy;
        this.wordleWord = wordleWord;
        this.creationDate = new Date();
    }

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

    public DictionaryEntry getWordleWord() {
        return wordleWord;
    }

    public void setWordleWord(DictionaryEntry wordleWord) {
        this.wordleWord = wordleWord;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public List<Guess> getGuesses() {
        return guesses;
    }

    public void setGuesses(List<Guess> guesses) {
        this.guesses = guesses;
    }

    public void addGuess(Guess guess) {
        guess.setStrategyExecution(this);
        guesses.add(guess);
    }

    public boolean isInProgress() {
        return !isSuccess && guesses.size() < 6;
    }

    public Guess getCurrentGuess() {
        return guesses.get(guesses.size() - 1);
    }
}
