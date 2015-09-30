package com.support.android.designlibdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;

import org.json.JSONException;
import org.json.JSONObject;

import utils.SpinnerArrayAdapter;

public class PublishInAdoptionActivity2 extends AppCompatActivity {

    private JSONObject object = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            object = new JSONObject(getIntent().getStringExtra("data"));
            Log.e("Object received", object.toString());
        } catch (JSONException e) {
            Log.e("Error receiving intent", e.getMessage());
        }
        setContentView(R.layout.activity_publish_in_adoption2);

        Spinner hairColor1Spinner = (Spinner) findViewById(R.id.spinner_hair_color1);
        ArrayAdapter<CharSequence> hairColor1Adapter = SpinnerArrayAdapter.createSpinnerArrayAdapter(this, utils.Constants.HAIR_COLORS, "Color principal");
        hairColor1Spinner.setAdapter(hairColor1Adapter);
        hairColor1Spinner.setSelection(hairColor1Adapter.getCount());

        Spinner hairColor2Spinner = (Spinner) findViewById(R.id.spinner_hair_color2);
        ArrayAdapter<CharSequence> hairColor2Adapter = SpinnerArrayAdapter.createSpinnerArrayAdapter(this, utils.Constants.HAIR_COLORS, "Color secundario");
        hairColor2Spinner.setAdapter(hairColor2Adapter);
        hairColor2Spinner.setSelection(hairColor2Adapter.getCount());

        Spinner eyeColorSpinner = (Spinner) findViewById(R.id.spinner_eye_color);
        ArrayAdapter<CharSequence> eyeColorAdapter = SpinnerArrayAdapter.createSpinnerArrayAdapter(this, utils.Constants.EYE_COLORS, "Color de ojos");
        eyeColorSpinner.setAdapter(eyeColorAdapter);
        eyeColorSpinner.setSelection(eyeColorAdapter.getCount());
    }

    /**********************************************************************************************/
    /**********************************************************************************************/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_publish_in_adoption_activity2, menu);
        return true;
    }

    /**********************************************************************************************/
    /**********************************************************************************************/

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

    /**********************************************************************************************/
    /**********************************************************************************************/

    public void nextPage(View view) {

        CheckBox castrated = (CheckBox)findViewById(R.id.castrated_check);
        CheckBox needs_transit_home = (CheckBox)findViewById(R.id.transit_home_check);
        RadioButton temp_meds = (RadioButton)findViewById(R.id.radio_temporary_medicine);
        RadioButton cron_meds = (RadioButton)findViewById(R.id.radio_chronic_medicine);
        EditText desc = (EditText) findViewById(R.id.pet_desc);
        Spinner hairColor1 = (Spinner) findViewById(R.id.spinner_hair_color1);
        Spinner hairColor2 = (Spinner) findViewById(R.id.spinner_hair_color2);
        Spinner eyesColor = (Spinner) findViewById(R.id.spinner_eye_color);

        try {
            object.put("castrado", castrated.isChecked());
            object.put("necesita_hogar_transito", needs_transit_home.isChecked());
            object.put("medicamentos_temporales", temp_meds.isChecked());
            object.put("medicamentos_cronicos", cron_meds.isChecked());
            object.put("descripcion", desc.getText());
            object.put("color_principal", hairColor1.getSelectedItem().toString());
            object.put("color_secundario", hairColor2.getSelectedItem().toString());
            object.put("color_de_ojos", eyesColor.getSelectedItem().toString());
        } catch (JSONException e) {
            Log.e("Error al crear el JSON", e.getMessage());
        }

        Intent intent = new Intent(getApplicationContext(), PublishInAdoptionActivity3.class);
        intent.putExtra("data", object.toString());
        if (intent != null)
            startActivity(intent);
    }
}
