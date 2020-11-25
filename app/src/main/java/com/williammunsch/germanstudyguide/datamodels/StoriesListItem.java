package com.williammunsch.germanstudyguide.datamodels;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import android.graphics.drawable.Drawable;

/**
 * Data model for each story in the recyclerView.
 */
@Entity(tableName = "stories_list_table")
public class StoriesListItem {
   // private int id;

    @PrimaryKey@NonNull
    private String title;

    private String author;
    //private String icon;
   // private String content;

    public StoriesListItem(String title, String author){//, Drawable nIcon){
        this.title = title;
        this.author = author;
        //this.content = content;
       // this.id = id;
        //this.icon = icon;
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

    //public String getIcon() {
    //    return icon;
   // }

   // public void setIcon(String icon) {
   //     this.icon = icon;
   // }


}
