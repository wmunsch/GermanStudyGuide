package com.williammunsch.germanstudyguide.room;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.williammunsch.germanstudyguide.datamodels.Hag_Sentences;
import com.williammunsch.germanstudyguide.datamodels.Hag_Words;
import com.williammunsch.germanstudyguide.datamodels.StoriesListItem;
import com.williammunsch.germanstudyguide.datamodels.Story;
import com.williammunsch.germanstudyguide.datamodels.VocabListItem;

import java.util.List;

/**
 * Interface for interaction with the story table in the database
 */

@Dao
public interface StoryDao {
    @Query("SELECT * FROM stories_list_table")
    LiveData<List<StoriesListItem>> getAllStoriesLists();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(StoriesListItem storyListItem);

    @Query("SELECT COUNT() FROM stories_list_table")
    Integer countStories();

    @Query("SELECT COUNT() FROM hag_sentences")
    Integer countPages();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertHagSentence(Hag_Sentences hag_sentences);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertHagWord(Hag_Words hag_words);

    @Query("SELECT * FROM hag_sentences")
    LiveData<List<Hag_Sentences>> getSentences();

    @Query("SELECT * FROM hag_words WHERE german = :german")
    Hag_Words getWord(String german);

    @Query("SELECT * FROM hag_words WHERE german = :german")
    LiveData<Hag_Words> getWord2(String german);


    @Query("SELECT * FROM hag_words WHERE german = 'und'")
    Hag_Words getWord3();




}
