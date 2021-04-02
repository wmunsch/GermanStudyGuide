package com.williammunsch.germanstudyguide.ui;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.williammunsch.germanstudyguide.GermanApp;
import com.williammunsch.germanstudyguide.R;
import com.williammunsch.germanstudyguide.adapters.StoriesRecyclerViewAdapter;
import com.williammunsch.germanstudyguide.datamodels.StoriesListItem;
import com.williammunsch.germanstudyguide.recyclerviewviewmodels.StoriesListViewModel;
import com.williammunsch.germanstudyguide.viewmodelhelpers.ViewModelFactory;

import java.util.List;

import javax.inject.Inject;

public class StoriesFragment extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";
    private StoriesListViewModel storiesListViewModel;
    private StoriesRecyclerViewAdapter mAdapter;

    @Inject
    ViewModelFactory viewModelFactory;

    public static StoriesFragment newInstance(int index) {
        StoriesFragment fragment = new StoriesFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        ((GermanApp) context.getApplicationContext()).getAppComponent().inject(this);
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Initializing the view model
        storiesListViewModel = ViewModelProviders.of(this,viewModelFactory).get(StoriesListViewModel.class);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);

        //Observe all of the LiveData objects so they will be updated in the view
        storiesListViewModel.getStoriesListItems().observe(getViewLifecycleOwner(), new Observer<List<StoriesListItem>>() {
            @Override
            public void onChanged(@Nullable List<StoriesListItem> storiesListItems) {
                mAdapter.setStoriesList(storiesListItems);
            }
        });
        storiesListViewModel.getHAGDownloadedText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                mAdapter.setDownloadButtonText(s);
            }
        });
        storiesListViewModel.getHAGDownloaded().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                mAdapter.setHAGDownloaded(integer);
            }
        });
        storiesListViewModel.getHagPartsDownloaded().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                mAdapter.setHAGPartsDownloaded(integer);
            }
        });
        storiesListViewModel.getHagWordsDownloaded().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                mAdapter.setHAGWordsDownloaded(integer);
            }
        });
        storiesListViewModel.getHagErrorVisibility().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                mAdapter.setHAGErrorVisibility(integer);
            }
        });
        storiesListViewModel.getHagPartsDownloadedVisibility().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                mAdapter.setHAGPartsDownloadedVisibility(integer);
            }
        });
        storiesListViewModel.getHagButtonVisibility().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                mAdapter.setHAGButtonVisibility(integer);
            }
        });

        final RecyclerView recyclerView = root.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mAdapter = new StoriesRecyclerViewAdapter(getContext(),storiesListViewModel);
        recyclerView.setAdapter(mAdapter);

        try {
            recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        }catch (NullPointerException e){
            //throw new Error
        }

        return root;
    }
}
