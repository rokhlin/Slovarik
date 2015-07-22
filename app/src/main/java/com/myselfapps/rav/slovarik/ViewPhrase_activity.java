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


public class ViewPhrase_activity extends AppCompatActivity implements View.OnClickListener {

    private SharedPreferences pref;
    private DatabaseHandler db;
    private Phrase phrase;
    private TextView wPrimary,
            wTranslation,
            wTranscription,
            wNote,
            wCategory;

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
        setContentView(R.layout.activity_view_phrase);



        init();
        populate(selected_ID);

    }



    private void init() {
        /**************** Init Fields *******************/
        wCategory = (TextView) findViewById(R.id.tv_viewWord_category);
        wPrimary = (TextView) findViewById(R.id.tv_viewWord_primary);
        wTranslation = (TextView) findViewById(R.id.tv_viewWord_translation);
        wTranscription = (TextView) findViewById(R.id.tv_viewWord_transcription);
        wNote = (TextView) findViewById(R.id.tv_viewWord_notes);
        sameWords = (ListView) findViewById(R.id.iv_ViewWord_sameword);
        editButtons = (LinearLayout) findViewById(R.id.ll_viewWord_hiddenEditBottons);

        btnCancel = (ImageButton) findViewById(R.id.ib_viewWord_cancel);
        btnEdit = (ImageButton) findViewById(R.id.ib_viewWord_edit);
        btnDelete = (ImageButton) findViewById(R.id.ib_viewWord_delete);

        btnCancel.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        btnEdit.setOnClickListener(this);
        db = new DatabaseHandler(this);

        /**************** Init Preferences *******************/
        pref = getApplicationContext().getSharedPreferences(PREFS_NAME, MODE_PRIVATE);


        /**************** Init Toolbar *******************/
        Toolbar mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mActionBarToolbar);

        Intent intent = getIntent();
        selected_ID = Integer.parseInt(intent.getStringExtra("selected_ID"));

    }


    private void populate(int id) {
        /**************** Init DATABASE *******************/
        DatabaseHandler db = new DatabaseHandler(this);
        phrase = db.getPhraseByID(id);
        wPrimary.setText(phrase.getPrimary());
        if(phrase.getTranscription() != null){
            wTranscription.setText(phrase.getTranscription());
        }
        wTranslation.setText(phrase.getSecondary());
        wCategory.setText(phrase.getCategory());
        if(phrase.getNotes() != null) {
            wNote.setText(phrase.getNotes());
        }
        String labels = phrase.getLabel();
        if(phrase.getLabel()!= null) {// заполнить для заполнения меток Labels
            fillLabels(labels);
        }
    }

    private void fillLabels(String labels) {
        String[] strings = new String[0];
            if(labels != null) {
                strings = labels.split("<->");
            }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, strings);

        sameWords.setAdapter(adapter);
        sameWords.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               // прописать операции при нажатии на метку

            }
        });

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
                db.deletePhrase(selected_ID);

                intent = new Intent(ViewPhrase_activity.this, PhrasesDictionary_activity.class);
                startActivity(intent);
                break;
            case R.id.ib_viewWord_edit:
                intent = new Intent(this, EditPhrase_activity.class);
                intent.putExtra("selected_ID", phrase.getId());
                startActivity(intent);
                break;
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
                int id = item.getItemId();

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



}
