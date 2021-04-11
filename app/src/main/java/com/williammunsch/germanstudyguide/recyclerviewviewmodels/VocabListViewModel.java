package com.williammunsch.germanstudyguide.recyclerviewviewmodels;

import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.williammunsch.germanstudyguide.datamodels.VocabListItem;
import com.williammunsch.germanstudyguide.repositories.Repository;

import java.util.List;

import javax.inject.Inject;

/**
 * The view model for the vocab list fragment in MainActivity.
 * It displays a recycler view which contains different levels
 * of vocabulary, which when clicked on open a new FlashcardActivity.
 */
public class VocabListViewModel extends ViewModel {

    private final Repository mRepository;//injected

    private final LiveData<List<VocabListItem>> mVocabListItems;
    private final LiveData<Integer> a1Max;
    private final LiveData<Integer> a1Learned;
    private final LiveData<Integer> a1Mastered;

    private final MutableLiveData<Integer> downloadProgressVisibility = new MutableLiveData<>(View.GONE);
    private final MutableLiveData<Integer> downloadButtonVisibility = new MutableLiveData<>(View.VISIBLE);


    @Inject
    public VocabListViewModel(Repository repository){
       this.mRepository = repository;
       mVocabListItems = mRepository.getAllVocabLists();
       a1Max = mRepository.getA1Max();
       a1Learned = mRepository.getA1Learned();
       a1Mastered = mRepository.getA1Mastered();

        //Check if the ROOM tables for the lessons and story lists for the recyclerviews have been created
       repository.checkLessons();

        //Check if vocab lessons and stories are fully downloaded from the remote database.
       repository.checkA1();

    }

    /**
     * Calls the download A1 AsyncTask to download the data from the remote database on
     * a background thread
     */
    public void downloadA1(){
        mRepository.downloadA1(downloadButtonVisibility);
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

    public LiveData<String> getA1DownloadedText() { return mRepository.getA1DownloadedText(); }

    public LiveData<Integer> getDownloadProgressVisibility() {
        return downloadProgressVisibility;
    }
    public LiveData<Integer> getWordsLearnedVisibility() {
        return mRepository.getWordsLearnedVisibility();
    }
    public LiveData<Integer> getDownloadButtonVisibility() {
        return downloadButtonVisibility;
    }

    public LiveData<Integer> getA1WordsDownloadedVisibility(){
        return mRepository.getWordsDownloadedVisibility();
    }
    public LiveData<Integer> getA1ErrorDownloadingVisibility(){
        return mRepository.getErrorDownloadingVisibility();
    }
}
