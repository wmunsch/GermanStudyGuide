package com.williammunsch.germanstudyguide.viewmodels;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.williammunsch.germanstudyguide.datamodels.VocabModel;
import com.williammunsch.germanstudyguide.datamodels.Word;
import com.williammunsch.germanstudyguide.repositories.FlashcardRepository;
import com.williammunsch.germanstudyguide.repositories.Repository;
import com.williammunsch.germanstudyguide.repositories.WordRepository;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import javax.inject.Inject;

public class FlashcardViewModel extends ViewModel {
    private FlashcardRepository mFlashcardRepository;
    private LiveData<List<VocabModel>> vocabList;
    private MediatorLiveData<List<VocabModel>> mediatorVocabList = new MediatorLiveData<>();

    //private MutableLiveData<List<VocabModel>> mutableVocabList;


    /**
     * Keeps track of the order of flashcards in the live data here,
     * moves the index positions around and removes them when necessary.
     */
    @Inject
    public FlashcardViewModel(FlashcardRepository flashcardRepository) {
        this.mFlashcardRepository = flashcardRepository;

       vocabList = mFlashcardRepository.getVocabData();

       //Livedata does not get updated when removing a node from mediatorlivedata, until the activity is recreated ?
       mediatorVocabList.addSource(vocabList, value -> mediatorVocabList.setValue(value));
        /* Using lambda for simplicity instead of the code below
        mediatorVocabList.addSource(vocabList, new Observer<List<VocabModel>>() {
            @Override
            public void onChanged(List<VocabModel> vocabModels) {
                mediatorVocabList.setValue(vocabModels);
            }
        });
*/



        /* use this to update the scores?
        vocabList = Transformations.switchMap(
                mediatorVocabList,
                value -> mFlashcardRepository.getVocabData()
        );
        */

    }
/*
    public MediatorLiveData<List<VocabModel>> getMediatorVocabList(){
        //return vocabList2;
        return mediatorVocabList;
    }
*/

    /**
     *
     * @return The MediatorLiveData as LiveData so it cant be changed from the activity
     */
    public LiveData<List<VocabModel>> getMediatorVocabList(){
        //return vocabList2;
        return mediatorVocabList;
    }

    public LiveData<List<VocabModel>> getVocabList(){
        return vocabList;
    }




    /**
     * Removes the top item from the mediatorLiveData list
     */
    public void popNode(){
        List<VocabModel> list = mediatorVocabList.getValue();
        try {
            list.remove(0);
        }catch(NullPointerException e){
            System.out.println("List is null");
        }
        mediatorVocabList.setValue(list);
    }

    /**
     * Moves the item in the mediatorLiveData list down a few indexes
     */
    public void moveNode(){
        List<VocabModel> list = mediatorVocabList.getValue();


        mediatorVocabList.setValue(list);
    }





}
