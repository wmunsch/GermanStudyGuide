package com.williammunsch.germanstudyguide;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.williammunsch.germanstudyguide.databinding.ActivityNoungenderBinding;
import com.williammunsch.germanstudyguide.viewmodels.FlashcardViewModel;
import com.williammunsch.germanstudyguide.viewmodels.NounGenderViewModel;
import com.williammunsch.germanstudyguide.viewmodels.ViewModelFactory;

import javax.inject.Inject;

public class NounGenderActivity extends AppCompatActivity {

    @Inject
    ViewModelFactory viewModelFactory;

    NounGenderViewModel nounGenderViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityNoungenderBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_noungender);
        binding.setLifecycleOwner(this);
       // setSupportActionBar(binding.toolbar);
        ((GermanApp) getApplicationContext()).getAppComponent().inject(this); //Need to add the inject method in AppComponent for this to work.
        nounGenderViewModel = ViewModelProviders.of(this,viewModelFactory).get(NounGenderViewModel.class);
        binding.setNoungenderviewmodel(nounGenderViewModel);
    }
}
