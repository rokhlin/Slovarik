package com.myselfapps.rav.slovarik;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.melnykov.fab.FloatingActionButton;


public class AddNewPhrase_activity extends AppCompatActivity {
    private Toolbar mActionBarToolbar;
    private Spinner spinner;
    private EditText primary,secondary, notes, transcription,addlabel;
    private TextView labels;
    private Phrase phrase;
    private DatabaseHandler db;
    private SharedPreferences pref;

    private String FIRST_LANGUAGE ="IL";
    private String SECOND_LANGUAGE ="RU";
    private String pDictionary = FIRST_LANGUAGE+"-"+SECOND_LANGUAGE+"_"+"PHRASES";

    public static final String PREFS_NAME = "SLOVARIK_PREFS";
    public static final String PREFS_FIRST_LANGUAGE = "FIRST_LANGUAGE";
    public static final String PREFS_SECOND_LANGUAGE = "SECOND_LANGUAGE";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_phrase);
        initToolbar();
        initSpinner();
        pref = getApplicationContext().getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        initFloatingButton();

        primary = (EditText) findViewById(R.id.et_PrimaryWord_AddPrase);
        secondary = (EditText) findViewById(R.id.et_SecondaryPhrase);
        transcription  = (EditText) findViewById(R.id.et_Transcription_AddPrase);
        notes  = (EditText) findViewById(R.id.et_Notes_AddPrase);
        addlabel  = (EditText) findViewById(R.id.et_labels);
        labels = (TextView) findViewById(R.id.tv_show_labels);
        ImageButton ib_PrimaryFlag = (ImageButton) findViewById(R.id.ib_PrimaryFlag_AddPrase);
        ImageButton ib_SecondaryFlag = (ImageButton) findViewById(R.id.ib_SecondaryFlag_AddPrase);
        setFlags(ib_PrimaryFlag, FIRST_LANGUAGE);
        setFlags(ib_SecondaryFlag, SECOND_LANGUAGE);
        ImageButton addCategory = (ImageButton) findViewById(R.id.ib_AddNewCategory);
        addCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Добавить действие
            }
        });
        ImageButton addLabel = (ImageButton) findViewById(R.id.ib_AddNewLabel);
        addLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Добавить действие
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_new_word_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_addWord:
                //clearFields();
                Intent intent = new Intent(AddNewPhrase_activity.this, PhrasesDictionary_activity.class);
                startActivity(intent);
                break;
        }
        return true;
    }

    private void initSpinner() {

        final String[] spinnerData= {"Set Category","Shopping","Car", "Restaurant"};//Заменить на загрузку из таблицы

        spinner = (Spinner) findViewById(R.id.spinner_Category);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinnerData);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
        spinner.setPrompt(getResources().getString(R.string.Label_Category));
        spinner.setSelection(0);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    private void initToolbar() {
        mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mActionBarToolbar);

    }

    private void readPreferences() {
        FIRST_LANGUAGE = pref.getString(PREFS_FIRST_LANGUAGE, null);
        SECOND_LANGUAGE = pref.getString(PREFS_SECOND_LANGUAGE, null);
    }

    private void initFloatingButton() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_addPhrase);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (addSecondWord()) {
//                    try {
//                        db.addWords(words);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//
//                    clearFields();
//                    for (Word word : words) {
//                        Log.d("MyLog", "Words: " + word.getId() + " - " + word.getPrimary() + " - " + word.getSecondary());
//                    }
//                    Intent intent = new Intent(AddNewWord_activity.this, Dictionary_activity.class);
//                    startActivity(intent);
//                }

                if(addPhrase()){
                    try {
                        db = new DatabaseHandler(getApplicationContext());
                        db.addPhrase(phrase);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Intent intent = new Intent(AddNewPhrase_activity.this, PhrasesDictionary_activity.class);
                    startActivity(intent);

                }
            }
        });
    }

    private boolean addPhrase() {
        boolean res = false;
        String pPrimary = primary.getText().toString();
        String pTranscription = transcription.getText().toString();
        String pSecondary = secondary.getText().toString();
        String pNotes = notes.getText().toString();
        String pCategory = spinner.getSelectedItem().toString();
        if(pPrimary.equals("")){
            showAlert(1);
        }
        else if(pSecondary.equals("")){
            showAlert(2);
        }
        else {
            phrase = new Phrase(pPrimary,pTranscription,pSecondary,pCategory,pNotes,pDictionary);
            res = true;
        }
        return res;
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
                        if (primary.getText().toString().equals(""))
                            primary.setHintTextColor(getResources().getColor(R.color.red_hint));
                        if (secondary.getText().toString().equals(""))
                            secondary.setHintTextColor(getResources().getColor(R.color.red_hint));
                        if (transcription.getText().toString().equals(""))
                            transcription.setHintTextColor(getResources().getColor(R.color.red_hint));
                    }
                })

                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
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
}