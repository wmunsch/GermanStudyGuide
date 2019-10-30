package com.williammunsch.germanstudyguide.datamodels;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Database for saving the user's current scores, frequencies,
 * and whether or not a word is being studied for the A1 vocabulary.
 * Updates every time user finishes a A1 vocabulary activity.
 */
@Entity(tableName = "score_tableA1")
public class ScoreModelA1 {
    @PrimaryKey
    private int _id;

    private int score; //between 0 and 100
    private int freq; //between 0 and 99
    private int studying; //between 0 and 1

    public ScoreModelA1(int _id, int score, int freq, int studying) {
        this._id = _id;
        this.score = score;
        this.freq = freq;
        this.studying = studying;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
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
}
