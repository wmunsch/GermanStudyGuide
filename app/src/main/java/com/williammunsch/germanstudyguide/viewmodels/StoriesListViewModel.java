package com.williammunsch.germanstudyguide.viewmodels;


import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.annotation.NonNull;

import com.williammunsch.germanstudyguide.datamodels.StoriesListItem;
import com.williammunsch.germanstudyguide.repositories.Repository;
import com.williammunsch.germanstudyguide.repositories.StoriesRepository;

import java.util.List;

import javax.inject.Inject;

/**
 * View model for the stories recyclerView in mainActivity.
 */
public class StoriesListViewModel extends ViewModel {

    private LiveData<List<StoriesListItem>> mStoriesListItems;
    private StoriesRepository storiesRepository;
    private Repository repository;

    @Inject
    public StoriesListViewModel(Repository repository) {
        this.repository = repository;
        //this.storiesRepository = storiesRepository;
        mStoriesListItems = repository.getStoriesListItems();
    }


    public LiveData<List<StoriesListItem>> getStoriesListItems(){
        return mStoriesListItems;
    }
}
