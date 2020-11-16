package com.williammunsch.germanstudyguide.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.williammunsch.germanstudyguide.FlashcardActivity;
import com.williammunsch.germanstudyguide.NounGenderActivity;
import com.williammunsch.germanstudyguide.R;
import com.williammunsch.germanstudyguide.repositories.NounGenderRepository;
import com.williammunsch.germanstudyguide.viewmodels.NounGenderViewModel;

import javax.inject.Inject;

public class GrammarRecyclerViewAdapter  extends RecyclerView.Adapter<GrammarRecyclerViewAdapter.ViewHolder> {
    private Context mContext;
   // private NounGenderViewModel nounGenderViewModel;


    public GrammarRecyclerViewAdapter(Context mContext){
        this.mContext = mContext;
        //this.nounGenderViewModel = nounGenderViewModel;
    }


    @NonNull
    @Override
    public GrammarRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grammaritem_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GrammarRecyclerViewAdapter.ViewHolder viewHolder, int position) {
        //On click, expand the layout by increase vertical size and showing buttons
        viewHolder.parentLayout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                //TODO : set minimum height and isExpanded when clicking on other ones
                if (!viewHolder.isExpanded){
                    viewHolder.parentLayout.setMinimumHeight(450);
                    viewHolder.buttonLayout.setVisibility(View.VISIBLE);
                    //viewHolder.buttonLayout.startAnimation(slideDown);
                    viewHolder.isExpanded=true;}
                else{
                    viewHolder.parentLayout.setMinimumHeight(100);
                    viewHolder.buttonLayout.setVisibility(View.GONE);
                    viewHolder.isExpanded=false;}
            }
        });

        viewHolder.learnButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                System.out.println("Clicked on button");

                if (viewHolder.getAdapterPosition()==0){
                   // System.out.println("Adding source from recyclerviewadaper88888888888");
                   // vocabListViewModel.addSource();
                   // nounGenderViewModel.addSource();
                    Intent intent = new Intent(mContext, NounGenderActivity.class);
                    //intent.putExtra("table", "A1");
                    mContext.startActivity(intent);
                }
                /*else if (viewHolder.getAdapterPosition()==1){

                    Intent intent = new Intent(mContext, FlashcardActivity.class);
                    intent.putExtra("table", "A2");
                    mContext.startActivity(intent);
                }*/
            }
        });
    }

    @Override
    public int getItemCount() {
        return 1;
    }


    /**
     * Holds each widget in memory for it to be recycled.
     */
    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView listItemName, wordsLearned, image;
        private ProgressBar progressBar;
        private RelativeLayout parentLayout;
        boolean isExpanded = false;
        private LinearLayout buttonLayout;
        private Button  learnButton;//,learnButton2;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.imageView);
            listItemName = itemView.findViewById(R.id.item_name_textview);
            parentLayout = itemView.findViewById(R.id.parent_layout);
            buttonLayout = itemView.findViewById(R.id.buttonLayout);
            learnButton = itemView.findViewById(R.id.learnButton);
            //learnButton = itemView.findViewById(R.id.learnButton2);
        }
    }
}
