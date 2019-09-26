package com.williammunsch.germanstudyguide.repositories;


import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.williammunsch.germanstudyguide.api.DatabaseService;
import com.williammunsch.germanstudyguide.datamodels.VocabListItem;
import com.williammunsch.germanstudyguide.datamodels.VocabModel;
import com.williammunsch.germanstudyguide.room.GermanDatabase;
import com.williammunsch.germanstudyguide.room.VocabDao;
import com.williammunsch.germanstudyguide.room.VocabListDao;

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
 *  Base repository that handles api calls and ROOM updates.
 *
 */

@Singleton
public class Repository {

    private VocabDao mVocabDao;
    private VocabListDao mVocabListDao;
    private LiveData<List<VocabModel>> mAllVocab;
   // private List<VocabListItem> dataSet;
    //private LiveData<Integer> a1Max;
    //private LiveData<List<VocabListItem>> dataSet;
    private List<VocabListItem> dataSet;
    private MediatorLiveData<List<VocabListItem>> mObservableListItems;

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
        mVocabListDao = db.vocabListDao();
        mAllVocab = mVocabDao.getAllVocabs();

        setVocabListItems();


/**
 * Calls the MySQL database A1 table and inserts all the rows into the ROOM database locally
 */
        Call<List<VocabModel>> call = apiService.vocabList();
        call.enqueue(new Callback<List<VocabModel>>() {
            @Override
            public void onResponse(Call<List<VocabModel>> call, Response<List<VocabModel>> response) {
                System.out.println("Response to call: ");
                int statusCode = response.code();
                List<VocabModel> vocabList = response.body();

                //Delete all current entries in the A1 table so it doesn't give non unique errors while testing
                deleteAll();

                for (int i = 0; i < vocabList.size();i++){
                    System.out.println(vocabList.get(i).toString() + " " + vocabList.get(i).toSentence() + " " + vocabList.get(i).getStudying());
                   insert(vocabList.get(i));
                }

            }

            @Override
            public void onFailure(Call<List<VocabModel>> call, Throwable t) {
                System.out.println("Error on call");
            }
        });

/*
        mObservableListItems = new MediatorLiveData<>();

        mObservableListItems.addSource(mVocabListDao.getAllVocabLists(), productEntities -> {
                    if (db.getDatabaseCreated().getValue() != null) {
                        mObservableListItems.postValue(productEntities);
                    }
                });

*/
/*
        if (mVocabListDao.getAllVocabLists().getValue() == null){
            System.out.println(" VOCABLIST TABLE IS EMPTY");
            //insertList(new VocabListItem("Beginner Level 1","A1",0,0,0));
        }else{
            System.out.println(" VOCABLIST TABLE IS NOT EMPTY");
            for (int i = 0 ; i < mVocabListDao.getAllVocabLists().getValue().size(); i ++ ){
                System.out.println( mVocabListDao.getAllVocabLists().getValue().get(i));
            }
        }
*/



    }

    private void setVocabListItems(){
        //retrieve data from database here
        //MAKE SURE TO USE ASYNC TASK FOR DATABASE QUERIES IN FUTURE
       // System.out.println("Checking database for listitems");
       // System.out.println("total words: " + mVocabDao.count().getValue());
        //System.out.println("anything?: " + mVocabDao.getAllVocabs().getValue());

        /*
        List<VocabListItem> vl = mVocabListDao.getAllVocabLists().getValue();
        if (vl == null || vl.isEmpty()){
            insertList(new VocabListItem("Beginner Level 1","A1",mVocabDao.countLearned().getValue(),mVocabDao.count().getValue(),mVocabDao.countMastered().getValue()));
        }
        /*
        try{
            for (int i = 0 ; i < vl.size();i++){
                if (vl.get(i) !=)
            }
        }catch(NullPointerException e){
            System.out.println("null vocablistitem");
        }
       */
        dataSet = new ArrayList<>();
        dataSet.add(new VocabListItem("Beginner Level 1","A1",0,0,0));
        dataSet.add(new VocabListItem("Beginner Level 2","A2",0,0,0));
        dataSet.add(new VocabListItem("Intermediate Level 1","B1",0,0,0));
       // dataSet.add(new VocabListItem("Intermediate Level 2","B2",0,0,0));
       // dataSet.add(new VocabListItem("Advanced Level 1","C1",0,0,0));
       // dataSet.add(new VocabListItem("Advanced Level 2","C2",0,0,0));
    }

/*
    public MutableLiveData<List<VocabListItem>> getVocabListItems(){
        setVocabListItems();
        MutableLiveData<List<VocabListItem>> data = new MutableLiveData<>();
        data.setValue(dataSet);
        return data;
    }
*/
    public LiveData<List<VocabListItem>> getVocabListItems(){
        MutableLiveData<List<VocabListItem>> data = new MutableLiveData<>();
        data.setValue(dataSet);
        return data;
    }


    public LiveData<Integer> getA1Max(){
        return mVocabDao.count();
    }

   // public LiveData<Integer> getA1Max() {return a1Max;}
    public LiveData<List<VocabModel>> getAll() { return mAllVocab; }

    LiveData<VocabModel> getOne(){return mVocabDao.getOneVocab();}

    public void insert (VocabModel vocabModel) {
        new insertAsyncTask(mVocabDao).execute(vocabModel);
    }

    public void update (VocabModel vocabModel) {
        new updateAsyncTask(mVocabDao).execute(vocabModel);
    }

    public void deleteAll(){
        new deleteAsyncTask(mVocabDao).execute();
    }

    public LiveData<Integer> count() {
        return mVocabDao.count();
    }

    public void insertList (VocabListItem vocabListItem){
        new insertListAsyncTask(mVocabListDao).execute(vocabListItem);
    }


    private static class insertListAsyncTask extends AsyncTask<VocabListItem, Void, Void> {

        private VocabListDao mAsyncTaskDao;

        insertListAsyncTask(VocabListDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final VocabListItem... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
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

    private static class updateAsyncTask extends AsyncTask<VocabModel, Void, Void> {
        private VocabDao mAsyncTaskDao;

        updateAsyncTask(VocabDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final VocabModel... params) {
          //  mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }


}
