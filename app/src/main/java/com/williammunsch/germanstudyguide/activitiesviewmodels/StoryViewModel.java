package com.williammunsch.germanstudyguide.activitiesviewmodels;

import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.williammunsch.germanstudyguide.datamodels.Hag_Words;
import com.williammunsch.germanstudyguide.repositories.StoriesRepository;

import java.util.List;

import javax.inject.Inject;

public class StoryViewModel extends ViewModel {
    StoriesRepository storiesRepository;

    @Inject
    public StoryViewModel(StoriesRepository storiesRepository){
        this.storiesRepository = storiesRepository;
    }

    public void pageForward(){
        storiesRepository.pageForward();
    }
    public void pageBack(){
        storiesRepository.pageBack();
    }

    public void clickOnWord(String word){
     //   System.out.println("CLICKED ON WORD " +word);
        storiesRepository.setEnglishVisibility(View.GONE);
        storiesRepository.setTranslationVisibility(View.VISIBLE);
        storiesRepository.setNotesVisibility(View.VISIBLE);
        storiesRepository.updateHagWord(word);
    }

    public void toggleEnglishVisibility(){
        try {
            if (storiesRepository.getEnglishVisibility().getValue() == View.GONE) {
                storiesRepository.setEnglishVisibility(View.VISIBLE);
                storiesRepository.setTranslationVisibility(View.GONE);
                storiesRepository.setNotesVisibility(View.GONE);
            } else {
                storiesRepository.setEnglishVisibility(View.GONE);
            }
        }catch(Exception e){
           // System.out.println("NULL");
        }
    }

    public LiveData<Hag_Words> getHagWord() {
        return storiesRepository.getHagWord();
    }

    public void setStoryName(String storyName){
        storiesRepository.setStoryName(storyName);
    }


    public LiveData<String> getPageNumberText() {
        return storiesRepository.getPageNumberText();
    }
    public LiveData<String> getCurrentEnglishSentence() {
        return storiesRepository.getCurrentEnglishSentence();
    }


    public LiveData<List<String>> getCurrentSentenceWordList() {
        return storiesRepository.getCurrentSentenceWordList();
    }
    public LiveData<Hag_Words> getHagWordLive() {
        return storiesRepository.getHagWordLive();
    }
    public LiveData<Integer> getEnglishVisibility() {
        return storiesRepository.getEnglishVisibility();
    }
    public LiveData<Integer> getNotesVisibility() {
        return storiesRepository.getNotesVisibility();
    }

    public LiveData<Integer> getTranslationVisibility() {
        return storiesRepository.getTranslationVisibility();
    }

}
