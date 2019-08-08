package com.williammunsch.germanstudyguide.repositories;

import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import com.williammunsch.germanstudyguide.DBManager;
import com.williammunsch.germanstudyguide.datamodels.Word;

import java.util.ArrayList;
import java.util.List;

public class WordRepository {
    private static WordRepository instance;
    private ArrayList<Word> dataSet = new ArrayList<>();

    public static WordRepository getInstance(){
        if (instance ==null){
            instance = new WordRepository();
        }
        return instance;
    }

    //Get data from a web service or online source
    public MutableLiveData<List<Word>> getWordList(Context context){
        setWordList(context);
        MutableLiveData<List<Word>> data = new MutableLiveData<>();
        data.setValue(dataSet);
        return data;
    }

    private void setWordList(Context context){
        //retrieve data from database here
        //MAKE SURE TO USE ASYNC TASK FOR DATABASE QUERIES IN FUTURE

        DBManager db = DBManager.getInstance(context);
        dataSet = db.getWordList("A1");
        db.getWordListAllLearned("A1");
    }
}
