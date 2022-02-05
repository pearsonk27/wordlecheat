package com.wordlecheat.strategyanalysis.game;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.wordlecheat.dictionary.object.DictionaryEntry;
import com.wordlecheat.strategyanalysis.object.StrategyExecution;

@Entity
@Table(name = "guess")
public class Guess {
    
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    
    @OneToOne
    @JoinColumn(name = "guess_dictionary_id", referencedColumnName = "id", nullable = false)
    private DictionaryEntry dictionaryEntry;

    @ManyToOne
    @JoinColumn(name = "strategy_execution_id", referencedColumnName = "id", nullable = false)
    private StrategyExecution strategyExecution;

    @Column(name = "guess_number", nullable = false)
    private int guessNumber;

    @ElementCollection
    @CollectionTable(name = "contained_letters_guess_input", joinColumns = @JoinColumn(name = "guess_id"))
    @Column(name = "letter", length = 1)
    private Set<String> containedLettersInput;

    @ElementCollection
    @CollectionTable(name = "not_contained_letters_guess_input", joinColumns = @JoinColumn(name = "guess_id"))
    @Column(name = "letter", length = 1)
    private Set<String> notContainedLettersInput;

    @ManyToMany(cascade = { CascadeType.MERGE })
    @JoinTable(
        name = "known_letter_placements_input", 
        joinColumns = { @JoinColumn(name = "guess_id") }, 
        inverseJoinColumns = { @JoinColumn(name = "letter_placement_id") }
    )
    private Set<LetterPlacement> knownLetterPlacementsInput;

    @ManyToMany(cascade = { CascadeType.MERGE })
    @JoinTable(
        name = "known_non_letter_placements_input", 
        joinColumns = { @JoinColumn(name = "guess_id") }, 
        inverseJoinColumns = { @JoinColumn(name = "letter_placement_id") }
    )
    private Set<LetterPlacement> knownNonLetterPlacementsInput;

    @Column(name = "count_possible_answers")
    private int countPossibleAnswers;

    public Guess() {}

    public Guess(DictionaryEntry dictionaryEntry, int guessNumber) {
        this.dictionaryEntry = dictionaryEntry;
        this.guessNumber = guessNumber;
    }

    public Guess(DictionaryEntry dictionaryEntry, int guessNumber, int countPossibleAnswers) {
        this(dictionaryEntry, guessNumber);
        this.countPossibleAnswers = countPossibleAnswers;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public DictionaryEntry getDictionaryEntry() {
        return dictionaryEntry;
    }

    public void setDictionaryEntry(DictionaryEntry dictionaryEntry) {
        this.dictionaryEntry = dictionaryEntry;
    }

    public StrategyExecution getStrategyExecution() {
        return strategyExecution;
    }

    public void setStrategyExecution(StrategyExecution strategyExecution) {
        this.strategyExecution = strategyExecution;
    }

    public int getGuessNumber() {
        return guessNumber;
    }

    public void setGuessNumber(int guessNumber) {
        this.guessNumber = guessNumber;
    }

    public Set<String> getContainedLettersInput() {
        return containedLettersInput;
    }

    public void setContainedLettersInput(Set<String> containedLettersInput) {
        this.containedLettersInput = containedLettersInput;
    }

    public Set<String> getNotContainedLettersInput() {
        return notContainedLettersInput;
    }

    public void setNotContainedLettersInput(Set<String> notContainedLettersInput) {
        this.notContainedLettersInput = notContainedLettersInput;
    }

    public Set<LetterPlacement> getKnownLetterPlacementsInput() {
        return knownLetterPlacementsInput;
    }

    public void setKnownLetterPlacementsInput(Set<LetterPlacement> knownLetterPlacementsInput) {
        this.knownLetterPlacementsInput = knownLetterPlacementsInput;
    }

    public Set<LetterPlacement> getKnownNonLetterPlacementsInput() {
        return knownNonLetterPlacementsInput;
    }

    public void setKnownNonLetterPlacementsInput(Set<LetterPlacement> knownNonLetterPlacementsInput) {
        this.knownNonLetterPlacementsInput = knownNonLetterPlacementsInput;
    }

    public int getCountPossibleAnswers() {
        return countPossibleAnswers;
    }

    public void setCountPossibleAnswers(int countPossibleAnswers) {
        this.countPossibleAnswers = countPossibleAnswers;
    }

    @Override
    public String toString() {
        return "Guess [containedLettersInput=" + containedLettersInput + ", dictionaryEntry=" + dictionaryEntry
                + ", guessNumber=" + guessNumber + ", id=" + id + ", knownLetterPlacementsInput="
                + knownLetterPlacementsInput + ", knownNonLetterPlacementsInput=" + knownNonLetterPlacementsInput
                + ", notContainedLettersInput=" + notContainedLettersInput + ", countPossibleAnswers=" + countPossibleAnswers + "]";
    }
}
