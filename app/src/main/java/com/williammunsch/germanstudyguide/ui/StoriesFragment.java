package com.williammunsch.germanstudyguide.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.williammunsch.germanstudyguide.R;
import com.williammunsch.germanstudyguide.adapters.RecyclerViewAdapter;
import com.williammunsch.germanstudyguide.adapters.StoriesRecyclerViewAdapter;
import com.williammunsch.germanstudyguide.datamodels.StoriesListItem;
import com.williammunsch.germanstudyguide.datamodels.VocabListItem;
import com.williammunsch.germanstudyguide.viewmodels.StoriesViewModel;
import com.williammunsch.germanstudyguide.viewmodels.VocabViewModel;

import java.util.List;

public class StoriesFragment extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";
    private StoriesViewModel storiesViewModel;
    private StoriesRecyclerViewAdapter mAdapter;

    public static StoriesFragment newInstance(int index) {
        StoriesFragment fragment = new StoriesFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Initializing the view model

        storiesViewModel = ViewModelProviders.of(this).get(StoriesViewModel.class);
        storiesViewModel.init();//retrieve data from repository

        storiesViewModel.getStoriesListItems().observe(this, new Observer<List<StoriesListItem>>() {
            @Override
            public void onChanged(@Nullable List<StoriesListItem> storiesListItems) {
                mAdapter.notifyDataSetChanged();
            }
        });

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);

        final RecyclerView recyclerView = root.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new StoriesRecyclerViewAdapter(getContext(),storiesViewModel.getStoriesListItems().getValue());
        recyclerView.setAdapter(mAdapter);

        try {
            recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        }catch (NullPointerException e){
            //throw new Error
        }

        /*
        final TextView textView = root.findViewById(R.id.section_label);
        pageViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        */
        return root;
    }
}
