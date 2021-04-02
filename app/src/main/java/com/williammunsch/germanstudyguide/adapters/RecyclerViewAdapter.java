package com.williammunsch.germanstudyguide.adapters;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.williammunsch.germanstudyguide.FlashcardActivity;
import com.williammunsch.germanstudyguide.R;
import com.williammunsch.germanstudyguide.activitiesviewmodels.MainActivityViewModel;
import com.williammunsch.germanstudyguide.datamodels.VocabListItem;
import com.williammunsch.germanstudyguide.recyclerviewviewmodels.VocabListViewModel;

import java.util.List;

/**
 * Adapter that holds the vocabulary tabs such as (A1) Beginner Level 1 and the experience bar
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{
    private List<VocabListItem> mVocabList;
    private Integer a1Max=0, a1Learned=0, a1Percent=0,a1Mastered=0, a1MasteredPercent=0,
            a1Downloaded = 0, a1WordsLearnedVisibility =View.GONE, a1DownloadButtonVisibility=View.VISIBLE,
            a1WordsDownloadedVisibility = View.GONE, a1ErrorDownloadingVisibility = View.GONE;
    private String a1ButtonText= "";
    private String a1DownloadText = "Words learned : ";
    private Context mContext;
    private VocabListViewModel vocabListViewModel;
    //private MainActivityViewModel mainActivityViewModel;
   // private int a1Max;

    public RecyclerViewAdapter(Context mContext, VocabListViewModel viewModel){
        this.mContext = mContext;
        this.vocabListViewModel = viewModel;
        //mainActivityViewModel = mainViewModel;
    }


    @NonNull
    @Override
    //inflates the view and recycles views
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.listitem_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        viewHolder.listItemName.setText(mVocabList.get(i).getName());
        viewHolder.image.setText(mVocabList.get(i).getImage());
        viewHolder.progressBar.setProgress(a1MasteredPercent);
        viewHolder.progressBar.setSecondaryProgress(a1Percent);
        viewHolder.wordsDownloaded.setText("Words Downloaded: " + a1Max + "/700");

        if (i==0){
            viewHolder.learnButton.setText(a1ButtonText);
            if (a1Learned < a1Max){
                viewHolder.wordsLearned.setText(a1DownloadText + a1Learned + "/" + a1Max);
                //viewHolder.learnButton.setText("Learn");
            }else{
                viewHolder.wordsLearned.setText("Words mastered: " + a1Mastered + "/" + a1Max);
                //viewHolder.learnButton.setText("Study");
            }

            viewHolder.learnButton.setVisibility(a1DownloadButtonVisibility);
            viewHolder.wordsLearned.setVisibility(a1WordsLearnedVisibility);
            viewHolder.wordsDownloaded.setVisibility(a1WordsDownloadedVisibility);
            viewHolder.errorDownloading.setVisibility(a1ErrorDownloadingVisibility);
        }



        if (i==0 || i ==1){
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

        viewHolder.learnButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (viewHolder.getAdapterPosition()==0){
                    //Either download the data from the remote database or start the activity
                    if (a1Downloaded == 0){
                        vocabListViewModel.downloadA1();
                    }
                    else{
                        vocabListViewModel.addSource();
                        Intent intent = new Intent(mContext, FlashcardActivity.class);
                        intent.putExtra("table", "A1");
                        mContext.startActivity(intent);
                    }



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
     * This function allows the recyclerview to be updated with livedata in the fragment while observing
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
        a1Percent = (int)(((double)a1Learned/700)*100); //Sets the progress bar for the main page activities A1, etc.

        notifyDataSetChanged();
    }

    public void setA1Mastered(Integer i){
        this.a1Mastered = i;
        //TODO change the 14 back to 700
        a1MasteredPercent = (int)(((double)a1Mastered/700)*100); //Sets the progress bar for the main page activities A1, etc.
        notifyDataSetChanged();
    }

    public void setA1Percent(Integer i){
        this.a1Percent = i;
        notifyDataSetChanged();
    }

    public void setA1Downloaded(Integer i){
        this.a1Downloaded = i;
        notifyDataSetChanged();
    }

    public void setA1ButtonText(String s){
        this.a1ButtonText = s;
        notifyDataSetChanged();
    }

    public void setWordsLearnedVisibility(Integer i){
        this.a1WordsLearnedVisibility = i;
        notifyDataSetChanged();
    }
    public void setDownloadButtonVisibility(Integer i){
        this.a1DownloadButtonVisibility = i;
        notifyDataSetChanged();
    }


    public void setA1DownloadText(String s){
        this.a1DownloadText = s;
        notifyDataSetChanged();
    }
    public void setA1WordsDownloadedVisibility(Integer i){
        this.a1WordsDownloadedVisibility = i;
        notifyDataSetChanged();
    }
    public void setA1ErrorDownloadingVisibility(Integer i){
        this.a1ErrorDownloadingVisibility = i;
        notifyDataSetChanged();
    }



    /**
     * Holds each widget in memory for it to be recycled.
     */
    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView listItemName, wordsLearned, image, wordsDownloaded, errorDownloading;
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
//            viewButton = itemView.findViewById(R.id.viewButton);
            learnButton = itemView.findViewById(R.id.learnButton);
            wordsDownloaded = itemView.findViewById(R.id.wordsDownloadedTV);
            errorDownloading = itemView.findViewById(R.id.errorDownloadingTV);
        }
    }
}
