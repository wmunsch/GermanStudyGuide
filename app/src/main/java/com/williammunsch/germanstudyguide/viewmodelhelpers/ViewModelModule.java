package com.williammunsch.germanstudyguide.viewmodelhelpers;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.williammunsch.germanstudyguide.activitiesviewmodels.FlashcardViewModel;
import com.williammunsch.germanstudyguide.activitiesviewmodels.MainActivityViewModel;
import com.williammunsch.germanstudyguide.activitiesviewmodels.NounGenderViewModel;
import com.williammunsch.germanstudyguide.activitiesviewmodels.StoryViewModel;
import com.williammunsch.germanstudyguide.recyclerviewviewmodels.GrammarListViewModel;
import com.williammunsch.germanstudyguide.recyclerviewviewmodels.StoriesListViewModel;
import com.williammunsch.germanstudyguide.recyclerviewviewmodels.VocabListViewModel;

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
    @IntoMap
    @ViewModelKey(MainActivityViewModel.class)
    abstract ViewModel bindMainActivityViewModel(MainActivityViewModel mainActivityViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(NounGenderViewModel.class)
    abstract ViewModel bindNounGenderViewModel(NounGenderViewModel nounGenderViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(StoriesListViewModel.class)
    abstract ViewModel bindStoriesListViewModel(StoriesListViewModel storiesListViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(StoryViewModel.class)
    abstract ViewModel bindStoryViewModel(StoryViewModel storyViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(GrammarListViewModel.class)
    abstract ViewModel bindGrammarListViewModel(GrammarListViewModel grammarListViewModel);

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory viewModelFactory);
}
