package com.williammunsch.germanstudyguide;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;


public class DBManager extends SQLiteOpenHelper{

    //Store the db on github and dl from thre instead of from the assets folder?

    private static String DB_PATH;// = "/data/data/com.williammunsch.ascend/databases/"; //path to copy the DB to on device to be used in code
    //private static String DB_PATH = "";
    //private static String DB_PATH = Context.getApplicationInfo().dataDir+"/databases/";
    private static String DB_PATH2 = "Context.getFilesDir().getPath()";

    private static final String DB_NAME = "GermanVocabDB.db";
    private static final int DB_VERSION = 1;
    private SQLiteDatabase vocabDatabase;
    private final Context myContext;

    /**
     * Constructor
     * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
     * @param context
     */
    public DBManager(Context context){
        super(context, DB_NAME, null, 1);
        this.myContext = context;
         DB_PATH = myContext.getDatabasePath(DB_NAME).getPath();
    }

    /**
     * Check if db exists based on checkDatabase(), if not
     * creates an empty database on the system and overwrites it with your own database.
     * */
    public void createDatabase() throws IOException{
        boolean dbExist = checkDatabase();
        System.out.println("DBEXISTS??" + dbExist);
        if (dbExist){
        }else {
            this.getWritableDatabase();
            System.out.println("Got writable database");
            try {
                copyDatabase();
            }catch (IOException e){
                throw new Error("Error copying database");
            }
        }


    }

    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDatabase(){
        File databasePath = myContext.getDatabasePath(DB_NAME);
        //return databasePath.exists();
        return false;  //used for resets

    }

    /**
     * Uses a byte stream to copy the database from assets folder into the application to be used
     * */
    private void copyDatabase() throws IOException{
        //Open your local db as the input stream
        InputStream is = myContext.getAssets().open(DB_NAME);

        // Path to the just created empty db
        String outFile = DB_PATH;//+DB_NAME;

        //Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFile);

        //transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = is.read(buffer))>0){
            myOutput.write(buffer,0,length);
        }

        myOutput.flush();
        myOutput.close();
        is.close();
    }



    public int getWordsMax(String tableName){
        String myPath = DB_PATH;// + DB_NAME;
        vocabDatabase = SQLiteDatabase.openDatabase(myPath, null,SQLiteDatabase.OPEN_READONLY);
        String selection = "_id = ?";
        String[] selectionArgs = {"1"};
        Cursor mCursor = vocabDatabase.query(
                tableName,
                null,
                null,
                null,
                null,
                null,
                "_id"
        );
        mCursor.moveToLast();
        int lastId = mCursor.getInt(mCursor.getColumnIndex("_id"));
        mCursor.close();
        return lastId;
    }

    public int getWordsLearned(String tableName){
        String myPath = DB_PATH;// + DB_NAME;
        vocabDatabase = SQLiteDatabase.openDatabase(myPath, null,SQLiteDatabase.OPEN_READONLY);
        String selection = "studying = ?";
        String[] selectionArgs = {"1"};
        Cursor mCursor = vocabDatabase.query(
                tableName,
                null,
                selection,
                selectionArgs,
                null,
                null,
                "_id"
        );
        int lastId;
        try {
            mCursor.moveToLast();
            lastId = mCursor.getInt(mCursor.getColumnIndex("_id"));
        }
        catch(Exception e) {
            System.out.println("Cursor index out of bounds, no words learned yet.");
            lastId = 0;
        }

        mCursor.close();
        return lastId;
    }

    public int getWordsMastered(String tableName){
        String myPath = DB_PATH;// + DB_NAME;
        vocabDatabase = SQLiteDatabase.openDatabase(myPath, null,SQLiteDatabase.OPEN_READONLY);
        String selection = "score = ?";
        String[] selectionArgs = {"100"};
        Cursor mCursor = vocabDatabase.query(
                tableName,
                null,
                selection,
                selectionArgs,
                null,
                null,
                "_id"
        );
        int count = mCursor.getCount();
        mCursor.close();
        return count;
    }

    public ArrayList<String> getGermanList(String tableName){
        String myPath = DB_PATH;
        vocabDatabase = SQLiteDatabase.openDatabase(myPath, null,SQLiteDatabase.OPEN_READONLY);
        Cursor mCursor = vocabDatabase.query(
                tableName,
                null,
                null,
                null,
                null,
                null,
                "_id"
        );
        ArrayList<String> germanList = new ArrayList<>();
        for (int i = 0; i < mCursor.getCount(); i++){
            mCursor.moveToNext();
            germanList.add(mCursor.getString(mCursor.getColumnIndex("german")));

        }
        mCursor.close();
        return germanList;
    }

    public ArrayList<String> getEnglishList(String tableName){
        String myPath = DB_PATH;
        vocabDatabase = SQLiteDatabase.openDatabase(myPath, null,SQLiteDatabase.OPEN_READONLY);
        Cursor mCursor = vocabDatabase.query(
                tableName,
                null,
                null,
                null,
                null,
                null,
                "_id"
        );
        ArrayList<String> englishList = new ArrayList<>();
        for (int i = 0; i < mCursor.getCount(); i++){
            mCursor.moveToNext();
            englishList.add(mCursor.getString(mCursor.getColumnIndex("english")));

        }
        mCursor.close();
        return englishList;
    }

    public ArrayList<Integer> getScoreList(String tableName){
        String myPath = DB_PATH;
        vocabDatabase = SQLiteDatabase.openDatabase(myPath, null,SQLiteDatabase.OPEN_READONLY);
        Cursor mCursor = vocabDatabase.query(
                tableName,
                null,
                null,
                null,
                null,
                null,
                "_id"
        );
        ArrayList<Integer> getScoreList = new ArrayList<>();
        for (int i = 0; i < mCursor.getCount(); i++){
            mCursor.moveToNext();
            getScoreList.add(mCursor.getInt(mCursor.getColumnIndex("score")));

        }
        mCursor.close();
        return getScoreList;
    }


    @Override
    public synchronized void close() {
        if (vocabDatabase != null){
            vocabDatabase.close();
        }
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
