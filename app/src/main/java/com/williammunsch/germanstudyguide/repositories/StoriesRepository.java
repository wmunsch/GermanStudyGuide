package com.williammunsch.germanstudyguide.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import android.os.AsyncTask;
import android.view.View;

import com.williammunsch.germanstudyguide.datamodels.Hag_Sentences;
import com.williammunsch.germanstudyguide.datamodels.Hag_Words;
import com.williammunsch.germanstudyguide.room.GermanDatabase;
import com.williammunsch.germanstudyguide.room.StoryDao;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;



@Singleton
public class StoriesRepository {
    private MutableLiveData<String> pageNumberText = new MutableLiveData<>();
    private LiveData<String> currentEnglishSentence;
    private LiveData<List<String>> currentSentenceWordList;
    private MediatorLiveData<List<Hag_Sentences>> mediatorSentence = new MediatorLiveData<>();
    private LiveData<List<Hag_Sentences>> sentenceLiveData;

    private MutableLiveData<List<String>> mutableWordList = new MutableLiveData<>();


    private LiveData<Hag_Words> hagWord;
    private MediatorLiveData<Hag_Words> mHagWord = new MediatorLiveData<>();


    private MutableLiveData<Integer> englishVisibility = new MutableLiveData<>();
    private MutableLiveData<Integer> notesVisibility = new MutableLiveData<>();
    private MutableLiveData<Integer> translationVisibility = new MutableLiveData<>();

    private LiveData<Hag_Words> hagWordLive;
   // private LiveData<String> germanWordText;
    //private MutableLiveData<String> englishWordText = new MutableLiveData<>();

    private StoryDao storyDao;
    GermanDatabase db;
    private String storyName;

    private int pageOn = 0;

    @Inject
    public StoriesRepository(GermanDatabase db){
        this.db = db;
        storyDao = db.storyDao();
        hagWord = storyDao.getWord2("");
        sentenceLiveData = storyDao.getSentences();
        englishVisibility.setValue(View.GONE);
        notesVisibility.setValue(View.GONE);
        translationVisibility.setValue(View.GONE);
        //hagWord.setValue(new Hag_Words("","",""));
       // germanWordText.setValue("");
       // englishWordText.setValue("");


        currentEnglishSentence = Transformations.map(mediatorSentence, value -> {
            if (mediatorSentence.getValue() != null){
                return mediatorSentence.getValue().get(pageOn).getEnglish();
            }
            return null;
        });

        currentSentenceWordList = Transformations.map(mediatorSentence, value -> {
            pageNumberText.setValue(pageOn+1 + " / 106");
            if (mediatorSentence.getValue() != null){
                String tempString = mediatorSentence.getValue().get(pageOn).getGerman();
                //tempString = tempString.replace(";","");
               // tempString = tempString.replace(",","");
                //tempString = tempString.replace(".","");
               // tempString = tempString.replace("\"","");
                String[] wordArray = tempString.split(" ");//mediatorSentence.getValue().get(pageOn).getGerman().split(" ");
                if (wordArray.length < 77){
                    wordArray = Arrays.copyOf(wordArray,77);
                }

                return Arrays.asList(wordArray);
            }
            return null;
        });

        hagWordLive = Transformations.map(mHagWord, value -> {
            if (mHagWord.getValue() != null){
                mHagWord.getValue().setGerman(mHagWord.getValue().getGerman() + "  -  ");
                return mHagWord.getValue();
            }
            return null;
        });

        addSource();
        addSource2();
    }

    public void pageForward(){
        if (pageOn<105){//mediatorSentence.getValue().size()-1){
            pageOn++;
        }
        mediatorSentence.setValue(mediatorSentence.getValue());
        englishVisibility.setValue(View.GONE);
        notesVisibility.setValue(View.GONE);
        translationVisibility.setValue(View.GONE);
    }

    public void pageBack(){
        if (pageOn>0){
            pageOn--;
        }
        mediatorSentence.setValue(mediatorSentence.getValue());
        englishVisibility.setValue(View.GONE);
        notesVisibility.setValue(View.GONE);
        translationVisibility.setValue(View.GONE);
    }

    public void setMediatorSentence(Hag_Sentences hag_sentences){
       // this.mediatorSentence.setValue(hag_sentences);
    }

    public void addSource(){
        if (mediatorSentence.getValue()==null){
           // System.out.println("*\n*\nADDING SOURCE\n*\n*");
            try {
                mediatorSentence.addSource(sentenceLiveData, value -> mediatorSentence.setValue(value));
            }catch(Exception e){
              //  System.out.println("Error: " + e);

            }
        }
    }

    /**
     * Adds the mediator live data mHagWord
     */
    public void addSource2(){
        if (mHagWord.getValue()==null){
          //  System.out.println("*\n*\nADDING SOURCE\n*\n*");
            try {
                mHagWord.addSource(hagWord, value -> mHagWord.setValue(value));
            }catch(Exception e){
           //     System.out.println("Error: " + e);

            }
        }
    }

    /**
     * Updates the liveData hag_word that the activity_read.xml uses to show on the bottom part of the screen.
     * This works by using a mediatorLiveData mHagWord, mapped to the first liveData hagWord,
     * then the actual LiveData that is being observed is set to mediatorLiveData via a transformation map.
     * The mediatorLiveData is changed in an asyncTask which queries the ROOM database.
     */
    public void updateHagWord(String german){
      //  System.out.println("UPDATING HAGWORD");
        String tempString;
        try{
            if (german != null){
                tempString = german.replace(";","").replace(".","").replace(",","").replace("\"","").replace("?","").replace(":","").replace("!","");
            }else{
                tempString ="";
            }
          //  System.out.println("looking for : " + tempString);
            new getHagWordAsyncTask(storyDao,mHagWord).execute(tempString);
        }
        catch(Exception e){
           // System.out.println("EXCEPTION");
            mHagWord.setValue(new Hag_Words("","",""));
        }

       // tempString = tempString.toLowerCase();
       // System.out.println("LOOKING FOR " + tempString);
        //Hag_Words hagWord = new Hag_Words("testg","teste","note");
       // new getHagWordAsyncTask(tempString,storyDao,mHagWord).execute();
        //mHagWord.setValue(hagWord);// = storyDao.getWord(german);
    }
    public LiveData<Hag_Words> getHagWord() {
        return hagWord;
    }

    /**
     * Takes the word that was clicked on and searches for it in the ROOM database.
     */
    private static class getHagWordAsyncTask extends AsyncTask<String, Void, Void> {
        StoryDao storyDao;
        Hag_Words hagWord;
        //String s;
        MediatorLiveData<Hag_Words> mld;
        getHagWordAsyncTask(StoryDao storyDao, MediatorLiveData<Hag_Words> mld){
            this.storyDao = storyDao;
            //this.s = s;
            this.mld = mld;
        }

        @Override
        protected Void doInBackground(String... params) {
           // System.out.println("running search for " + params[0]);
            hagWord = storyDao.getWord(params[0]);
            if (hagWord == null) {
               // System.out.println("First was null, running again in lower case");
                String tempS= "";
                tempS = params[0].toLowerCase();
                hagWord = storyDao.getWord(tempS);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
          //  System.out.println(hagWord);
            mld.setValue(hagWord);
        }
    }




    //Getters and setters
    public LiveData<Hag_Words> getHagWordLive() {
        return hagWordLive;
    }
    public LiveData<List<String>> getCurrentSentenceWordList() {
        return currentSentenceWordList;
    }
    public void setStoryName(String storyName){
        this.storyName = storyName;
   }
    public LiveData<String> getPageNumberText() {
        return pageNumberText;
    }
    public LiveData<String> getCurrentEnglishSentence() {
        return currentEnglishSentence;
    }
    public int getPageOn() {
        return pageOn;
    }
    public void setPageOn(int pageOn) {
        this.pageOn = pageOn;
    }
    public MediatorLiveData<List<Hag_Sentences>> getMediatorSentence() {
        return mediatorSentence;
    }
    public void setEnglishVisibility(int i){this.englishVisibility.setValue(i);}
    public void setNotesVisibility(int i){this.notesVisibility.setValue(i);}
    public void setTranslationVisibility(int i){this.translationVisibility.setValue(i);}
    public LiveData<Integer> getEnglishVisibility() {
        return englishVisibility;
    }
    public LiveData<Integer> getNotesVisibility() {
        return notesVisibility;
    }
    public LiveData<Integer> getTranslationVisibility() {
        return translationVisibility;
    }
}

