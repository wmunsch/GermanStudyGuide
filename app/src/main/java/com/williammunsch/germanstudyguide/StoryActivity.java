package com.williammunsch.germanstudyguide;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.williammunsch.germanstudyguide.databinding.ActivityNoungenderBinding;
import com.williammunsch.germanstudyguide.databinding.ActivityReadBinding;
import com.williammunsch.germanstudyguide.viewmodels.NounGenderViewModel;
import com.williammunsch.germanstudyguide.viewmodels.StoryViewModel;
import com.williammunsch.germanstudyguide.viewmodels.ViewModelFactory;

import javax.inject.Inject;

public class StoryActivity extends AppCompatActivity {

    @Inject
    ViewModelFactory viewModelFactory;

    StoryViewModel storyViewModel;//Need a reference to the viewmodel for databinding.

    private String storyName;

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
        storyName =bIntent.getStringExtra("story");
        storyViewModel.setStoryName(storyName);
    }
}
