/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.support.android.designlibdemo.data.maps;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.support.android.designlibdemo.FoundPetActivity;
import com.support.android.designlibdemo.R;
import com.support.android.designlibdemo.model.MapCoordenates;
import com.support.android.designlibdemo.model.ReportLostPet;

import org.json.JSONException;
import org.json.JSONObject;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener{

    private double lat = -34.603620;
    private double lng = -58.381598;
    GoogleMap googleMap = null;
    SharedPreferences preferences = null;
    JSONObject object = null;
    String objName = null;
    LatLng myLatLng = null;
    String fromActivity = null;
    Class from;

    /**********************************************************************************************/
    /**********************************************************************************************/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        from = (Class) getIntent().getSerializableExtra("From");
        MapCoordenates.getInstance().clear();
        /*Log.e("CLASS", fromActivity);
        if (fromActivity == "ReportLostPet") {
            from = ReportLostPet.class;
        } else {
            from = FoundPetActivity.class;
        }*/

        /*String strObj = getIntent().getStringExtra("missing");
        if (strObj != null) {
            try {
                object = new JSONObject(getIntent().getStringExtra("missing"));
                Log.e("Object received", object.toString());
            } catch (JSONException e) {
                Log.e("Error receiving intent", e.getMessage());
                object = new JSONObject();
            }
        } else {
            object = new JSONObject();
        }*/

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_map);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        googleMap = mapFragment.getMap();
        mapFragment.getMapAsync(this);
    }

    /**********************************************************************************************/
    /**********************************************************************************************/

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**********************************************************************************************/
    /**********************************************************************************************/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0,0,0,"LISTO").setIcon(R.drawable.ic_check_white_24dp).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return true;
    }

    /**********************************************************************************************/
    /**********************************************************************************************/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        MapCoordenates mapCoordenates = MapCoordenates.getInstance();
        if ((myLatLng != null) && (myLatLng != null)) {
            mapCoordenates.setLatitude(Double.toString(myLatLng.latitude));
            mapCoordenates.setLongitude(Double.toString(myLatLng.longitude));
        }
        Intent upIntent = null;
        Intent intent = new Intent(MapActivity.this, from);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        switch (item.getItemId()) {
            case android.R.id.home:
               /* upIntent = NavUtils.getParentActivityIntent(this);
                if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
                    TaskStackBuilder.create(this)
                            .addNextIntentWithParentStack(upIntent)
                            .startActivities();
                } else {
                    NavUtils.navigateUpTo(this, upIntent);
                }*/
                startActivity(intent);
                break;
            case 0:
                /*upIntent = NavUtils.getParentActivityIntent(this);
                setCoordsToReturn(upIntent);
                if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
                    TaskStackBuilder.create(this)
                            .addNextIntentWithParentStack(upIntent)
                            .startActivities();
                } else {
                    NavUtils.navigateUpTo(this, upIntent);
                }*/
                setCoordsToReturn(intent);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    /**********************************************************************************************/
    /**********************************************************************************************/

    private void setCoordsToReturn(Intent intent) {
        if ((myLatLng != null) && (myLatLng != null)) {
            String lat = Double.toString(myLatLng.latitude);
            String lng = Double.toString(myLatLng.longitude);
        }
        /*try {
            JSONObject aux = new JSONObject();
            aux.put("latitude", lat);
            aux.put("longitude", lng);
            object.put("lastSeenLocation", aux);
        } catch (JSONException e) {
            Log.e("Error put latlng", e.getMessage());
        }*/
        //intent.putExtra("missing", object.toString());
    }

    /**********************************************************************************************/
    /**********************************************************************************************/

    @Override
    public void onMapReady(GoogleMap map) {
        //googleMap = map;
        map.clear();
        LatLng coords = new LatLng(lat, lng);
        //map.addMarker(new MarkerOptions().position(coords));
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(coords, 14f);
        map.moveCamera(cameraUpdate);
        googleMap.setOnMapClickListener(this);
    }

    /**********************************************************************************************/
    /**********************************************************************************************/

    @Override
    public void onMapClick(LatLng latLng) {
        googleMap.clear();
        myLatLng = latLng;
        googleMap.addMarker(new MarkerOptions().position(latLng));
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, googleMap.getCameraPosition().zoom);
        googleMap.animateCamera(cameraUpdate);
        //googleMap.moveCamera(cameraUpdate);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
