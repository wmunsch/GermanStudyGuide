package com.williammunsch.germanstudyguide.datamodels;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Table for storing each page of the Hansel and Gretel story
 */
@Entity(tableName = "hag_sentences")
public class Hag_Sentences {


    @PrimaryKey
    private int _id;

    private String german;
    private String english;

    public Hag_Sentences(int id, String german, String english){
        this._id = id;
        this.german = german;
        this.english = english;
    }


    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
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
}
