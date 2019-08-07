package com.williammunsch.germanstudyguide.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import com.williammunsch.germanstudyguide.datamodels.SimpleWord;
import com.williammunsch.germanstudyguide.repositories.SimpleWordRepository;

import java.util.List;

public class ViewAllViewModel extends AndroidViewModel {
    private MutableLiveData<List<SimpleWord>> simpleWordListItems;
    private SimpleWordRepository mRepo;

    public ViewAllViewModel(@NonNull Application application) {
        super(application);
    }

    public void init(){
        if (simpleWordListItems!= null){
            return;
        }
        //Storing all data in the repository in this viewModel
        mRepo = SimpleWordRepository.getInstance();
        simpleWordListItems = mRepo.getSimpleWordList(getApplication().getApplicationContext());
    }

    public LiveData<List<SimpleWord>> getSimpleWordListItems(){
        return simpleWordListItems;
    }
}
