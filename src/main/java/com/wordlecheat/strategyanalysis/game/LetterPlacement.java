package com.wordlecheat.strategyanalysis.game;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "letter_placement", uniqueConstraints = @UniqueConstraint(columnNames={"letter", "string_index"}))
public class LetterPlacement {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    
    @Column(name = "letter", nullable = false)
    private String letter;

    @Column(name = "string_index", nullable = false)
    private int stringIndex;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }

    public int getStringIndex() {
        return stringIndex;
    }

    public void setStringIndex(int stringIndex) {
        this.stringIndex = stringIndex;
    }

    @Override
    public String toString() {
        return "LetterPlacement [id=" + id + ", letter=" + letter + ", stringIndex=" + stringIndex + "]";
    }
}
