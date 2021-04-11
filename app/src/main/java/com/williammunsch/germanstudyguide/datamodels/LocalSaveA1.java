package com.williammunsch.germanstudyguide.datamodels;

import androidx.room.Entity;
import androidx.room.PrimaryKey;


/**
 * The table for storing the local save data when the user is not logged in
 * so it won't be lost when logging in and back out.
 */
@Entity(tableName = "local_tableA1")
public class LocalSaveA1 {
    @PrimaryKey
    private int _id;

    private int score; //between 0 and 100
    private int freq; //between 0 and 99
    private int studying; //between 0 and 1

    public LocalSaveA1(int _id, int score, int freq, int studying) {
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
