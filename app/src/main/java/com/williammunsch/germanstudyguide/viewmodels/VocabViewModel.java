package com.williammunsch.germanstudyguide.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import com.williammunsch.germanstudyguide.datamodels.VocabListItem;
import com.williammunsch.germanstudyguide.repositories.VocabRecyclerViewRepository;

import java.util.List;

/**
 * View model for the vocabulary recyclerView in mainActivity.
 */

public class VocabViewModel extends AndroidViewModel {

    private MutableLiveData<List<VocabListItem>> mVocabListItems;
    private VocabRecyclerViewRepository mRepo;

    public VocabViewModel(@NonNull Application application) {
        super(application);
    }

    public void init(){
        if (mVocabListItems != null){
            return;
        }
        //Storing all data in the repository in this viewModel
        mRepo = VocabRecyclerViewRepository.getInstance();
        mVocabListItems = mRepo.getVocabListItems(getApplication().getApplicationContext());
    }

    public LiveData<List<VocabListItem>> getVocabListItems(){
        return mVocabListItems;
    }
}
