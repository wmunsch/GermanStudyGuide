package com.williammunsch.germanstudyguide.recyclerviewviewmodels;

import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.williammunsch.germanstudyguide.repositories.Repository;

import javax.inject.Inject;

/**
 * View model for the activities in the grammar tab of the main screen.
 */
public class GrammarListViewModel extends ViewModel {
    private final Repository repository;

   // private MutableLiveData<Integer> genderButtonVisibility = new MutableLiveData<>(View.VISIBLE);
   // private MutableLiveData<Integer> genderDownloadFirstVisibility = new MutableLiveData<>(View.VISIBLE);


    @Inject
    public GrammarListViewModel(Repository repository){
        this.repository = repository;
    }

    //Getters and setters
    public LiveData<Integer> getGenderButtonVisibility() {
        return repository.getGenderButtonVisibility();
    }
    public LiveData<Integer> getGenderDownloadFirstVisibility() {
        return repository.getGenderTextVisibility();
    }

}
