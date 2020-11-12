package com.williammunsch.germanstudyguide.datamodels;

import androidx.room.Entity;
import androidx.room.PrimaryKey;


/**
 * Table for the A1 700 vocabulary words with sentences.
 * Only gets updated if the database is changed, or downloaded on app installation.
 */
@Entity(tableName = "vocab_tableA1")
public class VocabModelA1 {
    @PrimaryKey
    private int _id;

    private int score;
    private int freq;
    private int studying;
    private String german;
    private String english;
    private String gsentence;
    private String esentence;
    //private String[] englishStringsArray;

    public VocabModelA1(int id, String german, String english, String gsentence, String esentence, int studying){//}, int score, int freq, int studying){
        this._id = id;
        this.german = german;
        this.english = english;
        this.gsentence = gsentence;
        this.esentence = esentence;
       // this.score = score;
       // this.freq = freq;
        this.studying = studying;

    }

    public String toString(int score){
        if (score > 50){
            return english;
        }else{
            return german;
        }
       // return _id+ " " + german+ " " +english;
    }

    public String getAnswer(int score){
        if (score < 50){
            return english;
        }else{
            return german;
        }
    }

    public String toSentence(int score){
        if (score > 50){
            return esentence;
        }else{
            return gsentence;
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

    public void increaseScore(){
        if (score < 95){
            score += 70; //should be 5
            if (score >100){score=100;}
        }else{
            score = 100;
        }

    }

    public void fixScore(){
        score+=70;
        if (score > 100){score=100;}
    } //should be 10

    public void decreaseScore(){
        if (score >-95){
            score -=5;
        }else{
            score = -100;
        }
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

    public String getGsentence() {
        return gsentence;
    }

    public void setGsentence(String gsentence) {
        this.gsentence = gsentence;
    }

    public String getEsentence() {
        return esentence;
    }

    public void setEsentence(String esentence) {
        this.esentence = esentence;
    }
}
