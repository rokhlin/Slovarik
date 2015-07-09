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

    //Table Names
    private static final String TABLE_WORDS = "words";
    private static final String TABLE_PHRASES = "phrases";
    private static final String TABLE_CATEGORY = "category";
    private static final String TABLE_LABELS = "labels";

    //Table Columns names
    private static final String KEY_ID = "rowid";
    private static final String KEY_PRIMARY = "primary_parameter";
    private static final String KEY_SECONDARY = "translation";
    private static final String KEY_TRANSCRIPTION = "transcription";
    private static final String KEY_GENDER = "gender";
    private static final String KEY_PARTOFSPEECH = "part_of_speech";
    private static final String KEY_AUDIO = "audio";
    private static final String KEY_PICTURE = "picture";
    private static final String KEY_NOTES = "notes";
    private static final String KEY_GROUP = "group1";
    private static final String KEY_GROUP2 = "group2";
    private static final String KEY_DICTIONARY = "dictionary";
    private static final String KEY_PLURALFORM = "pluralform";
    private static final String KEY_EXCEPTION = "exception";
    private static final String KEY_FIELD1 = "field1";
    private static final String KEY_FIELD2 = "field2";
    private static final String KEY_FIELD3 = "field3";
    private static final String KEY_FIELD4 = "field4";
    private static final String KEY_FIELD5 = "field5";
    private static final String KEY_CATEGORY = "category";
    private static final String KEY_LABEL = "label";
    private static final String KEY_NAME = "name";
    private static final String KEY_PHRASE_ID = "phrase_id";
    private static final String KEY_WORD_ID = "word_id";
    private static final String KEY_VISIBILITY = "visibility";

    //Query strings
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
            + KEY_GROUP2 + " TEXT,"
            + KEY_DICTIONARY + " TEXT,"
            + KEY_PLURALFORM + " TEXT,"
            + KEY_EXCEPTION + " TEXT,"
            + KEY_FIELD1 + " TEXT,"
            + KEY_FIELD2 + " TEXT,"
            + KEY_FIELD3 + " TEXT,"
            + KEY_FIELD4 + " TEXT,"
            + KEY_FIELD5 + " TEXT"
            + ")";

    private static final String CREATE_PHRASES_TABLE = "CREATE VIRTUAL TABLE " + TABLE_PHRASES + " USING fts3("
            + KEY_PRIMARY + " TEXT,"
            + KEY_TRANSCRIPTION + " TEXT,"
            + KEY_SECONDARY + " TEXT,"
            + KEY_CATEGORY + " TEXT,"
            + KEY_LABEL + " TEXT,"
            + KEY_NOTES + " TEXT,"
            + KEY_DICTIONARY + " TEXT,"
            + KEY_FIELD1 + " TEXT,"
            + KEY_FIELD2 + " TEXT,"
            + KEY_FIELD3 + " TEXT"
            + ")";

    private static final String CREATE_CATEGORY_TABLE = "CREATE VIRTUAL TABLE " + TABLE_CATEGORY + " USING fts3("
            + KEY_NAME + " TEXT,"
            + KEY_PHRASE_ID + " TEXT,"
            + KEY_WORD_ID + " TEXT,"
            + KEY_DICTIONARY + " TEXT,"
            + KEY_NOTES + " TEXT,"
            + KEY_VISIBILITY + " TEXT"
            + ")";

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

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_WORDS_TABLE);
        db.execSQL(CREATE_PHRASES_TABLE);
        db.execSQL(CREATE_CATEGORY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_WORDS);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_PHRASES);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_CATEGORY);
        onCreate(db);
        Log.d("myLog", "--------------------------------Table was created------------------------------");
    }

    //***************************************METHODS USING TABLE CATEGORY*****************************************//


    //***************************************METHODS USING TABLE PHRASES*****************************************//
    // Fill list Phrases from CURSOR
    private List<Phrase> fillPhrases(Cursor cursor) {
        List<Phrase> phrases = new ArrayList<>();
        do {
            Phrase phrase = new Phrase();
            phrase.setId(cursor.getString(cursor.getColumnIndex(KEY_ID)));
            phrase.setPrimary(cursor.getString(cursor.getColumnIndex(KEY_PRIMARY)));
            phrase.setTranscription(cursor.getString(cursor.getColumnIndex(KEY_TRANSCRIPTION)));
            phrase.setSecondary(cursor.getString(cursor.getColumnIndex(KEY_SECONDARY)));
            phrase.setCategory(cursor.getString(cursor.getColumnIndex(KEY_CATEGORY)));
            phrase.setLabel(cursor.getString(cursor.getColumnIndex(KEY_LABEL)));
            phrase.setNotes(cursor.getString(cursor.getColumnIndex(KEY_NOTES)));
            phrase.setDictionary(cursor.getString(cursor.getColumnIndex(KEY_DICTIONARY)));
            phrase.setField1(cursor.getString(cursor.getColumnIndex(KEY_FIELD1)));
            phrase.setField2(cursor.getString(cursor.getColumnIndex(KEY_FIELD2)));
            phrase.setField3(cursor.getString(cursor.getColumnIndex(KEY_FIELD3)));

            phrases.add(phrase);
        } while (cursor.moveToNext());
        return phrases;
    }

    // Fill Phrase from CURSOR
    private Phrase fillPhrase(Cursor cursor) {
        return fillPhrases(cursor).get(0);
    }

    // Add new phrase
    public void addPhrase(Phrase phrase) {
        Log.d("myLog", "--------------------------------Add phrase started------------------------------");

        if(!checkDuplicates(phrase)) {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(KEY_PRIMARY, phrase.getPrimary());
            values.put(KEY_TRANSCRIPTION, phrase.getTranscription());
            values.put(KEY_SECONDARY, phrase.getSecondary());
            values.put(KEY_CATEGORY, phrase.getCategory());
            values.put(KEY_LABEL, phrase.getLabel());
            values.put(KEY_NOTES, phrase.getNotes());
            values.put(KEY_DICTIONARY, phrase.getDictionary());
            values.put(KEY_FIELD1, phrase.getField1());
            values.put(KEY_FIELD2, phrase.getField2());
            values.put(KEY_FIELD3, phrase.getField3());


            db.insert(TABLE_PHRASES, null, values);
            Log.d("myLog", "--------------------------------Add phrase finished----------------------------");
        }
        else {
            Log.d("myLog", "--------------------------------Add phrase aborted, Found Duplicate------------");
        }
    }

    private Boolean checkDuplicates(Phrase phrase) {
        boolean res = false;
        Phrase w = getPhraseByPair(phrase.getPrimary(), phrase.getSecondary());
        if( w != null){
            res = true;
        }

        return res;
    }

    // Getting Phrase  By Pair Primary and Secondary
    public Phrase getPhraseByPair(String wPrimary, String wTranslation) {
        Log.d("myLog", "--------------------------------Getting phrase  By Pair = "+wPrimary+"-"+wTranslation+"------------------------------");
        Phrase phrase = null;
        SQLiteDatabase db = this.getWritableDatabase();

        selectionArgs = new String[] { wPrimary, wTranslation };
        query ="SELECT rowid,* FROM " + TABLE_PHRASES + " WHERE "+KEY_PRIMARY + " = ? AND "+KEY_SECONDARY+ " = ?";

        Cursor cursor = db.rawQuery(query, selectionArgs);
        Log.d("myLog", "--------------------------------cursor.getCount() = " + cursor.getCount() + "------------------------------");
        if (cursor.moveToFirst()) {
            phrase = fillPhrase(cursor);
        }
        else {
            Log.d("myLog", "--------------------------------Pair = "+wPrimary+"-"+wTranslation+" was not found------------------------------");
        }
        return phrase;
    }

    // Getting All Phrases
    public List<Phrase> getAllPhrases() {
        Log.d("myLog", "--------------------------------Get All Phrases------------------------------");
        List<Phrase> phrases = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  rowid,* FROM " + TABLE_PHRASES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            phrases = fillPhrases(cursor);
        }
        return phrases;
    }

    // Getting Phrase  By ID
    public Phrase getPhraseByID(int id) {
        Log.d("myLog", "--------------------------------Getting Phrase  By ID = "+id+"------------------------------");
        Phrase phrase = null;

        SQLiteDatabase db = this.getWritableDatabase();
        selection = id+"";
        selectionArgs = new String[] { selection };

        query ="SELECT rowid,* FROM " + TABLE_PHRASES + " WHERE "+KEY_ID + " = ?";
        Cursor cursor = db.rawQuery(query, selectionArgs);
        // looping through all rows and adding to list
        Log.d("myLog", "--------------------------------cursor.getCount() = " + cursor.getCount() + "------------------------------");
        if (cursor.moveToFirst()) {
            phrase = fillPhrase(cursor);

        }
        return phrase;
    }

    // Updating single phrase
    public int updatePhrase(Phrase phrase) {
        Log.d("myLog", "--------------------------------Update phrase------------------------------");
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PRIMARY, phrase.getPrimary());
        values.put(KEY_TRANSCRIPTION, phrase.getTranscription());
        values.put(KEY_SECONDARY, phrase.getSecondary());
        values.put(KEY_CATEGORY, phrase.getCategory());
        values.put(KEY_LABEL, phrase.getLabel());
        values.put(KEY_NOTES, phrase.getNotes());
        values.put(KEY_DICTIONARY, phrase.getDictionary());
        values.put(KEY_FIELD1, phrase.getField1());
        values.put(KEY_FIELD2, phrase.getField2());
        values.put(KEY_FIELD3, phrase.getField3());

        return db.update(TABLE_PHRASES, values, KEY_ID + " = '"+phrase.getId()+"'", null);
    }

    // Deleting Phrase by id
    public void deletePhrase(int id) {
        Log.d("myLog", "--------------------------------Delete phrase------------------------------");
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            query = KEY_ID + " = ?";
            selectionArgs = new String[]{(id+"")};
            int res = db.delete(TABLE_PHRASES, query, selectionArgs);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        db.close();
    }

    // FTS Search Phrases by STRING
    public List<Phrase> searchPhrases(String searchStr) {
        SQLiteDatabase db = this.getWritableDatabase();
        List<Phrase> phrases = new ArrayList<>();
        Cursor cursor = null;

        selection = TABLE_PHRASES + " MATCH '"+ searchStr + "*'";

        try{
            cursor = db.query(true, TABLE_PHRASES, columns, selection, selectionArgs, groupBy, having, orderBy, limit);

            if(cursor!= null && cursor.moveToFirst()){
                phrases = fillPhrases(cursor);
            }
        }catch(Exception e){
            Log.e("myLog", "An error occurred while searching for " + searchStr + ": " + e.toString(), e);
        }finally{
            if(cursor!=null && !cursor.isClosed()){
                cursor.close();
            }
        }


        return phrases;
    }




    //***************************************METHODS USING TABLE WORD*********************************************//
    // Filling Phrase from CURSOR
    private Word fillWord(Cursor cursor) {
        return fillWords(cursor).get(0);
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
            word.setDictionary(cursor.getString(cursor.getColumnIndex(KEY_DICTIONARY)));
            word.setPluralForm(cursor.getString(cursor.getColumnIndex(KEY_PLURALFORM)));
            word.setException(cursor.getString(cursor.getColumnIndex(KEY_EXCEPTION)));
            word.setField1(cursor.getString(cursor.getColumnIndex(KEY_FIELD1)));
            word.setField2(cursor.getString(cursor.getColumnIndex(KEY_FIELD2)));
            word.setField3(cursor.getString(cursor.getColumnIndex(KEY_FIELD3)));
            word.setField4(cursor.getString(cursor.getColumnIndex(KEY_FIELD4)));
            word.setField5(cursor.getString(cursor.getColumnIndex(KEY_FIELD5)));

            words.add(word);
        } while (cursor.moveToNext());
        return words;
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
            values.put(KEY_DICTIONARY, word.getDictionary());
            values.put(KEY_PLURALFORM, word.getPluralForm());
            values.put(KEY_EXCEPTION, word.getException());
            values.put(KEY_FIELD1, word.getField1());
            values.put(KEY_FIELD2, word.getField2());
            values.put(KEY_FIELD3, word.getField3());
            values.put(KEY_FIELD4, word.getField4());
            values.put(KEY_FIELD5, word.getField5());

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
        orderBy = "'"+parameter+"'";
       SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_WORDS, columns, selection, selectionArgs, groupBy, having, orderBy, limit);

        // looping through all words and adding to list
        if (cursor.moveToFirst()) {
            words = fillWords(cursor);
        }
        return words;
    }

    // Getting All Translates conforming the Primary
    public List<Word> getAllWordsBy(String primary) {
        Log.d("myLog", "--------------------------------Getting All Translates by Primary = "+primary+"------------------------------");
        List<Word> words = new ArrayList<>();
        selection = KEY_PRIMARY + "= ?";
        selectionArgs = new String[]{primary};

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
        query ="SELECT rowid,* FROM " + TABLE_WORDS + " WHERE "+KEY_PRIMARY + "=? AND "+KEY_SECONDARY + "=?";
        /*query ="SELECT rowid,* FROM " + TABLE_WORDS
                + " WHERE rowid IN (SELECT rowid FROM " + TABLE_WORDS
                + " WHERE "+KEY_PRIMARY + " MATCH '"+ wPrimary+ "'"
                + "AND rowid IN ( SELECT rowid FROM " + TABLE_WORDS
                + " WHERE "+KEY_SECONDARY + " MATCH '" + wTranslation
                + "'))";
        */
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
        values.put(KEY_DICTIONARY, word.getDictionary());
        values.put(KEY_PLURALFORM, word.getPluralForm());
        values.put(KEY_EXCEPTION, word.getException());
        values.put(KEY_FIELD1, word.getField1());
        values.put(KEY_FIELD2, word.getField2());
        values.put(KEY_FIELD3, word.getField3());
        values.put(KEY_FIELD4, word.getField4());
        values.put(KEY_FIELD5, word.getField5());

        return db.update(TABLE_WORDS, values, KEY_ID + " = '"+word.getId()+"'", null);
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
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("myLog", "--------------------------------Get Words Count------------------------------");
        String countQuery = "SELECT  rowid, * FROM " + TABLE_WORDS;
        Cursor cursor = db.rawQuery(countQuery, null);
        int res = cursor.getCount();
        cursor.close();

        return res;
    }

    // FTS Search Words by STRING
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
