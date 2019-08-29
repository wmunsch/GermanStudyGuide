package com.williammunsch.germanstudyguide;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import androidx.appcompat.widget.SearchView;
import com.williammunsch.germanstudyguide.adapters.RecyclerViewFilterAdapter;
import com.williammunsch.germanstudyguide.datamodels.SimpleWord;
import com.williammunsch.germanstudyguide.viewmodels.ViewAllViewModel;
import java.util.List;

public class ViewActivity extends AppCompatActivity {
    RecyclerViewFilterAdapter adapter3;
    private ViewAllViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        Intent bIntent = getIntent();
        String tableName =bIntent.getStringExtra("table");
        Toolbar myToolbar = findViewById(R.id.toolbar_search);
        setSupportActionBar(myToolbar);

        try {
            getSupportActionBar().setTitle(tableName+" Word List");
        }catch(NullPointerException e){
            throw new Error("Null icon");
        }

        myToolbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        viewModel = ViewModelProviders.of(this).get(ViewAllViewModel.class);
        viewModel.init();//retrieve data from repository

        viewModel.getSimpleWordListItems().observe(this, new Observer<List<SimpleWord>>() {
            @Override
            public void onChanged(@Nullable List<SimpleWord> wordList) {
               adapter3.notifyDataSetChanged();
            }
        });

        RecyclerView recyclerView2 = findViewById(R.id.recycler_list);
        adapter3 = new RecyclerViewFilterAdapter(viewModel.getSimpleWordListItems().getValue());
        recyclerView2.setAdapter(adapter3);
        recyclerView2.setLayoutManager(new LinearLayoutManager(this));
        recyclerView2.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
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
