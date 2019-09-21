package com.williammunsch.germanstudyguide.viewmodels;

import android.app.Application;
import android.net.TrafficStats;

import androidx.databinding.Bindable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.transition.Visibility;

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

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class FlashcardViewModel extends ViewModel {
    private FlashcardRepository mFlashcardRepository;
    private LiveData<List<VocabModel>> vocabList;
    private MediatorLiveData<List<VocabModel>> mediatorVocabList = new MediatorLiveData<>();
   // private String currentNode = "heehehehe";
    private String currentNodeAnswer = "lalala";
   // private String hintVisibility = "invisible";
   // private Visibility hintVisibility;
    //private boolean mHintVisibility = true;

   // private MutableLiveData<Integer> hintVisibility = new MutableLiveData<>();

    private LiveData<VocabModel> currentNode;// = new MutableLiveData<>();


  //  private Integer hintVisibility = VISIBLE;
    //private String hintVisibility = "VISIBLE";
   // private LiveData<Integer> hintVisibility;

    //Keep track of hint sentence visibility in a LiveData for data binding.
    private MutableLiveData<Integer> mHintVisibility = new MutableLiveData<>();




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


        /*
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

        mHintVisibility.setValue(INVISIBLE);




        /* use this to update the scores?
        vocabList = Transformations.switchMap(
                mediatorVocabList,
                value -> mFlashcardRepository.getVocabData()
        );
        */

    }




    public LiveData<Integer> getHintVisibility(){
        return mHintVisibility;
    }

    public void showSentence(){
        if (mHintVisibility.getValue() == VISIBLE){mHintVisibility.setValue(INVISIBLE);}
        else{mHintVisibility.setValue(VISIBLE);}
    }

    public LiveData<List<VocabModel>> getMediatorVocabList(){
        return mediatorVocabList;
    }



    public String getCurrentNodeAnswer(){
        return currentNodeAnswer;
    }


    public LiveData<VocabModel> getCurrentNode(){
        return currentNode;
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


/*

   /* Using lambda for simplicity instead of the code below
        mediatorVocabList.addSource(vocabList, new Observer<List<VocabModel>>() {
            @Override
            public void onChanged(List<VocabModel> vocabModels) {
                mediatorVocabList.setValue(vocabModels);
            }
        });

    public void setCurrentNode(){
      //  this.currentNode.setValue(mediatorVocabList.getValue().get(0));
    }

        /**
     *
     * @return The MediatorLiveData as LiveData so it cant be changed from the activity

    public LiveData<List<VocabModel>> getMediatorVocabList(){
    //return vocabList2;
    return mediatorVocabList;
}

    public LiveData<List<VocabModel>> getVocabList(){
        return vocabList;
    }

 */
