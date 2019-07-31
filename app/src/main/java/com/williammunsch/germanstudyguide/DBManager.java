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
import java.util.Random;


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
    private static final int newWords = 5; //use preferences to set this in options

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

    public ArrayList<SimpleWord> getSimpleWordList(String tableName){
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
        ArrayList<SimpleWord> simpleWordList = new ArrayList<>();
        for (int i = 0; i < mCursor.getCount(); i++){
            mCursor.moveToNext();
            simpleWordList.add(new SimpleWord(mCursor.getString(mCursor.getColumnIndex("german")),
                    mCursor.getString(mCursor.getColumnIndex("english")),
                    mCursor.getInt(mCursor.getColumnIndex("score"))));
        }
        mCursor.close();
        return simpleWordList;
    }



    public ArrayList<Word> getWordList(String tableName){
        vocabDatabase = SQLiteDatabase.openDatabase(DB_PATH,null,SQLiteDatabase.OPEN_READONLY);
        ArrayList<Word> wordList = new ArrayList<>();

        String selection = "studying = ?";
        String[] selectionArgs = {"0"};
        Cursor mCursor = vocabDatabase.query(
                tableName,             // The table to query
                null,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                "_id"               // The sort order
        );
        for (int i = 0; i < newWords; i++){
            try{
                //mCursor.getCount();
               // mCursor.moveToPosition(random.nextInt(mCursor.getCount()));
                mCursor.moveToNext();
                wordList.add(new Word(mCursor.getInt(mCursor.getColumnIndex("_id")),
                        mCursor.getInt(mCursor.getColumnIndex("score")),
                        mCursor.getInt(mCursor.getColumnIndex("freq")),
                        mCursor.getInt(mCursor.getColumnIndex("studying")),
                        mCursor.getString(mCursor.getColumnIndex("german")),
                        mCursor.getString(mCursor.getColumnIndex("english")),
                        mCursor.getString(mCursor.getColumnIndex("gsent")),
                        mCursor.getString(mCursor.getColumnIndex("esent"))));
            }catch(Exception e){
                System.out.println("Reached End of list.");
                //Set variable here to change Learn to Study
            }
        }
        mCursor.close();

        //Second pass for the 15 lowest score, and least frequency of those lowest scored.
        selection = "studying = ?";
        selectionArgs[0] = "1";
        mCursor = vocabDatabase.query(
                tableName,             // The table to query
                null,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                "score ASC, freq ASC, _id ASC"               // The sort order
        );
        for (int i = 0; i < 15; i++){
            try{
                mCursor.moveToNext();
                wordList.add(new Word(mCursor.getInt(mCursor.getColumnIndex("_id")),
                        mCursor.getInt(mCursor.getColumnIndex("score")),
                        mCursor.getInt(mCursor.getColumnIndex("freq")),
                        mCursor.getInt(mCursor.getColumnIndex("studying")),
                        mCursor.getString(mCursor.getColumnIndex("german")),
                        mCursor.getString(mCursor.getColumnIndex("english")),
                        mCursor.getString(mCursor.getColumnIndex("gsent")),
                        mCursor.getString(mCursor.getColumnIndex("esent"))));
            }catch(Exception e){
                System.out.println("Error retrieving words.");
            }
        }
        mCursor.close();

        return wordList;
    }

    public ArrayList<Word> getWordListAllLearned(String tableName){
        vocabDatabase = SQLiteDatabase.openDatabase(DB_PATH,null,SQLiteDatabase.OPEN_READONLY);
        ArrayList<Word> wordList = new ArrayList<>();
        Cursor mCursor = vocabDatabase.query(
                tableName,             // The table to query
                null,             // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                "score ASC, freq ASC, _id ASC"               // The sort order
        );
        for (int i = 0; i < 20; i++){
            try{
                mCursor.moveToNext();
                wordList.add(new Word(mCursor.getInt(mCursor.getColumnIndex("_id")),
                        mCursor.getInt(mCursor.getColumnIndex("score")),
                        mCursor.getInt(mCursor.getColumnIndex("freq")),
                        mCursor.getInt(mCursor.getColumnIndex("studying")),
                        mCursor.getString(mCursor.getColumnIndex("german")),
                        mCursor.getString(mCursor.getColumnIndex("english")),
                        mCursor.getString(mCursor.getColumnIndex("gsent")),
                        mCursor.getString(mCursor.getColumnIndex("esent"))));
            }catch(Exception e){
                System.out.println("Error retrieving words.");
            }
        }
        mCursor.close();

        return wordList;
    }

    public ArrayList<Word> getWordListAllMastered(String tableName){
        vocabDatabase = SQLiteDatabase.openDatabase(DB_PATH,null,SQLiteDatabase.OPEN_READONLY);
        ArrayList<Word> wordList = new ArrayList<>();
        Cursor mCursor = vocabDatabase.query(
                tableName,             // The table to query
                null,             // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                "_id ASC"               // The sort order
        );
        Random random = new Random();
        int rowCount = getWordsMax(tableName);
        for (int i = 0; i < 20; i++){
            try{
                mCursor.moveToPosition(random.nextInt(rowCount)+1);
                wordList.add(new Word(mCursor.getInt(mCursor.getColumnIndex("_id")),
                        mCursor.getInt(mCursor.getColumnIndex("score")),
                        mCursor.getInt(mCursor.getColumnIndex("freq")),
                        mCursor.getInt(mCursor.getColumnIndex("studying")),
                        mCursor.getString(mCursor.getColumnIndex("german")),
                        mCursor.getString(mCursor.getColumnIndex("english")),
                        mCursor.getString(mCursor.getColumnIndex("gsent")),
                        mCursor.getString(mCursor.getColumnIndex("esent"))));
            }catch(Exception e){
                System.out.println("Error retrieving words.");
            }
        }
        mCursor.close();

        return wordList;
    }

    public void updateWord(Word w, String tableName){
        vocabDatabase = SQLiteDatabase.openDatabase(DB_PATH,null,SQLiteDatabase.OPEN_READWRITE);
        ContentValues cv = new ContentValues();
        cv.put("score",w.getScore());
        cv.put("freq",w.getFreq()+1);
        vocabDatabase.update(tableName,cv,"_id="+w.getId(),null);
    }

    public void setStudying(Word w, String tableName){
        vocabDatabase = SQLiteDatabase.openDatabase(DB_PATH,null,SQLiteDatabase.OPEN_READWRITE);
        ContentValues cv = new ContentValues();
        cv.put("studying", 1);
        vocabDatabase.update(tableName,cv,"_id="+w.getId(),null);
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
