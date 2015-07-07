package com.myselfapps.rav.slovarik;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;


public class ViewWord extends AppCompatActivity implements View.OnClickListener {
    private SharedPreferences pref;
    private DatabaseHandler db;
    private Word word;
    private TextView wPrimary,
            wTranslation,
            wTranscription,
            wNote,
            wNoun,
            wPartOfSpeech,
            wGender,
            labelSameWords;

    private ImageButton btnEdit,
                btnDelete,
                btnCancel;
    private LinearLayout editButtons;
    private ListView sameWords;

    private String FIRST_LANGUAGE;
    private String SECOND_LANGUAGE;
    private String DEFAULT_TABLE;
    public static final String PREFS_NAME = "SLOVARIK_PREFS";
    public static final String PREFS_FIRST_LANGUAGE = "FIRST_LANGUAGE";
    public static final String PREFS_SECOND_LANGUAGE = "SECOND_LANGUAGE";
    public static final String PREFS_DEFAULT_TABLE = "DEFAULT_TABLE";

    private int selected_ID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_word);

        /**************** Init Fields *******************/
        wGender = (TextView) findViewById(R.id.tv_viewWord_gender);
        wPrimary = (TextView) findViewById(R.id.tv_viewWord_primary);
        wPartOfSpeech = (TextView) findViewById(R.id.tv_viewWord_partOfSpeech);
        wTranslation = (TextView) findViewById(R.id.tv_viewWord_translation);
        wTranscription = (TextView) findViewById(R.id.tv_viewWord_transcription);
        wNote = (TextView) findViewById(R.id.tv_viewWord_notes);
        wNoun = (TextView) findViewById(R.id.tv_viewWord_nouns);
        sameWords = (ListView) findViewById(R.id.iv_ViewWord_sameword);
        editButtons = (LinearLayout) findViewById(R.id.ll_viewWord_hiddenEditBottons);
        btnCancel = (ImageButton) findViewById(R.id.ib_viewWord_cancel);
        btnEdit = (ImageButton) findViewById(R.id.ib_viewWord_edit);
        btnDelete = (ImageButton) findViewById(R.id.ib_viewWord_delete);

        btnCancel.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        btnEdit.setOnClickListener(this);

        init();

        populateWord(selected_ID);
        fillSameWords(word.getPrimary(), word.getSecondary());


    }

    private void fillSameWords(String primary, String secondary) {
        /**************** Init DATABASE *******************/
        DatabaseHandler db = new DatabaseHandler(this);
        List<Word> words = null;

        try {
            words = db.getAllWordsBy(primary);
        }catch (Exception e){e.printStackTrace();}

        if(words != null ){
            for (Word w : words){
                if(w.getSecondary().equals(secondary)){
                    words.remove(w);
                    break;
                }
            }
            String[] translates = new String[words.size()];
            final Integer[] ids = new Integer[words.size()];
            for (int i = 0; i <words.size() ; i++) {
                translates[i] = words.get(i).getSecondary();
                ids[i] = Integer.parseInt(words.get(i).getId());
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_list_item_1, translates);

            sameWords.setAdapter(adapter);
            sameWords.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    populateWord(ids[position]);
                    fillSameWords(word.getPrimary(), word.getSecondary());
                }
            });

        }

    }

    private void populateWord(int id) {
        /**************** Init DATABASE *******************/
        DatabaseHandler db = new DatabaseHandler(this);
        word = db.getWordByID(id);
        wPrimary.setText(word.getPrimary());
        if(word.getTranscription() != null){
            wTranscription.setText(word.getTranscription());
        }
        wTranslation.setText(word.getSecondary());
        wGender.setText(word.getGender());
        wPartOfSpeech.setText(word.getPartOfSpeech());
        if(word.getGroup1()!= null) {
            wNoun.setText(word.getGroup1());
        }
        if(word.getNotes() != null) {
            wNote.setText(word.getNotes());
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_word, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.item_edit_word) {
            if(editButtons.getVisibility()==View.GONE) {
                editButtons.setVisibility(View.VISIBLE);
            }
            else if(editButtons.getVisibility()==View.VISIBLE) {
                editButtons.setVisibility(View.GONE);
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void init() {

        db = new DatabaseHandler(this);
        /**************** Init Preferences *******************/
        pref = getApplicationContext().getSharedPreferences(PREFS_NAME, MODE_PRIVATE);


        /**************** Init Toolbar *******************/
        Toolbar mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mActionBarToolbar);

        Intent intent = getIntent();
        selected_ID = Integer.parseInt(intent.getStringExtra("selected_ID"));

    }

    private void readPreferences() {
        Log.d("Mylog", "----------------------------Load changes-----------------------------");
        FIRST_LANGUAGE = pref.getString(PREFS_FIRST_LANGUAGE, null);
        SECOND_LANGUAGE = pref.getString(PREFS_SECOND_LANGUAGE, null);
    }



    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.ib_viewWord_cancel:
                editButtons.setVisibility(View.GONE);
                break;
            case R.id.ib_viewWord_delete:
                db.deleteWord(selected_ID);

                intent = new Intent(ViewWord.this, Dictionary_activity.class);
                startActivity(intent);
                break;
            case R.id.ib_viewWord_edit:
                intent = new Intent(this, EditWord_activity.class);
                intent.putExtra("selected_ID", word.getId());
                startActivity(intent);
                break;
        }
    }
}
