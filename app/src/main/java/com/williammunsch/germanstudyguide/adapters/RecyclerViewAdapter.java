package com.williammunsch.germanstudyguide.adapters;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.williammunsch.germanstudyguide.FlashcardActivity;
import com.williammunsch.germanstudyguide.R;
import com.williammunsch.germanstudyguide.ViewActivity;
import com.williammunsch.germanstudyguide.datamodels.VocabListItem;
import com.williammunsch.germanstudyguide.viewmodels.VocabListViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{
    private List<VocabListItem> mVocabList;
    private Integer a1Max, a1Learned, a1Percent,a1Mastered, a1MasteredPercent;
    private Context mContext;
    private VocabListViewModel vocabListViewModel;
   // private int a1Max;

    public RecyclerViewAdapter(Context mContext, VocabListViewModel viewModel){
   // public RecyclerViewAdapter(Context mContext, List<VocabListItem> mVocabList){
       // this.mVocabList = mVocabList;
        this.mContext = mContext;
        this.vocabListViewModel = viewModel;
       // this.a1Max = a1Max;
        //System.out.println("In recyclerviewadapter constructor : " + this.a1Max + " " + a1Max);
       // mVocabList = new ArrayList<>();

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
        //viewHolder.progressBar.setSecondaryProgress(mVocabList.get(i).getLearnedPercent());
       // viewHolder.progressBar.setProgress(mVocabList.get(i).getMasteredPercent());
       // a1Percent = (int)(((double)a1Learned/a1Max)*100);
        //viewHolder.progressBar.setProgress((int)(((double)a1Learned/a1Max)*100)); //This sets the progressbar for each lesson on the main page
        viewHolder.progressBar.setProgress(a1MasteredPercent);
        viewHolder.progressBar.setSecondaryProgress(a1Percent);


        if (a1Learned < a1Max){
            viewHolder.wordsLearned.setText("Words learned : " + a1Learned + "/" + a1Max);
            viewHolder.learnButton.setText("Learn");
        }else{
            viewHolder.wordsLearned.setText("Words mastered: " + a1Mastered + "/" + a1Max);
            viewHolder.learnButton.setText("Study");
        }

       // if ((int)mVocabList.get(i).getLearnedPercent()==100){viewHolder.wordsLearned.setText("Words mastered : " + mVocabList.get(i).getWordsMastered() + "/" + a1Max);}
       //else{ viewHolder.wordsLearned.setText("Words learned : " + mVocabList.get(i).getWordsLearned() + "/" + a1Max); }
       // else{ viewHolder.wordsLearned.setText("Words learned : " + a1Learned + "/" + a1Max); }



        if (i==0 || i ==1){
           // System.out.println("words : " + a1Learned + "   /   " + a1Max );
            if (a1Learned<a1Max){viewHolder.image.setBackground(ResourcesCompat.getDrawable(mContext.getResources(),R.drawable.a1circle1,null));}//a1circle1
            else{
                if (a1Mastered<a1Max){viewHolder.image.setBackground(ResourcesCompat.getDrawable(mContext.getResources(),R.drawable.a1circle2,null));} //a1circle2
                else{viewHolder.image.setBackground(ResourcesCompat.getDrawable(mContext.getResources(),R.drawable.a1circle3,null));}//a1circle3
            }
        }else if (i==2 || i==3){
            //TODO create the circles for b1,b2 and the if statements here
            viewHolder.image.setBackground(ResourcesCompat.getDrawable(mContext.getResources(),R.drawable.a1circle3,null));
        }else if (i==4||i==5){
            //TODO create the circles for c1,c2 and the if statements here
            viewHolder.image.setBackground(ResourcesCompat.getDrawable(mContext.getResources(),R.drawable.ccircle,null));
        }

        //if ((int)mVocabList.get(i).getLearnedPercent()==100){viewHolder.learnButton.setText("Study");}
       // if ((int)mVocabList.get(i).getMasteredPercent()==100){viewHolder.learnButton.setText("Review");}


        viewHolder.viewButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                System.out.println("Clicked on viewbutton");


                if (viewHolder.getAdapterPosition()==0){
                    Intent intent = new Intent(mContext, ViewActivity.class);
                    intent.putExtra("table", "A1");
                    mContext.startActivity(intent);
                }else if (viewHolder.getAdapterPosition()==1){
                    vocabListViewModel.addSource();
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
                    System.out.println("Adding source from recyclerviewadaper88888888888");
                    vocabListViewModel.addSource();
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


       // final Animation slideDown = AnimationUtils.loadAnimation(mContext,R.anim.slide_down);
        //On click, expand the layout by increase vertical size and showing buttons
        viewHolder.parentLayout.setOnClickListener(new View.OnClickListener(){
                  @Override
            public void onClick(View view){
                      if (!viewHolder.isExpanded){
                          viewHolder.parentLayout.setMinimumHeight(500);
                          viewHolder.buttonLayout.setVisibility(View.VISIBLE);
                          //viewHolder.buttonLayout.startAnimation(slideDown);
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
        if (mVocabList != null){
            return mVocabList.size();
        }
        else{
            return 0;
        }
    }

    /**
     * This method allows the recyclerview to be updated with livedata in the fragment while observing
     */
    public void setVocabList(List<VocabListItem> vocabList){
        this.mVocabList = vocabList;
        notifyDataSetChanged();
    }

    public void setA1Max(Integer i){
        this.a1Max = i;
        notifyDataSetChanged();
    }

    public void setA1Learned(Integer i ){
        this.a1Learned = i;
        //TODO change the 14 back to 700
        a1Percent = (int)(((double)a1Learned/10)*100); //Sets the progress bar for the main page activities A1, etc.

        notifyDataSetChanged();
    }

    public void setA1Mastered(Integer i){
        this.a1Mastered = i;
        //TODO change the 14 back to 700
        a1MasteredPercent = (int)(((double)a1Mastered/10)*100); //Sets the progress bar for the main page activities A1, etc.
        notifyDataSetChanged();
    }

    public void setA1Percent(Integer i){
        this.a1Percent = i;
        notifyDataSetChanged();
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
