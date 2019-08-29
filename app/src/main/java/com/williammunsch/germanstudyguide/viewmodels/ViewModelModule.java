package com.williammunsch.germanstudyguide.viewmodels;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

/**
 * Binds the factory and
 * ...viewmodels
 */
@Module
public abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(VocabViewModel.class)
    abstract ViewModel bindVocabViewModel(VocabViewModel vocabViewModel);


    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory viewModelFactory);
}
