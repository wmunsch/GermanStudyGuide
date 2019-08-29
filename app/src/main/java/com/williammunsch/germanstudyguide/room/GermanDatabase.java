package com.williammunsch.germanstudyguide.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.williammunsch.germanstudyguide.datamodels.Story;
import com.williammunsch.germanstudyguide.datamodels.VocabModel;

@Database(entities = {VocabModel.class, Story.class}, version = 1)
public abstract class GermanDatabase extends RoomDatabase {

    public abstract VocabDao vocabDao();

    public abstract StoryDao storyDao();
}
