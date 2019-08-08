package com.williammunsch.germanstudyguide.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import com.williammunsch.germanstudyguide.datamodels.Word;
import com.williammunsch.germanstudyguide.repositories.WordRepository;

import java.util.List;

public class FlashcardViewModel extends AndroidViewModel {
    private MutableLiveData<List<Word>> wordList; //shouldnt be takign a list of Word objects, but rather the queue.
    private WordRepository mRepo;

    public FlashcardViewModel(@NonNull Application application) {
        super(application);
    }

    public void init(){
        if (wordList != null){
            return;
        }
        //Storing all data in the repository in this viewModel
        mRepo = WordRepository.getInstance();
        wordList = mRepo.getWordList(getApplication().getApplicationContext());
    }

    public LiveData<List<Word>> getWordList(){
        return wordList;
    }
}
