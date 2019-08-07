package com.williammunsch.germanstudyguide.viewmodels;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.williammunsch.germanstudyguide.datamodels.StoriesListItem;
import com.williammunsch.germanstudyguide.repositories.StoriesRepository;

import java.util.List;

/**
 * View model for the stories recyclerView in mainActivity.
 */
public class StoriesViewModel extends ViewModel {

    private MutableLiveData<List<StoriesListItem>> mStoriesbListItems;
    private StoriesRepository mRepo;

    public void init(){
        if (mStoriesbListItems != null){
            return;
        }
        //Storing all data in the repository in this viewModel
        mRepo = StoriesRepository.getInstance();
        mStoriesbListItems = mRepo.getStoriesListItems();
    }

    public LiveData<List<StoriesListItem>> getStoriesListItems(){
        return mStoriesbListItems;
    }
}
