package com.williammunsch.germanstudyguide.repositories;


import android.os.AsyncTask;
import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.williammunsch.germanstudyguide.DatabasePID;
import com.williammunsch.germanstudyguide.responses.CreateUploadDataResponse;
import com.williammunsch.germanstudyguide.responses.SaveDataResponse;
import com.williammunsch.germanstudyguide.datamodels.User;
import com.williammunsch.germanstudyguide.api.DatabaseService;
import com.williammunsch.germanstudyguide.datamodels.Hag_Sentences;
import com.williammunsch.germanstudyguide.datamodels.Hag_Words;
import com.williammunsch.germanstudyguide.datamodels.LocalSaveA1;
import com.williammunsch.germanstudyguide.datamodels.StoriesListItem;
import com.williammunsch.germanstudyguide.datamodels.VocabListItem;
import com.williammunsch.germanstudyguide.datamodels.VocabModelA1;
import com.williammunsch.germanstudyguide.room.GermanDatabase;
import com.williammunsch.germanstudyguide.room.StoryDao;
import com.williammunsch.germanstudyguide.room.UserDao;
import com.williammunsch.germanstudyguide.room.VocabDao;
import com.williammunsch.germanstudyguide.room.VocabListDao;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Base repository that handles api calls, ROOM updates, and asynctask calls
 * Stores variables used between multiple viewmodels
 */

@Singleton
public class Repository {
    public DatabaseService apiService;
    GermanDatabase db;

    private final LiveData<String> A1DownloadedText;
    private final MutableLiveData<Integer> showLoadingBar = new MutableLiveData<>(View.GONE);
    private final MutableLiveData<Integer> showViewPager = new MutableLiveData<>(View.VISIBLE);
    private final MutableLiveData<Integer> A1Downloaded = new MutableLiveData<>(0);
    private final LiveData<Integer> a1Count;
    private final MutableLiveData<Integer> wordsLearnedVisibility = new MutableLiveData<>();
    private final MutableLiveData<Integer> wordsDownloadedVisibility = new MutableLiveData<>();
    private final MutableLiveData<Integer> errorDownloadingVisibility = new MutableLiveData<>();
    private final MutableLiveData<Integer> genderButtonVisibility = new MutableLiveData<>(View.VISIBLE);
    private final MutableLiveData<Integer> genderTextVisibility = new MutableLiveData<>(View.GONE);

    private final LiveData<User> currentUser;
    private final LiveData<String> userName;


    private final MutableLiveData<Integer> profileVisibility = new MutableLiveData<>();
    private final MutableLiveData<Integer> loginVisibility = new MutableLiveData<>();
    private final MutableLiveData<Integer> needToDownloadSaveData = new MutableLiveData<>(0);

    private int pageOn = 0;//Keep this in the repository so the page number on is saved between activities

    /**
     * Main page repository that handles updates and stores info for the vocab fragment (0/700 and name) and story fragment (title and author).
     */
    @Inject
     public Repository(DatabaseService apiService, GermanDatabase db) {
        this.apiService = apiService;
        this.db = db;
        a1Count = db.vocabDao().count();
        currentUser = getUserInfoFromRoom();


        //map username to currentuser.username to show username in top left of main screen and for saving vocab
        userName = Transformations.map(currentUser, name->{
            if (currentUser.getValue() != null){
                loginVisibility.setValue(View.GONE);
                profileVisibility.setValue(View.VISIBLE);
                return currentUser.getValue().getUsername();
            }
            loginVisibility.setValue(View.VISIBLE);
            profileVisibility.setValue(View.GONE);
            return "Log In";
        } );



        wordsLearnedVisibility.setValue(View.GONE);
        wordsDownloadedVisibility.setValue(View.GONE);
        errorDownloadingVisibility.setValue(View.GONE);

        //Set the A1 button to say either download or study based on whether or not the A1 data
        //has been downloaded from the remote database. Keep in repository because it's used for multiple views

        A1DownloadedText = Transformations.map(A1Downloaded, value->{
           if (A1Downloaded.getValue()!= null && A1Downloaded.getValue() == 0){
               wordsLearnedVisibility.setValue(View.GONE);
               genderButtonVisibility.setValue(View.GONE);
               genderTextVisibility.setValue(View.VISIBLE);
               return "Download";
           }
           else if (A1Downloaded.getValue()!= null){
               wordsLearnedVisibility.setValue(View.VISIBLE);
               genderButtonVisibility.setValue(View.VISIBLE);
               genderTextVisibility.setValue(View.GONE);

               //Call to download the save data after downloading A1 IF the user is logged in
               if (A1Downloaded.getValue() == 2 && currentUser.getValue()!=null && currentUser.getValue().getUsername()!= null){
                   downloadSaveData(currentUser.getValue().getUsername());
               }

               return "Study";
           }
           else{
               wordsLearnedVisibility.setValue(View.GONE);
               genderButtonVisibility.setValue(View.GONE);
               genderTextVisibility.setValue(View.VISIBLE);
               return "Error Loading";
           }
        });


    }

    /**
     * Called every time the app opens
     * If the local database has more progress than remote db, save to remote
     */
    //TODO :
    /*
    public void checkLocalSaveToRemote(){
        //Check if local database has more progress than remote db by getting studying on #
        new checkLocalToRemoteAsyncTask(db.vocabDao()).execute();
    }

    private void updateRemoteDatabaseFromLocal(){
        DatabasePID databasePID = new DatabasePID();
        String scoreList = db.vocabDao().getA1Scores().toString();
        String studyingList = db.vocabDao().getA1Studying().toString();
        String freqList = db.vocabDao().getA1Freq().toString();
        String tempS = scoreList.replaceAll("\\s","").replaceAll("\\[","").replaceAll("\\]","");
        String tempS2 = studyingList.replaceAll("\\s","").replaceAll("\\[","").replaceAll("\\]","");
        String tempS3 = freqList.replaceAll("\\s","").replaceAll("\\[","").replaceAll("\\]","");

        apiService.uploadData(userName.getValue(),"A1",tempS,tempS3,tempS2,databasePID.getPid());
    }

    private static class checkLocalToRemoteAsyncTask extends AsyncTask<Void, Void, Void> {
        private final VocabDao mAsyncTaskDao;

        checkLocalToRemoteAsyncTask(VocabDao dao){
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            int localStudyingCountA1 = mAsyncTaskDao.getCountStudyingA1();
            int remoteStudyingCountA1 = 0;
            if (localStudyingCountA1 > remoteStudyingCountA1){

            }

            return null;
        }


    }

     */



    /**
     * Called when logging in. Gets the user's save data and inputs it into the ROOM database.
     */
    public void downloadSaveData(String userN){
        Call<SaveDataResponse> call = apiService.getSaveData(userN);
        call.enqueue(new Callback<SaveDataResponse>() {
            @Override
            public void onResponse(Call<SaveDataResponse> call, Response<SaveDataResponse> response) {
                SaveDataResponse data = response.body();
                if (data!=null){
                    if (data.getTablename() != null && data.getScore() != null && data.getStudying() != null && data.getFreq() != null) {
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
              //  System.out.println("Error on call");
            }
        });
    }


    //********************************************************************
    //Check to see if all recyclerview objects have been created and inserted into local DB

    public void checkLessons(){
        new checkLessonAsyncTask(db.vocabListDao(), "Beginner Level 1", "  A1").execute();
        //For the future
        //new checkLessonAsyncTask(mVocabListDao, "Beginner Level 2", "  A2").execute();
        //new checkLessonAsyncTask(mVocabListDao, "Intermediate Level 1", "  B1").execute();
    }

    //Async task to check if all of the lesson list items for the recyclerview in
    // the Vocab fragment on the main page have been inserted into ROOM.
    //If not, it inserts them
    private static class checkLessonAsyncTask extends AsyncTask<Void, Void, Integer>{
        private final VocabListDao mVocabListDao;
        private final String mLessonName;
        private final String mImageName;

        checkLessonAsyncTask(VocabListDao vocabListDao, String lessonName, String imageName){
            mVocabListDao = vocabListDao;
            mLessonName = lessonName;
            mImageName = imageName;
        }

        @Override
        protected Integer doInBackground(Void... params) {
            return mVocabListDao.vocabLessonExists(mLessonName);
        }

        @Override
        protected void onPostExecute(Integer exists){
            if (exists != 1){
                new insertVocabListAsyncTask(mVocabListDao).execute(new VocabListItem(mLessonName,mImageName,0,0,0));
            }
        }
    }
    //********************************************************************

    //********************************************************************
    public void checkStories(){
        new checkStoryAsyncTask(db.storyDao(), "Hänsel und Gretel", "der Brüder Grimm").execute();
        //For the future
        //new checkLessonAsyncTask(mVocabListDao, "Story Title", "Author").execute();
    }

    //Async task to check if all of the story list items for the recyclerview in
    // the stories fragment on the main page have been inserted into ROOM.
    //If not, it inserts them. (This is not the actual story, just the list item.)
    private static class checkStoryAsyncTask extends AsyncTask<Void, Void, Integer>{
        private final StoryDao mStoryDao;
        private final String mStoryName;
        private final String mAuthorName;

        checkStoryAsyncTask(StoryDao storyDao, String storyName, String authorName){
            mStoryDao = storyDao;
            mStoryName = storyName;
            mAuthorName = authorName;
        }

        @Override
        protected Integer doInBackground(Void... params) {
            return mStoryDao.storyLessonExists(mStoryName);
        }

        @Override
        protected void onPostExecute(Integer exists){
            if (exists != 1){
                new insertStoryListAsyncTask(mStoryDao).execute(new StoriesListItem(mStoryName,mAuthorName));
            }
        }
    }
    //********************************************************************


    /**
     * Checks to see if the A1 table has been downloaded to the local database.
     * Determines whether the button reads "Download" or "Study" by setting mA1Downloaded to 1 or 0
     */
    public void checkA1(){
        new checkA1AsyncTask(db.vocabDao(), A1Downloaded).execute();
    }
    private static class checkA1AsyncTask extends AsyncTask<Void, Void, Integer> {
        private final VocabDao mAsyncTaskDao;
        private final MutableLiveData<Integer> mA1Downloaded;

        checkA1AsyncTask(VocabDao dao, MutableLiveData<Integer> a1Downloaded) {
            mAsyncTaskDao = dao;
            mA1Downloaded = a1Downloaded;
        }


        @Override
        protected Integer doInBackground(Void... params) {
            return mAsyncTaskDao.countA1();
        }

        @Override
        protected void onPostExecute(Integer vocabCount){
            if (vocabCount!=700){
                //Set the A1 button to be "Download"
                mA1Downloaded.setValue(0);

            }else{
                //Set the A1 button to be "Learn"
                mA1Downloaded.setValue(1);
            }
        }
    }

    /**
     * Inserts vocab into the local_tableA1 database and the user-dependent vocab_tableA1 table
     */
    private static class insertAsyncTask extends AsyncTask<VocabModelA1, Void, Void> {
        private final VocabDao mAsyncTaskDao;
        private final MutableLiveData<Integer> mDownloadButtonVisibility;
        private final MutableLiveData<Integer> mA1Downloaded;
        private final MutableLiveData<Integer> mWordsDownloadedVisibility;

        insertAsyncTask(VocabDao dao,  MutableLiveData<Integer> downloadButtonVisibility, MutableLiveData<Integer> a1Downloaded, MutableLiveData<Integer> wordsDownloadedVisibility){
            mAsyncTaskDao = dao;
            mDownloadButtonVisibility = downloadButtonVisibility;
            mA1Downloaded = a1Downloaded;
            mWordsDownloadedVisibility = wordsDownloadedVisibility;
        }

        @Override
        protected void onPreExecute() {
            mDownloadButtonVisibility.setValue(View.GONE);
            mWordsDownloadedVisibility.setValue(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(final VocabModelA1... params) {
            int incremental = 1;
            for (VocabModelA1 model : params){
                mAsyncTaskDao.insert(model);
                mAsyncTaskDao.insertLocal(new LocalSaveA1(incremental,0,0,0));
                incremental++;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void v){
            mDownloadButtonVisibility.setValue(View.VISIBLE);
            mWordsDownloadedVisibility.setValue(View.GONE);
            //Set to 2 so it calls downloadSaveData()
            mA1Downloaded.setValue(2);
        }

    }

    /**
     * Inserts a vocab lesson into the vocab_list_table to show in the recyclerview
     */
    private static class insertVocabListAsyncTask extends AsyncTask<VocabListItem, Void, Void> {
        private final VocabListDao mAsyncTaskDao;

        insertVocabListAsyncTask(VocabListDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final VocabListItem... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }


    /**
     * AsyncTask to download the A1 data from the remote database on a background thread
     * Calls insertAsyncTask when the data has been retrieved to input it into the local databases
     */
    public void downloadA1(MutableLiveData<Integer> downloadButtonVisibility){
        new downloadA1AndInsert(apiService,A1Downloaded, db.vocabDao(), downloadButtonVisibility,  wordsDownloadedVisibility, errorDownloadingVisibility).execute();
    }
    private static class downloadA1AndInsert extends AsyncTask<Void, Void, Void>{
        private final DatabaseService apiService;
        private final MutableLiveData<Integer> mA1Downloaded;
        private final VocabDao mVocabDao;
        private final MutableLiveData<Integer> mDownloadButtonVisibility;
        private final MutableLiveData<Integer> mWordsDownloadedVisibility;
        private final MutableLiveData<Integer> mErrorDownloadingVisibility;

        downloadA1AndInsert(DatabaseService api, MutableLiveData<Integer> a1Downloaded, VocabDao vocabDao,  MutableLiveData<Integer> downloadButtonVisibility, MutableLiveData<Integer> wordsDownloadedVisibility, MutableLiveData<Integer> errorDownloadingVisibility){
            apiService = api;
            mA1Downloaded = a1Downloaded;
            mVocabDao = vocabDao;
            mDownloadButtonVisibility = downloadButtonVisibility;
            mWordsDownloadedVisibility = wordsDownloadedVisibility;
            mErrorDownloadingVisibility = errorDownloadingVisibility;
        }

        @Override
        protected Void doInBackground(Void... params) {
            //Downloading A1 from remote
            Call<List<VocabModelA1>> call = apiService.vocabList();
            call.enqueue(new Callback<List<VocabModelA1>>() {
                @Override
                public void onResponse(Call<List<VocabModelA1>> call, Response<List<VocabModelA1>> response) {
                    List<VocabModelA1> vocabList = response.body();
                    VocabModelA1[] vocabListArray = vocabList.toArray(new VocabModelA1[vocabList.size()]);
                    //insert all of the vocab words into the room database and once finished add the Beginner Level 1 vocab list
                    new insertAsyncTask(mVocabDao, mDownloadButtonVisibility, mA1Downloaded, mWordsDownloadedVisibility).execute(vocabListArray);
                    mErrorDownloadingVisibility.setValue(View.GONE);
                }
                @Override
                public void onFailure(Call<List<VocabModelA1>> call, Throwable t) {
                    mErrorDownloadingVisibility.setValue(View.VISIBLE);
                }

            });
            return null;
        }

    }


    /**
     * Async task to check whether or not the Hansel and Gretel story data has been downloaded
     * from the remote database.
     */
    public void checkHAG(MutableLiveData<Integer> HAGDownloaded){
        new checkHAGAsyncTask(db.storyDao(), HAGDownloaded).execute();
    }
    private static class checkHAGAsyncTask extends AsyncTask<Void, Void, Integer>{
        private final StoryDao storyDao;
        private final MutableLiveData<Integer> mHAGDownloaded;

        public checkHAGAsyncTask(StoryDao storyDao, MutableLiveData<Integer> HAGDownloaded){
            this.storyDao = storyDao;
            mHAGDownloaded = HAGDownloaded;
        }

        @Override
        protected Integer doInBackground(Void... params) {
            return storyDao.countSentences();
        }

        @Override
        protected void onPostExecute(Integer sentenceCount){
            if (sentenceCount!=106){
                //HAG has NOT been downloaded
                mHAGDownloaded.setValue(0);
                //Set HAG button to "download"
            }else{
               //HAG has been downloaded
                mHAGDownloaded.setValue(1);
                //set HAG button to "read"
            }
         }
    }

    /**
     * Download and inserts Hansel and Gretel from the remote database to the local database
     */
    public void downloadHAG(MutableLiveData<Integer> HAGDownloaded, MutableLiveData<Integer> hagErrorVisibility, MutableLiveData<Integer> hagButtonVisibility, MutableLiveData<Integer> hagPartsDownloadedVisibility){
        new downloadAndInsertHAGAsyncTask(apiService, db.storyDao(),HAGDownloaded,hagErrorVisibility,hagButtonVisibility,hagPartsDownloadedVisibility).execute();
    }
    private static class downloadAndInsertHAGAsyncTask extends AsyncTask<Void, Void, Void>{
        private final DatabaseService apiService;
        private final StoryDao mStoryDao;
        private final MutableLiveData<Integer> mHAGDownloaded;
        private final MutableLiveData<Integer> hagErrorVisibility;
        private final MutableLiveData<Integer> hagButtonVisibility;
        private final MutableLiveData<Integer> hagPartsDownloadedVisibility;

        downloadAndInsertHAGAsyncTask(DatabaseService api, StoryDao storyDao, MutableLiveData<Integer> HAGDownloaded,
                                      MutableLiveData<Integer> mHagErrorVisibility, MutableLiveData<Integer> mHagButtonVisibility, MutableLiveData<Integer> mHagPartsDownloadedVisibility){
            apiService = api;
            mStoryDao = storyDao;
            mHAGDownloaded = HAGDownloaded;
            hagErrorVisibility = mHagErrorVisibility;
            hagButtonVisibility = mHagButtonVisibility;
            hagPartsDownloadedVisibility = mHagPartsDownloadedVisibility;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            //Call to download the story here
            Call<List<Hag_Sentences>> call = apiService.downloadHagSentences();
            call.enqueue(new Callback<List<Hag_Sentences>>() {
                @Override
                public void onResponse(Call<List<Hag_Sentences>> call, Response<List<Hag_Sentences>> response) {
                    List<Hag_Sentences> sentenceList = response.body();
                    Hag_Sentences[] sentenceListArray = sentenceList.toArray(new Hag_Sentences[sentenceList.size()]);
                    new insertSentencesAsyncTask(mStoryDao,apiService,mHAGDownloaded,hagErrorVisibility,hagButtonVisibility,hagPartsDownloadedVisibility).execute(sentenceListArray);
                }
                @Override
                public void onFailure(Call<List<Hag_Sentences>> call, Throwable t) {
                     //System.out.println("Error on call" + t);
                    hagButtonVisibility.setValue(View.VISIBLE);
                    hagErrorVisibility.setValue(View.VISIBLE);
                    hagPartsDownloadedVisibility.setValue(View.GONE);
                }
            });
            return null;
        }
    }

    /**
     * Async task to insert the story ROOM database data from the call to the remote database.
     */
    private static class insertSentencesAsyncTask extends AsyncTask<Hag_Sentences, Void, Void>{
        private final StoryDao storyDao;
        private final DatabaseService apiService;
        private final MutableLiveData<Integer> mHAGDownloaded;
        private final MutableLiveData<Integer> hagErrorVisibility;
        private final MutableLiveData<Integer> hagButtonVisibility;
        private final MutableLiveData<Integer> hagPartsDownloadedVisibility;

        insertSentencesAsyncTask(StoryDao storyDao,DatabaseService databaseService, MutableLiveData<Integer> HAGDownloaded,
                                 MutableLiveData<Integer> mHagErrorVisibility, MutableLiveData<Integer> mHagButtonVisibility, MutableLiveData<Integer> mHagPartsDownloadedVisibility){
            this.storyDao = storyDao;
            this.apiService = databaseService;
            mHAGDownloaded = HAGDownloaded;
            hagErrorVisibility = mHagErrorVisibility;
            hagButtonVisibility = mHagButtonVisibility;
            hagPartsDownloadedVisibility = mHagPartsDownloadedVisibility;
        }

        @Override
        protected void onPreExecute() {
            hagButtonVisibility.setValue(View.GONE);
            hagErrorVisibility.setValue(View.GONE);
            hagPartsDownloadedVisibility.setValue(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(final Hag_Sentences... params) {
            //inserting HAG sentences
            for (Hag_Sentences model : params){
                storyDao.insertHagSentence(model);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void v){
            //Call to download the words here
            Call<List<Hag_Words>> call = apiService.downloadHagWords();
            call.enqueue(new Callback<List<Hag_Words>>() {
                @Override
                public void onResponse(Call<List<Hag_Words>> call, Response<List<Hag_Words>> response) {
                    List<Hag_Words> wordList = response.body();
                    Hag_Words[] wordListArray = wordList.toArray(new Hag_Words[wordList.size()]);
                    new insertHagWordsAsyncTask(storyDao,mHAGDownloaded,hagErrorVisibility,hagButtonVisibility,hagPartsDownloadedVisibility).execute(wordListArray);
                }
                @Override
                public void onFailure(Call<List<Hag_Words>> call, Throwable t) {
                   //System.out.println("Error on call" + t);
                    hagButtonVisibility.setValue(View.VISIBLE);
                    hagErrorVisibility.setValue(View.VISIBLE);
                    hagPartsDownloadedVisibility.setValue(View.GONE);
                }
            });

        }

    }

    private static class insertHagWordsAsyncTask extends AsyncTask<Hag_Words, Void, Void>{
        private final StoryDao storyDao;
        private final MutableLiveData<Integer> mHAGDownloaded;
        private final MutableLiveData<Integer> hagErrorVisibility;
        private final MutableLiveData<Integer> hagButtonVisibility;
        private final MutableLiveData<Integer> hagPartsDownloadedVisibility;

        insertHagWordsAsyncTask(StoryDao storyDao, MutableLiveData<Integer> HAGDownloaded,
                                MutableLiveData<Integer> mHagErrorVisibility, MutableLiveData<Integer> mHagButtonVisibility, MutableLiveData<Integer> mHagPartsDownloadedVisibility){
            this.storyDao = storyDao;
            mHAGDownloaded = HAGDownloaded;
            hagErrorVisibility = mHagErrorVisibility;
            hagButtonVisibility = mHagButtonVisibility;
            hagPartsDownloadedVisibility = mHagPartsDownloadedVisibility;
        }

        @Override
        protected Void doInBackground(final Hag_Words... params) {
            //Inserting HAG words
            for (Hag_Words model : params){
                storyDao.insertHagWord(model);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            hagButtonVisibility.setValue(View.VISIBLE);
            hagErrorVisibility.setValue(View.GONE);
            hagPartsDownloadedVisibility.setValue(View.GONE);
            mHAGDownloaded.setValue(1);
        }
    }

    /**
     * Async task to insert the story into the story list ROOM table, so the viewpager will show it on the 3rd recyclerview.
     */
    private static class insertStoryListAsyncTask extends AsyncTask<StoriesListItem, Void, Void> {
        private final StoryDao storyDao;

        insertStoryListAsyncTask(StoryDao dao) {
            this.storyDao=dao;
        }

        @Override
        protected Void doInBackground(final StoriesListItem... params) {
            storyDao.insert(params[0]);
            return null;
        }
    }


    /**
     * Updates the vocabmodelA1 activity data when logging in.
     */
    private void updateVocabDataOnLogin(String[] data, String[] dataStudying, String[] dataFreq){
        //showLoadingBar.setValue(1);
        new updateVocabDataOnLoginAsyncTask(db.vocabDao(),showLoadingBar,showViewPager).execute(data, dataStudying, dataFreq);
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

            String[] scoreArray = strings[0];
            String[] studyingArray = strings[1];
            String[] freqArray = strings[2];
           //Begin updating scores
            for (int i = 0;i < 700;i++){
                if (Integer.parseInt(studyingArray[i])==0){break;}
                try {
                    mAsyncTaskDao.updateVocabScore(Integer.parseInt(scoreArray[i]), Integer.parseInt(studyingArray[i]), Integer.parseInt(freqArray[i]), i + 1);//mAsyncTaskDao.updateVocabScore(Integer.parseInt(strings[i]),i);
                }catch(Exception e){
                    break;
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            muld.setValue(View.GONE);
            muld2.setValue(View.VISIBLE);
        }
    }

    /**
     * Inserts a new user into the database
     */
    public void insertUser(User user){
        new insertUserAsyncTask(db.userDao()).execute(user);
    }
    private static class insertUserAsyncTask extends AsyncTask<User, Void, Void> {
        private final UserDao mAsyncTaskDao;

        insertUserAsyncTask(UserDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final User... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    /**
     * Deletes all users in the local room database. Used when logging out.
     */
    public void deleteAllUsers(){new deleteUserAsyncTask(db.userDao()).execute();}
    private static class deleteUserAsyncTask extends AsyncTask<Void, Void, Void> {
        private final UserDao mAsyncTaskDao;

        deleteUserAsyncTask(UserDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }

    /**
     * Deletes all A1 vocab data. Not currently used.
     */
    public void deleteAll(){
        new deleteAsyncTask(db.vocabDao()).execute();
    }
    private static class deleteAsyncTask extends AsyncTask<Void, Void, Void> {
        private final VocabDao mAsyncTaskDao;

        deleteAsyncTask(VocabDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mAsyncTaskDao.deleteAll();
            return null;
        }

    }

    /**
     * Deletes all A1 score data for the current user. Legacy method not in use.
     */
    public void deleteAllScores(){new deleteA1AsyncTask(db.vocabDao()).execute();}
    private static class deleteA1AsyncTask extends AsyncTask<Void, Void, Void> {
        private final VocabDao mAsyncTaskDao;

        deleteA1AsyncTask(VocabDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mAsyncTaskDao.deleteAllScores();
            return null;
        }

    }

    /**
     * Resets all scores for the A1 vocab data. Called when logging out.
     */
    public void resetAllScores(){new resetA1AsyncTask(db.vocabDao(),showLoadingBar,showViewPager).execute();}
    private static class resetA1AsyncTask extends AsyncTask<Void, Void, Void>{
        private final VocabDao mAsyncTaskDao;
        private final MutableLiveData<Integer> mShowLoadingBar;
        private final MutableLiveData<Integer> mShowViewPager;

        resetA1AsyncTask(VocabDao dao,MutableLiveData<Integer> showLoadingBar, MutableLiveData<Integer> showViewPager) {
            mAsyncTaskDao = dao;
            mShowLoadingBar = showLoadingBar;
            mShowViewPager = showViewPager;
        }

        @Override
        protected void onPreExecute() {
            mShowLoadingBar.setValue(View.VISIBLE);
            mShowViewPager.setValue(View.INVISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            //Resets all scores in vocab_tableA1 to the local_tableA1 scores
            LocalSaveA1[] saveList = mAsyncTaskDao.getFullLocalSaveList();
            for (LocalSaveA1 item : saveList){
                mAsyncTaskDao. updateVocabScore(item.getScore(), item.getStudying(), item.getFreq(), item.get_id());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            mShowLoadingBar.setValue(View.GONE);
            mShowViewPager.setValue(View.VISIBLE);
        }
    }


    //Getters and Setters **
    public LiveData<User> getUserInfoFromRoom(){
        return db.userDao().getUser();
    }
    public LiveData<List<StoriesListItem>> getStoriesListItems(){
        return db.storyDao().getAllStoriesLists();
    }
    public VocabDao getmVocabDao() {
        return db.vocabDao();
    }
    public LiveData<Integer> getA1Count() {
        return a1Count;
    }
    public LiveData<Integer> getShowLoadingBar(){return showLoadingBar;}
    public LiveData<Integer> getShowViewPager(){return showViewPager;}
    public LiveData<Integer> getA1Max(){
        return db.vocabDao().count();
    }
    public LiveData<Integer> getA1Learned() {return db.vocabDao().countLearned();}
    public LiveData<Integer> getA1Mastered() {return db.vocabDao().countMastered();}
    public MutableLiveData<Integer> getA1Downloaded() {
        return A1Downloaded;
    }
    public LiveData<String> getA1DownloadedText() {
        return A1DownloadedText;
    }
    public LiveData<Integer> getWordsLearnedVisibility() {
        return wordsLearnedVisibility;
    }
    public MutableLiveData<Integer> getWordsDownloadedVisibility() {
        return wordsDownloadedVisibility;
    }
    public MutableLiveData<Integer> getErrorDownloadingVisibility() {
        return errorDownloadingVisibility;
    }
    public LiveData<Integer> getHAGPartsDownloaded(){return db.storyDao().countHAGParts();}
    public LiveData<Integer> getHAGWordsDownloaded(){return db.storyDao().countHAGWords();}
    public LiveData<Integer> getGenderButtonVisibility() {
        return genderButtonVisibility;
    }
    public LiveData<Integer> getGenderTextVisibility() {
        return genderTextVisibility;
    }
    public LiveData<List<VocabListItem>> getAllVocabLists(){
        return db.vocabListDao().getAllVocabLists();
    }


    //Flashcard viewmodel
    public LiveData<Integer> countWordsMax(){
        return db.vocabDao().count();
    }
    public LiveData<Integer> countLearned(){
        return db.vocabDao().countLearned();
    }
    public LiveData<Integer> countMastered(){
        return db.vocabDao().countMastered();
    }
    public LiveData<List<VocabModelA1>> getVocabQueue(){
        return  db.vocabDao().getVocabQueue();
    }



    /**
     * Updates the scores for every vocabulary word that was in the activity
     * based on a finished List that all words are sent to after being popped from
     * the mediatorVocabList.
     */
    public void updateAllNodes(boolean login, LiveData<String> userName, ArrayList<VocabModelA1> finishedList ){
        updateNode(finishedList, login,userName);
        //finishedList.clear();
    }


    /**
     * Creates an updateNodeAsyncTask and executes it.
     * @param vocabModelA1s The list of all the nodes that will be updated.
     *  This needs to add every node to the execute portion manually.
     */
    private void updateNode(List<VocabModelA1> vocabModelA1s, boolean loggedIn, LiveData<String> userName){
        if (vocabModelA1s.size()==3){
            new updateNodeAsyncTask(db.vocabDao(),apiService,userName.getValue(),loggedIn).execute(vocabModelA1s.get(0), vocabModelA1s.get(1), vocabModelA1s.get(2));
        }else if (vocabModelA1s.size()==4){
            new updateNodeAsyncTask(db.vocabDao(),apiService,userName.getValue(),loggedIn).execute(vocabModelA1s.get(0), vocabModelA1s.get(1), vocabModelA1s.get(2), vocabModelA1s.get(3));
        }else if (vocabModelA1s.size()==5){
            new updateNodeAsyncTask(db.vocabDao(),apiService,userName.getValue(),loggedIn).execute(vocabModelA1s.get(0), vocabModelA1s.get(1), vocabModelA1s.get(2), vocabModelA1s.get(3), vocabModelA1s.get(4));
        }else if (vocabModelA1s.size()==10){
            new updateNodeAsyncTask(db.vocabDao(),apiService,userName.getValue(),loggedIn).execute(vocabModelA1s.get(0), vocabModelA1s.get(1), vocabModelA1s.get(2), vocabModelA1s.get(3), vocabModelA1s.get(4),
                    vocabModelA1s.get(5), vocabModelA1s.get(6), vocabModelA1s.get(7), vocabModelA1s.get(8), vocabModelA1s.get(9));
        }else if (vocabModelA1s.size()==14){
            new updateNodeAsyncTask(db.vocabDao(),apiService,userName.getValue(),loggedIn).execute(vocabModelA1s.get(0), vocabModelA1s.get(1), vocabModelA1s.get(2), vocabModelA1s.get(3), vocabModelA1s.get(4),
                    vocabModelA1s.get(5), vocabModelA1s.get(6), vocabModelA1s.get(7), vocabModelA1s.get(8), vocabModelA1s.get(9),
                    vocabModelA1s.get(10), vocabModelA1s.get(11), vocabModelA1s.get(12), vocabModelA1s.get(13));
        }else if (vocabModelA1s.size()==15){
            new updateNodeAsyncTask(db.vocabDao(),apiService,userName.getValue(),loggedIn).execute(vocabModelA1s.get(0), vocabModelA1s.get(1), vocabModelA1s.get(2), vocabModelA1s.get(3), vocabModelA1s.get(4),
                    vocabModelA1s.get(5), vocabModelA1s.get(6), vocabModelA1s.get(7), vocabModelA1s.get(8), vocabModelA1s.get(9),
                    vocabModelA1s.get(10), vocabModelA1s.get(11), vocabModelA1s.get(12), vocabModelA1s.get(13), vocabModelA1s.get(14));
        }else if (vocabModelA1s.size()==20){
            new updateNodeAsyncTask(db.vocabDao(),apiService,userName.getValue(),loggedIn).execute(vocabModelA1s.get(0), vocabModelA1s.get(1), vocabModelA1s.get(2), vocabModelA1s.get(3), vocabModelA1s.get(4),
                    vocabModelA1s.get(5), vocabModelA1s.get(6), vocabModelA1s.get(7), vocabModelA1s.get(8), vocabModelA1s.get(9),
                    vocabModelA1s.get(10), vocabModelA1s.get(11), vocabModelA1s.get(12), vocabModelA1s.get(13), vocabModelA1s.get(14),
                    vocabModelA1s.get(15), vocabModelA1s.get(16), vocabModelA1s.get(17), vocabModelA1s.get(18), vocabModelA1s.get(19));
        }

    }

    /**
     * AsyncTask to update each vocabulary word in the ROOM database on a separate thread
     * at the end of the activity, once every flashcard has been finished.
     * Updates the remote database if logged in after.
     */
    private static class updateNodeAsyncTask extends AsyncTask<VocabModelA1, Void, Void> {

        private final VocabDao mAsyncTaskDao;
        private String tempS,tempS2,tempS3;
        private DatabaseService apiService;
        private String username;
        private boolean loggedIn;
        private final DatabasePID databasePID = new DatabasePID();

        updateNodeAsyncTask(VocabDao dao, DatabaseService api, String username, boolean login) {
            mAsyncTaskDao = dao;
            apiService = api;
            this.username = username;
            loggedIn=login;

        }

        @Override
        protected Void doInBackground(final VocabModelA1... params) {
            if (!loggedIn){
                for (VocabModelA1 model : params){
                    mAsyncTaskDao.updateNode(model);
                    mAsyncTaskDao.updateLocalNode(model.getScore(),model.getStudying(),model.getFreq(),model.getId()); //Updates the local save for when user is not logged in
                }
            }else{
                for (VocabModelA1 model : params){
                    mAsyncTaskDao.updateNode(model);
                }
            }

            String scoreList = mAsyncTaskDao.getA1Scores().toString();
            String studyingList = mAsyncTaskDao.getA1Studying().toString();
            String freqList = mAsyncTaskDao.getA1Freq().toString();
            tempS = scoreList.replaceAll("\\s","").replaceAll("\\[","").replaceAll("\\]","");
            tempS2 = studyingList.replaceAll("\\s","").replaceAll("\\[","").replaceAll("\\]","");
            tempS3 = freqList.replaceAll("\\s","").replaceAll("\\[","").replaceAll("\\]","");
            return null;
        }

        @Override
        protected void onPostExecute(Void v){
            //Saving to remote
            if (loggedIn){
                //Update remote database with account data
                Call<CreateUploadDataResponse> call = apiService.uploadData(username,"A1",tempS,tempS3,tempS2,databasePID.getPid());
                call.enqueue(new Callback<CreateUploadDataResponse>() {
                    @Override
                    public void onResponse(Call<CreateUploadDataResponse> call, Response<CreateUploadDataResponse> response) {
                         //System.out.println("Response to creating save data");
                         //System.out.println(response);
                        //CreateUploadDataResponse lr = response.body();
                         //System.out.println("Body is : " + lr);
                    }

                    @Override
                    public void onFailure(Call<CreateUploadDataResponse> call, Throwable t) {
                        //System.out.println("Error on call when creating save data" + t);
                    }
                });
            }

        }
    }





    //NounGender
    public LiveData<List<VocabModelA1>> getNounQueue(){
        return db.vocabDao().getNounQueue();
    }


    //Stories
    public LiveData<Hag_Words> getWord2(String text){
        return db.storyDao().getWord2(text);
    }
    public LiveData<List<Hag_Sentences>> getSentences(){
        return db.storyDao().getSentences();
    }
    /**
     * Updates the liveData hag_word that the activity_read.xml uses to show on the bottom part of the screen.
     * This works by using a mediatorLiveData mHagWord, mapped to the first liveData hagWord,
     * then the actual LiveData that is being observed is set to mediatorLiveData via a transformation map.
     * The mediatorLiveData is changed in an asyncTask which queries the ROOM database.
     */
    public void updateHagWord(String german, MediatorLiveData<Hag_Words> mHagWord){
        String tempString;
        try{
            if (german != null){
                tempString = german.replace(";","").replace(".","").replace(",","").replace("\"","").replace("?","").replace(":","").replace("!","");
            }else{
                tempString ="";
            }
            new getHagWordAsyncTask(db.storyDao(),mHagWord).execute(tempString);
        }
        catch(Exception e){
            mHagWord.setValue(new Hag_Words("","",""));
        }
    }

    /**
     * Takes the word that was clicked on and searches for it in the ROOM database.
     */
    private static class getHagWordAsyncTask extends AsyncTask<String, Void, Void> {
        StoryDao storyDao;
        Hag_Words hagWord;
        MediatorLiveData<Hag_Words> mld;
        getHagWordAsyncTask(StoryDao storyDao, MediatorLiveData<Hag_Words> mld){
            this.storyDao = storyDao;
            this.mld = mld;
        }

        @Override
        protected Void doInBackground(String... params) {
            hagWord = storyDao.getWord(params[0]);
            if (hagWord == null) {
                // First was null, running again in lower case
                String tempS= "";
                tempS = params[0].toLowerCase();
                hagWord = storyDao.getWord(tempS);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            mld.setValue(hagWord);
        }
    }



    public LiveData<User> getCurrentUser() {
        return currentUser;
    }
    public LiveData<String> getUserName() {
        return userName;
    }

    public MutableLiveData<Integer> getProfileVisibility() {
        return profileVisibility;
    }
    public MutableLiveData<Integer> getLoginVisibility() {
        return loginVisibility;
    }
    public void setLoginVisibility(Integer i){
        loginVisibility.setValue(i);
    }
    public void setProfileVisibility(Integer i){
        profileVisibility.setValue(i);
    }

    public int getPageOn() {
        return pageOn;
    }

    public void setPageOn(int pageOn) {
        this.pageOn = pageOn;
    }
}
