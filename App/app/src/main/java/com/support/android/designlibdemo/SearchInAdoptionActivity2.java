package com.support.android.designlibdemo;

import android.content.Intent;
import android.os.AsyncTask;
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

import com.support.android.designlibdemo.model.FiltrosBusquedaAdopcion;
import com.support.android.designlibdemo.model.Password;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import utils.LoginRequest;
import utils.SearchRequest;
import utils.SecurityHandler;

public class SearchInAdoptionActivity2 extends AppCompatActivity {

    private JSONObject object = null;
    FiltrosBusquedaAdopcion filters;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            object = new JSONObject(getIntent().getStringExtra("data"));
        } catch (JSONException e) {
            Log.e("Error receiving intent", e.getMessage());
        }

        setContentView(R.layout.activity_search_in_adoption_2);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_search);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search_in_adoption_2, menu);
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

    public void search(View view) {

        CheckBox sizeSmall = (CheckBox)findViewById(R.id.check_size_small);
        CheckBox sizeMedium = (CheckBox)findViewById(R.id.check_size_medium);
        CheckBox sizeLarge = (CheckBox)findViewById(R.id.check_size_large);
        CheckBox colorWhite = (CheckBox)findViewById(R.id.check_color_white);
        CheckBox colorGrey = (CheckBox)findViewById(R.id.check_color_grey);
        CheckBox colorBlack = (CheckBox)findViewById(R.id.check_color_black);
        CheckBox colorBrown = (CheckBox)findViewById(R.id.check_color_brown);
        CheckBox colorBeige = (CheckBox)findViewById(R.id.check_color_beige);
        CheckBox colorOrange = (CheckBox)findViewById(R.id.check_color_orange);
        CheckBox colorDun = (CheckBox)findViewById(R.id.check_color_dun);
        CheckBox colorOther = (CheckBox)findViewById(R.id.check_color_other);
        CheckBox eyeColorBlack = (CheckBox)findViewById(R.id.check_eye_color_black);
        CheckBox eyeColorBrown = (CheckBox)findViewById(R.id.check_eye_color_brown);
        CheckBox eyeColorGreen = (CheckBox)findViewById(R.id.check_eye_color_green);
        CheckBox eyeColorBlue = (CheckBox)findViewById(R.id.check_eye_color_blue);
        CheckBox eyeColorYellow = (CheckBox)findViewById(R.id.check_eye_color_yellow);
        CheckBox eyeColorOther = (CheckBox)findViewById(R.id.check_eye_color_other);
        AutoCompleteTextView city = (AutoCompleteTextView)findViewById(R.id.city);
        AutoCompleteTextView neighbourhood = (AutoCompleteTextView)findViewById(R.id.neighbourhood);


        // TODO: Faltan validaciones
        try {
            JSONArray sizes = new JSONArray();
            if (sizeSmall.isChecked()) sizes.put(sizeSmall.getText());
            if (sizeMedium.isChecked()) sizes.put(sizeMedium.getText());
            if (sizeLarge.isChecked()) sizes.put(sizeLarge.getText());
            object.put("tamanios", sizes);

            JSONArray colors = new JSONArray();
            if (colorWhite.isChecked()) colors.put(colorWhite.getText());
            if (colorGrey.isChecked()) colors.put(colorGrey.getText());
            if (colorBlack.isChecked()) colors.put(colorBlack.getText());
            if (colorBrown.isChecked()) colors.put(colorBrown.getText());
            if (colorBeige.isChecked()) colors.put(colorBeige.getText());
            if (colorOrange.isChecked()) colors.put(colorOrange.getText());
            if (colorDun.isChecked()) colors.put(colorDun.getText());
            if (colorOther.isChecked()) colors.put(colorOther.getText());
            object.put("colores", colors);

            JSONArray eyeColors = new JSONArray();
            if (eyeColorBlack.isChecked()) eyeColors.put(eyeColorBlack.getText());
            if (eyeColorBrown.isChecked()) eyeColors.put(eyeColorBrown.getText());
            if (eyeColorGreen.isChecked()) eyeColors.put(eyeColorGreen.getText());
            if (eyeColorBlue.isChecked()) eyeColors.put(eyeColorBlue.getText());
            if (eyeColorYellow.isChecked()) eyeColors.put(eyeColorYellow.getText());
            if (eyeColorOther.isChecked()) eyeColors.put(eyeColorOther.getText());
            object.put("coloresDeOjos", eyeColors);

            object.put("ciudad", city.getText().toString());
            object.put("barrio", neighbourhood.getText().toString());

        } catch (JSONException e) {
            Log.e("Error al crear el JSON", e.getMessage());
        }


        filters = new FiltrosBusquedaAdopcion(object);

        //Llamado a la clase interna que realiza el request
        QueryResultTask qTask = new QueryResultTask(filters);
        qTask.execute((Void) null);

    }
    public class QueryResultTask extends AsyncTask<Void, Void, Boolean> {

        FiltrosBusquedaAdopcion filters;
        JSONArray response;
        QueryResultTask( FiltrosBusquedaAdopcion filters) {
            this.filters = filters;
        }


        @Override
        protected Boolean doInBackground(Void... params) {
            //Llamado al request
            SearchRequest request = new SearchRequest(getApplicationContext());
            response = request.search(filters);
            return true;
        }


        @Override
        protected void onPostExecute(final Boolean success) {
            if(success){
                Intent intent = new Intent(getApplicationContext(), ResultListActivity.class);
                intent.putExtra("data", response.toString());
                if (intent != null)
                    startActivity(intent);
            }
        }

        @Override
        protected void onCancelled() {

        }


    }


}
