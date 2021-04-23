package com.williammunsch.germanstudyguide.di;

import android.app.Application;

import androidx.room.Room;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;


import com.williammunsch.germanstudyguide.api.DatabaseService;
import com.williammunsch.germanstudyguide.room.GermanDatabase;
import com.williammunsch.germanstudyguide.viewmodelhelpers.ViewModelModule;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module(includes = ViewModelModule.class)
public class AppModule {

    @Singleton
    @Provides
    public DatabaseService provideDatabaseService(){
        return new Retrofit.Builder().baseUrl("http://96.37.216.151/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(DatabaseService.class);
    }


    @Singleton
    @Provides
    public GermanDatabase provideDb(Application app){
        return Room.databaseBuilder(app, GermanDatabase.class, "german_database")
                .addMigrations(MIGRATION_1_2)
               // .fallbackToDestructiveMigration()
                .build();
    }

    private static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            //Delete story.class table
            database.execSQL("DROP TABLE story");
        }
    };

}
