package com.wordlecheat.dictionary.object;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "dictionary")
public class DictionaryEntry {
    
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "word", nullable = false, unique = true)
    private String word;

    @Column(name = "frequency")
    private Double frequency;

    @OneToMany(targetEntity = Definition.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "dictionary_id", referencedColumnName = "id")
    private Set<Definition> definitions;

    @Column(name = "last_checked_frequency")
    private Date lastCheckedFrequency;

    @Column(name = "letter_frequency")
    private Double letterFrequency;

    public DictionaryEntry() {

    }

    public DictionaryEntry(String entry) throws StringIndexOutOfBoundsException {
        String word = entry.substring(0, entry.indexOf("(") - 1).trim();
        this.word = word;
        this.definitions = new HashSet<>();
        addDefinition(new Definition(entry));
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public Double getFrequency() {
        return frequency;
    }

    public void setFrequency(Double frequency) {
        this.frequency = frequency;
    }

    public Set<Definition> getDefinitions() {
        return definitions;
    }

    public void setDefinitions(Set<Definition> definitions) {
        this.definitions = definitions;
    }

    public void addDefinition(Definition newDefinition) {
        boolean addDefinition = true;
        for (Definition definition : definitions) {
            if (definition.getDefinition().equals(newDefinition.getDefinition())) {
                addDefinition = false;
            }
        }
        if (addDefinition) {
            newDefinition.setDictionaryEntry(this);
            definitions.add(newDefinition);
        }
    }

    public Date getLastCheckedFrequency() {
        return lastCheckedFrequency;
    }

    public void setLastCheckedFrequency(Date lastCheckedFrequency) {
        this.lastCheckedFrequency = lastCheckedFrequency;
    }

    public boolean isValidWordlerWord() {
        return word != null && word.length() == 5 && word.matches("^[a-zA-Z]*$");
    }

    public Double getLetterFrequency() {
        return letterFrequency;
    }

    public void setLetterFrequency(Double letterFrequency) {
        this.letterFrequency = letterFrequency;
    }

    @Override
    public String toString() {
        return "DictionaryEntry [definitions=" + definitions.toString() + ", frequency=" + frequency + ", id=" + id
                + ", lastCheckedFrequency=" + lastCheckedFrequency + ", word=" + word + "]";
    }
}
