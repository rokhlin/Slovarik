package com.myselfapps.rav.slovarik;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.xml.sax.SAXException;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;


public class BuckupManager_activity extends AppCompatActivity implements View.OnClickListener {
    private Button read,create, load;
    private TextView message;
    private String path;
    private XmlHelper xml;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buckup_manager);
        create =(Button)findViewById(R.id.btn_create_backup);
        read = (Button) findViewById(R.id.btn_read_backup);
        load = (Button) findViewById(R.id.btn_load_backup);
        message = (TextView) findViewById(R.id.tvMessage);
        create.setOnClickListener(this);
        read.setOnClickListener(this);
        load.setOnClickListener(this);
        xml = new XmlHelper(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_buckup_manager_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_create_backup:
                try {
                    xml.create();
                    path = xml.getPath();
                    message.setText("Done"+"\n"+path);
                } catch (TransformerException | IOException | ParserConfigurationException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btn_read_backup:
                try {
                    message.setText(xml.read(path));
                } catch (IOException | SAXException e) {
                    e.printStackTrace();
                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btn_load_backup:
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("application/xml");
                startActivityForResult(intent,1);

                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode){
            case 1:
                if(resultCode==RESULT_OK){
                    try {
                        xml.setPath(data.getData().getPath());
                        message.setText(xml.load());
                    } catch (ParserConfigurationException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (SAXException e) {
                        e.printStackTrace();
                    }

                }
                break;

        }
    }
}
