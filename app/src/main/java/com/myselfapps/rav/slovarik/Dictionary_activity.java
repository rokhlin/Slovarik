package com.myselfapps.rav.slovarik;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.melnykov.fab.FloatingActionButton;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.util.List;


public class Dictionary_activity extends AppCompatActivity implements Drawer.OnDrawerItemClickListener{
    private static enum SortingParameter{
        primary_word, translation
    }

    private LeftDrawer leftDrawer;
    private RecyclerView rv;
    private SharedPreferences pref;
    private int Statecount = 0;


    private String FIRST_LANGUAGE ="IL";
    private String SECOND_LANGUAGE ="RU";
    private String SORT_PARAM = String.valueOf(SortingParameter.primary_word);

    public static final String PREFS_NAME = "SLOVARIK_PREFS";
    public static final String PREFS_FIRST_LANGUAGE = "FIRST_LANGUAGE";
    public static final String PREFS_SECOND_LANGUAGE = "SECOND_LANGUAGE";
    public static final String PREFS_SORT_PARAM = "SORT_PARAM";
    public static final String PREFS_DEFAULT_TABLE = "DEFAULT_TABLE";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary);

        init();

        //populateWords(SORT_PARAM);

    }

    @Override
    protected void onStart() {
        super.onStart();
        populateWords(SORT_PARAM);
    }

    @Override
    protected void onResume() {
        super.onResume();
        readPreferences();
        populateWords(SORT_PARAM);
    }

    @Override
    protected void onPause() {
        super.onPause();
        savePreferences();
    }

    @Override
    protected void onStop() {
        super.onStop();
        savePreferences();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_dictionary, menu);

        return true;
    }

    @Override
    public void onBackPressed(){

        if(leftDrawer.getResult() != null && leftDrawer.getResult().isDrawerOpen()){
            leftDrawer.drawerClose();
        }
        else if(Statecount == 0){
            Statecount = Statecount + 1;
            Log.d("Mylog", "Press Count = " + Statecount);
            new MyTask().execute();

        }
        else if(Statecount == 1){
            Log.d("Mylog", "Press Count Exit = " + Statecount);
            this.finish();
        }


    }

    class MyTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Thread.sleep(600);
                Statecount = 0;
                Log.d("Mylog", "Press Count after Thread = " + Statecount);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

        }
    }

    /**************** Action Drawer *******************/
    @Override
    public boolean onItemClick(AdapterView<?> adapterView, View view, int i, long l, IDrawerItem iDrawerItem) {
        Intent intent = new Intent(Dictionary_activity.this, Verbs_activity.class);
        startActivity(intent);
        return true;
    }

    /**************** Toolbar Menu *******************/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()){
            case R.id.change_language:
                //сменa языка
                String s = FIRST_LANGUAGE;
                FIRST_LANGUAGE = SECOND_LANGUAGE;
                SECOND_LANGUAGE = s;

                Log.d("myLog", "-------Language changed: " + FIRST_LANGUAGE + "-" + SECOND_LANGUAGE + "--------");

                if(SORT_PARAM.equals(String.valueOf(SortingParameter.translation))){
                    SORT_PARAM = String.valueOf(SortingParameter.primary_word);
                }
                else {
                    SORT_PARAM = String.valueOf(SortingParameter.translation);
                }
                populateWords(SORT_PARAM);
                break;

        }


        return super.onOptionsItemSelected(item);
    }

    private void populateWords() {
        /**************** Init DATABASE *******************/
        DatabaseHandler db = new DatabaseHandler(this);

        List<Word> words = db.getAllWords();

        RecyclerViewWordAdapter adapter = new RecyclerViewWordAdapter(words);
        rv.setAdapter(adapter);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
    }

    private void populateWords(String parameter) {
        /**************** Init DATABASE *******************/
        DatabaseHandler db = new DatabaseHandler(this);

        List<Word> words = db.getAllWordsSortBy(parameter);

        RecyclerViewWordAdapter adapter = new RecyclerViewWordAdapter(words);
        rv.setAdapter(adapter);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
    }

    private void init() {

        /**************** Init Preferences *******************/
        pref = getApplicationContext().getSharedPreferences(PREFS_NAME, MODE_PRIVATE);


        /**************** Init Toolbar *******************/
        Toolbar mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mActionBarToolbar);

        /**************** Init ActionDrawer *******************/
        leftDrawer = new LeftDrawer(mActionBarToolbar,this);

        /**************** Init Recycler View *******************/
        rv = (RecyclerView)findViewById(R.id.rv);

        /**************** Init Floating Action Button *******************/
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.attachToRecyclerView(rv);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dictionary_activity.this, AddNewWord_activity.class);
                startActivity(intent);
            }
        });
    }

    private void savePreferences() {
        Log.d("Mylog", "----------------------------Save changes-----------------------------");
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(PREFS_FIRST_LANGUAGE, FIRST_LANGUAGE);
        editor.putString(PREFS_SECOND_LANGUAGE, SECOND_LANGUAGE);
        editor.putString(PREFS_SORT_PARAM, SORT_PARAM);

        editor.apply();
    }

    private void readPreferences() {
        Log.d("Mylog", "----------------------------Load changes-----------------------------");
        FIRST_LANGUAGE = pref.getString(PREFS_FIRST_LANGUAGE, "IL");
        SECOND_LANGUAGE = pref.getString(PREFS_SECOND_LANGUAGE, "RU");
        SORT_PARAM = pref.getString(PREFS_SORT_PARAM, String.valueOf(SortingParameter.primary_word));
    }




}
