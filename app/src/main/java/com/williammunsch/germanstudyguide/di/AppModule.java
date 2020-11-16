package com.williammunsch.germanstudyguide.di;

import android.app.Application;
import android.view.View;

import androidx.room.Room;


import com.williammunsch.germanstudyguide.api.DatabaseService;
import com.williammunsch.germanstudyguide.room.GermanDatabase;
import com.williammunsch.germanstudyguide.viewmodels.ViewModelFactory;
import com.williammunsch.germanstudyguide.viewmodels.ViewModelModule;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module(includes = ViewModelModule.class)
public class AppModule {
    /*
    AppDatabase db;

    public AppModule(Application application){
        db = Room.databaseBuilder(application, AppDatabase.class, "appdb.db")
                .fallbackToDestructiveMigration()
                .build();
    }

/*
    Application mApplication;
    public AppModule(Application application){
        mApplication = application;
    }

    /*
    @Singleton
    @Provides
    Application provideApplication(){
        return mApplication;
    }
*/


    @Singleton
    @Provides
    public DatabaseService provideDatabaseService(){
        return new Retrofit.Builder().baseUrl("http://96.37.216.151/")//"http://96.37.220.94/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(DatabaseService.class);
    }


    @Singleton
    @Provides
    public GermanDatabase provideDb(Application app){
        return Room.databaseBuilder(app, GermanDatabase.class, "german_database")
                .fallbackToDestructiveMigration()
                .build();
    }
/*
    @Singleton
    @Provides
    public VocabDao provideVocabDao(AppDatabase db){
        return db.vocabDao();
    }
    */
/*
    @Singleton
    @Provides
    public Repository provideRepository(Application app){
        return new Repository();
        return null;
    }
*/


/*

   @Provides
   @Singleton
   public SharedPreferences providePreferences(Application application){
       return application.getSharedPreferences("prefs", Context.MODE_PRIVATE);
   }
   */
}
