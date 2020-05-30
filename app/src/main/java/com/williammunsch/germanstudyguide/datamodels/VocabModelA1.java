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
    private String gesent;
    private String esent;
    //private String[] englishStringsArray;

    public VocabModelA1(int id, String german, String english, String gesent, String esent, int studying){//}, int score, int freq, int studying){
        this._id = id;
        this.german = german;
        this.english = english;
        this.gesent = gesent;
        this.esent = esent;
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

    public void increaseScore(){
        if (score < 95){
            score += 5;
        }else{
            score = 100;
        }

    }

    public void fixScore(){
        if (score == 0){
            score += 5;
        }else{
            score+=10;
        }
    }

    public void decreaseScore(){
        if (score >5){
            score -=5;
        }else{
            score = 0;
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
