package com.support.android.designlibdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

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

        AutoCompleteTextView hairColor1 = (AutoCompleteTextView) findViewById(R.id.hair_color1);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, Constants.hairColors);
        hairColor1.setAdapter(adapter1);

        AutoCompleteTextView hairColor2 = (AutoCompleteTextView) findViewById(R.id.hair_color2);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, Constants.hairColors);
        hairColor2.setAdapter(adapter2);

        AutoCompleteTextView eyes = (AutoCompleteTextView) findViewById(R.id.eyes_color);
        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, Constants.eyesColors);
        eyes.setAdapter(adapter3);
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
        CheckBox temp_meds = (CheckBox)findViewById(R.id.temp_meds_check);
        CheckBox cron_meds = (CheckBox)findViewById(R.id.cron_meds_check);
        EditText desc = (EditText) findViewById(R.id.pet_desc);
        AutoCompleteTextView hairColor1 = (AutoCompleteTextView) findViewById(R.id.hair_color1);
        AutoCompleteTextView hairColor2 = (AutoCompleteTextView) findViewById(R.id.hair_color2);
        AutoCompleteTextView eyesColor = (AutoCompleteTextView) findViewById(R.id.eyes_color);

        try {
            object.put("castrado", castrated.isChecked());
            object.put("necesita_hogar_transito", needs_transit_home.isChecked());
            object.put("medicamentos_temporales", temp_meds.isChecked());
            object.put("medicamentos_cronicos", cron_meds.isChecked());
            object.put("descripcion", desc.getText());
            object.put("color_principal", hairColor1);
            object.put("color_secundario", hairColor2);
            object.put("color_de_ojos", eyesColor);
        } catch (JSONException e) {
            Log.e("Error al crear el JSON", e.getMessage());
        }

        Intent intent = new Intent(getApplicationContext(), PublishInAdoptionActivity3.class);
        intent.putExtra("data", object.toString());
        if (intent != null)
            startActivity(intent);
    }
}
