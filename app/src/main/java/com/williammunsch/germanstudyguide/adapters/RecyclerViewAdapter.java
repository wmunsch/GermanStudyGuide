package com.williammunsch.germanstudyguide.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.williammunsch.germanstudyguide.DBManager;
import com.williammunsch.germanstudyguide.FlashcardActivity;
import com.williammunsch.germanstudyguide.R;
import com.williammunsch.germanstudyguide.ViewActivity;
import com.williammunsch.germanstudyguide.datamodels.VocabListItem;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{
    private List<VocabListItem> mVocabList;
    private Context mContext;

    public RecyclerViewAdapter(Context mContext, List<VocabListItem> mVocabList){
        this.mVocabList = mVocabList;
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
        viewHolder.listItemName.setText(mVocabList.get(i).getName());
        viewHolder.image.setText(mVocabList.get(i).getImage());
        viewHolder.progressBar.setSecondaryProgress(mVocabList.get(i).getLearnedPercent());
        viewHolder.progressBar.setProgress(mVocabList.get(i).getMasteredPercent());

        if ((int)mVocabList.get(i).getLearnedPercent()==100){viewHolder.wordsLearned.setText("Words mastered : " + mVocabList.get(i).getWordsMastered() + "/" + mVocabList.get(i).getWordsMax());}
        else{ viewHolder.wordsLearned.setText("Words learned : " + mVocabList.get(i).getWordsLearned() + "/" + mVocabList.get(i).getWordsMax()); }

        if (i==0 || i ==1){
            viewHolder.image.setBackground(ResourcesCompat.getDrawable(mContext.getResources(),R.drawable.a1circle,null));
        }else if (i==2 || i==3){
            viewHolder.image.setBackground(ResourcesCompat.getDrawable(mContext.getResources(),R.drawable.bcircle,null));
        }else if (i==4||i==5){
            viewHolder.image.setBackground(ResourcesCompat.getDrawable(mContext.getResources(),R.drawable.ccircle,null));
        }

        if ((int)mVocabList.get(i).getLearnedPercent()==100){viewHolder.learnButton.setText("Study");}
        if ((int)mVocabList.get(i).getMasteredPercent()==100){viewHolder.learnButton.setText("Review");}


        viewHolder.viewButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                System.out.println("Clicked on viewbutton");

                if (viewHolder.getAdapterPosition()==0){
                    Intent intent = new Intent(mContext, ViewActivity.class);
                    intent.putExtra("table", "A1");
                    mContext.startActivity(intent);
                }else if (viewHolder.getAdapterPosition()==1){
                    Intent intent = new Intent(mContext, ViewActivity.class);
                    intent.putExtra("table", "A2");
                    mContext.startActivity(intent);
                }
            }
        });

        viewHolder.learnButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                System.out.println("Clicked on learnbutton");

               // if ((int)wordsLearned.get(i)==(int)wordsMax.get(i)){viewHolder.learnButton.setText("Study");}



                if (viewHolder.getAdapterPosition()==0){
                    Intent intent = new Intent(mContext, FlashcardActivity.class);
                    intent.putExtra("table", "A1");
                    mContext.startActivity(intent);
                }else if (viewHolder.getAdapterPosition()==1){
                    Intent intent = new Intent(mContext, FlashcardActivity.class);
                    intent.putExtra("table", "A2");
                    mContext.startActivity(intent);
                }
            }
        });


        //On click, expand the layout by increase vertical size and showing buttons
        viewHolder.parentLayout.setOnClickListener(new View.OnClickListener(){
                  @Override
            public void onClick(View view){
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
        return mVocabList.size();
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
        private Button viewButton, learnButton;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.imageView);
            listItemName = itemView.findViewById(R.id.item_name_textview);
            progressBar = itemView.findViewById(R.id.determinateBar);
            parentLayout = itemView.findViewById(R.id.parent_layout);
            buttonLayout = itemView.findViewById(R.id.buttonLayout);
            wordsLearned = itemView.findViewById(R.id.wordsLearnedTV);
            viewButton = itemView.findViewById(R.id.viewButton);
            learnButton = itemView.findViewById(R.id.learnButton);
        }
    }
}