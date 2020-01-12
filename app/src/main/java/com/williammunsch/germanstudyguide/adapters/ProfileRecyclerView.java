package com.williammunsch.germanstudyguide.adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.williammunsch.germanstudyguide.R;

public class ProfileRecyclerView  extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    public ProfileRecyclerView(){

    }


    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    /**
     * Holds each widget in memory for it to be recycled.
     */
    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView itemName, itemValue;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
           // image = itemView.findViewById(R.id.imageView);
        }
    }
}
