package com.williammunsch.germanstudyguide;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.v7.widget.SearchView;

import java.util.ArrayList;

public class ViewActivity extends AppCompatActivity {
    private ArrayList<String> germWordList = new ArrayList<>();
    private ArrayList<String> engWordList = new ArrayList<>();
    private ArrayList<Integer> scoreList = new ArrayList<>();
    private ArrayList<SimpleWord> wordList = new ArrayList<>();
    RecyclerViewFilterAdapter adapter3;
    DBManager dbManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        dbManager = new DBManager(this);
        Intent bIntent = getIntent();
        String tableName =bIntent.getStringExtra("table");
        Toolbar myToolbar = findViewById(R.id.toolbar_search);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle(tableName+" Word List");



        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       // getSupportActionBar().setDisplayShowHomeEnabled(true);

        myToolbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        System.out.println("tableNAme string = " +tableName);

        wordList = dbManager.getSimpleWordList(tableName);

        //germWordList = dbManager.getGermanList(tableName);
        //engWordList = dbManager.getEnglishList(tableName);
        //scoreList = dbManager.getScoreList(tableName);

        //TextView tv = findViewById(R.id.tableNameLabel);
       // tv.setText(tableName + " Word List");

        RecyclerView recyclerView2 = findViewById(R.id.recycler_list);
        //adapter2 = new RecyclerViewFilterAdapter(this, germWordList, engWordList, scoreList);
        adapter3 = new RecyclerViewFilterAdapter(wordList);
        recyclerView2.setAdapter(adapter3);
        recyclerView2.setLayoutManager(new LinearLayoutManager(this));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.search_menu,menu);
        MenuItem mSearch = menu.findItem(R.id.action_search);
        SearchView mSearchView = (SearchView) mSearch.getActionView();
        mSearchView.setQueryHint("Filter...");
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
            @Override
            public boolean onQueryTextSubmit(String query){
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText){
                adapter3.getFilter().filter(newText);
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_search){
            return true;
        }
       // if (item.getItemId() == android.R.id.home){
            //finish();
        //}
        return true;
    }



}
