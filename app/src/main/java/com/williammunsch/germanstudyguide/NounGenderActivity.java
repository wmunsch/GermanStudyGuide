package com.williammunsch.germanstudyguide;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.williammunsch.germanstudyguide.databinding.ActivityNoungenderBinding;
import com.williammunsch.germanstudyguide.activitiesviewmodels.NounGenderViewModel;
import com.williammunsch.germanstudyguide.viewmodelhelpers.ViewModelFactory;

import javax.inject.Inject;

/**
 * Activity for the noun gender lessons
 */
public class NounGenderActivity extends AppCompatActivity {

    @Inject
    ViewModelFactory viewModelFactory;

    NounGenderViewModel nounGenderViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityNoungenderBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_noungender);
        binding.setLifecycleOwner(this);
        ((GermanApp) getApplicationContext()).getAppComponent().inject(this); //Need to add the inject method in AppComponent for this to work.
        nounGenderViewModel = ViewModelProviders.of(this,viewModelFactory).get(NounGenderViewModel.class);
        binding.setNoungenderviewmodel(nounGenderViewModel);
    }
}
