package com.myselfapps.rav.slovarik;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.melnykov.fab.FloatingActionButton;
import com.myselfapps.rav.slovarik.Handlers.DatabaseHandler;
import com.myselfapps.rav.slovarik.Objects.Word;


public class EditWord_activity extends AppCompatActivity {
    private SharedPreferences pref;
    private DatabaseHandler db;
    private Word word;
    private Toolbar mActionBarToolbar;
    private Spinner spinner3;
    private EditText primaryWord,secondaryWord, notes, transcriptionWord;
    private TextView secondaryWords;
    private RadioGroup rg1;
    private RadioButton rbNeuter,rbMale,rbFemale;
    private int selected_ID;
    private int gender_ID;
    private int spinner_ID;
    private String FIRST_LANGUAGE;
    private String SECOND_LANGUAGE;
    public static final String PREFS_NAME = "SLOVARIK_PREFS";
    public static final String PREFS_FIRST_LANGUAGE = "FIRST_LANGUAGE";
    public static final String PREFS_SECOND_LANGUAGE = "SECOND_LANGUAGE";
    private String[] genders;
    private String[] partOfSpeechData;

    @SuppressWarnings("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_word);
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.ll_addSameWords);
        linearLayout.setVisibility(View.GONE);
        primaryWord = (EditText) findViewById(R.id.et_PrimaryWord_AddPrase);
        secondaryWord = (EditText) findViewById(R.id.et_SecondaryWord);
        transcriptionWord  = (EditText) findViewById(R.id.et_TranscriptionWord);
        secondaryWords = (TextView) findViewById(R.id.tv_SecondaryWordHint);
        notes  = (EditText) findViewById(R.id.et_Notes);
        rg1 =(RadioGroup) findViewById(R.id.rg_Gender);
        rbNeuter = (RadioButton)rg1.findViewById(R.id.rb_Neuter);
        rbMale = (RadioButton)rg1.findViewById(R.id.rb_Male);
        rbFemale = (RadioButton)rg1.findViewById(R.id.rb_Female);
        rbNeuter.setId(R.id.rb_Neuter);
        rbMale.setId(R.id.rb_Male);
        rbFemale.setId(R.id.rb_Female);
        ImageButton ib_PrimaryFlag = (ImageButton) findViewById(R.id.ib_PrimaryFlag);
        ImageButton ib_SecondaryFlag = (ImageButton) findViewById(R.id.ib_SecondaryFlag);
        init();
        setFlags(ib_PrimaryFlag, FIRST_LANGUAGE);
        setFlags(ib_SecondaryFlag, SECOND_LANGUAGE);
        populateWord(selected_ID);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_word_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private void init() {

        db = new DatabaseHandler(this);
        /**************** Init Preferences *******************/
        pref = getApplicationContext().getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        readPreferences();

        /**************** Init Toolbar *******************/
        Toolbar mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mActionBarToolbar);

        Intent intent = getIntent();
        selected_ID = Integer.parseInt(intent.getStringExtra("selected_ID"));

        //leftDrawer = new LeftDrawer(mActionBarToolbar,this);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_addWord);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (primaryWord.getText().toString().equals("")) { //Check zero-fields
                    showAlert(1);
                } else if (secondaryWord.getText().toString().equals("")) {
                    showAlert(2);
                } else {
                    if (checkChanges()) {//Updating WORD
                        word.setId(selected_ID + "");
                        word.setPrimary(primaryWord.getText().toString());
                        word.setSecondary(secondaryWord.getText().toString());
                        word.setTranscription(transcriptionWord.getText().toString());
                        word.setNotes(notes.getText().toString());
                        String ss = (String) ((RadioButton) findViewById(gender_ID)).getText();
                        word.setGender(ss);
                        word.setPartOfSpeech(partOfSpeechData[spinner_ID]);
                    }

                    db.updateWord(word);

                    Intent intent = new Intent(EditWord_activity.this, ViewWord_activity.class);
                    intent.putExtra("selected_ID", word.getId());
                    startActivity(intent);
                }
            }
        });
        genders = new String[]{getResources().getString(R.string.Label_Gender_Neuter),
                getResources().getString(R.string.Label_Gender_Male),
                getResources().getString(R.string.Label_Gender_Female)};
        initSpinner();
    }

    private boolean checkChanges() {
        boolean result = false;

        if(!(primaryWord.getText().toString().equals(word.getPrimary()))){result = true;}
        else if(!(secondaryWord.getText().toString().equals(word.getSecondary()))){result = true;}
        else if(!(transcriptionWord.getText().toString().equals(word.getTranscription()))){result = true;}
        else if(!(notes.getText().toString().equals(word.getNotes()))){result = true;}
        else if(!(secondaryWord.getText().toString().equals(word.getSecondary()))){result = true;}
        else if(rg1.getCheckedRadioButtonId() != gender_ID ){
            result = true;
            gender_ID = rg1.getCheckedRadioButtonId();
        }
        else if(!(word.getPartOfSpeech().equals(partOfSpeechData[spinner_ID])) ){result = true;}

        return result;
    }

    private void readPreferences() {
        Log.d("Mylog", "----------------------------Load changes-----------------------------");
        FIRST_LANGUAGE = pref.getString(PREFS_FIRST_LANGUAGE, null);
        SECOND_LANGUAGE = pref.getString(PREFS_SECOND_LANGUAGE, null);
    }

    private void showAlert(int i) {
        String message = null;
        switch (i){
            case 1: message = getResources().getString(R.string.Alert_dialog_Invalid_Primary);
                break;
            case 2: message = getResources().getString(R.string.Alert_dialog_Invalid_Translation);
                break;
        }

        new AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.Alert_dialog_title))
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (primaryWord.getText().toString().equals(""))
                            primaryWord.setHintTextColor(getResources().getColor(R.color.red_hint));
                        if (secondaryWord.getText().toString().equals(""))
                            secondaryWord.setHintTextColor(getResources().getColor(R.color.red_hint));
                        if (transcriptionWord.getText().toString().equals(""))
                            transcriptionWord.setHintTextColor(getResources().getColor(R.color.red_hint));
                    }
                })

                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void populateWord(int id) {
        /**************** Init DATABASE *******************/
        DatabaseHandler db = new DatabaseHandler(this);
        word = db.getWordByID(id);
        primaryWord.setText(word.getPrimary());
        if(word.getTranscription() != null){
            transcriptionWord.setText(word.getTranscription());
        }
        secondaryWord.setText(word.getSecondary());
        setGender(word.getGender());
        setPartOfSpeech(word.getPartOfSpeech());

        if(word.getNotes() != null) {
            notes.setText(word.getNotes());
        }

    }

    private void setPartOfSpeech(String partOfSpeech) {
        spinner_ID = 0;
        for (int i = 0; i <partOfSpeechData.length ; i++) {
            if(partOfSpeech.equals(partOfSpeechData[i])){
                spinner_ID = i; break;}
        }
        spinner3.setSelection(spinner_ID);
    }

    private void setGender(String gender) {
        if(gender.equals(genders[0])){ gender_ID = R.id.rb_Neuter; }
        else if(gender.equals(genders[1])){gender_ID = R.id.rb_Male;}
        else if(gender.equals(genders[2])){gender_ID = R.id.rb_Female;}
        else {gender_ID = R.id.rb_Neuter;}
        rg1.check(gender_ID);
    }

    private void setFlags(ImageButton ib,String language) {
        switch (language){
            case "RU":
                ib.setBackgroundResource(R.drawable.ru_thumb);
                break;
            case "IL":
                ib.setBackgroundResource(R.drawable.il_thumb);
                break;
            case "US":
                ib.setBackgroundResource(R.drawable.us_thumb);
                break;
            case "EN":
                ib.setBackgroundResource(R.drawable.gb_thumb);
                break;
            case "UA":
                ib.setBackgroundResource(R.drawable.ua_thumb);
                break;
            case "FR":
                ib.setBackgroundResource(R.drawable.fr_thumb);
                break;
            case "GE":
                ib.setBackgroundResource(R.drawable.ge_thumb);
                break;
        }


    }

    private void initSpinner() {

        partOfSpeechData= new String[]{
                getResources().getString(R.string.PartsOfSpeech_NotSelected),
                getResources().getString(R.string.PartsOfSpeech_Adjective),
                getResources().getString(R.string.PartsOfSpeech_Adverb),
                getResources().getString(R.string.PartsOfSpeech_Numeral),
                getResources().getString(R.string.PartsOfSpeech_Preposition),
                getResources().getString(R.string.PartsOfSpeech_Pronoun),
                getResources().getString(R.string.PartsOfSpeech_Substantive),
                getResources().getString(R.string.PartsOfSpeech_Verb)};

        spinner3 = (Spinner) findViewById(R.id.spinner_PartsOfSpeech);

        // Spinner Part of Speech адаптер
        ArrayAdapter<String> adapter3 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, partOfSpeechData);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner3.setAdapter(adapter3);
        spinner3.setPrompt(getResources().getString(R.string.PartsOfSpeech));
        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                spinner_ID = position;

            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }


}
