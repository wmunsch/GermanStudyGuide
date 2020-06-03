package com.williammunsch.germanstudyguide.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.williammunsch.germanstudyguide.datamodels.ScoreModelA1;
import com.williammunsch.germanstudyguide.datamodels.VocabModelA1;

import java.util.List;

/**
 * The data access object responsible for retrieving vocabulary data from the local ROOM database.
 * ROOM creates the DAO at compile time.
 */
@Dao
public interface VocabDao {
    @Query("SELECT * FROM vocab_tableA1")
    LiveData<List<VocabModelA1>> getAllVocabs();   //ROOM does not support MutableLiveData

    @Query("SELECT * FROM vocab_tableA1 LIMIT 1;")
    LiveData<VocabModelA1> getOneVocab();



    //@Query("SELECT * FROM vocab_tableA1 WHERE studying = 0 ORDER BY _id LIMIT 5;")// + "SELECT * FROM vocab_table WHERE studying = 1 ODER BY _id LIMIT 5")
   // List<VocabModelA1> getFiveNewVocab();



    //Gets # of new vocab and # of old vocab for review combined
    @Query("SELECT * FROM ( SELECT * FROM vocab_tableA1 WHERE studying = 1 ORDER BY score LIMIT 15) UNION SELECT * FROM (SELECT * FROM vocab_tableA1 WHERE studying = 0 ORDER BY _id LIMIT 5) ORDER BY studying ASC")
    LiveData<List<VocabModelA1>> getVocabQueue();

    //Gets 20 learned vocab for review after learning all
    @Query("SELECT * FROM vocab_tableA1 WHERE studying = 1 ORDER BY score LIMIT 20")
    LiveData<List<VocabModelA1>> getVocabQueueFinished();


    //Gets # of new vocab and # of old vocab for review combined
   // @Query("SELECT * FROM ( SELECT * FROM vocab_table WHERE studying = 0 LIMIT 3) UNION SELECT * FROM (SELECT * FROM vocab_table WHERE studying = 1 LIMIT 2) ORDER BY _id DESC")
    //List<VocabModelA1>getVocabQueue();


    /**
     * Update the vocabModel word with the new score.
     */
    @Update
    void updateNode(VocabModelA1... vocabModelA1s);
   // @Query("UPDATE vocab_table SET score = score")
   // void updateNode(VocabModelA1 vocabModel);



    //Inserts the A1 data, will replace unique constraints in case of a non-fully downloaded previous attempt
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(VocabModelA1 vocabModelA1);

    //Deletes everything in the A1 table
    @Query("DELETE FROM vocab_tableA1")
    void deleteAll();

   // @Query("SELECT * FROM vocab_TableA1 WHERE studying = 1 ORDER BY score;")
    //List<VocabModelA1> getFiveOldVocab();


    /**
     * Called when logging in.
     */
    @Insert
    void insertIntoScoreModelA1(ScoreModelA1... scoreModelA1s);

    /**
     * Called when logging out.
     */
    @Query("DELETE FROM score_tableA1")
    void deleteAllScores();


    //@Query("SELECT * FROM vocab_table WHERE ")

    @Query("SELECT COUNT(*) FROM  vocab_tableA1")
    Integer countA1();


    /**
     * Counts the number of entries in the A1 table.
     * @return The number of entries.
     */
    @Query("SELECT COUNT(*) FROM  vocab_tableA1")
    LiveData<Integer> count();

    /**
     * Counts the number of learned words in the table.
     * @return The number of learned words.
     */
    @Query("SELECT COUNT(*) FROM  vocab_tableA1 WHERE studying = 1")
    LiveData<Integer> countLearned();

    /**
     * Counts the number of mastered words in the table.
     * @return The number of mastered words.
     */
    @Query("SELECT COUNT(*) FROM  vocab_tableA1 WHERE score >= 100")
    LiveData<Integer> countMastered();


}
