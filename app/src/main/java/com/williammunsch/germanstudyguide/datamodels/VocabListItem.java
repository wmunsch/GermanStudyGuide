package com.williammunsch.germanstudyguide.datamodels;

/**
 * Data model for each item in the vocabulary recyclerView.
 */

public class VocabListItem {
    private String name,image;
    private int learnedPercent;
    private int masteredPercent;
    private int wordsLearned;
    private int wordsMax;
    private int wordsMastered;

    public VocabListItem(String mName, String mImage, int mWordsLearned, int mWordsMax, int mWordsMastered){
        this.name = mName;
        this.image = mImage;
        this.wordsLearned = mWordsLearned;
        this.wordsMax = mWordsMax;
        this.wordsMastered = mWordsMastered;
        this.learnedPercent = (int)(((double)wordsLearned/wordsMax)*100);
        this.masteredPercent = (int)(((double)wordsMastered/wordsMax)*100);
    }

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
