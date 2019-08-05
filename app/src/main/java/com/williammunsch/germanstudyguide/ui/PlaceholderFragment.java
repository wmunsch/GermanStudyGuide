package com.williammunsch.germanstudyguide.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.williammunsch.germanstudyguide.DBManager;
import com.williammunsch.germanstudyguide.R;
import com.williammunsch.germanstudyguide.RecyclerViewAdapter;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private PageViewModel pageViewModel;
    DBManager dbManager;

    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<Integer> mProgressLearnedPercents = new ArrayList<>();
    private ArrayList<Integer> mProgressMasteredPercents = new ArrayList<>();
    private ArrayList<Integer> mWordsLearned= new ArrayList<>();
    private ArrayList<Integer> mWordsMax = new ArrayList<>();
    private ArrayList<String> mImages = new ArrayList<>();
    private ArrayList<Integer> mWordsMastered = new ArrayList<>();

    public static PlaceholderFragment newInstance(int index) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    /*
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageViewModel = ViewModelProviders.of(this).get(PageViewModel.class);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        pageViewModel.setIndex(index);
    }
    */

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);
        //final RecyclerView recyclerView = root.findViewById(R.id.recycler_view);
         dbManager = new DBManager(getActivity());

        final RecyclerView recyclerView = root.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        System.out.println("MNAMES AT 1 = " + mNames.get(1));
        initRecyclerViewData();
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(mNames, mProgressLearnedPercents, mProgressMasteredPercents, mWordsLearned, mWordsMastered, mWordsMax, mImages,getContext());
        recyclerView.setAdapter(adapter);

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


    private void initRecyclerViewData(){
        mImages.add("A1");
        mImages.add("A2");
        mImages.add("B1");
        mImages.add("B2");
        mImages.add("C1");
        mImages.add("C2");

        mNames.add("Beginner Level 1");
        mNames.add("Beginner Level 2");
        mNames.add("Intermediate Level 1");
        mNames.add("Intermediate Level 2");
        mNames.add("Advanced Level 1");
        mNames.add("Advanced Level 2");

        //SQL Query
        int a1Max = dbManager.getWordsMax("A1");
        int a1Learned = dbManager.getWordsLearned("A1");
        int a1Mastered = dbManager.getWordsMastered("A1");

        //Filling data with SQL Query
        mWordsLearned.add(a1Learned);
        mWordsLearned.add(0);
        mWordsLearned.add(0);
        mWordsLearned.add(0);
        mWordsLearned.add(0);
        mWordsLearned.add(0);

        mWordsMax.add(a1Max);
        mWordsMax.add(0);
        mWordsMax.add(0);
        mWordsMax.add(0);
        mWordsMax.add(0);
        mWordsMax.add(0);

        mWordsMastered.add(a1Mastered);
        mWordsMastered.add(0);
        mWordsMastered.add(0);
        mWordsMastered.add(0);
        mWordsMastered.add(0);
        mWordsMastered.add(0);

        mProgressLearnedPercents.add((int)(((double)a1Learned/a1Max)*100));
        mProgressLearnedPercents.add(0);
        mProgressLearnedPercents.add(0);
        mProgressLearnedPercents.add(0);
        mProgressLearnedPercents.add(0);
        mProgressLearnedPercents.add(0);

        mProgressMasteredPercents.add((int)(((double)a1Mastered/a1Max)*100));
        mProgressMasteredPercents.add(0);
        mProgressMasteredPercents.add(0);
        mProgressMasteredPercents.add(0);
        mProgressMasteredPercents.add(0);
        mProgressMasteredPercents.add(0);

        //initRecyclerView();
    }
}