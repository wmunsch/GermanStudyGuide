package com.williammunsch.germanstudyguide.datamodels;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Data model for each item in the vocabulary recyclerView (page 1 of the sectionsPageAdapter).
 */

@Entity(tableName = "vocab_list_table")
public class VocabListItem {
    @PrimaryKey@NonNull
    private String name;

    private String image;
    private int learnedPercent;
    private int masteredPercent;
    private int wordsLearned;
    private int wordsMax;
    private int wordsMastered;

    public VocabListItem(String name, String image, int wordsLearned, int wordsMax, int wordsMastered){
        this.name = name;
        this.image = image;
        this.wordsLearned = wordsLearned;
        this.wordsMax = wordsMax;
        this.wordsMastered = wordsMastered;
        this.learnedPercent = (int)(((double)wordsLearned/wordsMax)*100);
        this.masteredPercent = (int)(((double)wordsMastered/wordsMax)*100);
    }

    public String toString(){return name + " : " + wordsLearned + " / " + wordsMax;}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getLearnedPercent() {
        return learnedPercent;
    }

    public void setLearnedPercent(int learnedPercent) {
        this.learnedPercent = learnedPercent;
    }

    public int getMasteredPercent() {
        return masteredPercent;
    }

    public void setMasteredPercent(int masteredPercent) {
        this.masteredPercent = masteredPercent;
    }

    public int getWordsLearned() {
        return wordsLearned;
    }

    public void setWordsLearned(int wordsLearned) {
        this.wordsLearned = wordsLearned;
    }

    public int getWordsMax() {
        return wordsMax;
    }

    public void setWordsMax(int wordsMax) {
        this.wordsMax = wordsMax;
    }

    public int getWordsMastered() {
        return wordsMastered;
    }

    public void setWordsMastered(int wordsMastered) {
        this.wordsMastered = wordsMastered;
    }
}
