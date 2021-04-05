package com.williammunsch.germanstudyguide.ui;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.williammunsch.germanstudyguide.GermanApp;
import com.williammunsch.germanstudyguide.R;
import com.williammunsch.germanstudyguide.adapters.GrammarRecyclerViewAdapter;
import com.williammunsch.germanstudyguide.recyclerviewviewmodels.GrammarListViewModel;
import com.williammunsch.germanstudyguide.recyclerviewviewmodels.StoriesListViewModel;
import com.williammunsch.germanstudyguide.viewmodelhelpers.ViewModelFactory;

import javax.inject.Inject;

/**
 * Fragment for the grammar page recycler view
 */
public class GrammarFragment extends Fragment{
    private static final String ARG_SECTION_NUMBER = "section_number";
    private GrammarRecyclerViewAdapter mAdapter;
    private GrammarListViewModel grammarListViewModel;

    @Inject
    ViewModelFactory viewModelFactory;

    public static GrammarFragment newInstance(int index) {
        GrammarFragment fragment = new GrammarFragment();
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
        grammarListViewModel = new ViewModelProvider(this,viewModelFactory).get(GrammarListViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);

        grammarListViewModel.getGenderDownloadFirstVisibility().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                mAdapter.setGenderTextVisibility(integer);
            }
        });
        grammarListViewModel.getGenderButtonVisibility().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                mAdapter.setGenderButtonVisibility(integer);
            }
        });


        final RecyclerView recyclerView = root.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        mAdapter = new GrammarRecyclerViewAdapter(getContext());
        recyclerView.setAdapter(mAdapter);

        try {
            recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        }catch (NullPointerException e){
            //throw new Error
        }

        return root;
    }
}
