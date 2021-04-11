package com.williammunsch.germanstudyguide.activitiesviewmodels;

import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.williammunsch.germanstudyguide.datamodels.Hag_Sentences;
import com.williammunsch.germanstudyguide.datamodels.Hag_Words;
import com.williammunsch.germanstudyguide.repositories.Repository;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

/**
 * Handles all of the livedata objects and logic for the story activity.
 */
public class StoryViewModel extends ViewModel {
    Repository mRepository;
    private String storyName;
    private int pageOn = 0;
    private final LiveData<Hag_Words> hagWordLive;
    private final MutableLiveData<String> pageNumberText = new MutableLiveData<>();
    private final LiveData<String> currentEnglishSentence;
    private final LiveData<List<String>> currentSentenceWordList;
    private final MediatorLiveData<List<Hag_Sentences>> mediatorSentence = new MediatorLiveData<>();
    private final LiveData<List<Hag_Sentences>> sentenceLiveData;

    private final LiveData<Hag_Words> hagWord;
    private final MediatorLiveData<Hag_Words> mHagWord = new MediatorLiveData<>();

    private final MutableLiveData<Integer> englishVisibility = new MutableLiveData<>(View.GONE);
    private final MutableLiveData<Integer> notesVisibility = new MutableLiveData<>(View.GONE);
    private final MutableLiveData<Integer> translationVisibility = new MutableLiveData<>(View.GONE);

    @Inject
    public StoryViewModel(Repository repository){
        this.mRepository = repository;
        hagWord = mRepository.getWord2("");
        sentenceLiveData = mRepository.getSentences();
        addSource();
        addSource2();

        //Maps the current page of text
        currentEnglishSentence = Transformations.map(mediatorSentence, value -> {
            if (mediatorSentence.getValue() != null){
                return mediatorSentence.getValue().get(pageOn).getEnglish();
            }
            return null;
        });

        //Gets the current page text and returns it as an array of words for word lookup
        currentSentenceWordList = Transformations.map(mediatorSentence, value -> {
            pageNumberText.setValue(pageOn+1 + " / 106");
            if (mediatorSentence.getValue() != null){
                String tempString = mediatorSentence.getValue().get(pageOn).getGerman();
                String[] wordArray = tempString.split(" ");
                if (wordArray.length < 77){
                    wordArray = Arrays.copyOf(wordArray,77);
                }

                return Arrays.asList(wordArray);
            }
            return null;
        });

        //Maps the selected word to the translated word
        hagWordLive = Transformations.map(mHagWord, value -> {
            if (mHagWord.getValue() != null){
                mHagWord.getValue().setGerman(mHagWord.getValue().getGerman() + "  -  ");
                return mHagWord.getValue();
            }
            return null;
        });
    }

    /**
     * Moves the page forward when clicked
     */
    public void pageForward(){
        if (pageOn<105){
            pageOn++;
        }
        mediatorSentence.setValue(mediatorSentence.getValue());
        englishVisibility.setValue(View.GONE);
        notesVisibility.setValue(View.GONE);
        translationVisibility.setValue(View.GONE);
    }

    /**
     * Moves the page back when clicked
     */
    public void pageBack(){
        if (pageOn>0){
            pageOn--;
        }
        mediatorSentence.setValue(mediatorSentence.getValue());
        englishVisibility.setValue(View.GONE);
        notesVisibility.setValue(View.GONE);
        translationVisibility.setValue(View.GONE);
    }

    /**
     * When clicking on a word, updates the view visibilities and calls an AsyncTask to get the
     * word data from the ROOM database
     */
    public void clickOnWord(String word){
        englishVisibility.setValue(View.GONE);
        translationVisibility.setValue(View.VISIBLE);
        notesVisibility.setValue(View.VISIBLE);
        mRepository.updateHagWord(word,mHagWord);
    }

    public void toggleEnglishVisibility(){
        try {
            if (englishVisibility.getValue()!=null && englishVisibility.getValue() == View.GONE) {
                englishVisibility.setValue(View.VISIBLE);
                translationVisibility.setValue(View.GONE);
                notesVisibility.setValue(View.GONE);
            } else {
                englishVisibility.setValue(View.GONE);
            }
        }catch(Exception e){
           // System.out.println("NULL");
        }
    }


    public void addSource(){
        if (mediatorSentence.getValue()==null){
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
            try {
                mHagWord.addSource(hagWord, value -> mHagWord.setValue(value));
            }catch(Exception e){
                //     System.out.println("Error: " + e);

            }
        }
    }

    public LiveData<Hag_Words> getHagWord() {
        return hagWord;
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
    public LiveData<List<String>> getCurrentSentenceWordList() {
        return currentSentenceWordList;
    }
    public LiveData<Hag_Words> getHagWordLive() {
        return hagWordLive;
    }
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
