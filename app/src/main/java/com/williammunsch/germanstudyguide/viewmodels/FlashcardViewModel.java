package com.williammunsch.germanstudyguide.viewmodels;

import android.app.Application;
import android.net.TrafficStats;

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
   // private String currentNode = "heehehehe";
    private String currentNodeAnswer = "lalala";

    private LiveData<VocabModel> currentNode;// = new MutableLiveData<>();



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

        /**
         * Allows the textview to use databinding without having to call observe in the activity.
         * First argument is the livedata where the value is coming from.
         * Second argument (return value) is the object that the currentNode livedata needs to hold (a VocabModel in this case).
         */
        currentNode = Transformations.map(mediatorVocabList, value -> {
            if (mediatorVocabList.getValue() != null){
                return mediatorVocabList.getValue().get(0);
            }else{
                return null;//new VocabModel(0,null,null,null,null,0,0,0);
            }
        });
      // currentNode = Transformations.map(mediatorVocabList, value -> mediatorVocabList.getValue().get(0));



        /* use this to update the scores?
        vocabList = Transformations.switchMap(
                mediatorVocabList,
                value -> mFlashcardRepository.getVocabData()
        );
        */

    }



    public String getCurrentNodeAnswer(){
        return currentNodeAnswer;
    }



    public void setCurrentNode(){
      //  this.currentNode.setValue(mediatorVocabList.getValue().get(0));
    }

    public LiveData<VocabModel> getCurrentNode(){
        return currentNode;
    }

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
