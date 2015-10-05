package com.support.android.designlibdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PublishInAdoptionActivity3 extends AppCompatActivity {

    private JSONObject object = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            object = new JSONObject(getIntent().getStringExtra("data"));
        } catch (JSONException e) {
            Log.e("Error receiving intent", e.getMessage());
        }
        setContentView(R.layout.activity_publish_in_adoption3);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_publish_in_adoption_activity3, menu);
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


    public void finish(View view) {


        CheckBox sociable = (CheckBox)findViewById(R.id.sociable_check);
        CheckBox good_to_others = (CheckBox)findViewById(R.id.other_animals_check);
        CheckBox good_with_kids = (CheckBox)findViewById(R.id.with_kids_check);
        CheckBox companion = (CheckBox)findViewById(R.id.companion_check);

        CheckBox funny = (CheckBox)findViewById(R.id.plays_check);
        CheckBox quiet = (CheckBox)findViewById(R.id.quiet_check);
        CheckBox guardian = (CheckBox)findViewById(R.id.guardian_check);
        CheckBox agressive = (CheckBox)findViewById(R.id.agressive_check);


        try {
            JSONArray behavior = new JSONArray();
            behavior.put(sociable.isChecked());
            behavior.put(good_to_others.isChecked());
            behavior.put(good_with_kids.isChecked());
            behavior.put(companion.isChecked());
            behavior.put(funny.isChecked());
            behavior.put(quiet.isChecked());
            behavior.put(guardian.isChecked());
            behavior.put(agressive.isChecked());
            object.put("behavior", behavior);
            Log.e("Objeto a enviar", object.toString());
        } catch (JSONException e) {
            Log.e("Error al crear el JSON", e.getMessage());
        }




        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        Toast.makeText(getApplicationContext(), "Publicación creada", Toast.LENGTH_SHORT).show();
        if (intent != null)
            startActivity(intent);
    }

}
