package com.williammunsch.germanstudyguide.viewmodels;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.annotation.NonNull;
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
