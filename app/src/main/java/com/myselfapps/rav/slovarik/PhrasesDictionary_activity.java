package com.myselfapps.rav.slovarik;

import android.app.SearchManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
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


public class PhrasesDictionary_activity extends AppCompatActivity implements Drawer.OnDrawerItemClickListener {
    private enum SortingParameter {
        primary_parameter, translation
    }

    SearchManager manager;
    private RecyclerView rv;
    private SharedPreferences pref;
    private int Statecount = 0;
    public SearchView searchView;
    private LeftDrawer leftDrawer;

    private String FIRST_LANGUAGE = "IL";
    private String SECOND_LANGUAGE = "RU";
    private String SORT_PARAM = String.valueOf(SortingParameter.primary_parameter);
    private String DEFAULT_TABLE = "IL_RU";
    public static final String PREFS_NAME = "SLOVARIK_PREFS";
    public static final String PREFS_FIRST_LANGUAGE = "FIRST_LANGUAGE";
    public static final String PREFS_SECOND_LANGUAGE = "SECOND_LANGUAGE";
    public static final String PREFS_SORT_PARAM = "SORT_PARAM";
    public static final String PREFS_DEFAULT_TABLE = "DEFAULT_TABLE";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phrases_dictionary);
        init();
        populatePhrases(SORT_PARAM);
    }

    @Override
    protected void onStart() {
        super.onStart();
        populatePhrases(SORT_PARAM);
    }

    @Override
    protected void onResume() {
        super.onResume();
        readPreferences();
        populatePhrases(SORT_PARAM);
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
    //Function for closing app on double press Back Button
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_dictionary, menu);
        manager = (SearchManager) getSystemService(SEARCH_SERVICE);
        MenuItem menuItem = menu.findItem(R.id.action_searc);
        searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setSearchableInfo(manager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(final String query) {
                if (query == null || query.equals("")) {
                    populatePhrases(SORT_PARAM);
                } else {
                    populatePhrases(query);
                }
                return true;
            }

            @Override
            public boolean onQueryTextSubmit(final String query) {
                if (searchView.isActivated() && query.equals("")) {
                    populatePhrases(SORT_PARAM);
                } else {
                    populatePhrases(query);
                }
                return true;
            }
        });
        return true;
    }



    /**************** Action Drawer *******************/
    @Override
    public boolean onItemClick(AdapterView<?> adapterView, View view, int i, long l, IDrawerItem iDrawerItem) {

        switch (iDrawerItem.getIdentifier()) {
            case 1:
                Intent intent = new Intent(PhrasesDictionary_activity.this, Dictionary_activity.class);
                startActivity(intent);
                break;
            case 2:
                populatePhrases(SORT_PARAM);
                break;

        }

        return true;
    }

    /*** ************* Toolbar Menu *******************/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.change_language:
                //сменa языка
                String s = FIRST_LANGUAGE;
                FIRST_LANGUAGE = SECOND_LANGUAGE;
                SECOND_LANGUAGE = s;

                Log.d("myLog", "-------Language changed: " + FIRST_LANGUAGE + "-" + SECOND_LANGUAGE + "--------");

                if (SORT_PARAM.equals(String.valueOf(SortingParameter.translation))) {
                    SORT_PARAM = String.valueOf(SortingParameter.primary_parameter);
                } else {
                    SORT_PARAM = String.valueOf(SortingParameter.translation);
                }
                populatePhrases(SORT_PARAM);
                break;

        }


        return super.onOptionsItemSelected(item);
    }

    private void init() {
        /**************** Init Preferences *******************/
        pref = getApplicationContext().getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        readPreferences();
        /**************** Init Toolbar *******************/
        Toolbar mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mActionBarToolbar);

        /**************** Init ActionDrawer *******************/
        leftDrawer = new LeftDrawer(mActionBarToolbar, this);


        /**************** Init Recycler View *******************/
        rv = (RecyclerView) findViewById(R.id.rv2);

        /**************** Init Floating Action Button *******************/
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.attachToRecyclerView(rv);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PhrasesDictionary_activity.this, AddNewPhrase_activity.class);
                startActivity(intent);
            }
        });
    }

    private void populatePhrases(String parameter) {
        /**************** Init DATABASE *******************/
        DatabaseHandler db = new DatabaseHandler(this);
        List<Phrase> phrases;
        if(parameter.equals(SORT_PARAM)){
            phrases = db.getAllPhrasesSortBy(SORT_PARAM, DEFAULT_TABLE);
        }
        else {
            phrases = db.searchPhrases(parameter.trim().toLowerCase(),SORT_PARAM, DEFAULT_TABLE);
        }

        RvPhrasesAdapter adapter = new RvPhrasesAdapter(phrases);
        rv.setAdapter(adapter);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
    }


    private void savePreferences() {
        Log.d("Mylog", "----------------------------Save changes-----------------------------");
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(PREFS_FIRST_LANGUAGE, FIRST_LANGUAGE);
        editor.putString(PREFS_SECOND_LANGUAGE, SECOND_LANGUAGE);
        editor.putString(PREFS_SORT_PARAM, SORT_PARAM);
        editor.putString(PREFS_DEFAULT_TABLE, DEFAULT_TABLE);

        editor.apply();
    }

    private void readPreferences() {
        Log.d("Mylog", "----------------------------Load changes-----------------------------");
        FIRST_LANGUAGE = pref.getString(PREFS_FIRST_LANGUAGE, "IL");
        SECOND_LANGUAGE = pref.getString(PREFS_SECOND_LANGUAGE, "RU");
        SORT_PARAM = pref.getString(PREFS_SORT_PARAM, String.valueOf(SortingParameter.primary_parameter));
        DEFAULT_TABLE = pref.getString(PREFS_DEFAULT_TABLE, "IL_RU");
    }

}