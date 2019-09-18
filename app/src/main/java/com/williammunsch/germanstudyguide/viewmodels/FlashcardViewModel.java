package com.williammunsch.germanstudyguide.viewmodels;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
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
    private final MutableLiveData<String> mutableVocabList = new MutableLiveData<>();

    //List used to keep the position of each flashcard in the queue,
    //so the LiveData doesn't have to be changed,
    // just which index is being shown in the live data.
    private ArrayList<Integer> flashcardOrderList = new ArrayList<>();


    private LiveData<LinkedList<VocabModel>> wordQueue;
    private LiveData<List<VocabModel>> vocabQueue;


    /**
     * Keep track of the order of flashcards in the live data here, move the index
     * positions around and remove them here.
     */
    @Inject
    public FlashcardViewModel(FlashcardRepository flashcardRepository) {
        this.mFlashcardRepository = flashcardRepository;


       vocabList = mFlashcardRepository.getVocabData();

        for (int i = 0; i < 5;i++){
            flashcardOrderList.add(i);
        }

        vocabList = Transformations.switchMap(
                mutableVocabList,
                value -> mFlashcardRepository.getVocabData()
        );
    }

    public ArrayList<Integer> getFlashcardOrderList(){
        return flashcardOrderList;
    }



    public void removeNode(int i){
      //  flashcardOrderList.remove(i);
      // mutableVocabList.getValue(

    }


    public LiveData<List<VocabModel>> getVocabData(){
        return vocabList;
    }

    /*
    public void removeFlashcard(int i){
       mFlashcardRepository.removeFlashcard(i);
   }
*/
}
