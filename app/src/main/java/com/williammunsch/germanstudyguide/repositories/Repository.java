package com.williammunsch.germanstudyguide.repositories;


import android.os.AsyncTask;
import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.williammunsch.germanstudyguide.responses.SaveDataResponse;
import com.williammunsch.germanstudyguide.User;
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

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 *  Base repository that handles api calls and ROOM updates.
 *
 */

@Singleton
public class Repository {

    private VocabDao mVocabDao;
    private VocabListDao mVocabListDao;
    private UserDao mUserDao;
    private StoryDao storyDao;
    private List<VocabListItem> dataSet;
    public DatabaseService apiService;
    private LiveData<User> currentUser;
    private LiveData<String> userName;
    private LiveData<String> userEmail;
    private LiveData<String> A1DownloadedText;
    private LiveData<String> HAGDownloadedText;

    private LiveData<List<VocabListItem>> vocabListItemList;
    private LiveData<List<StoriesListItem>> storiesListItems;
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


    private MutableLiveData<Integer> A1Downloaded = new MutableLiveData<>();
    private MutableLiveData<Integer> HAGDownloaded = new MutableLiveData<>();

    GermanDatabase db;


    private LiveData<Integer> a1Count;


    private MutableLiveData<Integer> downloadProgressVisibility = new MutableLiveData<>();
    private MutableLiveData<Integer> downloadButtonVisibility = new MutableLiveData<>();
    private MutableLiveData<Integer> wordsLearnedVisibility = new MutableLiveData<>();
    private MutableLiveData<Integer> wordsDownloadedVisibility = new MutableLiveData<>();
    private MutableLiveData<Integer> errorDownloadingVisibility = new MutableLiveData<>();

    private MutableLiveData<Integer> hagErrorVisibility = new MutableLiveData<>();
    private MutableLiveData<Integer> hagButtonVisibility = new MutableLiveData<>();
    private MutableLiveData<Integer> hagPartsDownloadedVisibility = new MutableLiveData<>();

    private MutableLiveData<Integer> genderButtonVisibility = new MutableLiveData<>(View.VISIBLE);
    private MutableLiveData<Integer> genderTextVisibility = new MutableLiveData<>(View.GONE);

    /**
     * Main page repository that handles updates and stores info for the vocab fragment (0/700 and name) and story fragment (title and author).
     */
    @Inject
     public Repository(DatabaseService apiService, GermanDatabase db) {
        this.apiService = apiService;
        this.db = db;
        mVocabDao = db.vocabDao();
        storyDao = db.storyDao();
        mVocabListDao = db.vocabListDao();
        mUserDao = db.userDao();
        getUserInfoFromRoom();
        a1Count = db.vocabDao().count();
        dataSet = new ArrayList<>();
        initialSetters();

        //map username to currentuser.username to show username in top left of main screen
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

        //map the dialogue to a successful account creation or not
        accountCreation = Transformations.map(allGood, success->{
           if (allGood.getValue()!=null && allGood.getValue()){
               //call api here for registration
               //change allGood back to false?
               return "Registration successful.";
           }
           return "Error: Registration failed.";
        });

        downloadProgressVisibility.setValue(View.GONE);
        downloadButtonVisibility.setValue(View.VISIBLE);
        wordsLearnedVisibility.setValue(View.GONE);
        wordsDownloadedVisibility.setValue(View.GONE);
        errorDownloadingVisibility.setValue(View.GONE);

        hagButtonVisibility.setValue(View.VISIBLE);
        hagErrorVisibility.setValue(View.GONE);
        hagPartsDownloadedVisibility.setValue(View.GONE);



        //Check if the ROOM tables for the lessons and story lists for the recyclerviews have been created
        checkLessons();
        checkStories();

        //Check if vocab lessons and stories are fully downloaded from the remote database.
        A1Downloaded.setValue(0);
        HAGDownloaded.setValue(0);
        checkA1();
        checkHAG();

        //Set the A1 button to say either download or study based on whether or not the A1 data
        //has been downloaded from the remote database
        A1DownloadedText = Transformations.map(A1Downloaded, value->{
           if (A1Downloaded.getValue() == 0){
               wordsLearnedVisibility.setValue(View.GONE);
               genderButtonVisibility.setValue(View.GONE);
               genderTextVisibility.setValue(View.VISIBLE);
               //wordsDownloadedVisibility.setValue(View.VISIBLE);
               return "Download";
           }
           else{
               wordsLearnedVisibility.setValue(View.VISIBLE);
               genderButtonVisibility.setValue(View.VISIBLE);
               genderTextVisibility.setValue(View.GONE);
               //wordsDownloadedVisibility.setValue(View.GONE);
               return "Study";
           }
        });

        //Change the text of the button in the HAG activity depending on whether it has been downloaded or not
        HAGDownloadedText = Transformations.map(HAGDownloaded, value->{
            if (HAGDownloaded.getValue() == 0){
                return "Download";
            }
            else{
                return "Read";
            }
        });

        //Get all of the vocab lessons and story lessons in a list to display in the recyclerviews in their fragments.
        vocabListItemList = mVocabListDao.getAllVocabLists();
        storiesListItems = storyDao.getAllStoriesLists();

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
       // System.out.println("Calling downloadsavedata");
        Call<SaveDataResponse> call = apiService.getSaveData(userN);
        call.enqueue(new Callback<SaveDataResponse>() {
            @Override
            public void onResponse(Call<SaveDataResponse> call, Response<SaveDataResponse> response) {
              //  System.out.println("Response to call: ");
              //  System.out.println(response);
                SaveDataResponse data = response.body();
               // System.out.println("Data body: ");
               // System.out.println(data + " is null?");

                if (data!=null){
                    if (data.getTablename()==null || data.getScore()==null || data.getStudying()==null || data.getFreq()==null){
                       // createNewEntryAndUpload();
                    }else{
                      //  System.out.println("Data not null, inputting data");
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
    public void resetAllScores(){new resetA1AsyncTask(mVocabDao,showLoadingBar,showViewPager).execute();}





    //********************************************************************

    public void checkLessons(){
        new checkLessonAsyncTask(mVocabListDao, "Beginner Level 1", "  A1").execute();
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
            int exists = mVocabListDao.vocabLessonExists(mLessonName);
            System.out.println("DOES " +mLessonName + "  EXIST? " + exists);
            return exists;
        }

        @Override
        protected void onPostExecute(Integer exists){
            if (exists != 1){
                System.out.println(mLessonName + " does not exist, adding.");
                new insertVocabListAsyncTask(mVocabListDao).execute(new VocabListItem(mLessonName,mImageName,0,0,0));
            }
            else{
                System.out.println(mLessonName + "  exists");
            }

        }

    }
    //********************************************************************

    //********************************************************************
    public void checkStories(){
        new checkStoryAsyncTask(storyDao, "Hänsel und Gretel", "der Brüder Grimm").execute();
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
            int exists = mStoryDao.storyLessonExists(mStoryName);
            System.out.println("DOES " +mStoryName + "  EXIST? " + exists);
            return exists;
        }

        @Override
        protected void onPostExecute(Integer exists){
            if (exists != 1){
                System.out.println(mStoryName + " does not exist, adding.");
                new insertStoryListAsyncTask(mStoryDao).execute(new StoriesListItem(mStoryName,mAuthorName));
            }
            else{
                System.out.println(mStoryName + "  exists");
            }

        }

    }
    //********************************************************************



    public void checkA1(){
        new checkA1AsyncTask(mVocabDao, A1Downloaded).execute();
    }
    /**
     * Checks to see if the A1 table has been downloaded to the local database.
     * Determines whether the button reads "Download" or "Study" by setting mA1Downloaded to 1 or 0
     */
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
            mA1Downloaded.setValue(1);
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



    public void downloadA1(){
        new downloadA1AndInsert(apiService,A1Downloaded, mVocabDao, downloadButtonVisibility,  wordsDownloadedVisibility, errorDownloadingVisibility).execute();
    }
    /**
     * AsyncTask to download the A1 data from the remote database on a background thread
     * Calls insertAsyncTask when the data has been retrieved to input it into the local databases
     */
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
            System.out.println("Downloading A1 from remote");
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



    public void checkHAG(){
        new checkHAGAsyncTask(storyDao, HAGDownloaded).execute();
    }
    /**
     * Async task to check whether or not the Hansel and Gretel story data has been downloaded
     * from the remote database.
     */
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
            System.out.println("sent count: " + sentenceCount);
            if (sentenceCount!=106){
                System.out.println("HAG has NOT been downloaded");
                mHAGDownloaded.setValue(0);
                //Set HAG button to "download"
            }else{
                System.out.println("HAG has been downloaded");
                mHAGDownloaded.setValue(1);
                //set HAG button to "read"
            }
         }
    }

    public void downloadHAG(){
        new downloadAndInsertHAGAsyncTask(apiService, storyDao,HAGDownloaded,hagErrorVisibility,hagButtonVisibility,hagPartsDownloadedVisibility).execute();
    }

    /**
     * Download and inserts Hansel and Gretel from the remote database to the local database
     */
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
            System.out.println("Downloading HAG");
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
                     System.out.println("Error on call" + t);
                    //TODO : ADD ERROR TEXT HERE
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
            System.out.println("inserting HAG sentences");
            for (Hag_Sentences model : params){
                storyDao.insertHagSentence(model);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void v){
            System.out.println("downloading hag words");
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
                   System.out.println("Error on call" + t);
                    hagButtonVisibility.setValue(View.VISIBLE);
                    hagErrorVisibility.setValue(View.VISIBLE);
                    hagPartsDownloadedVisibility.setValue(View.GONE);
                    //TODO : create a variable live data for visibility and a button and textview for "could not connect to server, tap to try again"

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
            System.out.println("Inserting HAG words");
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

            String[] scoreArray = strings[0];
            String[] studyingArray = strings[1];
            String[] freqArray = strings[2];
           // System.out.println("BEGGINING UPDATING SCORES");
            for (int i = 0;i < 700;i++){
                if (Integer.parseInt(studyingArray[i])==0){break;}
                try {
                    mAsyncTaskDao.updateVocabScore(Integer.parseInt(scoreArray[i]), Integer.parseInt(studyingArray[i]), Integer.parseInt(freqArray[i]), i + 1);//mAsyncTaskDao.updateVocabScore(Integer.parseInt(strings[i]),i);
                }catch(Exception e){
                    break;
                }
                }
           // System.out.println("END UPDATING SCORES");

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
        private final VocabDao mAsyncTaskDao;
        private final MutableLiveData<Integer> muld;
        private final MutableLiveData<Integer> muld2;

        resetA1AsyncTask(VocabDao dao,MutableLiveData<Integer> mld, MutableLiveData<Integer> mld2) {
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
        protected Void doInBackground(Void... voids) {
           // mAsyncTaskDao.resetAllScores();
            //Resets all scores in vocab_tableA1 to the local_tableA1 scores
            //TODO : hide begginer level 1 tab and show loadnig bar until done
            LocalSaveA1[] saveList = mAsyncTaskDao.getFullLocalSaveList();
            for (LocalSaveA1 item : saveList){
                mAsyncTaskDao. updateVocabScore(item.getScore(), item.getStudying(), item.getFreq(), item.get_id());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            muld.setValue(View.GONE);
            muld2.setValue(View.VISIBLE);
        }
    }




    //View Visibility setters
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

    //Getters and Setters **
    public void getUserInfoFromRoom(){
        currentUser = mUserDao.getUser();
    }
    public void setPasswordErrorVisibility(int i){
        passwordErrorVisibility.setValue(i);
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
    public LiveData<List<StoriesListItem>> getStoriesListItems(){
        return storiesListItems;
    }
    public LiveData<User> getCurrentUser() {
        return currentUser;
    }
    public VocabDao getmVocabDao() {
        return mVocabDao;
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
    public LiveData<Integer> count() {
        return mVocabDao.count();
    }

    private void initialSetters(){
        registrationVisibility.setValue(View.GONE);
        passwordErrorVisibility.setValue(View.GONE);
        emailTakenVisibility.setValue(View.GONE);
        emailValidVisibility.setValue(View.GONE);
        errorConnectingToDatabaseVisibility.setValue(View.GONE);
        showLoadingBar.setValue(View.GONE);
        showViewPager.setValue(View.VISIBLE);
        couldNotConnectVisibility.setValue(View.GONE);
    }

    public MutableLiveData<Integer> getA1Downloaded() {
        return A1Downloaded;
    }

    public void setA1Downloaded(MutableLiveData<Integer> a1Downloaded) {
        A1Downloaded = a1Downloaded;
    }

    public LiveData<String> getA1DownloadedText() {
        return A1DownloadedText;
    }
    public LiveData<Integer> getDownloadProgressVisibility() {
        return downloadProgressVisibility;
    }
    public LiveData<Integer> getWordsLearnedVisibility() {
        return wordsLearnedVisibility;
    }
    public LiveData<Integer> getDownloadButtonVisibility() {
        return downloadButtonVisibility;
    }

    public MutableLiveData<Integer> getWordsDownloadedVisibility() {
        return wordsDownloadedVisibility;
    }
    public MutableLiveData<Integer> getErrorDownloadingVisibility() {
        return errorDownloadingVisibility;
    }
    public LiveData<String> getHAGDownloadedText() {
        return HAGDownloadedText;
    }
    public MutableLiveData<Integer> getHAGDownloaded() {
        return HAGDownloaded;
    }

    public LiveData<Integer> getHAGPartsDownloaded(){return storyDao.countHAGParts();}
    public LiveData<Integer> getHAGWordsDownloaded(){return storyDao.countHAGWords();}
    public LiveData<Integer> getHagErrorVisibility() {
        return hagErrorVisibility;
    }
    public LiveData<Integer> getHagButtonVisibility() {
        return hagButtonVisibility;
    }
    public LiveData<Integer> getHagPartsDownloadedVisibility() {
        return hagPartsDownloadedVisibility;
    }
    public LiveData<Integer> getGenderButtonVisibility() {
        return genderButtonVisibility;
    }
    public LiveData<Integer> getGenderTextVisibility() {
        return genderTextVisibility;
    }
}
