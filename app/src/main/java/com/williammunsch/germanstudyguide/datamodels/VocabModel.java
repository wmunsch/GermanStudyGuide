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
    private String gesent;
    private String esent;
    //private String[] englishStringsArray;

    public VocabModel(int id, String german, String english, String gesent, String esent, int score, int freq, int studying){
        this._id = id;
        this.german = german;
        this.english = english;
        this.gesent = gesent;
        this.esent = esent;
        this.score = score;
        this.freq = freq;
        this.studying = studying;

    }

    public String toString(){
        if (score > 50){
            return english;
        }else{
            return german;
        }
       // return _id+ " " + german+ " " +english;
    }

    public String toSentence(){
        if (score > 50){
            return esent;
        }else{
            return gesent;
        }
    }

    public String[] getEnglishStringsArray()
    {
        if (english!=null){
            String[] englishStringsArray = english.split(",");
            return englishStringsArray;
        }
        return null;

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

    public String getGesent() {
        return gesent;
    }

    public void setGesent(String gesent) {
        this.gesent = gesent;
    }

    public String getEsent() {
        return esent;
    }

    public void setEsent(String esent) {
        this.esent = esent;
    }
}
