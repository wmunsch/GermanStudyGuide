package com.williammunsch.germanstudyguide.viewmodels;

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
    private LiveData<Integer> a1Percent;
   // private LiveData<Integer> wordCount;

    //TODO : in case where studying all words, or less than 5 are not studying, replace with studying=1 words for full 20

    @Inject
    public VocabListViewModel(Repository repository, FlashcardRepository flashcardRepository){
       this.mRepository = repository;
        this.flashcardRepository = flashcardRepository;

       mVocabListItems = mRepository.getVocabListItems();
       a1Max = mRepository.getA1Max();
       a1Learned = mRepository.getA1Learned();
       a1Percent = mRepository.getA1Percent();




     //  wordCount = mRepository.count();

      //  mAllVocab = mRepository.getAll();//this is the entire vocabmodel table from the ROOM database.

    }

   // public LiveData<Integer> getA1Max() {return a1Max;}


    public void addSource(){
        flashcardRepository.addSource();
    }

    /**
     * Add a "getter" method for all the words. This completely hides the implementation from the UI.
     */
    public LiveData<List<VocabModelA1>> getAllVocab(){
        return mAllVocab;
    }


    public LiveData<List<VocabListItem>> getVocabListItems(){
        return mVocabListItems;
    }

    public LiveData<Integer> getA1Max(){return a1Max;}
    public LiveData<Integer> getA1Learned(){return a1Learned;}

    //public LiveData<Integer> getA1Count(){ return wordCount;}

    /**
     * wrapper insert() method that calls the Repository's insert() method. In this way, the implementation of insert() is completely hidden from the UI.
     */
    public void insert(VocabModelA1 vocabModelA1) {mRepository.insert(vocabModelA1);}

  //  public Integer count() {return mRepository.count();}

    public void deleteAllFromDatabase(){
        mRepository.deleteAll();
    }

}
