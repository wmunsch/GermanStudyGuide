package com.williammunsch.germanstudyguide.repositories;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
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
import java.util.Arrays;
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
    private LiveData<String> userEmail;
    private LiveData<List<VocabListItem>> vocabListItemList;

    private MutableLiveData<Integer> profileVisibility = new MutableLiveData<>();
    private MutableLiveData<Integer> loginVisibility = new MutableLiveData<>();
    private MutableLiveData<Integer> registrationVisibility = new MutableLiveData<>();
    private MutableLiveData<Integer> passwordErrorVisibility = new MutableLiveData<>();

    private MutableLiveData<Integer> emailTakenVisibility = new MutableLiveData<>();


    private MutableLiveData<Integer> emailValidVisibility = new MutableLiveData<>();

    private MutableLiveData<Integer> couldNotConnectVisibility = new MutableLiveData<>();

    private MutableLiveData<Boolean> allGood = new MutableLiveData<>();
    private LiveData<String> accountCreation;

    private MutableLiveData<Integer> errorConnectingToDatabaseVisibility = new MutableLiveData<>();
    private MutableLiveData<Integer> showLoadingBar = new MutableLiveData<>();
    private MutableLiveData<Integer> showViewPager = new MutableLiveData<>();


    GermanDatabase db;

    private LiveData<Integer> a1Percent;


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

        registrationVisibility.setValue(View.GONE);
        passwordErrorVisibility.setValue(View.GONE);
        emailTakenVisibility.setValue(View.GONE);
        emailValidVisibility.setValue(View.GONE);
        errorConnectingToDatabaseVisibility.setValue(View.GONE);
        showLoadingBar.setValue(View.GONE);
        showViewPager.setValue(View.VISIBLE);
        couldNotConnectVisibility.setValue(View.GONE);

        //map username to currentuser.username to show username in top left of main screen
        userName = Transformations.map(currentUser, name->{
            if (currentUser.getValue() != null){
                loginVisibility.setValue(View.GONE);
                profileVisibility.setValue(View.VISIBLE);
                System.out.println("^^^^^^^^^^^^^^^^^^^^^^^currentuser is not null");
                return currentUser.getValue().getUsername();
            }
            loginVisibility.setValue(View.VISIBLE);
            profileVisibility.setValue(View.GONE);
            System.out.println("^^^^^^^^^^^^^^^^^^^^^^^currentuser IS null");
            return "Log In";
        } );


        /*
        userEmail = Transformations.map(currentUser, name->{
            if (currentUser.getValue() != null){
                return currentUser.getValue().getEmail();
            }
            return "";
        } );
*/

        accountCreation = Transformations.map(allGood, success->{
           if (allGood.getValue()!=null && allGood.getValue()){
               //call api here for registration
               //change allGood back to false?
               System.out.println("Registration successful.");
               return "Registration successful.";
           }
            System.out.println("Error: Registration failed");
           return "Error: Registration failed.";
        });

        checkA1();

        vocabListItemList = mVocabListDao.getAllVocabLists();


    }

    public LiveData<User> getCurrentUser() {
        return currentUser;
    }

    public boolean checkEmail(String email){
        //check the database to see if email exists
        return false;
    }

    public boolean checkPassword(String password){
        //check password to see if

        return false;
    }

    public VocabDao getmVocabDao() {
        return mVocabDao;
    }

    public void getUserInfoFromRoom(){
        currentUser = mUserDao.getUser();
    }



    public void setPasswordErrorVisibility(int i){
        passwordErrorVisibility.setValue(i);
    }

    public void setRegistrationVisibility(){
        loginVisibility.setValue(View.GONE);
        profileVisibility.setValue(View.GONE);
        registrationVisibility.setValue(View.VISIBLE);
        passwordErrorVisibility.setValue(View.INVISIBLE);
    }
    public void setRegistrationVisibilityF(){
        loginVisibility.setValue(View.VISIBLE);
        profileVisibility.setValue(View.GONE);
        registrationVisibility.setValue(View.GONE);
        passwordErrorVisibility.setValue(View.GONE);
    }

    public void setLoginAndRegistrationVisibilityGone(){
        loginVisibility.setValue(View.GONE);
        profileVisibility.setValue(View.GONE);
        registrationVisibility.setValue(View.GONE);
        passwordErrorVisibility.setValue(View.GONE);
    }
    public MutableLiveData<Integer> getEmailTakenVisibility() {
        return emailTakenVisibility;
    }

    public void setEmailTakenVisibility(int i) {
        this.emailTakenVisibility.setValue(i);
    }

    public LiveData<Integer> getCouldNotConnectVisibility(){return couldNotConnectVisibility;}
    public void setCouldNotConnectVisibility(int i){couldNotConnectVisibility.setValue(i);}
    public LiveData<Integer> getEmailValidVisibility() {
        return emailValidVisibility;
    }

    public void setEmailValidVisibility(int i) {
        this.emailValidVisibility.setValue(i);
    }
    public void logOut(){
        //deleteUser();
        //deleteAllScores();
        mVocabDao.resetAllScores();
    }


    /**
     * Called when logging in. Gets the user's save data and inputs it into the ROOM database.
     */
    public void downloadSaveData(String userN){
        System.out.println("Calling downloadsavedata");
        Call<SaveDataResponse> call = apiService.getSaveData(userN);
        call.enqueue(new Callback<SaveDataResponse>() {
            @Override
            public void onResponse(Call<SaveDataResponse> call, Response<SaveDataResponse> response) {
                System.out.println("Response to call: ");
                System.out.println(response);
                SaveDataResponse data = response.body();
                System.out.println("Data body: ");
                System.out.println(data + " is null?");

                if (data!=null){
                    if (data.getTablename()==null || data.getScore()==null || data.getStudying()==null || data.getFreq()==null){
                       // createNewEntryAndUpload();
                    }else{
                        System.out.println("Data not null, inputting data");
                        //Format the score data into an array
                        String[] scores = data.getScore().split(",");
                        String[] studying = data.getStudying().split(",");
                        String[] freq = data.getFreq().split(",");
                        updateVocabDataOnLogin(scores, studying, freq);
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
    public LiveData<String> getUserEmail() {
        return userEmail;
    }

    public LiveData<Integer> getA1Count() {
        return a1Count;
    }
    public LiveData<Integer> getPasswordErrorVisibility() {
        return passwordErrorVisibility;
    }
    public LiveData<Integer> getProfileVisibility() {
        return profileVisibility;
    }
    public LiveData<Integer> getRegistrationVisibility() {
        return registrationVisibility;
    }
    public LiveData<Integer> getLoginVisibility() {
        return loginVisibility;
    }
    public LiveData<List<VocabListItem>> getVocabListItems(){
        return vocabListItemList;
        //MutableLiveData<List<VocabListItem>> data = new MutableLiveData<>();
        //data.setValue(dataSet);
        //return data;
    }

    public LiveData<Integer> getShowLoadingBar(){return showLoadingBar;}
    public LiveData<Integer> getShowViewPager(){return showViewPager;}

    public LiveData<Integer> getA1Max(){
        return mVocabDao.count();
    }

    public LiveData<Integer> getA1Learned() {return mVocabDao.countLearned();}
    public LiveData<Integer> getA1Mastered() {return mVocabDao.countMastered();}
    public LiveData<Integer> getA1Percent() {return mVocabDao.countLearned();}

   // public LiveData<Integer> getA1Score(){return mVocabDao.getA1Scores();}


    //public void insert (VocabModelA1 vocabModelA1) {
    //    new insertAsyncTask(mVocabDao, mVocabListDao).execute(vocabModelA1);
    //}

    public void insertUser(User user){
        new insertUserAsyncTask(mUserDao).execute(user);
    }
    public void deleteAllUsers(){new deleteUserAsyncTask(mUserDao).execute();}
    public void checkUser(){
      // new checkUserAsyncTask(mUserDao).execute();
    }

    public void update (VocabModelA1 vocabModelA1) {
        new updateAsyncTask(mVocabDao).execute(vocabModelA1);
    }

    public void deleteAll(){
        new deleteAsyncTask(mVocabDao).execute();
    }

    public void deleteAllScores(){new deleteA1AsyncTask(mVocabDao).execute();}

    public void resetAllScores(){new resetA1AsyncTask(mVocabDao).execute();}

    public LiveData<Integer> count() {
        return mVocabDao.count();
    }

    public void insertList (VocabListItem vocabListItem){
        new insertListAsyncTask(mVocabListDao).execute(vocabListItem);
    }


    public void checkA1(){
        new checkA1AsyncTask(mVocabDao,apiService,mVocabListDao,showLoadingBar,couldNotConnectVisibility).execute();
    }
    /**
     * Checks to see if the A1 table has been downloaded to the local database, if not, downloads it.
     * If it does exist, or after downloading, adds A1 to VocabListItems.
     */
    private static class checkA1AsyncTask extends AsyncTask<Void, Void, Integer> {
        private VocabDao mAsyncTaskDao;
        private VocabListDao mVocabListDao;
        private DatabaseService apiService;
        private final MutableLiveData<Integer> muld, muld2;

        checkA1AsyncTask(VocabDao dao, DatabaseService api, VocabListDao vDao, MutableLiveData<Integer> mld, MutableLiveData<Integer> mld2) {
            mAsyncTaskDao = dao;
            apiService = api;
            mVocabListDao = vDao;
            muld=mld;
            muld2 = mld2;
        }

        @Override
        protected void onPreExecute() {
            muld2.setValue(View.GONE);
        }

        @Override
        protected Integer doInBackground(Void... params) {
            System.out.println("COUNT : " + mAsyncTaskDao.countA1());
            return mAsyncTaskDao.countA1();
        }

        @Override
        protected void onPostExecute(Integer vocabCount){
            if (vocabCount!=700){
                System.out.println("Downloading A1 table");
                Call<List<VocabModelA1>> call = apiService.vocabList();
                call.enqueue(new Callback<List<VocabModelA1>>() {
                    @Override
                    public void onResponse(Call<List<VocabModelA1>> call, Response<List<VocabModelA1>> response) {
                        List<VocabModelA1> vocabList = response.body();
                        VocabModelA1[] vocabListArray = vocabList.toArray(new VocabModelA1[vocabList.size()]);
                        //insert all of the vocab words into the room database and once finished add the Beginner Level 1 vocab list
                        new insertAsyncTask(mAsyncTaskDao,mVocabListDao,muld).execute(vocabListArray);
                    }
                    @Override
                    public void onFailure(Call<List<VocabModelA1>> call, Throwable t) {
                        System.out.println("Error on call" + t);
                        muld2.setValue(View.VISIBLE);
                        //TODO : create a variable live data for visibility and a button and textview for "could not connect to server, tap to try again"

                    }
                });
            }else{
                System.out.println("Already downloaded");
            }


        }
    }


    private static class insertAsyncTask extends AsyncTask<VocabModelA1, Void, Void> {
        private VocabDao mAsyncTaskDao;
        private VocabListDao mVocabListDao;
        private final MutableLiveData<Integer> muld;

        insertAsyncTask(VocabDao dao, VocabListDao vlDao, MutableLiveData<Integer> mld) {
            mAsyncTaskDao = dao;
            mVocabListDao = vlDao;
            muld=mld;
        }

        @Override
        protected void onPreExecute() {
            muld.setValue(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(final VocabModelA1... params) {
            for (VocabModelA1 model : params){
                mAsyncTaskDao.insert(model);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void v){
            //Add the vocab list item
            muld.setValue(View.GONE);
            new insertVocabListAsyncTask(mVocabListDao).execute(new VocabListItem("Beginner Level 1","  A1",0,0,0));
           // new insertVocabListAsyncTask(mVocabListDao).execute(new VocabListItem("Beginner Level 2","  A2",0,0,0));
          //  new insertVocabListAsyncTask(mVocabListDao).execute(new VocabListItem("Intermediate Level 1","  B1",0,0,0));
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


    /**
     * Updates the vocabmodelA1 activity data when logging in.
     */
    private void updateVocabDataOnLogin(String[] data, String[] dataStudying, String[] dataFreq){
        //showLoadingBar.setValue(1);
        new updateVocabDataOnLoginAsyncTask(mVocabDao,showLoadingBar,showViewPager).execute(data, dataStudying, dataFreq);
    }

    private static class updateVocabDataOnLoginAsyncTask extends AsyncTask<String[], Void, Void>{
        private final VocabDao mAsyncTaskDao;
        private final MutableLiveData<Integer> muld;
        private final MutableLiveData<Integer> muld2;

        updateVocabDataOnLoginAsyncTask(VocabDao dao, MutableLiveData<Integer> mld, MutableLiveData<Integer> mld2){
            mAsyncTaskDao = dao;
            muld = mld;
            muld2 = mld2;
        }

        @Override
        protected void onPreExecute() {
            muld.setValue(View.VISIBLE);
            muld2.setValue(View.INVISIBLE);
        }

        @Override
        protected Void doInBackground(String[]... strings) {
            /*
            int[] numArray = new int[700];
            for (int i =0;i<700;i++){
                numArray[i] = i+1;
            }
            System.out.println("BEGGINING UPDATING SCORES");
            mAsyncTaskDao.updateVocabScore(strings,numArray);
            System.out.println("END UPDATING SCORES");
*/

            //mAsyncTaskDao.resetAllScores();

            String[] scoreArray = strings[0];
            String[] studyingArray = strings[1];
            String[] freqArray = strings[2];
            System.out.println("BEGGINING UPDATING SCORES");
            for (int i = 0;i < 700;i++){
                try {
                    mAsyncTaskDao.updateVocabScore(Integer.parseInt(scoreArray[i]), Integer.parseInt(studyingArray[i]), Integer.parseInt(freqArray[i]), i + 1);//mAsyncTaskDao.updateVocabScore(Integer.parseInt(strings[i]),i);
                }catch(Exception e){
                    break;
                }
                }
            System.out.println("END UPDATING SCORES");

            /*
            System.out.println("Getting full empty list");
            VocabModelA1[] vocabList = mAsyncTaskDao.getFullA1List();
            System.out.println("finished full empty list get");
            //String[] scores = strings;
            for (int i = 0;i < 700;i++){
                //vocabList[i-1].setId(i);
                System.out.println("Updating node");
                vocabList[i].setScore(Integer.parseInt(strings[i]));
                mAsyncTaskDao.updateVocabScoresOnLogin(vocabList[i]);
            }
            System.out.println("$$$$$$ Finshed updating vocablist from remote");
            return null;
             */
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            muld.setValue(View.GONE);
            muld2.setValue(View.VISIBLE);
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

    private static class deleteUserAsyncTask extends AsyncTask<Void, Void, Void> {
        private UserDao mAsyncTaskDao;

        deleteUserAsyncTask(UserDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mAsyncTaskDao.deleteAll();
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

    private static class resetA1AsyncTask extends AsyncTask<Void, Void, Void>{
        private VocabDao mAsyncTaskDao;

        resetA1AsyncTask(VocabDao dao) {mAsyncTaskDao = dao;}
        @Override
        protected Void doInBackground(Void... voids) {
            mAsyncTaskDao.resetAllScores();
            return null;
        }
    }


}
