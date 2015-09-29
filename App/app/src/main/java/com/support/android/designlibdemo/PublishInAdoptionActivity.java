package com.support.android.designlibdemo;

import android.app.Notification;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.design.widget.TabLayout.Tab;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class PublishInAdoptionActivity extends AppCompatActivity {

    private TabLayout mTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_in_adoption);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_publish);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_publish_in_adoption, menu);
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


    @Override
    public void onResume() {
        super.onResume();

    }

    public void nextPage(View view) {
        JSONObject object = new JSONObject();
        EditText name = (EditText)findViewById(R.id.published_pet_name);
        Switch petType = (Switch)findViewById(R.id.switch_pet_type);
        Switch petGender = (Switch)findViewById(R.id.switch_pet_gender);
        AutoCompleteTextView breed = (AutoCompleteTextView)findViewById(R.id.breed);
        TextView age = (TextView) findViewById(R.id.age_label);
        TextView size = (TextView) findViewById(R.id.size_label);


        try {
            object.put("name", name.getText());

            if (petType.isChecked()) {
                object.put("type", petType.getTextOn());
            } else {
                object.put("type", petType.getTextOff());
            }

            if (petGender.isChecked()) {
                object.put("genero", petType.getTextOn());
            } else {
                object.put("genero", petType.getTextOff());
            }

            object.put("breed", breed.getText());
            object.put("age", age.getText());
            object.put("tamaño", size.getText());
            object.put("name", name.getText());

        } catch (JSONException e) {
            Log.e("Error al crear el JSON", e.getMessage());
        }
        Intent intent = new Intent(getApplicationContext(), PublishInAdoptionActivity2.class);
        intent.putExtra("data", object.toString());
        if (intent != null)
            startActivity(intent);
    }
}