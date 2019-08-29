package com.williammunsch.germanstudyguide.datamodels;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import android.graphics.drawable.Drawable;

/**
 * Data model for each story in the recyclerView.
 */

public class StoriesListItem {
    private int id;
    private String title;
    private String author;
    private Drawable icon;
    private String content;

    public StoriesListItem(int id, String nTitle, String nAuthor, String content){//, Drawable nIcon){
        this.title = nTitle;
        this.author = nAuthor;
        this.content = content;
        this.id = id;
        this.icon = null;
        //this.icon = nIcon;
    }

    public StoriesListItem(String nTitle, String nAuthor){//, Drawable nIcon){
        this.title = nTitle;
        this.author = nAuthor;
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

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
