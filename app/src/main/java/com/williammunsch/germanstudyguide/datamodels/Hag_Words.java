package com.williammunsch.germanstudyguide.datamodels;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "hag_words")
public class Hag_Words {

    @PrimaryKey @NonNull
    private String german;

    private String english;
    private String notes;

    public Hag_Words(String german, String english, String notes){
        this.german = german;
        this.english = english;
        this.notes = notes;

    }

    public String getGerman() {
        return german + "  -  ";
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

    public String getNotes() {
        String tempString = notes.replace(";","\n");
        return tempString;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
