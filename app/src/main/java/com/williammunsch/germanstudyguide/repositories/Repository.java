package com.williammunsch.germanstudyguide.repositories;


import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.williammunsch.germanstudyguide.DBManager;
import com.williammunsch.germanstudyguide.api.DatabaseService;
import com.williammunsch.germanstudyguide.datamodels.VocabListItem;
import com.williammunsch.germanstudyguide.datamodels.VocabModel;
import com.williammunsch.germanstudyguide.room.GermanDatabase;
import com.williammunsch.germanstudyguide.room.VocabDao;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 *  A Repository class handles data operations. It provides a clean API to the rest of the app for app data.
 *  Manages query threads and allows you to use multiple backends. In the most common example,
 *  the Repository implements the logic for deciding whether to fetch data from a network or use results cached in a local database.
 *
 */

@Singleton
public class Repository {

    private VocabDao mVocabDao;
    private LiveData<List<VocabModel>> mAllVocab;
    private List<VocabListItem> dataSet;

    DatabaseService apiService;

    GermanDatabase db;

    /**
     * Main page repository that handles updates and stores info for the vocab fragment (0/700 and name) and story fragment (title and author).
     */
    @Inject
     public Repository(DatabaseService apiService, GermanDatabase db) {
        this.apiService = apiService;
        this.db = db;

        mVocabDao = db.vocabDao();
        mAllVocab = mVocabDao.getAllVocabs();

        dataSet = new ArrayList<>();
       // dataSet = mVocabDao.getVocabListItems();

        /**
         * Need to make a new table in the database to hold the various list displays for the first fragment
         * instead of creating them below
         */

/*
        Call<List<VocabModel>> call = apiService.vocabList();
        call.enqueue(new Callback<List<VocabModel>>() {
            @Override
            public void onResponse(Call<List<VocabModel>> call, Response<List<VocabModel>> response) {
                System.out.println("Response to call: ");
                int statusCode = response.code();
                List<VocabModel> vocabList = response.body();
                for (int i = 0; i < vocabList.size();i++){
                    System.out.println(vocabList.get(i).toString());
                    insert(vocabList.get(i));
                }

            }

            @Override
            public void onFailure(Call<List<VocabModel>> call, Throwable t) {
                System.out.println("Error on call");
            }
        });
        */

    }

    private void setVocabListItems(Context context){
        //retrieve data from database here
        //MAKE SURE TO USE ASYNC TASK FOR DATABASE QUERIES IN FUTURE

        dataSet.add(new VocabListItem("Beginner Level 1","A1",0,0,0));
        dataSet.add(new VocabListItem("Beginner Level 2","A2",0,0,0));
        dataSet.add(new VocabListItem("Intermediate Level 1","B1",0,0,0));
        dataSet.add(new VocabListItem("Intermediate Level 2","B2",0,0,0));
        dataSet.add(new VocabListItem("Advanced Level 1","C1",0,0,0));
        dataSet.add(new VocabListItem("Advanced Level 2","C2",0,0,0));
    }

    public MutableLiveData<List<VocabListItem>> getVocabListItems(Context context){
        setVocabListItems(context);
        MutableLiveData<List<VocabListItem>> data = new MutableLiveData<>();
        data.setValue(dataSet);
        return data;
    }


    public LiveData<List<VocabModel>> getAll() { return mAllVocab; }

    LiveData<VocabModel> getOne(){return mVocabDao.getOneVocab();}

    public void insert (VocabModel vocabModel) {
        new insertAsyncTask(mVocabDao).execute(vocabModel);
    }

    public void deleteAll(){
        new deleteAsyncTask(mVocabDao).execute();
    }

    public Integer count() {
        return mVocabDao.count().getValue();
    }




    private static class insertAsyncTask extends AsyncTask<VocabModel, Void, Void> {

        private VocabDao mAsyncTaskDao;

        insertAsyncTask(VocabDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final VocabModel... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class deleteAsyncTask extends AsyncTask<Void, Void, Void> {

        private VocabDao mAsyncTaskDao;

        deleteAsyncTask(VocabDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mAsyncTaskDao.deleteAll();
            return null;
        }

    }

}
