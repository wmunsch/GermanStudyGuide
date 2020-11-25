package com.williammunsch.germanstudyguide.di;

import android.app.Application;


import com.williammunsch.germanstudyguide.FlashcardActivity;
import com.williammunsch.germanstudyguide.GermanApp;
import com.williammunsch.germanstudyguide.MainActivity;
import com.williammunsch.germanstudyguide.NounGenderActivity;
import com.williammunsch.germanstudyguide.StoryActivity;
import com.williammunsch.germanstudyguide.adapters.GrammarRecyclerViewAdapter;
import com.williammunsch.germanstudyguide.repositories.FlashcardRepository;
import com.williammunsch.germanstudyguide.repositories.NounGenderRepository;
import com.williammunsch.germanstudyguide.repositories.Repository;
import com.williammunsch.germanstudyguide.repositories.StoriesRepository;
import com.williammunsch.germanstudyguide.ui.GrammarFragment;
import com.williammunsch.germanstudyguide.ui.ProfileFragment;
import com.williammunsch.germanstudyguide.ui.StoriesFragment;
import com.williammunsch.germanstudyguide.ui.VocabFragment;
import com.williammunsch.germanstudyguide.viewmodels.FlashcardViewModel;
import com.williammunsch.germanstudyguide.viewmodels.MainActivityViewModel;
import com.williammunsch.germanstudyguide.viewmodels.NounGenderViewModel;
import com.williammunsch.germanstudyguide.viewmodels.StoriesListViewModel;
import com.williammunsch.germanstudyguide.viewmodels.VocabListViewModel;

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

    VocabListViewModel getViewModel();

    FlashcardViewModel getFlashcardViewModel();

    void inject(Repository repository);

    void inject(FlashcardRepository flashcardRepository);

    void inject(MainActivity mainActivity);

    void inject(VocabFragment vocabFragment);

    void inject(FlashcardActivity flashcardActivity);

    void inject(MainActivityViewModel mainActivityViewModel);

    void inject(ProfileFragment profileFragment);

    void inject(NounGenderActivity nounGenderActivity);

    void inject(GrammarFragment grammarFragment);

    void inject(NounGenderViewModel nounGenderViewModel);

    void inject(NounGenderRepository nounGenderRepository);

    void inject(StoriesListViewModel storiesListViewModel);

    void inject(StoriesRepository storiesRepository);

    void inject(StoriesFragment storiesFragment);

    void inject(StoryActivity storyActivity);




   // void inject(VocabTestActivity vocabTestActivity);

    //SharedPreferences getSharedPrefs();
}
