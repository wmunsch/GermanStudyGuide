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
    @ViewModelKey(VocabListViewModel.class)
    abstract ViewModel bindVocabViewModel(VocabListViewModel vocabListViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(FlashcardViewModel.class)
    abstract ViewModel bindFlashcardViewModel(FlashcardViewModel flashcardViewModel);


    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory viewModelFactory);
}