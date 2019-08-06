package com.williammunsch.germanstudyguide.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.williammunsch.germanstudyguide.datamodels.VocabListItem;
import com.williammunsch.germanstudyguide.viewmodels.VocabViewModel;

import java.util.List;

public class StoriesFragment extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";

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
        /*
        mVocabViewModel = ViewModelProviders.of(this).get(VocabViewModel.class);
        mVocabViewModel.init();//retrieve data from repository

        mVocabViewModel.getVocabListItems().observe(this, new Observer<List<VocabListItem>>() {
            @Override
            public void onChanged(@Nullable List<VocabListItem> vocabListItems) {
                mAdapter.notifyDataSetChanged();
            }
        });
        */
    }
}
