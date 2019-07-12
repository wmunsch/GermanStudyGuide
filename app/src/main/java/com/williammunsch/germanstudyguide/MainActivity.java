package com.williammunsch.germanstudyguide;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    //variables
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<Integer> mProgressLearnedPercents = new ArrayList<>();
    private ArrayList<Integer> mProgressMasteredPercents = new ArrayList<>();
    private ArrayList<Integer> mWordsLearned= new ArrayList<>();
    private ArrayList<Integer> mWordsMax = new ArrayList<>();
    private ArrayList<String> mImageUrls = new ArrayList<>();
    DBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: started.");
        dbManager = new DBManager(this);

        //tests to see if DB was copied (only happens once per install)
        try {
            System.out.println("creating database");
            dbManager.createDatabase();
        }catch(IOException e){
            throw new Error("Unable to create database");
        }


        initRecyclerViewData();
    }

    private void initRecyclerViewData(){

        mNames.add("Beginner Level 1");
        mNames.add("Beginner Level 2");
        mNames.add("Intermediate Level 1");
        mNames.add("Intermediate Level 2");
        mNames.add("Advanced Level 1");
        mNames.add("Advanced Level 2");

        //SQL Query
        int a1Max = dbManager.getWordsMax("A1");
        int a1Learned = dbManager.getWordsLearned("A1");
        int a1Mastered = dbManager.getWordsMastered("A1");

        //Filling data with SQL Query
        mWordsLearned.add(a1Learned);
        mWordsLearned.add(0);
        mWordsLearned.add(0);
        mWordsLearned.add(0);
        mWordsLearned.add(0);
        mWordsLearned.add(0);

        mWordsMax.add(a1Max);
        mWordsMax.add(0);
        mWordsMax.add(0);
        mWordsMax.add(0);
        mWordsMax.add(0);
        mWordsMax.add(0);

        mProgressLearnedPercents.add((int)(((double)a1Learned/a1Max)*100));
        mProgressLearnedPercents.add(0);
        mProgressLearnedPercents.add(0);
        mProgressLearnedPercents.add(0);
        mProgressLearnedPercents.add(0);
        mProgressLearnedPercents.add(0);

        mProgressMasteredPercents.add((int)(((double)a1Mastered/a1Max)*100));
        mProgressMasteredPercents.add(0);
        mProgressMasteredPercents.add(0);
        mProgressMasteredPercents.add(0);
        mProgressMasteredPercents.add(0);
        mProgressMasteredPercents.add(0);

        initRecyclerView();
    }

    private void initRecyclerView(){
        Log.d(TAG,"initRecyclerView: init recyclerview");
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(mNames, mProgressLearnedPercents, mProgressMasteredPercents, mWordsLearned, mWordsMax,this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }


}
