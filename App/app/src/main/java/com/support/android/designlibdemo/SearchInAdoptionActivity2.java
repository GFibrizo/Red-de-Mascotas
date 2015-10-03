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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;

import com.support.android.designlibdemo.model.SearchForAdoptionFilters;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import utils.Encoder;
import utils.SearchRequest;

import static utils.Constants.CITIES;
import static utils.Constants.NEIGHBOURHOODS;

public class SearchInAdoptionActivity2 extends AppCompatActivity {

    private JSONObject object = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            object = new JSONObject(getIntent().getStringExtra("data"));
        } catch (JSONException e) {
            Log.e("Error receiving intent", e.getMessage());
        }

        setContentView(R.layout.activity_search_in_adoption_2);

        AutoCompleteTextView cityTextView = (AutoCompleteTextView) findViewById(R.id.autocomplete_city);
        ArrayAdapter<String> cityAdapter = new ArrayAdapter<>(this, R.layout.autocomplete_list_item, CITIES);
        cityTextView.setAdapter(cityAdapter);

        AutoCompleteTextView neighbourhoodTextView = (AutoCompleteTextView) findViewById(R.id.autocomplete_neighbourhood);
        ArrayAdapter<String> neighbourhoodAdapter = new ArrayAdapter<>(this, R.layout.autocomplete_list_item, NEIGHBOURHOODS);
        neighbourhoodTextView.setAdapter(neighbourhoodAdapter);

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
        CheckBox sizeSmall = (CheckBox) findViewById(R.id.check_size_small);
        CheckBox sizeMedium = (CheckBox) findViewById(R.id.check_size_medium);
        CheckBox sizeLarge = (CheckBox) findViewById(R.id.check_size_large);
        CheckBox colorWhite = (CheckBox) findViewById(R.id.check_color_white);
        CheckBox colorGrey = (CheckBox) findViewById(R.id.check_color_grey);
        CheckBox colorBlack = (CheckBox) findViewById(R.id.check_color_black);
        CheckBox colorBrown = (CheckBox) findViewById(R.id.check_color_brown);
        CheckBox colorBeige = (CheckBox) findViewById(R.id.check_color_beige);
        CheckBox colorOrange = (CheckBox) findViewById(R.id.check_color_orange);
        CheckBox colorDun = (CheckBox) findViewById(R.id.check_color_dun);
        CheckBox colorOther = (CheckBox) findViewById(R.id.check_color_other);
        CheckBox eyeColorBlack = (CheckBox) findViewById(R.id.check_eye_color_black);
        CheckBox eyeColorBrown = (CheckBox) findViewById(R.id.check_eye_color_brown);
        CheckBox eyeColorGreen = (CheckBox) findViewById(R.id.check_eye_color_green);
        CheckBox eyeColorBlue = (CheckBox) findViewById(R.id.check_eye_color_blue);
        CheckBox eyeColorYellow = (CheckBox) findViewById(R.id.check_eye_color_yellow);
        CheckBox eyeColorOther = (CheckBox) findViewById(R.id.check_eye_color_other);
        AutoCompleteTextView city = (AutoCompleteTextView) findViewById(R.id.autocomplete_city);
        AutoCompleteTextView neighbourhood = (AutoCompleteTextView) findViewById(R.id.autocomplete_neighbourhood);

        try {
            JSONArray sizes = new JSONArray();
            if (sizeSmall.isChecked()) sizes.put(sizeSmall.getText());
            if (sizeMedium.isChecked()) sizes.put(sizeMedium.getText());
            if (sizeLarge.isChecked()) sizes.put(sizeLarge.getText());
            object.put("sizes", sizes);

            JSONArray colors = new JSONArray();
            if (colorWhite.isChecked()) colors.put(Encoder.encode(colorWhite.getText().toString()));
            if (colorGrey.isChecked()) colors.put(Encoder.encode(colorGrey.getText().toString()));
            if (colorBlack.isChecked()) colors.put(Encoder.encode(colorBlack.getText().toString()));
            if (colorBrown.isChecked()) colors.put(Encoder.encode(colorBrown.getText().toString()));
            if (colorBeige.isChecked()) colors.put(Encoder.encode(colorBeige.getText().toString()));
            if (colorOrange.isChecked()) colors.put(Encoder.encode(colorOrange.getText().toString()));
            if (colorDun.isChecked()) colors.put(Encoder.encode(colorDun.getText().toString()));
            if (colorOther.isChecked()) colors.put(Encoder.encode(colorOther.getText().toString()));
            object.put("colors", colors);

            JSONArray eyeColors = new JSONArray();
            if (eyeColorBlack.isChecked()) eyeColors.put(Encoder.encode(eyeColorBlack.getText().toString()));
            if (eyeColorBrown.isChecked()) eyeColors.put(Encoder.encode(eyeColorBrown.getText().toString()));
            if (eyeColorGreen.isChecked()) eyeColors.put(Encoder.encode(eyeColorGreen.getText().toString()));
            if (eyeColorBlue.isChecked()) eyeColors.put(Encoder.encode(eyeColorBlue.getText().toString()));
            if (eyeColorYellow.isChecked()) eyeColors.put(Encoder.encode(eyeColorYellow.getText().toString()));
            if (eyeColorOther.isChecked()) eyeColors.put(Encoder.encode(eyeColorOther.getText().toString()));
            object.put("eyeColors", eyeColors);

            object.put("city", Encoder.encode(city.getText().toString()));
            object.put("neighbourhood", Encoder.encode(neighbourhood.getText().toString()));

        } catch (JSONException e) {
            Log.e("Error al crear el JSON", e.getMessage());
        }

        SearchForAdoptionFilters filters = new SearchForAdoptionFilters(object);
        QueryResultTask qTask = new QueryResultTask(filters);
        qTask.execute((Void) null);
    }


    public class QueryResultTask extends AsyncTask<Void, Void, Boolean> {

        SearchForAdoptionFilters filters;
        JSONArray response;

        QueryResultTask( SearchForAdoptionFilters filters) {
            this.filters = filters;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            SearchRequest request = new SearchRequest(getApplicationContext());
            response = request.search(filters);
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (success) {
                Intent intent = new Intent(getApplicationContext(), ResultListActivity.class);
                if (response != null) {
                    intent.putExtra("data", response.toString());
                    startActivity(intent);
                }
            }
        }

        @Override
        protected void onCancelled() { }

    }

}
