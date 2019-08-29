package com.williammunsch.germanstudyguide.repositories;

import androidx.lifecycle.MutableLiveData;
import android.content.Context;

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

    public static VocabRecyclerViewRepository getInstance(){
        if (instance ==null){
            instance = new VocabRecyclerViewRepository();
        }
        return instance;
    }


    //Get data from a web service or online source
    public MutableLiveData<List<VocabListItem>> getVocabListItems(Context context){
        setVocabListItems(context);
        MutableLiveData<List<VocabListItem>> data = new MutableLiveData<>();
        data.setValue(dataSet);
        return data;
    }

    private void setVocabListItems(Context context){
        //retrieve data from database here
        //MAKE SURE TO USE ASYNC TASK FOR DATABASE QUERIES IN FUTURE
        DBManager db = DBManager.getInstance(context);


        dataSet.add(new VocabListItem("Beginner Level 1","A1",0,0,0));//db.getWordsLearned("A1"),db.getWordsMax("A1"),db.getWordsMastered("A1")));
        //dataSet.add(new VocabListItem("Beginner Level 2","A2",db.getWordsLearned("A2"),db.getWordsMax("A2"),db.getWordsMastered("A2")));
        //dataSet.add(new VocabListItem("Intermediate Level 1","B1",db.getWordsLearned("B1"),db.getWordsMax("B1"),db.getWordsMastered("B1")));
        dataSet.add(new VocabListItem("Beginner Level 2","A2",0,0,0));
        dataSet.add(new VocabListItem("Intermediate Level 1","B1",0,0,0));
        dataSet.add(new VocabListItem("Intermediate Level 2","B2",0,0,0));
        dataSet.add(new VocabListItem("Advanced Level 1","C1",0,0,0));
        dataSet.add(new VocabListItem("Advanced Level 2","C2",0,0,0));
    }
}


