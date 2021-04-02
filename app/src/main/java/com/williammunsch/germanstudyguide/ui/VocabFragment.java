package com.williammunsch.germanstudyguide.ui;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.williammunsch.germanstudyguide.GermanApp;
import com.williammunsch.germanstudyguide.R;
import com.williammunsch.germanstudyguide.activitiesviewmodels.MainActivityViewModel;
import com.williammunsch.germanstudyguide.adapters.RecyclerViewAdapter;
import com.williammunsch.germanstudyguide.databinding.ActivityMainBinding;
import com.williammunsch.germanstudyguide.datamodels.VocabListItem;
import com.williammunsch.germanstudyguide.viewmodelhelpers.ViewModelFactory;
import com.williammunsch.germanstudyguide.recyclerviewviewmodels.VocabListViewModel;

import java.util.List;

import javax.inject.Inject;

/**
 * Fragment for the vocabulary recyclerView which is the first page.
 */
public class VocabFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private VocabListViewModel mVocabListViewModel;
    private MainActivityViewModel mainActivityViewModel;
    private RecyclerViewAdapter mAdapter;
    private int a1Max;
    @Inject
    ViewModelFactory viewModelFactory;



    public static VocabFragment newInstance(int index) {
        VocabFragment fragment = new VocabFragment();
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
        //mVocabListViewModel = ViewModelProviders.of(this,viewModelFactory).get(VocabListViewModel.class);
        mVocabListViewModel = new ViewModelProvider(this,viewModelFactory).get(VocabListViewModel.class);
        mainActivityViewModel = new ViewModelProvider(this, viewModelFactory).get(MainActivityViewModel.class);
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);

        mVocabListViewModel.getVocabListItems().observe(getViewLifecycleOwner(), new Observer<List<VocabListItem>>(){

            @Override
            public void onChanged(List<VocabListItem> vocabListItems) {
                mAdapter.setVocabList(vocabListItems);
            }
        });


        mVocabListViewModel.getA1Max().observe(getViewLifecycleOwner(), new Observer<Integer>(){
            @Override
            public void onChanged(Integer num) {
                mAdapter.setA1Max(num);
            }
        });

        mVocabListViewModel.getA1Learned().observe(getViewLifecycleOwner(), new Observer<Integer>(){
            @Override
            public void onChanged(Integer num) {
                mAdapter.setA1Learned(num);
            }
        });

        mVocabListViewModel.getA1Mastered().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer num) {
                mAdapter.setA1Mastered(num);
            }
        });
        mVocabListViewModel.getA1Downloaded().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer num) {
                mAdapter.setA1Downloaded(num);
            }
        });
        mVocabListViewModel.getA1DownloadedText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                mAdapter.setA1ButtonText(s);
            }
        });
        mVocabListViewModel.getWordsLearnedVisibility().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                mAdapter.setWordsLearnedVisibility(integer);
            }
        });
        mVocabListViewModel.getDownloadButtonVisibility().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                mAdapter.setDownloadButtonVisibility(integer);
            }
        });
        mVocabListViewModel.getA1WordsDownloadedVisibility().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                mAdapter.setA1WordsDownloadedVisibility(integer);
            }
        });
        mVocabListViewModel.getA1ErrorDownloadingVisibility().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                mAdapter.setA1ErrorDownloadingVisibility(integer);
            }
        });


        final RecyclerView recyclerView = root.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

      //  mAdapter = new RecyclerViewAdapter(getContext(), mVocabListViewModel.getVocabListItems().getValue());
       // mAdapter = new RecyclerViewAdapter(getContext(), mVocabListViewModel.getVocabListItems().getValue()); //should this be observed instead?
        mAdapter = new RecyclerViewAdapter(getContext(), mVocabListViewModel);
        recyclerView.setAdapter(mAdapter);

        try {
            recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        }catch (NullPointerException e){
            //throw new Error
        }

        return root;
    }
}