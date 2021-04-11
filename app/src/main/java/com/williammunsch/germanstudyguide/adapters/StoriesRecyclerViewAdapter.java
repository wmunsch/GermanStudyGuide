package com.williammunsch.germanstudyguide.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.williammunsch.germanstudyguide.R;
import com.williammunsch.germanstudyguide.StoryActivity;
import com.williammunsch.germanstudyguide.datamodels.StoriesListItem;
import com.williammunsch.germanstudyguide.recyclerviewviewmodels.StoriesListViewModel;

import java.util.List;

/**
 * Adapter for the stories list on the main activity page (3rd tab)
 */
public class StoriesRecyclerViewAdapter extends RecyclerView.Adapter<StoriesRecyclerViewAdapter.ViewHolder>{
    private List<StoriesListItem> mStoriesList;
    private final Context mContext;
    private final StoriesListViewModel storiesListViewModel;
    private Integer HAGDownloaded =0, HAGPartsDownloaded = 0,HAGWordsDownloaded =0,HAGErrorVisibility = View.GONE, HAGPartsDownloadedVisibility = View.GONE, HAGButtonVisibility = View.VISIBLE;
    private String downloadButtonText = "Download";

    public StoriesRecyclerViewAdapter(Context mContext, StoriesListViewModel storiesListViewModel){
        this.storiesListViewModel = storiesListViewModel;
        this.mContext = mContext;
    }


    @NonNull
    @Override
    //inflates the view and recycles views
    public StoriesRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.storyitem_layout, viewGroup, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final StoriesRecyclerViewAdapter.ViewHolder viewHolder, final int i) {
        viewHolder.titleTextView.setText(mStoriesList.get(i).getTitle());
        viewHolder.authorTextView.setText(mStoriesList.get(i).getAuthor());

        //Sets the image and parts downloaded text for each lesson
        if(i==0){
            viewHolder.icon.setBackground(ResourcesCompat.getDrawable(mContext.getResources(),R.drawable.ic_hanselundgretal,null));
            viewHolder.readButton.setText(downloadButtonText);
            int hagParts = HAGPartsDownloaded + HAGWordsDownloaded;
            viewHolder.partsDownloadedTextView.setText("Parts Downloaded: " + hagParts + "/919");//HAGPartsDownloaded + HAGWordsDownloaded);
        }

        viewHolder.readButton.setVisibility(HAGButtonVisibility);
        viewHolder.partsDownloadedTextView.setVisibility(HAGPartsDownloadedVisibility);
        viewHolder.errorDownloadingTextView.setVisibility(HAGErrorVisibility);

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

        viewHolder.readButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (viewHolder.getAdapterPosition()==0){
                    if (HAGDownloaded == 0){
                        storiesListViewModel.downloadHAG();
                    }
                    else{
                        Intent intent = new Intent(mContext, StoryActivity.class);
                        intent.putExtra("story", "hag");
                        mContext.startActivity(intent);
                    }

                }
                else if (viewHolder.getAdapterPosition()==1){
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        if (mStoriesList != null){
            return mStoriesList.size();
        }
        else{
            return 0;
        }
    }

    public void setStoriesList(List<StoriesListItem> storiesList){
        this.mStoriesList= storiesList;
        notifyDataSetChanged();
    }

    public void setDownloadButtonText(String s){
        this.downloadButtonText = s;
        notifyDataSetChanged();
    }

    public void setHAGDownloaded(Integer i){
        this.HAGDownloaded = i;
        notifyDataSetChanged();
    }

    public void setHAGPartsDownloaded(Integer i){
        this.HAGPartsDownloaded = i;
        notifyDataSetChanged();
    }
    public void setHAGWordsDownloaded(Integer i){
        this.HAGWordsDownloaded=i;
        notifyDataSetChanged();
    }
    public void setHAGErrorVisibility(Integer i){
        this.HAGErrorVisibility=i;
        notifyDataSetChanged();
    }
    public void setHAGPartsDownloadedVisibility(Integer i){
        this.HAGPartsDownloadedVisibility=i;
        notifyDataSetChanged();
    }
    public void setHAGButtonVisibility(Integer i){
        this.HAGButtonVisibility =i;
        notifyDataSetChanged();
    }


    /**
     * Holds each widget in memory for it to be recycled.
     */
    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView titleTextView, authorTextView, partsDownloadedTextView, errorDownloadingTextView;
        private ImageView icon;
        private RelativeLayout parentLayout;
        boolean isExpanded = false;
        private LinearLayout buttonLayout;
        private Button readButton;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.title_textview);
            authorTextView = itemView.findViewById(R.id.author_textview);
            icon = itemView.findViewById(R.id.story_imageView);
            parentLayout = itemView.findViewById(R.id.parentRelative_layout);
            buttonLayout = itemView.findViewById(R.id.buttonLayout2);
            readButton = itemView.findViewById(R.id.readButton);
            partsDownloadedTextView = itemView.findViewById(R.id.partsDownloadedTV);
            errorDownloadingTextView = itemView.findViewById(R.id.errorDownloadingTV);
        }
    }
}
