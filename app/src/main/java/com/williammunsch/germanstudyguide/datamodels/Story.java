package com.williammunsch.germanstudyguide.datamodels;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.graphics.drawable.Drawable;

/**
 * Data model for each story in the recyclerView.
 */
@Entity(tableName = "story")
public class Story {

    @PrimaryKey() //false by default
    @ColumnInfo(name="_id")
    private int id;
    @ColumnInfo(name="title")
    private String title;
    @ColumnInfo(name="author")
    private String author;
    @ColumnInfo(name="content")
    private String content;
    //private Drawable icon;

    public Story(int id, String title, String author, String content){//, Drawable nIcon){
        this.title = title;
        this.author = author;
        this.content = content;
        //this.icon = nIcon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    // public Drawable getIcon() {
    // return icon;
    // }

    public void setIcon(Drawable icon) {
        //this.icon = icon;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
