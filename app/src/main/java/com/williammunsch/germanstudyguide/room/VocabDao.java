package com.williammunsch.germanstudyguide.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.williammunsch.germanstudyguide.datamodels.VocabModel;

import java.util.List;

@Dao
public interface VocabDao {
    @Query("SELECT * FROM vocab_table")
    LiveData<List<VocabModel>> getAllVocabs();

    @Query("SELECT * FROM vocab_table LIMIT 1;")
    LiveData<VocabModel> getOneVocab();

    @Insert
    void insert(VocabModel vocabModel);

    @Query("DELETE FROM vocab_table")
    void deleteAll();


    //@Query("SELECT * FROM vocab_table WHERE ")


    /**
     * Counts the number of entries in the table.
     * @return The number of entries.
     */
    @Query("SELECT COUNT(*) FROM  vocab_table")
    LiveData<Integer> count();

}
