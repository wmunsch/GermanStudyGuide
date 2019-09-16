package com.williammunsch.germanstudyguide.room;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.williammunsch.germanstudyguide.datamodels.VocabModel;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

@Dao
public interface VocabDao {
    @Query("SELECT * FROM vocab_table")
    LiveData<List<VocabModel>> getAllVocabs();   //ROOM does not support MutableLiveData

    @Query("SELECT * FROM vocab_table LIMIT 1;")
    LiveData<VocabModel> getOneVocab();



    @Query("SELECT * FROM vocab_table WHERE studying = 0 ORDER BY _id LIMIT 5;")// + "SELECT * FROM vocab_table WHERE studying = 1 ODER BY _id LIMIT 5")
    List<VocabModel> getFiveNewVocab();



    //Gets # of new vocab and # of old vocab for review combined
    @Query("SELECT * FROM ( SELECT * FROM vocab_table WHERE studying = 0 LIMIT 3) UNION SELECT * FROM (SELECT * FROM vocab_table WHERE studying = 1 LIMIT 2) ORDER BY _id DESC")
    LiveData<List<VocabModel>> getVocabQueue();




    @Insert
    void insert(VocabModel vocabModel);

    @Query("DELETE FROM vocab_table")
    void deleteAll();

    @Query("SELECT * FROM vocab_Table WHERE studying = 1 ORDER BY score;")
    List<VocabModel> getFiveOldVocab();




    //@Query("SELECT * FROM vocab_table WHERE ")


    /**
     * Counts the number of entries in the table.
     * @return The number of entries.
     */
    @Query("SELECT COUNT(*) FROM  vocab_table")
    LiveData<Integer> count();

    /**
     * Counts the number of learned words in the table.
     * @return The number of learned words.
     */
    @Query("SELECT COUNT(*) FROM  vocab_table WHERE studying = 1")
    LiveData<Integer> countLearned();

    /**
     * Counts the number of mastered words in the table.
     * @return The number of mastered words.
     */
    @Query("SELECT COUNT(*) FROM  vocab_table WHERE score = 100")
    LiveData<Integer> countMastered();

}
