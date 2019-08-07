package com.williammunsch.germanstudyguide.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.williammunsch.germanstudyguide.R;
import com.williammunsch.germanstudyguide.datamodels.SimpleWord;

import java.util.ArrayList;
import java.util.List;

/**
 * RecyclerView adapter with filtering capabilities,
 * used to display a list of all the words in the current table.
 */

public class RecyclerViewFilterAdapter extends RecyclerView.Adapter<RecyclerViewFilterAdapter.ViewHolder> implements Filterable {
    public List<SimpleWord> wordList, filteredList;

    public RecyclerViewFilterAdapter(List<SimpleWord> simpleList) {
        this.wordList=simpleList;
        this.filteredList=this.wordList;
        getFilter();
    }

    @NonNull
    @Override
    public RecyclerViewFilterAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.viewlist_layout, viewGroup, false);
        RecyclerViewFilterAdapter.ViewHolder holder = new RecyclerViewFilterAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewFilterAdapter.ViewHolder viewHolder, int i) {
        viewHolder.germWord.setText(filteredList.get(i).getGerman());
        viewHolder.engWord.setText(filteredList.get(i).getEnglish());
        viewHolder.score.setText(filteredList.get(i).getScore());
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    @Override
    public Filter getFilter(){
        return new Filter(){
            @Override
            protected FilterResults performFiltering(CharSequence charSequence){
                FilterResults results = new FilterResults();

                if(charSequence == null || charSequence.length() == 0) {
                    results.values = wordList;
                    results.count = wordList.size();
                }
                else {
                    ArrayList<SimpleWord> filterResultsData = new ArrayList<>();
                    for(SimpleWord word : wordList) {
                        //Loop through every SimpleWord object to see if either meaning starts with the entered string
                        if(word.getGerman().toLowerCase().contains(charSequence.toString().toLowerCase()) ||
                            word.getEnglish().toLowerCase().contains(charSequence.toString().toLowerCase())) {
                            filterResultsData.add(word);
                        }
                    }

                    results.values = filterResultsData;
                    results.count = filterResultsData.size();
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredList = (ArrayList<SimpleWord>)filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


    /**
     * Holds each widget in memory for it to be recycled.
     */
    public class ViewHolder extends RecyclerView.ViewHolder{

       TextView germWord, engWord, score;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            germWord = itemView.findViewById(R.id.tvgerman);
            engWord = itemView.findViewById(R.id.tvenglish);
            score = itemView.findViewById(R.id.tvscore);
        }

    }
}
