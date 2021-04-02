package com.williammunsch.germanstudyguide.recyclerviewviewmodels;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

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
        mStoriesListItems = repository.getStoriesListItems();
    }

    /**
     * Calls the downloadHAG AsyncTask to download the data from the remote database on
     * a background thread
     */
    public void downloadHAG(){
        repository.downloadHAG();
    }

    public LiveData<List<StoriesListItem>> getStoriesListItems(){
        return mStoriesListItems;
    }
    public LiveData<String> getHAGDownloadedText(){
        return repository.getHAGDownloadedText();
    }
    public LiveData<Integer> getHAGDownloaded(){
        return repository.getHAGDownloaded();
    }
    public LiveData<Integer> getHagPartsDownloaded() {return repository.getHAGPartsDownloaded();}
    public LiveData<Integer> getHagWordsDownloaded() {return repository.getHAGWordsDownloaded();}
    public LiveData<Integer> getHagErrorVisibility() {
        return repository.getHagErrorVisibility();
    }
    public LiveData<Integer> getHagButtonVisibility() {
        return repository.getHagButtonVisibility();
    }
    public LiveData<Integer> getHagPartsDownloadedVisibility() {
        return repository.getHagPartsDownloadedVisibility();
    }
}
