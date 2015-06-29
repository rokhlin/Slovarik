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
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;


public class ViewWord extends AppCompatActivity implements View.OnLongClickListener, View.OnClickListener {
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
                btnCancel,
                btnMainWord;
    private LinearLayout editButtons;


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
        wPrimary.setOnLongClickListener(this);
        wPartOfSpeech = (TextView) findViewById(R.id.tv_viewWord_partOfSpeech);
        wTranslation = (TextView) findViewById(R.id.tv_viewWord_translation);
        wTranslation.setOnLongClickListener(this);
        wTranscription = (TextView) findViewById(R.id.tv_viewWord_transcription);
        wNote = (TextView) findViewById(R.id.tv_viewWord_notes);
        wNote.setOnLongClickListener(this);
        wNoun = (TextView) findViewById(R.id.tv_viewWord_nouns);
        labelSameWords = (TextView) findViewById(R.id.tv_viewWord_labelSameWords);

        editButtons = (LinearLayout) findViewById(R.id.ll_viewWord_hiddenEditBottons);
        btnCancel = (ImageButton) findViewById(R.id.ib_viewWord_cancel);
        btnEdit = (ImageButton) findViewById(R.id.ib_viewWord_edit);
        btnDelete = (ImageButton) findViewById(R.id.ib_viewWord_delete);
        btnMainWord = (ImageButton) findViewById(R.id.ib_viewWord_mainWord);
        btnMainWord.setTag("1");

        btnMainWord.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnDelete.setOnClickListener(this);

        init();

        populateWord(selected_ID);


    }

    private void populateWord(int id) {
        /**************** Init DATABASE *******************/
        DatabaseHandler db = new DatabaseHandler(this);
        Word word = db.getWordByID(id);
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
        if (id == R.id.action_settings) {
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
    public boolean onLongClick(View v) {
        String strOld;

        if(v instanceof TextView) {
            switch (v.getId()) {
                case R.id.tv_viewWord_primary:
                    editButtons.setVisibility(View.VISIBLE);
                    break;
                default:
                   //открыть диалог с предложением ввести новое значение --старое значение должно быть уже внесено
                   //сравнить значение со strOld в случае если есть разница, тогда произвести обновление в бд
            }
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.ib_viewWord_mainWord:
                //дописать логику сохранения ключевого слова
                if(btnMainWord.getTag() == "1") {
                    btnMainWord.setImageResource(R.drawable.star_unchecked);
                    btnMainWord.setTag("2");
                }
                else {
                    btnMainWord.setImageResource(R.drawable.star_checked);
                    btnMainWord.setTag("1");
                }
                break;

            case R.id.ib_viewWord_cancel:
                editButtons.setVisibility(View.GONE);
                break;
            case R.id.ib_viewWord_delete:
                db.deleteWord(selected_ID);

                Intent intent = new Intent(ViewWord.this, Dictionary_activity.class);
                startActivity(intent);
                break;
        }
    }
}
