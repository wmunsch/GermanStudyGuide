package com.williammunsch.germanstudyguide;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    //variables
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mImageUrls = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: started.");

        initImageBitmaps();
    }

    private void initImageBitmaps(){
        Log.d(TAG,"initImageBitmaps: preparing bitmaps.");

        mNames.add("Beginner Level 1");
        mNames.add("Beginner Level 2");
        mNames.add("Intermediate Level 1");
        mNames.add("Intermediate Level 2");
        mNames.add("Advanced Level 1");
        mNames.add("Advanced Level 2");

        initRecyclerView();
    }

    private void initRecyclerView(){
        Log.d(TAG,"initRecyclerView: init recyclerview");
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(mNames,this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
