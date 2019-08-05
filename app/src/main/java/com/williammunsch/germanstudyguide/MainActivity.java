package com.williammunsch.germanstudyguide;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.williammunsch.germanstudyguide.ui.SectionsPagerAdapter;

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
    private ArrayList<String> mImages = new ArrayList<>();
    private ArrayList<Integer> mWordsMastered = new ArrayList<>();
    DBManager dbManager;

    ViewPager viewPager;
    BottomNavigationView navView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        try {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setIcon(R.mipmap.ic_launcher);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }catch(NullPointerException e){
            throw new Error("Null icon");
        }

        navView = findViewById(R.id.nav_view);

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);



        BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
                = new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        viewPager.setCurrentItem(0);
                        return true;
                    case R.id.navigation_dashboard:
                        viewPager.setCurrentItem(1);
                        return true;
                    case R.id.navigation_notifications:
                        viewPager.setCurrentItem(2);
                        return true;
                }
                return false;
            }
        };

        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener(){

            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                navView.getMenu().getItem(i).setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });



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
        mImages.add("A1");
        mImages.add("A2");
        mImages.add("B1");
        mImages.add("B2");
        mImages.add("C1");
        mImages.add("C2");

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

        mWordsMastered.add(a1Mastered);
        mWordsMastered.add(0);
        mWordsMastered.add(0);
        mWordsMastered.add(0);
        mWordsMastered.add(0);
        mWordsMastered.add(0);

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

        //initRecyclerView();
    }
/*
    private void initRecyclerView(){
        Log.d(TAG,"initRecyclerView: init recyclerview");
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(mNames, mProgressLearnedPercents, mProgressMasteredPercents, mWordsLearned, mWordsMastered, mWordsMax, mImages,this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

    }
*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings){
           // startSettings();
            return true;
        }
        return true;
    }


}
