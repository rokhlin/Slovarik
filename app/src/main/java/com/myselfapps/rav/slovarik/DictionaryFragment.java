package com.myselfapps.rav.slovarik;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.melnykov.fab.FloatingActionButton;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.util.List;


public class DictionaryFragment extends Fragment implements Drawer.OnDrawerItemClickListener{
    View view;
    FragmentTransaction ft;
    AddNewWord_Fragment addNewWord_fragment;
    @Override
    public boolean onItemClick(AdapterView<?> adapterView, View view, int i, long l, IDrawerItem iDrawerItem) {
        return false;
    }

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       view = inflater.inflate(R.layout.activity_dictionary,container,false);

        init();
        populateWords(SORT_PARAM);

        return view;
    }

    private void init() {

        /**************** Init Preferences *******************/
        pref = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);


        /**************** Init Toolbar *******************/
        Toolbar mActionBarToolbar = (Toolbar)view.findViewById(R.id.toolbar);
       // getActivity().setSupportActionBar(mActionBarToolbar);
         mActionBarToolbar.setTitle("111");
        mActionBarToolbar.inflateMenu(R.menu.menu_dictionary);
        /**************** Init ActionDrawer *******************/
        leftDrawer = new LeftDrawer(mActionBarToolbar, getActivity());

        /**************** Init Recycler View *******************/
        rv = (RecyclerView)view.findViewById(R.id.rv);

        /**************** Init Floating Action Button *******************/
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.attachToRecyclerView(rv);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.ll_dictionary_layout, addNewWord_fragment);
                ft.addToBackStack("");
                ft.commit();
            }
        });
    }

    private void populateWords() {
        /**************** Init DATABASE *******************/
        DatabaseHandler db = new DatabaseHandler(getActivity());

        List<Word> words = db.getAllWords();
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);
        RecyclerViewWordAdapter adapter = new RecyclerViewWordAdapter(words);
        rv.setAdapter(adapter);

    }

    private void populateWords(String parameter) {
        /**************** Init DATABASE *******************/
        DatabaseHandler db = new DatabaseHandler(getActivity());

        List<Word> words = db.getAllWordsSortBy(parameter);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);

        RecyclerViewWordAdapter adapter = new RecyclerViewWordAdapter(words);
        rv.setAdapter(adapter);

    }


    @Override
    public void onStart() {
        super.onStart();
        populateWords();
    }

    @Override
    public void onResume() {
        super.onResume();
        readPreferences();
        populateWords(SORT_PARAM);
    }

    @Override
    public void onPause() {
        super.onPause();
        savePreferences();
    }

    @Override
    public void onStop() {
        super.onStop();
        savePreferences();
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
        FIRST_LANGUAGE = pref.getString(PREFS_FIRST_LANGUAGE, null);
        SECOND_LANGUAGE = pref.getString(PREFS_SECOND_LANGUAGE, null);
        SORT_PARAM = pref.getString(PREFS_SORT_PARAM, String.valueOf(SortingParameter.primary_word));
    }

    /**************** Toolbar Menu *******************/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()){
            case R.id.action_search:
//                Intent intent = new Intent(Dictionary_activity.this, Verbs_activity.class);
//                startActivity(intent);
                break;
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

}
