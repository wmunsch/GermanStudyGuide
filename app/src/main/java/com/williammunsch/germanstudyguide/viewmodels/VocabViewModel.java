package com.williammunsch.germanstudyguide.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.williammunsch.germanstudyguide.datamodels.VocabListItem;
import com.williammunsch.germanstudyguide.repositories.VocabRecyclerViewRepository;

import java.util.List;

/**
 * View model for the vocabulary recyclerView in mainActivity.
 */

public class VocabViewModel extends ViewModel {

    private MutableLiveData<List<VocabListItem>> mVocabListItems;
    private VocabRecyclerViewRepository mRepo;

    public void init(){
        if (mVocabListItems != null){
            return;
        }
        //Storing all data in the repository in this viewModel
        mRepo = VocabRecyclerViewRepository.getInstance();
        mVocabListItems = mRepo.getVocabListItems();
    }

    public LiveData<List<VocabListItem>> getVocabListItems(){
        return mVocabListItems;
    }
}
