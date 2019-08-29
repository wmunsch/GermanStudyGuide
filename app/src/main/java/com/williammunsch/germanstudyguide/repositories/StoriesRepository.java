package com.williammunsch.germanstudyguide.repositories;

import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;
import android.content.Context;
import androidx.core.content.res.ResourcesCompat;

import com.williammunsch.germanstudyguide.datamodels.StoriesListItem;

import java.util.ArrayList;
import java.util.List;


public class StoriesRepository {
    private static StoriesRepository instance;
    private ArrayList<StoriesListItem> dataSet = new ArrayList<>();

    public static StoriesRepository getInstance(){
        if (instance ==null){
            instance = new StoriesRepository();
        }
        return instance;
    }

    //Get data from a web service or online source
    public MutableLiveData<List<StoriesListItem>> getStoriesListItems(Context context){
        setStoriesListItems(context);
        MutableLiveData<List<StoriesListItem>> data = new MutableLiveData<>();
        data.setValue(dataSet);
        return data;
    }

    private void setStoriesListItems(Context context){
        //retrieve data from database here
        //MAKE SURE TO USE ASYNC TASK FOR DATABASE QUERIES IN FUTURE

        /*
        DBManager db = DBManager.getInstance(context);
        dataSet = db.getStories();

        for (int i = 0; i < dataSet.size(); i++){
            if (dataSet.get(i).getId()==1){
                dataSet.get(i).setIcon(ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_frog,null));
            }else if (dataSet.get(i).getId()==2){
                dataSet.get(i).setIcon(ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_hansel,null));
            }
        }

*/
    }
}
