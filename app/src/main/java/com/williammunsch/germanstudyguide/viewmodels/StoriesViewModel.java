package com.williammunsch.germanstudyguide.viewmodels;


import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.annotation.NonNull;

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
