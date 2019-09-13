package com.williammunsch.germanstudyguide.di;

import android.app.Application;


import com.williammunsch.germanstudyguide.GermanApp;
import com.williammunsch.germanstudyguide.MainActivity;
import com.williammunsch.germanstudyguide.repositories.Repository;
import com.williammunsch.germanstudyguide.ui.VocabFragment;
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

    void inject(Repository repository);

    void inject(MainActivity mainActivity);

    void inject(VocabFragment vocabFragment);

   // void inject(VocabTestActivity vocabTestActivity);

    //SharedPreferences getSharedPrefs();
}
