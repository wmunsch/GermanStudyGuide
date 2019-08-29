package com.williammunsch.germanstudyguide.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.williammunsch.germanstudyguide.datamodels.VocabListItem;
import com.williammunsch.germanstudyguide.datamodels.VocabModel;
import com.williammunsch.germanstudyguide.repositories.Repository;

import java.util.List;

import javax.inject.Inject;

/**
 * Never pass context into a viewmodel. Application context is ok as is used here.
 */
public class VocabViewModel extends ViewModel {
    private Repository mRepository;//injected

    private LiveData<List<VocabModel>> mAllVocab;
    private MutableLiveData<List<VocabListItem>> mVocabListItems;

    @Inject
    public VocabViewModel(Repository repository){
        this.mRepository = repository;

        mVocabListItems = mRepository.getVocabListItems();

        mAllVocab = mRepository.getAll();//this is the entire vocabmodel table from the ROOM database.

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

    /**
     * wrapper insert() method that calls the Repository's insert() method. In this way, the implementation of insert() is completely hidden from the UI.
     */
    public void insert(VocabModel vocabModel) {mRepository.insert(vocabModel);}

    public Integer count() {return mRepository.count();}

    public void deleteAllFromDatabase(){
        mRepository.deleteAll();
    }

}
