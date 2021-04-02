package com.williammunsch.germanstudyguide;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.williammunsch.germanstudyguide.databinding.ActivityReadBinding;
import com.williammunsch.germanstudyguide.activitiesviewmodels.StoryViewModel;
import com.williammunsch.germanstudyguide.viewmodelhelpers.ViewModelFactory;

import javax.inject.Inject;

/**
 * The activity for displaying the short stories and fairy tales.
 */
public class StoryActivity extends AppCompatActivity {

    @Inject
    ViewModelFactory viewModelFactory;

    StoryViewModel storyViewModel;//Need a reference to the viewmodel for databinding.

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityReadBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_read);
        binding.setLifecycleOwner(this);
        // setSupportActionBar(binding.toolbar);
        ((GermanApp) getApplicationContext()).getAppComponent().inject(this); //Need to add the inject method in AppComponent for this to work.
        storyViewModel = ViewModelProviders.of(this,viewModelFactory).get(StoryViewModel.class);
        binding.setStoryviewmodel(storyViewModel);

        Intent bIntent = getIntent();
        String storyName = bIntent.getStringExtra("story");
        storyViewModel.setStoryName(storyName);
    }
}
