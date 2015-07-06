package com.myselfapps.rav.slovarik;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 6;
    private static final String DATABASE_NAME = "myDictionary";


    private static final String TABLE_WORDS = "words";

    // Contacts Table Columns names
    private static final String KEY_ID = "rowid";
    private static final String KEY_PRIMARY = "primary_word";
    private static final String KEY_SECONDARY = "translation";
    private static final String KEY_TRANSCRIPTION = "transcription";
    private static final String KEY_GENDER = "gender";
    private static final String KEY_PARTOFSPEECH = "part_of_speech";
    private static final String KEY_AUDIO = "audio";
    private static final String KEY_PICTURE = "picture";
    private static final String KEY_NOTES = "notes";
    private static final String KEY_GROUP = "group1";
    private static final String KEY_GROUP2 = "group2";
    private static final String CREATE_WORDS_TABLE = "CREATE VIRTUAL TABLE " + TABLE_WORDS + " USING fts3("
            + KEY_PRIMARY + " TEXT,"
            + KEY_TRANSCRIPTION + " TEXT,"
            + KEY_SECONDARY + " TEXT,"
            + KEY_GENDER + " TEXT,"
            + KEY_PARTOFSPEECH + " TEXT,"
            + KEY_AUDIO + " TEXT,"
            + KEY_PICTURE + " TEXT,"
            + KEY_NOTES + " TEXT,"
            + KEY_GROUP + " TEXT,"
            + KEY_GROUP2 + " TEXT"
            + ")";

    // query strings
    private String[] columns = new String[]{KEY_ID,"*"};
    private String selection = null;
    private String[] selectionArgs = null;
    private String groupBy = null;
    private String having = null;
    private String orderBy = null;
    private String limit = null;
    private String query = null;
    public DatabaseHandler(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Filling Word from CURSOR
    private Word fillWord(Cursor cursor) {
        Word word;
        do {
            word = new Word();
            word.setId(cursor.getString(cursor.getColumnIndex(KEY_ID)));
            word.setPrimary(cursor.getString(cursor.getColumnIndex(KEY_PRIMARY)));
            word.setTranscription(cursor.getString(cursor.getColumnIndex(KEY_TRANSCRIPTION)));
            word.setSecondary(cursor.getString(cursor.getColumnIndex(KEY_SECONDARY)));
            word.setGender(cursor.getString(cursor.getColumnIndex(KEY_GENDER)));
            word.setPartOfSpeech(cursor.getString(cursor.getColumnIndex(KEY_PARTOFSPEECH)));
            word.setAudio(cursor.getString(cursor.getColumnIndex(KEY_AUDIO)));
            word.setPicture(cursor.getString(cursor.getColumnIndex(KEY_PICTURE)));
            word.setNotes(cursor.getString(cursor.getColumnIndex(KEY_NOTES)));
            word.setGroup1(cursor.getString(cursor.getColumnIndex(KEY_GROUP)));
            word.setGroup2(cursor.getString(cursor.getColumnIndex(KEY_GROUP2)));


        } while (cursor.moveToNext());
        return word;
    }

    // Filling list Words from CURSOR
    private List<Word> fillWords(Cursor cursor) {
        List<Word> words = new ArrayList<>();
        do {
            Word word = new Word();
            word.setId(cursor.getString(cursor.getColumnIndex(KEY_ID)));
            word.setPrimary(cursor.getString(cursor.getColumnIndex(KEY_PRIMARY)));
            word.setTranscription(cursor.getString(cursor.getColumnIndex(KEY_TRANSCRIPTION)));
            word.setSecondary(cursor.getString(cursor.getColumnIndex(KEY_SECONDARY)));
            word.setGender(cursor.getString(cursor.getColumnIndex(KEY_GENDER)));
            word.setPartOfSpeech(cursor.getString(cursor.getColumnIndex(KEY_PARTOFSPEECH)));
            word.setAudio(cursor.getString(cursor.getColumnIndex(KEY_AUDIO)));
            word.setPicture(cursor.getString(cursor.getColumnIndex(KEY_PICTURE)));
            word.setNotes(cursor.getString(cursor.getColumnIndex(KEY_NOTES)));
            word.setGroup1(cursor.getString(cursor.getColumnIndex(KEY_GROUP)));
            word.setGroup2(cursor.getString(cursor.getColumnIndex(KEY_GROUP2)));

            words.add(word);
        } while (cursor.moveToNext());
        return words;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_WORDS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_WORDS);
        onCreate(db);
        Log.d("myLog", "--------------------------------Table was created------------------------------");
    }


    // Adding new word
    private void addWord(Word word) {
        Log.d("myLog", "--------------------------------Add word started------------------------------");

        if(!checkDuplicates(word)) {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(KEY_PRIMARY, word.getPrimary());
            values.put(KEY_TRANSCRIPTION, word.getTranscription());
            values.put(KEY_SECONDARY, word.getSecondary());
            values.put(KEY_GENDER, word.getGender());
            values.put(KEY_PARTOFSPEECH, word.getPartOfSpeech());
            values.put(KEY_AUDIO, word.getAudio());
            values.put(KEY_PICTURE, word.getPicture());
            values.put(KEY_NOTES, word.getNotes());
            values.put(KEY_GROUP, word.getGroup1());
            values.put(KEY_GROUP2, word.getGroup2());

            db.insert(TABLE_WORDS, null, values);
            Log.d("myLog", "--------------------------------Add word finished----------------------------");
        }
        else {
            Log.d("myLog", "--------------------------------Add word aborted, Found Duplicate------------");
        }
    }

    private Boolean checkDuplicates(Word word) {
        boolean res = false;
        Word w = getWordByPair(word.getPrimary(), word.getSecondary());
        if( w != null){
            res = true;
        }

        return res;
    }

    // Adding new words
    public void addWords(ArrayList<Word> words) {
        Log.d("myLog", "--------------------------------Add words------------------------------");
        SQLiteDatabase db = this.getWritableDatabase();

        for (Word word : words){
            addWord(word);
        }
        db.close();
    }

    // Getting All Words
    public List<Word> getAllWords() {
        Log.d("myLog", "--------------------------------Get All Words------------------------------");
        List<Word> words = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_WORDS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            words = fillWords(cursor);
        }
        return words;
    }

    // Getting All Words Sort By Parameter
    public List<Word> getAllWordsSortBy(String parameter) {
        Log.d("myLog", "--------------------------------Get All Words Sort By "+parameter+"------------------------------");
        List<Word> words = new ArrayList<>();
        orderBy = parameter;
       SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_WORDS, columns, selection, selectionArgs, groupBy, having, orderBy, limit);

        // looping through all words and adding to list
        if (cursor.moveToFirst()) {
            words = fillWords(cursor);
        }
        return words;
    }


    // Getting All Words Sort By List
    public List<Word> getAllWordsSortBy(List<Integer> list) {
        Log.d("myLog", "--------------------------------Get All Words Sort By list------------------------------");
        List<Word> words = new ArrayList<>();

        for (int x : list){
           words.add(getWordByID(x));
        }

        return words;
    }


    // Getting Word  By ID
    public Word getWordByID(int id) {
        Log.d("myLog", "--------------------------------Getting Word  By ID = "+id+"------------------------------");
        Word word = null;

        SQLiteDatabase db = this.getWritableDatabase();
        selection = id+"";
        selectionArgs = new String[] { selection };

        query ="SELECT rowid,* FROM " + TABLE_WORDS + " WHERE "+KEY_ID + " = ?";
        Cursor cursor = db.rawQuery(query, selectionArgs);
                // looping through all rows and adding to list
        Log.d("myLog", "--------------------------------cursor.getCount() = " + cursor.getCount() + "------------------------------");
        if (cursor.moveToFirst()) {
            word = fillWord(cursor);

        }
        return word;
    }



    // Getting Word  By Pair Primary word and Translation
    public Word getWordByPair(String wPrimary, String wTranslation) {
        Log.d("myLog", "--------------------------------Getting Word  By Pair = "+wPrimary+"-"+wTranslation+"------------------------------");
        Word word = null;
        SQLiteDatabase db = this.getWritableDatabase();

        selectionArgs = new String[] { wPrimary, wTranslation };
        query ="SELECT rowid,* FROM " + TABLE_WORDS + " WHERE "+KEY_PRIMARY + " = ? AND "+KEY_TRANSCRIPTION + " = ?";

        Cursor cursor = db.rawQuery(query, selectionArgs);
        Log.d("myLog", "--------------------------------cursor.getCount() = " + cursor.getCount() + "------------------------------");
        if (cursor.moveToFirst()) {
            word = fillWord(cursor);
        }
        else {
            Log.d("myLog", "--------------------------------Pair = "+wPrimary+"-"+wTranslation+" was not found------------------------------");
        }
        return word;
    }

    // Updating single word
    public int updateWord(Word word) {
        Log.d("myLog", "--------------------------------Update word------------------------------");
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PRIMARY, word.getPrimary());
        values.put(KEY_TRANSCRIPTION, word.getTranscription());
        values.put(KEY_SECONDARY, word.getSecondary());
        values.put(KEY_GENDER, word.getGender());
        values.put(KEY_PARTOFSPEECH, word.getPartOfSpeech());
        values.put(KEY_AUDIO, word.getAudio());
        values.put(KEY_PICTURE, word.getPicture());
        values.put(KEY_NOTES, word.getNotes());
        values.put(KEY_GROUP, word.getGroup1());
        values.put(KEY_GROUP2, word.getGroup2());

        return db.update(TABLE_WORDS, values, KEY_ID + " = ",
                new String[] { String.valueOf(word.getId()) });
    }

    // Deleting Word
    public void deleteWord(Word word) {
        Log.d("myLog", "--------------------------------Delete word------------------------------");
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            query = KEY_ID + " = ?";
            String id = word.getId();

            selectionArgs = new String[]{id};

            int res = db.delete(TABLE_WORDS, query, selectionArgs);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        db.close();
    }

    // Deleting Word by id
    public void deleteWord(int id) {
        Log.d("myLog", "--------------------------------Delete word------------------------------");
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            query = KEY_ID + " = ?";


            selectionArgs = new String[]{(id+"")};

            int res = db.delete(TABLE_WORDS, query, selectionArgs);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        db.close();
    }

    // Getting Word Count
    public int getWordsCount() {
        Log.d("myLog", "--------------------------------Get Words Count------------------------------");
        String countQuery = "SELECT  rowid,* FROM " + TABLE_WORDS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        return cursor.getCount();
    }
    // Search method
    public List<Word> search(String searchStr) {
        SQLiteDatabase db = this.getWritableDatabase();
        List<Word> words = new ArrayList<>();
        Cursor cursor = null;

        selection = TABLE_WORDS + " MATCH '"+ searchStr + "*'";

        try{
            cursor = db.query(true, TABLE_WORDS, columns, selection, selectionArgs, groupBy, having, orderBy, limit);

            if(cursor!= null && cursor.moveToFirst()){
                words = fillWords(cursor);
            }
        }catch(Exception e){
            Log.e("myLog", "An error occurred while searching for "+searchStr+": "+e.toString(), e);
        }finally{
            if(cursor!=null && !cursor.isClosed()){
                cursor.close();
            }
        }


        return words;
    }
}
