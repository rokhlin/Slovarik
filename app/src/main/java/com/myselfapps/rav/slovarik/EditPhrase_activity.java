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

import java.util.List;


public class EditPhrase_activity extends AppCompatActivity implements View.OnClickListener {
    private ImageButton addLabel, addLabel2;
    private FloatingActionButton fab;
    private Spinner spinner;
    private List<String> spinnerData;
    private EditText primary,secondary, notes, transcription,etlabel;
    private TextView tvLabels;
    private Phrase phrase;
    private int selected_ID;
    private boolean pressState = false;
    private DatabaseHandler db;
    private SharedPreferences pref;
    private List<Label> labels;
    private String pLabels = null;
    String pPrimary,pTranscription,pSecondary,pNotes,pCategory;
    private String FIRST_LANGUAGE ="IL";
    private String SECOND_LANGUAGE ="RU";

    private String DEFAULT_TABLE = "IL-RU_PHRASES";
    public static final String PREFS_NAME = "SLOVARIK_PREFS";
    public static final String PREFS_FIRST_LANGUAGE = "FIRST_LANGUAGE";
    public static final String PREFS_SECOND_LANGUAGE = "SECOND_LANGUAGE";
    public static final String PREFS_DEFAULT_TABLE = "DEFAULT_TABLE";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_phrase);




        primary = (EditText) findViewById(R.id.et_PrimaryWord_AddPrase);
        secondary = (EditText) findViewById(R.id.et_SecondaryPhrase);
        transcription  = (EditText) findViewById(R.id.et_Transcription_AddPrase);
        notes  = (EditText) findViewById(R.id.et_Notes_AddPrase);
        etlabel  = (EditText) findViewById(R.id.et_labels);
        tvLabels = (TextView) findViewById(R.id.tv_hint_addNewPhrase);

        initToolbar();
        initSpinner();
        initLabels();
        pref = getApplicationContext().getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        readPreferences();
        initFloatingButton();
        Intent intent = getIntent();
        selected_ID = Integer.parseInt(intent.getStringExtra("selected_ID"));
        populate(selected_ID);


        final ImageButton ib_PrimaryFlag = (ImageButton) findViewById(R.id.ib_PrimaryFlag_AddPrase);
        final ImageButton ib_SecondaryFlag = (ImageButton) findViewById(R.id.ib_SecondaryFlag_AddPrase);
        setFlags(ib_PrimaryFlag, FIRST_LANGUAGE);
        setFlags(ib_SecondaryFlag, SECOND_LANGUAGE);
        addLabel = (ImageButton) findViewById(R.id.ib_AddNewLabel);
        addLabel2 = (ImageButton) findViewById(R.id.ib_AddNewLabel2);
        addLabel.setOnClickListener(this);
        addLabel2.setOnClickListener(this);
    }

    private void populate(int selected_ID) {
        db = new DatabaseHandler(this);
        phrase = db.getPhraseByID(selected_ID);
        primary.setText(phrase.getPrimary());
        secondary.setText(phrase.getSecondary());
        transcription.setText(phrase.getTranscription());
        notes.setText(phrase.getNotes());
        for (int i = 0; i <spinnerData.size(); i++){
                if(spinnerData.get(i).toLowerCase().equals(phrase.getCategory())){
                spinner.setSelection(i);
                break;
            }
        }

        setAddedLabels();
    }

    private void setAddedLabels() {
        pLabels = phrase.getLabel();
        if(pLabels != null) {
            String[] strings = pLabels.split("<->");
            String str = getResources().getString(R.string.Label_Labels2)+" [";
            for (String s : strings) {
                if(str.equals(getResources().getString(R.string.Label_Labels2)+" [")) str += s;
                else str +=", " + s;
            }
            tvLabels.setText(str + "]");
            etlabel.setText("");
        }
    }

    private boolean checkLabel(String string) {
        boolean foundInBase = false;
        for (int i = 0; i <labels.size() ; i++) {
            if(labels.get(i).getName().equals(string)){
                foundInBase = true;
                break;
            }
        }
        return foundInBase;
    }


    private void initLabels() {
        db = new DatabaseHandler(this);
        if (db.getLabelCount()<=0) {
            String[] baseLabels = new String[]{"my", "your", "eat", "verb", "exception"};// заменить потом на значения из ресурсов
            for (String baseLabel : baseLabels) {
                db.addCategory(new Category(baseLabel));//?????
            }
        }
        labels = db.getAllLabels();

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
                Intent intent = new Intent(EditPhrase_activity.this, PhrasesDictionary_activity.class);
                startActivity(intent);
                break;
        }
        return true;
    }

    private void initSpinner() {
        db = new DatabaseHandler(this);
        if (db.getCategoryCount()<=0){
            String[] baseCategories = new String[]{
                    getResources().getString(R.string.Base_Category_Unsorted),
                    getResources().getString(R.string.Base_Category_Answers),
                    getResources().getString(R.string.Base_Category_Questions),
                    getResources().getString(R.string.Base_Category_Official),
                    getResources().getString(R.string.Base_Category_Lingo)};
            for (String baseCategory : baseCategories) {
                db.addCategory(new Category(baseCategory));
            }
        }

        spinnerData=  db.getAllNamesOfCategory();

        spinner = (Spinner) findViewById(R.id.spinner_Category);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinnerData);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
        spinner.setPrompt(getResources().getString(R.string.Label_Category));
        //spinner.setSelection(0);
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
        Toolbar mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mActionBarToolbar);

    }

    private void readPreferences() {
        FIRST_LANGUAGE = pref.getString(PREFS_FIRST_LANGUAGE, null);
        SECOND_LANGUAGE = pref.getString(PREFS_SECOND_LANGUAGE, null);
        DEFAULT_TABLE = pref.getString(PREFS_DEFAULT_TABLE, "IL-RU_PHRASES");
    }

    private void initFloatingButton() {
        fab = (FloatingActionButton) findViewById(R.id.fab_addPhrase);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editPhrase()) {
                    try {
                        db = new DatabaseHandler(getApplicationContext());
                        db.updatePhrase(phrase);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                Intent intent = new Intent(EditPhrase_activity.this, PhrasesDictionary_activity.class);
                startActivity(intent);
            }
        });
    }

    private boolean checkChanges() {
        boolean result = false;
        if(!(pPrimary.equals(phrase.getPrimary()))){result = true;}
        else if(!(pSecondary.equals(phrase.getSecondary()))){result = true;}
        else if(!(pTranscription.equals(phrase.getTranscription()))){result = true;}
        else if(!(pNotes.equals(phrase.getNotes()))){result = true;}
        else if(!(pCategory.equals(phrase.getCategory()))){result = true;}
        return result;
    }

    private boolean editPhrase() {
        boolean res = false;
        pPrimary = primary.getText().toString().trim().toLowerCase();
        pTranscription = transcription.getText().toString().trim().toLowerCase();
        pSecondary = secondary.getText().toString().trim().toLowerCase();
        pNotes = notes.getText().toString().trim().toLowerCase();
        pCategory = spinner.getSelectedItem().toString().trim().toLowerCase();
        if(pPrimary.equals("")){
            showAlert(1);
        }
        else if(pSecondary.equals("")){
            showAlert(2);
        }
        else {
            if(checkChanges()) {
                phrase = new Phrase(pPrimary, pTranscription, pSecondary, pCategory, pLabels, pNotes, DEFAULT_TABLE);
                phrase.setId(selected_ID+"");
                res = true;
            }
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

    //Adding Labels
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ib_AddNewLabel:
                if(pressState){
                    String newLabel = etlabel.getText().toString().trim();
                    if (!checkLabel(newLabel) && !newLabel.equals("")){
                        db.addLabel(new Label(newLabel,DEFAULT_TABLE));
                    }

                    if(pLabels != null && !newLabel.equals("")){
                        pLabels = pLabels +"<->"+newLabel;
                    }
                    else {
                        pLabels = newLabel;
                    }

                    setAddedLabels();
                    pressState = false;
                    etlabel.setVisibility(View.GONE);
                    tvLabels.setVisibility(View.VISIBLE);
                    fab.setVisibility(View.VISIBLE);
                    addLabel.setVisibility(View.GONE);
                    addLabel2.setVisibility(View.VISIBLE);

                }
                break;
            case R.id.ib_AddNewLabel2:
                addLabel2.setVisibility(View.GONE);
                addLabel.setVisibility(View.VISIBLE);
                pressState = true;
                etlabel.setVisibility(View.VISIBLE);
                etlabel.setFocusable(true);
                etlabel.requestFocus();
                tvLabels.setVisibility(View.GONE);
                fab.setVisibility(View.GONE);
                break;
        }
    }


}
