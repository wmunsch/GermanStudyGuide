package com.williammunsch.germanstudyguide.room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.williammunsch.germanstudyguide.datamodels.Story;

import java.util.List;

/**
 * Interface for interaction with the story table in the database
 */

@Dao
public interface StoryDao {
    @Query("SELECT * FROM story")
    List<Story> getStoryList();

    @Query("SELECT COUNT() FROM story")
    int getNumberOfRows();
}
