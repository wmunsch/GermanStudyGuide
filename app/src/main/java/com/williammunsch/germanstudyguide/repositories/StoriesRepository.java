package com.williammunsch.germanstudyguide.repositories;

import android.arch.lifecycle.MutableLiveData;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.v4.content.res.ResourcesCompat;

import com.williammunsch.germanstudyguide.R;
import com.williammunsch.germanstudyguide.datamodels.StoriesListItem;
import com.williammunsch.germanstudyguide.datamodels.VocabListItem;

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
    public MutableLiveData<List<StoriesListItem>> getStoriesListItems(){
        setStoriesListItems();
        MutableLiveData<List<StoriesListItem>> data = new MutableLiveData<>();
        data.setValue(dataSet);
        return data;
    }

    private void setStoriesListItems(){
        //retrieve data from database here
        //MAKE SURE TO USE ASYNC TASK FOR DATABASE QUERIES IN FUTURE
        //dbManager = DBManager.getInstance();

        //Resources res = getResources();
        //Drawable drawable = res.getDrawable(R.drawable.ic_frog);

        //Need to add the image here as well
        dataSet.add(new StoriesListItem("Der Froschkönig oder der eiserne Heinrich","Brüder Grimm"));//, drawable));
        dataSet.add(new StoriesListItem("Hänsel und Gretel","Brüder Grimm"));//,ResourcesCompat.getDrawable(mContext.getResources(),R.drawable.ic_frog,null));));//, drawable));
    }
}
