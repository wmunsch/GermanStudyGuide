package com.williammunsch.germanstudyguide;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{
    private static final String TAG = "RecyclerViewAdapter";

    private ArrayList<String> mListNames = new ArrayList<>();
    private Context mContext;
    private ArrayList<Integer> progressLearnedPercentage = new ArrayList<>();
    private ArrayList<Integer> progressMasteredPercentage = new ArrayList<>();

    public RecyclerViewAdapter(ArrayList<String> mImageNames, ArrayList<Integer> mProgressLearnedPercents, ArrayList<Integer> mProgressMasteredPercents, Context mContext) {
        this.mListNames = mImageNames;
        this.progressLearnedPercentage = mProgressLearnedPercents;
        this.progressMasteredPercentage = mProgressMasteredPercents;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    //inflates the view and recycles views
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.listitem_layout, viewGroup, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        Log.d(TAG, "onBindViewHolder: called.");

        viewHolder.listItemName.setText(mListNames.get(i));
        viewHolder.progressBar.setSecondaryProgress(progressLearnedPercentage.get(i));
        viewHolder.progressBar.setProgress(progressMasteredPercentage.get(i));

        //On click, expand the layout by increase vertical size and showing buttons
        viewHolder.parentLayout.setOnClickListener(new View.OnClickListener(){
                  @Override
            public void onClick(View view){
                      Log.d(TAG, "onClick: clicked on: " + mListNames.get(i));
                      if (!viewHolder.isExpanded){
                          viewHolder.parentLayout.setMinimumHeight(500);
                          viewHolder.buttonLayout.setVisibility(View.VISIBLE);
                          viewHolder.isExpanded=true;}
                      else{
                          viewHolder.parentLayout.setMinimumHeight(100);
                          viewHolder.buttonLayout.setVisibility(View.GONE);
                          viewHolder.isExpanded=false;}
                  }
        });
    }

    @Override
    public int getItemCount() {
        return mListNames.size();
    }


    /**
     * Holds each widget in memory for it to be recycled.
     */
    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView image;
        TextView listItemName;
        ProgressBar progressBar;
        RelativeLayout parentLayout;
        boolean isExpanded = false;
        LinearLayout buttonLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.imageView);
            listItemName = itemView.findViewById(R.id.item_name_textview);
            progressBar = itemView.findViewById(R.id.determinateBar);
            parentLayout = itemView.findViewById(R.id.parent_layout);
            buttonLayout = itemView.findViewById(R.id.buttonLayout);

        }

    }
}
