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

import com.williammunsch.germanstudyguide.DBManager;
import com.williammunsch.germanstudyguide.GermanApp;
import com.williammunsch.germanstudyguide.R;
import com.williammunsch.germanstudyguide.adapters.RecyclerViewAdapter;
import com.williammunsch.germanstudyguide.datamodels.VocabListItem;
import com.williammunsch.germanstudyguide.datamodels.VocabModel;
import com.williammunsch.germanstudyguide.di.AppComponent;
import com.williammunsch.germanstudyguide.viewmodels.ViewModelFactory;
import com.williammunsch.germanstudyguide.viewmodels.VocabViewModel;
import java.util.List;

import javax.inject.Inject;

/**
 * Fragment for the vocabulary recyclerView which is the first page.
 */
public class VocabFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private VocabViewModel mVocabViewModel;
    private RecyclerViewAdapter mAdapter;
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
       // mVocabViewModel = ViewModelProviders.of(this).get(VocabViewModel.class);
        //mVocabViewModel.init();//retrieve data from repository

        //AppComponent component = ((GermanApp)getApplicationContext()).getAppComponent();
        //component.inject(getContext());

        mVocabViewModel = ViewModelProviders.of(this,viewModelFactory).get(VocabViewModel.class);



        mVocabViewModel.getAllVocab().observe(this, new Observer<List<VocabModel>>(){

            @Override
            public void onChanged(List<VocabModel> vocabModels) {
                StringBuilder b = new StringBuilder();
                String all = "";
                for (int i = 0; i < vocabModels.size(); i++){
                    b.append(vocabModels.get(i).toString() + " \n");
                }
                all=b.toString();
                //mDataText.setText(all);
                mAdapter.notifyDataSetChanged();
            }
        });

    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);

         //dbManager = new DBManager(getActivity());

        final RecyclerView recyclerView = root.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mAdapter = new RecyclerViewAdapter(getContext(),mVocabViewModel.getVocabListItems().getValue());
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