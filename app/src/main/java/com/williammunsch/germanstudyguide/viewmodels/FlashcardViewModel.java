package com.williammunsch.germanstudyguide.viewmodels;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.williammunsch.germanstudyguide.datamodels.VocabModel;
import com.williammunsch.germanstudyguide.datamodels.Word;
import com.williammunsch.germanstudyguide.repositories.FlashcardRepository;
import com.williammunsch.germanstudyguide.repositories.Repository;
import com.williammunsch.germanstudyguide.repositories.WordRepository;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import javax.inject.Inject;

public class FlashcardViewModel extends ViewModel {
    private LiveData<List<VocabModel>> modelList; //shouldnt be takign a list of Word objects, but rather the queue.
    private FlashcardRepository mFlashcardRepository;
    private LiveData<List<VocabModel>> vocabList;

    //List used to keep the position of each flashcard in the queue,
    //so the LiveData doesn't have to be changed,
    // just which index is being shown in the live data.
    private ArrayList<Integer> flashcardOrderList = new ArrayList<>();


    private LiveData<LinkedList<VocabModel>> wordQueue;
    private LiveData<List<VocabModel>> vocabQueue;
    //private LiveData<List<Word>> wordList;

    /**
     * Keep track of the order of flashcards in the live data here, move the index
     * positions around and remove them here.
     */
    @Inject
    public FlashcardViewModel(FlashcardRepository flashcardRepository) {
        this.mFlashcardRepository = flashcardRepository;
       // wordList = mRepository.getA1VocabFlashcards();
       // wordQueue = mFlashcardRepository.getModelQueue();

       modelList = mFlashcardRepository.getModelList();
       vocabList = mFlashcardRepository.getVocabData();
       // this.vocabQueue = mFlashcardRepository.getVocabQueue();

        for (int i = 0; i < 20;i++){
            flashcardOrderList.add(i);
        }
    }




   // public LiveData<List<VocabModel>> getWordList(){
  //     return wordList;
  //  }
//

    public LiveData<List<VocabModel>> getVocabData(){
        return vocabList;
    }

    public void removeFlashcard(int i){
       mFlashcardRepository.removeFlashcard(i);
   }
    public LiveData<List<VocabModel>> getVocabQueue(){return vocabQueue;}
    public LiveData<LinkedList<VocabModel>> getWordQueue(){return wordQueue;}
    public LiveData<List<VocabModel>> getModelList(){return modelList;}
    //public LiveData<Queue<VocabModel>> getWordQueue(){return wordQueue;}
}
