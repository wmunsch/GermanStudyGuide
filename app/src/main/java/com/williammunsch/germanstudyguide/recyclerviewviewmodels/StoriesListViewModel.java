package com.williammunsch.germanstudyguide.recyclerviewviewmodels;


import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.williammunsch.germanstudyguide.datamodels.StoriesListItem;
import com.williammunsch.germanstudyguide.repositories.Repository;

import java.util.List;

import javax.inject.Inject;

/**
 * View model for the stories recyclerView in mainActivity.
 */
public class StoriesListViewModel extends ViewModel {

    private final LiveData<List<StoriesListItem>> mStoriesListItems;
    private final Repository repository;

    private final LiveData<String> HAGDownloadedText;
    private final MutableLiveData<Integer> HAGDownloaded = new MutableLiveData<>(0);

    private final MutableLiveData<Integer> hagErrorVisibility = new MutableLiveData<>(View.GONE);
    private final MutableLiveData<Integer> hagButtonVisibility = new MutableLiveData<>(View.VISIBLE);
    private final MutableLiveData<Integer> hagPartsDownloadedVisibility = new MutableLiveData<>(View.GONE);

    @Inject
    public StoriesListViewModel(Repository repository) {
        this.repository = repository;
        mStoriesListItems = repository.getStoriesListItems();

        repository.checkStories();
        repository.checkHAG(HAGDownloaded);

        //Change the text of the button in the HAG activity depending on whether it has been downloaded or not
        HAGDownloadedText = Transformations.map(HAGDownloaded, value->{
            if (HAGDownloaded.getValue()!=null && HAGDownloaded.getValue() == 0){
                return "Download";
            }
            else{
                return "Read";
            }
        });

    }

    /**
     * Calls the downloadHAG AsyncTask to download the data from the remote database on
     * a background thread
     */
    public void downloadHAG(){
        repository.downloadHAG(HAGDownloaded,hagErrorVisibility,hagButtonVisibility,hagPartsDownloadedVisibility);
    }

    public LiveData<List<StoriesListItem>> getStoriesListItems(){
        return mStoriesListItems;
    }

    public LiveData<Integer> getHagPartsDownloaded() {return repository.getHAGPartsDownloaded();}
    public LiveData<Integer> getHagWordsDownloaded() {return repository.getHAGWordsDownloaded();}

    public LiveData<Integer> getHagErrorVisibility() {
        return hagErrorVisibility;
    }
    public LiveData<Integer> getHagButtonVisibility() {
        return hagButtonVisibility;
    }
    public LiveData<Integer> getHagPartsDownloadedVisibility() {
        return hagPartsDownloadedVisibility;
    }
    public LiveData<String> getHAGDownloadedText() {
        return HAGDownloadedText;
    }
    public MutableLiveData<Integer> getHAGDownloaded() {
        return HAGDownloaded;
    }
}
