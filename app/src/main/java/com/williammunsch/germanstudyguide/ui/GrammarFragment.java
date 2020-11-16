package com.williammunsch.germanstudyguide.ui;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.williammunsch.germanstudyguide.GermanApp;
import com.williammunsch.germanstudyguide.R;
import com.williammunsch.germanstudyguide.adapters.GrammarRecyclerViewAdapter;
import com.williammunsch.germanstudyguide.adapters.RecyclerViewAdapter;
import com.williammunsch.germanstudyguide.datamodels.VocabListItem;
import com.williammunsch.germanstudyguide.viewmodels.NounGenderViewModel;
import com.williammunsch.germanstudyguide.viewmodels.ViewModelFactory;
import com.williammunsch.germanstudyguide.viewmodels.VocabListViewModel;

import java.util.List;

import javax.inject.Inject;

/**
 * Fragment for the grammar page recycler view
 */
public class GrammarFragment extends Fragment{
    private static final String ARG_SECTION_NUMBER = "section_number";
    private GrammarRecyclerViewAdapter mAdapter;
    //private NounGenderViewModel nounGenderViewModel;
   // @Inject
    //ViewModelFactory viewModelFactory;

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

       // nounGenderViewModel = ViewModelProviders.of(this,viewModelFactory).get(NounGenderViewModel.class);

        //if (viewModelFactory == null){System.out.println("NULL FACTORY IN VOCABFRAGMENT");}
       // else{ System.out.println("NONNULL FACTORY IN VOCABFRAGMENT"); }

       // mVocabListViewModel = ViewModelProviders.of(this,viewModelFactory).get(VocabListViewModel.class);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);

/*
        mVocabListViewModel.getVocabListItems().observe(this, new Observer<List<VocabListItem>>(){

            @Override
            public void onChanged(List<VocabListItem> vocabListItems) {
                mAdapter.setVocabList(vocabListItems);
                //Below not needed, just for testing
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
        */
        final RecyclerView recyclerView = root.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        //  mAdapter = new RecyclerViewAdapter(getContext(), mVocabListViewModel.getVocabListItems().getValue());
        // mAdapter = new RecyclerViewAdapter(getContext(), mVocabListViewModel.getVocabListItems().getValue()); //should this be observed instead?
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
