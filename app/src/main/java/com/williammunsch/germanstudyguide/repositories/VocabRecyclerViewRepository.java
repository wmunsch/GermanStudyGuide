package com.williammunsch.germanstudyguide.repositories;

import android.arch.lifecycle.MutableLiveData;

import com.williammunsch.germanstudyguide.DBManager;
import com.williammunsch.germanstudyguide.datamodels.VocabListItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Gets the data for displaying the recyclerView from the database and stores it.
 * Singleton pattern
 */

public class VocabRecyclerViewRepository {

    private static VocabRecyclerViewRepository instance;
    private ArrayList<VocabListItem> dataSet = new ArrayList<>();
    //private DBManager dbManager;

    public static VocabRecyclerViewRepository getInstance(){
        if (instance ==null){
            instance = new VocabRecyclerViewRepository();
        }
        return instance;
    }


    //Get data from a web service or online source
    public MutableLiveData<List<VocabListItem>> getVocabListItems(){
        setVocabListItems();
        MutableLiveData<List<VocabListItem>> data = new MutableLiveData<>();
        data.setValue(dataSet);
        return data;
    }

    private void setVocabListItems(){
        //retrieve data from database here
        //MAKE SURE TO USE ASYNC TASK FOR DATABASE QUERIES IN FUTURE
        //dbManager = DBManager.getInstance();

        /*

       //SQL Query
        int a1Max = dbManager.getWordsMax("A1");
        int a1Learned = dbManager.getWordsLearned("A1");
        int a1Mastered = dbManager.getWordsMastered("A1");
         */

        dataSet.add(new VocabListItem("Beginner Level 1","A1",500,700,100));
        dataSet.add(new VocabListItem("Beginner Level 2","A2",500,700,100));
        dataSet.add(new VocabListItem("Intermediate Level 1","B1",500,1400,100));
        dataSet.add(new VocabListItem("Intermediate Level 2","B2",0,1400,0));
        dataSet.add(new VocabListItem("Advanced Level 1","C1",0,2000,0));
        dataSet.add(new VocabListItem("Advanced Level 2","C2",0,2000,0));
    }
}


