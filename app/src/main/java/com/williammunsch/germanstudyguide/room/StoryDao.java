package com.williammunsch.germanstudyguide.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

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
