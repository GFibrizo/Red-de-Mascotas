package com.support.android.designlibdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.RadioButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SearchInAdoptionActivity extends AppCompatActivity {

    private static final String AGE_0_TO_6_MONTHS = "0-6meses";
    private static final String AGE_6_TO_12_MONTHS = "6-12meses";
    private static final String AGE_1_TO_3_YEARS = "1-3anios";
    private static final String AGE_3_TO_7_YEARS = "3-7anios";
    private static final String AGE_7_OR_MORE_YEARS = "masDe7Anios";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_in_adoption);

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
        RadioButton petTypeDog = (RadioButton)findViewById(R.id.radio_dog);
        RadioButton petTypeCat = (RadioButton)findViewById(R.id.radio_cat);
        CheckBox petGenderMale = (CheckBox)findViewById(R.id.check_male);
        CheckBox petGenderFemale = (CheckBox)findViewById(R.id.check_female);
        AutoCompleteTextView breed = (AutoCompleteTextView)findViewById(R.id.breed);
        CheckBox age0to6m = (CheckBox)findViewById(R.id.check_0to6m_age);
        CheckBox age6to12m = (CheckBox)findViewById(R.id.check_6to12m_age);
        CheckBox age1to3y = (CheckBox)findViewById(R.id.check_1to3y_age);
        CheckBox age3to7y = (CheckBox)findViewById(R.id.check_3to7y_age);
        CheckBox age7toMorey = (CheckBox)findViewById(R.id.check_7toMorey_age);


        // TODO: Faltan validaciones
        try {
            if (petTypeDog.isChecked()) {
                object.put("tipo", petTypeDog.getText());
            } else {
                object.put("tipo", petTypeCat.getText());
            }

            JSONArray genders = new JSONArray();
            if (petGenderMale.isChecked()) genders.put(petGenderMale.getText());
            if (petGenderFemale.isChecked()) genders.put(petGenderFemale.getText());
            object.put("sexos", genders);

            object.put("raza", breed.getText().toString());

            JSONArray ages = new JSONArray();
            if (age0to6m.isChecked()) ages.put(AGE_0_TO_6_MONTHS);
            if (age6to12m.isChecked()) ages.put(AGE_6_TO_12_MONTHS);
            if (age1to3y.isChecked()) ages.put(AGE_1_TO_3_YEARS);
            if (age3to7y.isChecked()) ages.put(AGE_3_TO_7_YEARS);
            if (age7toMorey.isChecked()) ages.put(AGE_7_OR_MORE_YEARS);
            object.put("edades", ages);

        } catch (JSONException e) {
            Log.e("Error al crear el JSON", e.getMessage());
        }
        Intent intent = new Intent(getApplicationContext(), SearchInAdoptionActivity2.class);
        intent.putExtra("data", object.toString());
        if (intent != null)
            startActivity(intent);

    }

}
