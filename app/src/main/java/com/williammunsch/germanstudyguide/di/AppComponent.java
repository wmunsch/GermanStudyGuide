package com.williammunsch.germanstudyguide.di;

import android.app.Application;


import com.williammunsch.germanstudyguide.FlashcardActivity;
import com.williammunsch.germanstudyguide.GermanApp;
import com.williammunsch.germanstudyguide.MainActivity;
import com.williammunsch.germanstudyguide.NounGenderActivity;
import com.williammunsch.germanstudyguide.StoryActivity;
import com.williammunsch.germanstudyguide.recyclerviewviewmodels.GrammarListViewModel;
import com.williammunsch.germanstudyguide.repositories.Repository;
import com.williammunsch.germanstudyguide.ui.GrammarFragment;
import com.williammunsch.germanstudyguide.ui.ProfileFragment;
import com.williammunsch.germanstudyguide.ui.StoriesFragment;
import com.williammunsch.germanstudyguide.ui.VocabFragment;
import com.williammunsch.germanstudyguide.activitiesviewmodels.FlashcardViewModel;
import com.williammunsch.germanstudyguide.activitiesviewmodels.MainActivityViewModel;
import com.williammunsch.germanstudyguide.activitiesviewmodels.NounGenderViewModel;
import com.williammunsch.germanstudyguide.recyclerviewviewmodels.StoriesListViewModel;
import com.williammunsch.germanstudyguide.recyclerviewviewmodels.VocabListViewModel;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {
    void inject(GermanApp app);

    //Binds the application to the dependency graph at runtime
    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(Application application);
        AppComponent build();

    }

    void inject(Repository repository);

    void inject(MainActivity mainActivity);

    void inject(VocabFragment vocabFragment);

    void inject(FlashcardActivity flashcardActivity);

    void inject(MainActivityViewModel mainActivityViewModel);

    void inject(ProfileFragment profileFragment);

    void inject(NounGenderActivity nounGenderActivity);

    void inject(GrammarFragment grammarFragment);

    void inject(NounGenderViewModel nounGenderViewModel);

    void inject(StoriesListViewModel storiesListViewModel);

    void inject(StoriesFragment storiesFragment);

    void inject(StoryActivity storyActivity);

    void inject(GrammarListViewModel grammarListViewModel);

}
