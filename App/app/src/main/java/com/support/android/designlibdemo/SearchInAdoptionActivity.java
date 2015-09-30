package com.support.android.designlibdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.Switch;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import utils.Encoder;

import static utils.Constants.AGES;
import static utils.Constants.CATS;
import static utils.Constants.DOGS;

public class SearchInAdoptionActivity extends AppCompatActivity {

    Activity activity = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_in_adoption);

        AutoCompleteTextView breedTextView = (AutoCompleteTextView) findViewById(R.id.autocomplete_breed);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.autocomplete_list_item, DOGS);
        breedTextView.setAdapter(adapter);

        activity = this;
        RadioButton petTypeCat = (RadioButton) findViewById(R.id.radio_cat);
        petTypeCat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                AutoCompleteTextView breed = (AutoCompleteTextView) findViewById(R.id.autocomplete_breed);
                ArrayAdapter<String> adapter = null;
                if (isChecked) {
                    adapter = new ArrayAdapter<>(activity, android.R.layout.simple_dropdown_item_1line, CATS);
                } else {
                    adapter = new ArrayAdapter<>(activity, android.R.layout.simple_dropdown_item_1line, DOGS);
                }
                breed.setAdapter(adapter);
            }
        });

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_search);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search_in_adoption, menu);
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
        RadioButton petTypeDog = (RadioButton) findViewById(R.id.radio_dog);
        RadioButton petTypeCat = (RadioButton) findViewById(R.id.radio_cat);
        CheckBox petGenderMale = (CheckBox) findViewById(R.id.check_male);
        CheckBox petGenderFemale = (CheckBox)findViewById(R.id.check_female);
        AutoCompleteTextView breed = (AutoCompleteTextView) findViewById(R.id.autocomplete_breed);
        CheckBox age0to6m = (CheckBox) findViewById(R.id.check_0to6m_age);
        CheckBox age6to12m = (CheckBox) findViewById(R.id.check_6to12m_age);
        CheckBox age1to3y = (CheckBox) findViewById(R.id.check_1to3y_age);
        CheckBox age3to7y = (CheckBox) findViewById(R.id.check_3to7y_age);
        CheckBox age7toMorey = (CheckBox) findViewById(R.id.check_7toMorey_age);

        // TODO: Faltan validaciones
        try {
            if (petTypeDog.isChecked()) {
                object.put("type", petTypeDog.getText());
            } else if (petTypeCat.isChecked()) {
                object.put("type", petTypeCat.getText());
            }

            JSONArray genders = new JSONArray();
            if (petGenderMale.isChecked()) genders.put(petGenderMale.getText());
            if (petGenderFemale.isChecked()) genders.put(petGenderFemale.getText());
            object.put("genders", genders);

            object.put("breed", Encoder.encode(breed.getText().toString()));

            JSONArray ages = new JSONArray();
            if (age0to6m.isChecked()) ages.put(Encoder.encode(AGES[0]));
            if (age6to12m.isChecked()) ages.put(Encoder.encode(AGES[1]));
            if (age1to3y.isChecked()) ages.put(Encoder.encode(AGES[2]));
            if (age3to7y.isChecked()) ages.put(Encoder.encode(AGES[3]));
            if (age7toMorey.isChecked()) ages.put(Encoder.encode(AGES[4]));
            object.put("ages", ages);

        } catch (JSONException e) {
            Log.e("Error al crear el JSON", e.getMessage());
        }

        Intent intent = new Intent(getApplicationContext(), SearchInAdoptionActivity2.class);
        intent.putExtra("data", object.toString());
        startActivity(intent);
    }



}
