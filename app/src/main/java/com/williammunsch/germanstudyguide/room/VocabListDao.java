package com.williammunsch.germanstudyguide.room;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.williammunsch.germanstudyguide.datamodels.VocabListItem;

import java.util.List;

@Dao
public interface VocabListDao {

    @Query("SELECT * FROM vocab_list_table")
    LiveData<List<VocabListItem>> getAllVocabLists();

    @Query("SELECT COUNT(*) FROM vocab_list_table WHERE name = :name")
    Integer vocabLessonExists(String name);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(VocabListItem vocabListItem);

    @Query("DELETE FROM vocab_list_table")
    void deleteAll();
}
