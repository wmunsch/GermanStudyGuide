package com.williammunsch.germanstudyguide.repositories;


import android.os.AsyncTask;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.williammunsch.germanstudyguide.SaveDataResponse;
import com.williammunsch.germanstudyguide.User;
import com.williammunsch.germanstudyguide.api.DatabaseService;
import com.williammunsch.germanstudyguide.datamodels.ScoreModelA1;
import com.williammunsch.germanstudyguide.datamodels.VocabListItem;
import com.williammunsch.germanstudyguide.datamodels.VocabModelA1;
import com.williammunsch.germanstudyguide.room.GermanDatabase;
import com.williammunsch.germanstudyguide.room.UserDao;
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
    private UserDao mUserDao;
    private List<VocabListItem> dataSet;
    public DatabaseService apiService;
    private LiveData<User> currentUser;
    private LiveData<String> userName;
    private LiveData<List<VocabListItem>> vocabListItemList;

    GermanDatabase db;




    private LiveData<Integer> a1Count;

    /**
     * Main page repository that handles updates and stores info for the vocab fragment (0/700 and name) and story fragment (title and author).
     */
    @Inject
     public Repository(DatabaseService apiService, GermanDatabase db) {
        this.apiService = apiService;
        this.db = db;

        mVocabDao = db.vocabDao();
        mVocabListDao = db.vocabListDao();
        mUserDao = db.userDao();

        getUserInfoFromRoom();

        a1Count = db.vocabDao().count();

        dataSet = new ArrayList<>();

        //map username to currentuser.username to show username in top left of main screen
        userName = Transformations.map(currentUser, name->{
            if (currentUser.getValue() != null)
               return currentUser.getValue().getUsername();
            return "Log In";
        } );

        checkA1();

        vocabListItemList = mVocabListDao.getAllVocabLists();
    }

    public void getUserInfoFromRoom(){
        currentUser = mUserDao.getUser();
    }



    public void logOut(){
        //deleteUser();
        deleteAllScores();
    }

    /**
     * Called when logging in.
     */
    public void downloadSaveData(){
        Call<SaveDataResponse> call = apiService.getSaveData();
        call.enqueue(new Callback<SaveDataResponse>() {
            @Override
            public void onResponse(Call<SaveDataResponse> call, Response<SaveDataResponse> response) {
                System.out.println("Response to call: ");
                int statusCode = response.code();
                SaveDataResponse data = response.body();

                if (data!=null){
                    String dataSet = data.toString();
                    String temp = "";
                    for (int i = 0; i < 700; i ++){
                        //insert each entry into ScoreModelA1 table
                        temp=dataSet.substring(i,i+6);
                        mVocabDao.insertIntoScoreModelA1(new ScoreModelA1(i,Integer.parseInt(temp.substring(0,1)),Integer.parseInt(temp.substring(2,4)),Integer.parseInt((temp.substring(5)))));
                    }
                }


            }
            @Override
            public void onFailure(Call<SaveDataResponse> call, Throwable t) {
                System.out.println("Error on call");
            }
        });
    }

    public LiveData<String> getUserName() {
        return userName;
    }

    public LiveData<Integer> getA1Count() {
        return a1Count;
    }

    public LiveData<List<VocabListItem>> getVocabListItems(){
        return vocabListItemList;
        //MutableLiveData<List<VocabListItem>> data = new MutableLiveData<>();
        //data.setValue(dataSet);
        //return data;
    }


    public LiveData<Integer> getA1Max(){
        return mVocabDao.count();
    }


    public void insert (VocabModelA1 vocabModelA1) {
        new insertAsyncTask(mVocabDao).execute(vocabModelA1);
    }
    public void insertUser(User user){
        new insertUserAsyncTask(mUserDao).execute(user);
    }

    public void update (VocabModelA1 vocabModelA1) {
        new updateAsyncTask(mVocabDao).execute(vocabModelA1);
    }

    public void deleteAll(){
        new deleteAsyncTask(mVocabDao).execute();
    }

    public void deleteAllScores(){new deleteA1AsyncTask(mVocabDao).execute();}

    public LiveData<Integer> count() {
        return mVocabDao.count();
    }

    public void insertList (VocabListItem vocabListItem){
        new insertListAsyncTask(mVocabListDao).execute(vocabListItem);
    }


    public void checkA1(){
        new checkA1AsyncTask(mVocabDao,apiService,mVocabListDao).execute();
    }
    /**
     * Checks to see if the A1 table has been downloaded to the local database, if not, downloads it.
     * If it does exist, or after downloading, adds A1 to VocabListItems.
     */
    private static class checkA1AsyncTask extends AsyncTask<Void, Void, Integer> {
        private VocabDao mAsyncTaskDao;
        private VocabListDao mVocabListDao;
        private DatabaseService apiService;


        checkA1AsyncTask(VocabDao dao, DatabaseService api, VocabListDao vDao) {
            mAsyncTaskDao = dao;
            apiService = api;
            mVocabListDao = vDao;
        }

        @Override
        protected Integer doInBackground(Void... params) {
            System.out.println("COUNT : " + mAsyncTaskDao.countA1());
            return mAsyncTaskDao.countA1();
        }

        @Override
        protected void onPostExecute(Integer vocabCount){
            if (vocabCount!=14){
                System.out.println("Downloading A1 table");

                Call<List<VocabModelA1>> call = apiService.vocabList();
                call.enqueue(new Callback<List<VocabModelA1>>() {
                    @Override
                    public void onResponse(Call<List<VocabModelA1>> call, Response<List<VocabModelA1>> response) {
                        List<VocabModelA1> vocabList = response.body();
                        VocabModelA1[] vocabListArray = vocabList.toArray(new VocabModelA1[vocabList.size()]);
                        new insertAsyncTask(mAsyncTaskDao).execute(vocabListArray);
                    }
                    @Override
                    public void onFailure(Call<List<VocabModelA1>> call, Throwable t) {
                        System.out.println("Error on call");
                    }
                });

                //Add the vocab list item
                new insertVocabListAsyncTask(mVocabListDao).execute(new VocabListItem("Beginner Level 1","A1",0,0,0));

            }else{
                System.out.println("Already downloaded");
            }


        }
    }


    private static class insertAsyncTask extends AsyncTask<VocabModelA1, Void, Void> {
        private VocabDao mAsyncTaskDao;

        insertAsyncTask(VocabDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final VocabModelA1... params) {
            for (VocabModelA1 model : params){
                mAsyncTaskDao.insert(model);
            }
            return null;
        }
    }


    private static class insertVocabListAsyncTask extends AsyncTask<VocabListItem, Void, Void> {
        private VocabListDao mAsyncTaskDao;

        insertVocabListAsyncTask(VocabListDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final VocabListItem... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
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








    private static class insertUserAsyncTask extends AsyncTask<User, Void, Void> {
        private UserDao mAsyncTaskDao;

        insertUserAsyncTask(UserDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final User... params) {
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

    private static class updateAsyncTask extends AsyncTask<VocabModelA1, Void, Void> {
        private VocabDao mAsyncTaskDao;

        updateAsyncTask(VocabDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final VocabModelA1... params) {
          //  mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class deleteA1AsyncTask extends AsyncTask<Void, Void, Void> {
        private VocabDao mAsyncTaskDao;

        deleteA1AsyncTask(VocabDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mAsyncTaskDao.deleteAllScores();
            return null;
        }

    }


}
