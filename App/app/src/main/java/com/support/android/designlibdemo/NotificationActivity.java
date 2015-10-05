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
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.support.android.designlibdemo.data.communications.ImageUrlView;
import com.support.android.designlibdemo.model.Address;
import com.support.android.designlibdemo.model.PetAdoption;
import com.support.android.designlibdemo.model.TextAndImagePetContainer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class NotificationActivity extends AppCompatActivity {
    private JSONArray object = null;
    private ListView listView ;
    private List<PetAdoption> mascotas = null;
    protected String baseUrlForImage;
    private String IP_EMULADOR = "http://10.0.2.2:9000"; //ip generica del emulador


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

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
        ArrayList<TextAndImage> textAndImageArray = new ArrayList<TextAndImage>();


        for (int i = 0; i < 3; i++) {
            textAndImageArray.add(new TextAndImagePetContainer());
        }


        // ListView Item Click Listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }

        });

        NotificationImageAndTextArrayAdapter adapter = new NotificationImageAndTextArrayAdapter(this, R.layout.notification_image_and_text ,
                null, (ArrayList<TextAndImage>) textAndImageArray);
        listView.setAdapter(adapter);

    }


    private List<String> fromJSONArrayToList(JSONArray jsonArray) {
        List<String> list = new ArrayList<String>();
        try {
            for (int i=0; i<jsonArray.length(); i++) {
                list.add(jsonArray.getString(i));
            }
        } catch (JSONException e) {
            Log.e("Error al crear el JSON", e.getMessage());
        }
        return list;
    }

    private List<PetAdoption> fromJSONArrayToListMascotas(JSONArray jsonArray) {
        List<PetAdoption> list = new ArrayList<>();
        try {
            for (int i=0; i<jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                Address address = new Address();
                String barrio = ((JSONObject) object.get("address")).getString("neighbourhood");
                address.setNeighbourhood(barrio);
                PetAdoption mascota = new PetAdoption(object.getString("name"),
                        "",
                        "",
                        address,
                        "",
                        object.getString("gender"),
                        object.getString("age"),
                        object.getString("size"),
                        null,
                        "",
                        null,
                        null,
                        false,
                        false,
                        false,
                        false,
                        "");
                list.add(mascota);
            }
        } catch (JSONException e) {
            Log.e("Error al crear el JSON", e.getMessage());
        }
        return list;
    }

}
