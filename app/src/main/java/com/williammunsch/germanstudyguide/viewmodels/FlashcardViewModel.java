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

import java.util.List;
import java.util.Queue;

import javax.inject.Inject;

public class FlashcardViewModel extends ViewModel {
    private MutableLiveData<List<VocabModel>> wordList; //shouldnt be takign a list of Word objects, but rather the queue.
    private FlashcardRepository mFlashcardRepository;
    private LiveData<Queue<VocabModel>> wordQueue;
    //private LiveData<List<Word>> wordList;

    @Inject
    public FlashcardViewModel(FlashcardRepository flashcardRepository) {
        this.mFlashcardRepository = flashcardRepository;
       // wordList = mRepository.getA1VocabFlashcards();
        wordQueue = mFlashcardRepository.getA1Queue();
    }




   // public LiveData<List<VocabModel>> getWordList(){
  //     return wordList;
  //  }
//
    public LiveData<Queue<VocabModel>> getWordQueue(){return wordQueue;}
}
