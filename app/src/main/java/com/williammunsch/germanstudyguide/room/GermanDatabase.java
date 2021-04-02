package com.williammunsch.germanstudyguide.room;

import androidx.lifecycle.MutableLiveData;
import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.williammunsch.germanstudyguide.User;
import com.williammunsch.germanstudyguide.datamodels.Hag_Sentences;
import com.williammunsch.germanstudyguide.datamodels.Hag_Words;
import com.williammunsch.germanstudyguide.datamodels.LocalSaveA1;
import com.williammunsch.germanstudyguide.datamodels.ScoreModelA1;
import com.williammunsch.germanstudyguide.datamodels.StoriesListItem;
import com.williammunsch.germanstudyguide.datamodels.Story;
import com.williammunsch.germanstudyguide.datamodels.VocabListItem;
import com.williammunsch.germanstudyguide.datamodels.VocabModelA1;
import com.williammunsch.germanstudyguide.datamodels.VocabModelA2;
import com.williammunsch.germanstudyguide.datamodels.VocabModelB1;

/**
 * The ROOM database which stores usernames, stories, and vocab data.
 */

@Database(entities = {VocabModelA1.class, VocabModelA2.class, VocabModelB1.class, Story.class, VocabListItem.class, User.class, ScoreModelA1.class, StoriesListItem.class,
        Hag_Sentences.class, Hag_Words.class, LocalSaveA1.class}, version = 1, exportSchema = false)
public abstract class GermanDatabase extends RoomDatabase {

    public abstract VocabDao vocabDao();

    public abstract VocabListDao vocabListDao();

    public abstract StoryDao storyDao();

    public abstract UserDao userDao();



}
