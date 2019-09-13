package com.williammunsch.germanstudyguide.datamodels;

import androidx.room.Entity;
import androidx.room.PrimaryKey;



@Entity(tableName = "vocab_table")
public class VocabModel {
    @PrimaryKey
    private int _id;

    private int score;
    private int freq;
    private int studying;
    private String german;
    private String english;
    private String gsent;
    private String esent;

    public VocabModel(int id, String german, String english, String gsent, String esent, int score, int freq, int studying){
        this._id = id;
        this.german = german;
        this.english = english;
        this.gsent = gsent;
        this.esent = esent;
        this.score = score;
        this.freq = freq;
        this.studying = studying;
    }

    public String toString(){
        return _id+ " " + german+ " " +english;
    }

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        this._id = id;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getFreq() {
        return freq;
    }

    public void setFreq(int freq) {
        this.freq = freq;
    }

    public int getStudying() {
        return studying;
    }

    public void setStudying(int studying) {
        this.studying = studying;
    }

    public String getGerman() {
        return german;
    }

    public void setGerman(String german) {
        this.german = german;
    }

    public String getEnglish() {
        return english;
    }

    public void setEnglish(String english) {
        this.english = english;
    }

    public String getGsent() {
        return gsent;
    }

    public void setGsent(String gsent) {
        this.gsent = gsent;
    }

    public String getEsent() {
        return esent;
    }

    public void setEsent(String esent) {
        this.esent = esent;
    }
}
