package com.williammunsch.germanstudyguide.repositories;

import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import com.williammunsch.germanstudyguide.DBManager;
import com.williammunsch.germanstudyguide.datamodels.SimpleWord;

import java.util.ArrayList;
import java.util.List;

public class SimpleWordRepository {

    private static SimpleWordRepository instance;
    private ArrayList<SimpleWord> dataSet = new ArrayList<>();

    public static SimpleWordRepository getInstance(){
        if (instance ==null){
            instance = new SimpleWordRepository();
        }
        return instance;
    }

    //Get data from a web service or online source
    public MutableLiveData<List<SimpleWord>> getSimpleWordList(Context context){
        setSimpleWordsItems(context);
        MutableLiveData<List<SimpleWord>> data = new MutableLiveData<>();
        data.setValue(dataSet);
        return data;
    }

    private void setSimpleWordsItems(Context context){
        //retrieve data from database here
        //MAKE SURE TO USE ASYNC TASK FOR DATABASE QUERIES IN FUTURE

        DBManager db = DBManager.getInstance(context);
        dataSet = db.getSimpleWordList("A1");


    }
}
