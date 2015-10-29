package com.support.android.designlibdemo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**  Esta clase se va a encargar de enviarle los resultados de los matches en tuplas de imagen
 *   y texto al adapter ResultImageAndTextArrayAdapter para que haga su display.
 */

public class MatchResultListActivity extends AppCompatActivity {
    private JSONArray object = null;
    private ListView listView ;
    private List<MatchedPet> mascotas = null;
    SharedPreferences prefs = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_list_view);
        TextView titulo = (TextView) findViewById(R.id.titulo);
        titulo.setText("Resultados del Match");
        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_result);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);

        cargarResultados();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_result_list, menu);
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


    private void cargarResultados(){
        // Get ListView object from xml
        listView = (ListView) findViewById(R.id.list);

        try {
            //object = new JSONArray(getIntent().getStringExtra("data"));
            object = new JSONArray(prefs.getString("searchResponse", "[]"));
            if (object.length() != 0){
                mascotas = fromJSONArrayToListMascotas(object);
            }
        } catch (JSONException e) {
            Log.e("Error receiving intent", e.getMessage());
        }

        ArrayList<MatchedPet> textAndImageArray = new ArrayList<MatchedPet>();

        if (mascotas != null) {
            for (int i = 0; i < mascotas.size(); i++) {
                textAndImageArray.add(mascotas.get(i));
            }
        }

        // ListView Item Click Listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
               @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                   Intent intent = new Intent(getApplicationContext(), MatchedPetsDetailActivity.class);
                   MatchedPet petContainer = mascotas.get(position);
                   intent.putExtra("id", petContainer.getId());
                   intent.putExtra("tipo", petContainer.getType());
                   intent.putExtra("raza", petContainer.getBreed());
                   intent.putExtra("sexo", petContainer.getGender());
                   intent.putExtra("tamanio", petContainer.getSize());
                   intent.putExtra("colorPelaje", petContainer.getColors());
                   intent.putExtra("colorOjos", petContainer.getEyeColor());
                   intent.putExtra("contactEmail", petContainer.getContactEmail());
                   intent.putExtra("latitude", petContainer.getLatitude());
                   intent.putExtra("longitude", petContainer.getLongitude());
                   intent.putExtra("lastSeenOrFoundDate", petContainer.getLastSeenOrFoundDate());
                   intent.putExtra("matchingScore", petContainer.getMatchingScore());
                   intent.putExtra("images", petContainer.getImages());
                   startActivity(intent);
                }

        });

        MatchResultImageAndTextArrayAdapter adapter = new MatchResultImageAndTextArrayAdapter(this, R.layout.mock_match_image_and_text_single_row ,
                    null, textAndImageArray);
        listView.setAdapter(adapter);

    }


//    public MatchedPet(String id, String type, String breed,
//                      String gender, String size, List<String> colors, String eyeColor,
//                      List<String> images, String contactEmail, String latitude, String longitude,
//                      String lastSeenOrFoundDate, String matchingScore) {
    private List<MatchedPet> fromJSONArrayToListMascotas(JSONArray jsonArray) {
        List<MatchedPet> list = new ArrayList<>();
        try {
            for (int i=0; i<jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                List<String> colors = new ArrayList<>();
                List<String> images = new ArrayList<>();
                if (!object.get("colors").toString().equals("null")) {
                    for (int j = 0; j < object.getJSONArray("colors").length(); j++) {
                        colors.add((String) object.getJSONArray("colors").get(j));
                    }
                }
                JSONArray imageArray = object.getJSONArray("images");
                if (!imageArray.toString().equals("null")) {
                    for (int j = 0; j < imageArray.length(); j++) {
                        images.add(imageArray.getString(j));
                    }
                }
                MatchedPet mascota = new MatchedPet(null,
                        object.getString("type"),
                        object.getString("breed"),
                        object.getString("gender"),
                        object.getString("size"),
                        colors,
                        object.getString("eyeColor"),
                        images,
                        object.getString("contactEmail"),
                        ((JSONObject) object.get("lastSeenOrFoundLocation")).getString("latitude"),
                        ((JSONObject) object.get("lastSeenOrFoundLocation")).getString("longitude"),
                        object.getString("lastSeenOrFoundDate"),
                        object.getString("matchingScore"));
                list.add(mascota);
            }
        } catch (JSONException e) {
            Log.e("Error al crear el JSON", e.getMessage());
        }
        return list;
    }

}
