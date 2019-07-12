package com.williammunsch.germanstudyguide;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;

public class ViewActivity extends AppCompatActivity {
    private ArrayList<String> germWordList = new ArrayList<>();
    private ArrayList<String> engWordList = new ArrayList<>();
    private ArrayList<Integer> scoreList = new ArrayList<>();
    DBManager dbManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        dbManager = new DBManager(this);
        Intent bIntent = getIntent();
        String tableName =bIntent.getStringExtra("table");
        System.out.println("tableNAme string = " +tableName);

        germWordList = dbManager.getGermanList(tableName);
        engWordList = dbManager.getEnglishList(tableName);
        scoreList = dbManager.getScoreList(tableName);

        TextView tv = findViewById(R.id.tableNameLabel);
        tv.setText(tableName + " Word List");

        RecyclerView recyclerView2 = findViewById(R.id.recycler_list);
        RecyclerView2Adapter adapter2 = new RecyclerView2Adapter(this, germWordList, engWordList, scoreList);
        recyclerView2.setAdapter(adapter2);
        recyclerView2.setLayoutManager(new LinearLayoutManager(this));
    }

}
