package com.williammunsch.germanstudyguide.ui;

import androidx.lifecycle.Observer;
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
import com.williammunsch.germanstudyguide.adapters.RecyclerViewAdapter;
import com.williammunsch.germanstudyguide.datamodels.VocabListItem;
import com.williammunsch.germanstudyguide.datamodels.VocabModel;
import com.williammunsch.germanstudyguide.viewmodels.ViewModelFactory;
import com.williammunsch.germanstudyguide.viewmodels.VocabListViewModel;

import java.util.List;

import javax.inject.Inject;

/**
 * Fragment for the vocabulary recyclerView which is the first page.
 */
public class VocabFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private VocabListViewModel mVocabListViewModel;
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
       // mVocabListViewModel = ViewModelProviders.of(this).get(VocabListViewModel.class);
        //mVocabListViewModel.init();//retrieve data from repository

        //AppComponent component = ((GermanApp)getApplicationContext()).getAppComponent();
        //component.inject(getContext());

        mVocabListViewModel = ViewModelProviders.of(this,viewModelFactory).get(VocabListViewModel.class);

/*
        mVocabListViewModel.getA1Max().observe(this, new Observer<Integer>(){

            @Override
            public void onChanged(Integer a1WordsMax) {
                a1Max = a1WordsMax;
                System.out.println("ONCHANGED " + a1Max + " " + a1WordsMax); //it works here
                mAdapter.notifyDataSetChanged();
            }
        });
*/

        mVocabListViewModel.getVocabListItems().observe(this, new Observer<List<VocabListItem>>(){

            @Override
            public void onChanged(List<VocabListItem> vocabListItems) {
                mAdapter.setVocabList(vocabListItems);
                StringBuilder b = new StringBuilder();
                String all = "";
                for (int i = 0; i < vocabListItems.size(); i++){
                    b.append(vocabListItems.get(i).toString() + " \n");
                }
                all=b.toString();
                System.out.println("OBSERVING");
                System.out.println(all);
                //mDataText.setText(all);
                //mAdapter.notifyDataSetChanged();
            }
        });


        mVocabListViewModel.getA1Max().observe(this, new Observer<Integer>(){
            @Override
            public void onChanged(Integer num) {
                mAdapter.setA1Max(num);
            }
        });

    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);

         //dbManager = new DBManager(getActivity());

        final RecyclerView recyclerView = root.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

      //  mAdapter = new RecyclerViewAdapter(getContext(), mVocabListViewModel.getVocabListItems().getValue());
       // mAdapter = new RecyclerViewAdapter(getContext(), mVocabListViewModel.getVocabListItems().getValue()); //should this be observed instead?
        mAdapter = new RecyclerViewAdapter(getContext());
        recyclerView.setAdapter(mAdapter);

        try {
            recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        }catch (NullPointerException e){
            //throw new Error
        }

        return root;
    }
}