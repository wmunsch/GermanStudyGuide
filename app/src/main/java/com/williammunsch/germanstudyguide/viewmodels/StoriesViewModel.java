package com.williammunsch.germanstudyguide.viewmodels;


import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import com.williammunsch.germanstudyguide.datamodels.StoriesListItem;
import com.williammunsch.germanstudyguide.repositories.StoriesRepository;

import java.util.List;

/**
 * View model for the stories recyclerView in mainActivity.
 */
public class StoriesViewModel extends AndroidViewModel {

    private MutableLiveData<List<StoriesListItem>> mStoriesListItems;
    private StoriesRepository mRepo;

    public StoriesViewModel(@NonNull Application application) {
        super(application);
    }

    public void init(){
        if (mStoriesListItems != null){
            return;
        }
        //Storing all data in the repository in this viewModel
        mRepo = StoriesRepository.getInstance();
        mStoriesListItems = mRepo.getStoriesListItems(getApplication().getApplicationContext());
    }

    public LiveData<List<StoriesListItem>> getStoriesListItems(){
        return mStoriesListItems;
    }
}
