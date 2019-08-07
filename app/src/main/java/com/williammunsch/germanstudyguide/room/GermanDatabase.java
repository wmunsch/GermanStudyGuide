package com.williammunsch.germanstudyguide.room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.williammunsch.germanstudyguide.datamodels.Story;

@Database(entities = {Story.class}, version = 1)
public abstract class GermanDatabase extends RoomDatabase {

    private static final String DB_NAME = "german_db";
    private static GermanDatabase INSTANCE;

    /*
    public static synchronized GermanDatabase getInstance(Context context){
        if (INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), GermanDatabase.class, DB_NAME).fallbackToDestructiveMigration().build();
        }
        return INSTANCE;

    }
*/
    //database = Room.databaseBuilder(context.getApplicationContext(), GermanDatabase.class, "DB_NAME").build();

    public abstract StoryDao storyDao();
}
