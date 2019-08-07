package com.williammunsch.germanstudyguide.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.williammunsch.germanstudyguide.R;
import com.williammunsch.germanstudyguide.datamodels.StoriesListItem;

import java.util.List;

public class StoriesRecyclerViewAdapter extends RecyclerView.Adapter<StoriesRecyclerViewAdapter.ViewHolder>{
    private List<StoriesListItem> mStoriesList;
    private Context mContext;

    public StoriesRecyclerViewAdapter(Context mContext, List<StoriesListItem> storiesList){
        this.mStoriesList = storiesList;
        this.mContext = mContext;
    }




    @NonNull
    @Override
    //inflates the view and recycles views
    public StoriesRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.storyitem_layout, viewGroup, false);
        StoriesRecyclerViewAdapter.ViewHolder holder = new StoriesRecyclerViewAdapter.ViewHolder(view);
        return holder;
    }


    @Override
    public void onBindViewHolder(@NonNull final StoriesRecyclerViewAdapter.ViewHolder viewHolder, final int i) {
        viewHolder.titleTextView.setText(mStoriesList.get(i).getTitle());
        viewHolder.authorTextView.setText(mStoriesList.get(i).getAuthor());
        viewHolder.icon.setBackground(mStoriesList.get(i).getIcon());//ResourcesCompat.getDrawable(mContext.getResources(),R.drawable.ic_frog,null));//mStoriesList.get(i).getIcon());
        /*
        if (i==0){
            viewHolder.icon.setBackground(ResourcesCompat.getDrawable(mContext.getResources(),R.drawable.ic_frog,null));//setImageDrawable(mContext.getDrawable(R.drawable.ic_frog));
        }else if (i==1){
            viewHolder.icon.setBackground(ResourcesCompat.getDrawable(mContext.getResources(),R.drawable.ic_hansel,null));
        }
*/
    }

    @Override
    public int getItemCount() {
        return mStoriesList.size();
    }


    /**
     * Holds each widget in memory for it to be recycled.
     */
    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView titleTextView, authorTextView;
        private ImageView icon;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.title_textview);
            authorTextView = itemView.findViewById(R.id.author_textview);
            icon = itemView.findViewById(R.id.story_imageView);
        }
    }
}
