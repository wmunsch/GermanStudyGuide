package com.williammunsch.germanstudyguide.room;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.williammunsch.germanstudyguide.datamodels.Story;
import com.williammunsch.germanstudyguide.datamodels.VocabListItem;
import com.williammunsch.germanstudyguide.datamodels.VocabModel;

@Database(entities = {VocabModel.class, Story.class, VocabListItem.class}, version = 2)
public abstract class GermanDatabase extends RoomDatabase {
    //private static GermanDatabase instance;

    public abstract VocabDao vocabDao();

    public abstract VocabListDao vocabListDao();

    public abstract StoryDao storyDao();

    private final MutableLiveData<Boolean> isDatabaseCreated = new MutableLiveData<>();

    /*
    public static synchronized GermanDatabase getInstance(Context context){
        if (instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    GermanDatabase.class, "german_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

*/

    /**
     * Populate the database with the vocab list items when created
     */
    /*
    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };
*/
    /**
     * Async task to insert the vocab list items into the database
     */
    /*
    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void>{
        private VocabListDao vocabListDao;

        private PopulateDbAsyncTask(GermanDatabase db){
            vocabListDao = db.vocabListDao();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            vocabListDao.insert(new VocabListItem("Beginner Level 1","A1",0,0,0));
            vocabListDao.insert(new VocabListItem("Beginner Level 2","A2",0,0,0));
            vocabListDao.insert(new VocabListItem("Intermediate Level 1","B1",0,0,0));
            return null;
        }
    }
    */

}
