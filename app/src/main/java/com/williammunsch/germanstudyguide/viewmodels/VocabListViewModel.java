package com.williammunsch.germanstudyguide.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.williammunsch.germanstudyguide.datamodels.VocabListItem;
import com.williammunsch.germanstudyguide.datamodels.VocabModel;
import com.williammunsch.germanstudyguide.repositories.FlashcardRepository;
import com.williammunsch.germanstudyguide.repositories.Repository;

import java.util.List;

import javax.inject.Inject;

/**
 * Never pass context into a viewmodel. Application context is ok as is used here.
 */
public class VocabListViewModel extends ViewModel {
    private Repository mRepository;//injected
    private FlashcardRepository flashcardRepository;

    private LiveData<List<VocabModel>> mAllVocab;
    private LiveData<List<VocabListItem>> mVocabListItems;
    private LiveData<Integer> a1Max;
   // private LiveData<Integer> wordCount;

    //TODO : in case where studying all words, or less than 5 are not studying, replace with studying words for full 20

    @Inject
    public VocabListViewModel(Repository repository, FlashcardRepository flashcardRepository){
        this.mRepository = repository;
        this.flashcardRepository = flashcardRepository;

        mVocabListItems = mRepository.getVocabListItems();
        a1Max = mRepository.getA1Max();
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
    public LiveData<List<VocabModel>> getAllVocab(){
        return mAllVocab;
    }


    public LiveData<List<VocabListItem>> getVocabListItems(){
        return mVocabListItems;
    }

    public LiveData<Integer> getA1Max(){return a1Max;}

    //public LiveData<Integer> getA1Count(){ return wordCount;}

    /**
     * wrapper insert() method that calls the Repository's insert() method. In this way, the implementation of insert() is completely hidden from the UI.
     */
    public void insert(VocabModel vocabModel) {mRepository.insert(vocabModel);}

  //  public Integer count() {return mRepository.count();}

    public void deleteAllFromDatabase(){
        mRepository.deleteAll();
    }

}
