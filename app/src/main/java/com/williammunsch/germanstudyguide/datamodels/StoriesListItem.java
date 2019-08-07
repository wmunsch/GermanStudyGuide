package com.williammunsch.germanstudyguide.datamodels;

import android.graphics.drawable.Drawable;
import android.media.Image;

/**
 * Data model for each story in the recyclerView.
 */
public class StoriesListItem {
    private String title, author;
    //private Drawable icon;

    public StoriesListItem(String nTitle, String nAuthor){//, Drawable nIcon){
        this.title = nTitle;
        this.author = nAuthor;
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
}
