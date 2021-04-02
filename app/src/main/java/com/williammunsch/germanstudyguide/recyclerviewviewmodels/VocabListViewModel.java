package com.williammunsch.germanstudyguide.recyclerviewviewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.williammunsch.germanstudyguide.datamodels.VocabListItem;
import com.williammunsch.germanstudyguide.datamodels.VocabModelA1;
import com.williammunsch.germanstudyguide.repositories.FlashcardRepository;
import com.williammunsch.germanstudyguide.repositories.Repository;

import java.util.List;

import javax.inject.Inject;

/**
 * The view model for the vocab list fragment in MainActivity.
 * It displays a recycler view which contains different levels
 * of vocabulary, which when clicked on open a new FlashcardActivity.
 */
public class VocabListViewModel extends ViewModel {

    private Repository mRepository;//injected
    private FlashcardRepository flashcardRepository;//injected

    private LiveData<List<VocabModelA1>> mAllVocab;
    private LiveData<List<VocabListItem>> mVocabListItems;
    private LiveData<Integer> a1Max;
    private LiveData<Integer> a1Learned;
    private LiveData<Integer> a1Mastered;
    private LiveData<Integer> a1Percent;


    //TODO : in case where studying all words, or less than 5 are not studying, replace with studying=1 words for full 20

    @Inject
    public VocabListViewModel(Repository repository, FlashcardRepository flashcardRepository){
       this.mRepository = repository;
       this.flashcardRepository = flashcardRepository;

       mVocabListItems = mRepository.getVocabListItems();
       a1Max = mRepository.getA1Max();
       a1Learned = mRepository.getA1Learned();
       a1Mastered = mRepository.getA1Mastered();
       a1Percent = mRepository.getA1Percent();
       //A1DownloadedText = mRepository.getA1DownloadedText();
    }

    /**
     * Calls the download A1 AsyncTask to download the data from the remote database on
     * a background thread
     */
    public void downloadA1(){
        mRepository.downloadA1();
    }

    public void addSource(){
        flashcardRepository.addSource();
    }


    public LiveData<List<VocabListItem>> getVocabListItems(){
        return mVocabListItems;
    }

    public LiveData<Integer> getA1Max(){return a1Max;}
    public LiveData<Integer> getA1Learned(){return a1Learned;}
    public LiveData<Integer> getA1Mastered(){return a1Mastered;}



    public void deleteAllFromDatabase(){
        mRepository.deleteAll();
    }

    public LiveData<Integer> getA1Downloaded() {return mRepository.getA1Downloaded();}

    //public LiveData<String> getA1DownloadedText() { return A1DownloadedText; }
    public LiveData<String> getA1DownloadedText() { return mRepository.getA1DownloadedText(); }
    public LiveData<Integer> getDownloadProgressVisibility() {
        return mRepository.getDownloadProgressVisibility();
    }
    public LiveData<Integer> getWordsLearnedVisibility() {
        return mRepository.getWordsLearnedVisibility();
    }
    public LiveData<Integer> getDownloadButtonVisibility() {
        return mRepository.getDownloadButtonVisibility();
    }

    public LiveData<Integer> getA1WordsDownloadedVisibility(){
        return mRepository.getWordsDownloadedVisibility();
    }
    public LiveData<Integer> getA1ErrorDownloadingVisibility(){
        return mRepository.getErrorDownloadingVisibility();
    }
}
