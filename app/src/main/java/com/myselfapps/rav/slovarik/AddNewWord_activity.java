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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.melnykov.fab.FloatingActionButton;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.myselfapps.rav.slovarik.Handlers.DatabaseHandler;
import com.myselfapps.rav.slovarik.Handlers.LeftDrawer;
import com.myselfapps.rav.slovarik.Objects.Word;

import java.util.ArrayList;


public class AddNewWord_activity extends AppCompatActivity implements Drawer.OnDrawerItemClickListener {
    private Toolbar mActionBarToolbar;
    private LeftDrawer leftDrawer;
    private Spinner spinner3;
    private EditText primaryWord,secondaryWord, notes, transcriptionWord;
    private TextView secondaryWords;
    private RadioGroup rg1;
    private ArrayList<Word> words = new ArrayList<>();
    private DatabaseHandler db;
    private SharedPreferences pref;

    private String FIRST_LANGUAGE ="IL";
    private String SECOND_LANGUAGE ="RU";

    public static final String PREFS_NAME = "SLOVARIK_PREFS";
    public static final String PREFS_FIRST_LANGUAGE = "FIRST_LANGUAGE";
    public static final String PREFS_SECOND_LANGUAGE = "SECOND_LANGUAGE";

    int[] spinner_1_data = {R.drawable.ru_thumb, R.drawable.il_thumb};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_word);
        initToolbar();
        pref = getApplicationContext().getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        readPreferences();
        initFloatingButton();


        primaryWord = (EditText) findViewById(R.id.et_PrimaryWord_AddPrase);
        secondaryWord = (EditText) findViewById(R.id.et_SecondaryWord);
        transcriptionWord  = (EditText) findViewById(R.id.et_TranscriptionWord);
        secondaryWords = (TextView) findViewById(R.id.tv_SecondaryWordHint);
        notes  = (EditText) findViewById(R.id.et_Notes);
        rg1 =(RadioGroup) findViewById(R.id.rg_Gender);
        ImageButton ib_PrimaryFlag = (ImageButton) findViewById(R.id.ib_PrimaryFlag);
        ImageButton ib_SecondaryFlag = (ImageButton) findViewById(R.id.ib_SecondaryFlag);
        setFlags(ib_PrimaryFlag, FIRST_LANGUAGE);
        setFlags(ib_SecondaryFlag, SECOND_LANGUAGE);
        ImageButton addSecondaryWord = (ImageButton) findViewById(R.id.ib_AddNewSecondaryWord);
        addSecondaryWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addSecondWord(v);
            }
        });


        initSpinner();

         db = new DatabaseHandler(this);
    }

    private void initFloatingButton() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_addWord);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean state = addSecondWord(v);
                if(state || (!state && words.size() > 0)){
                    try {
                        db.addWords(words);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    clearFields();
                    for (Word word : words) {
                        Log.d("MyLog", "Words: " + word.getId() + " - " + word.getPrimary() + " - " + word.getSecondary());
                    }
                    Intent intent = new Intent(AddNewWord_activity.this, Dictionary_activity.class);
                    startActivity(intent);
                }
            }
        });
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

    private boolean addSecondWord(View v) {
        boolean res = false;
        int selectedId = rg1.getCheckedRadioButtonId();
        // find the radiobutton by returned id
        RadioButton radioSexButton = (RadioButton) findViewById(selectedId);
        String gender = radioSexButton.getText().toString().trim().toLowerCase();
        String partOfSpeech = spinner3.getSelectedItem().toString().toLowerCase();

        String primary = String.valueOf(primaryWord.getText()).trim().toLowerCase();
        String secondary = String.valueOf(secondaryWord.getText()).trim().toLowerCase();
        String transcription = String.valueOf(transcriptionWord.getText()).trim().toLowerCase();

        String note = String.valueOf(notes.getText()).trim().toLowerCase();
        if(primary.equals("")){
            showAlert(1);
        }
        else if(secondary.equals("")){
            showAlert(2);
        }
        else {
            words.add(new Word(primary, transcription, secondary, gender, partOfSpeech, note));

            //Clear fields
            secondaryWord.setText("");
            rg1.check(R.id.rb_Neuter);
            spinner3.setSelection(0);


            setSecondaryWordHint();
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

    private void readPreferences() {
        FIRST_LANGUAGE = pref.getString(PREFS_FIRST_LANGUAGE, null);
        SECOND_LANGUAGE = pref.getString(PREFS_SECOND_LANGUAGE, null);
    }

    private void setSecondaryWordHint() {
        String txt = "";
        for (Word w : words){
            txt += w.getSecondary()+", ";
        }
        secondaryWords.setText(txt);
    }

    private void initSpinner() {

        final String[] partOfSpeechData= {getResources().getString(R.string.PartsOfSpeech_NotSelected),
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

    private void clearFields() {
        primaryWord.setText("");
        transcriptionWord.setText("");
        secondaryWord.setText("");
        notes.setText("");
        secondaryWords.setText(String.valueOf(R.string.Label_SecondaryWordHint));
        rg1.check(R.id.rb_Neuter);
        spinner3.setSelection(0);

    }

    @Override
    public void onBackPressed(){
            super.onBackPressed();
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
                clearFields();
                Intent intent = new Intent(AddNewWord_activity.this, Dictionary_activity.class);
                startActivity(intent);
                break;
        }
        return true;
    }

    @Override
    public boolean onItemClick(AdapterView<?> adapterView, View view, int i, long l, IDrawerItem iDrawerItem) {
        Intent intent;
        switch (i) {
            case 0:
                intent = new Intent(AddNewWord_activity.this, Dictionary_activity.class);
                break;
            case 1:
                intent = new Intent(AddNewWord_activity.this, Verbs_activity.class);
                break;
            case 2:
                intent = new Intent(AddNewWord_activity.this, Verbs_activity.class);
                break;
            default:
                intent = new Intent(AddNewWord_activity.this, Dictionary_activity.class);
                break;
        }
        startActivity(intent);
        return true;
    }
}
