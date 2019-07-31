package com.williammunsch.germanstudyguide;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class RecyclerView2Adapter extends RecyclerView.Adapter<RecyclerView2Adapter.ViewHolder> implements Filterable {
    public ArrayList<String> germWords, engWords,filteredData;
    private ArrayList<Integer> scores;

    public RecyclerView2Adapter(Context mContext, ArrayList<String> germ, ArrayList<String> eng, ArrayList<Integer> score) {
        this.germWords = germ;
        this.engWords = eng;
        this.scores = score;
    }

    @NonNull
    @Override
    public RecyclerView2Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.viewlist_layout, viewGroup, false);
        RecyclerView2Adapter.ViewHolder holder = new RecyclerView2Adapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView2Adapter.ViewHolder viewHolder, int i) {
        viewHolder.germWord.setText(germWords.get(i));
        viewHolder.engWord.setText(engWords.get(i));
        viewHolder.score.setText(scores.get(i).toString());
    }

    @Override
    public int getItemCount() {
        return germWords.size();
    }

    @Override
    public Filter getFilter(){
        return new Filter(){
            @Override
            protected FilterResults performFiltering(CharSequence charSequence){
                FilterResults results = new FilterResults();

                //If there's nothing to filter on, return the original data for your list
                if(charSequence == null || charSequence.length() == 0)
                {
                    results.values = germWords;
                    results.count = germWords.size();
                }
                else
                {
                    ArrayList<String> filterResultsData = new ArrayList<>();

                    for(String data : germWords)
                    {
                        //In this loop, you'll filter through originalData and compare each item to charSequence.
                        //If you find a match, add it to your new ArrayList
                        //I'm not sure how you're going to do comparison, so you'll need to fill out this conditional
                        if(data.toLowerCase().startsWith(charSequence.toString().toLowerCase()))
                        {
                            filterResultsData.add(data);
                        }
                    }

                    results.values = filterResultsData;
                    results.count = filterResultsData.size();
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults)
            {
                germWords = (ArrayList<String>)filterResults.values;
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
