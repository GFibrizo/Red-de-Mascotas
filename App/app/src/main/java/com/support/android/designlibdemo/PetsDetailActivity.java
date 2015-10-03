/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.support.android.designlibdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.support.android.designlibdemo.model.Address;
import com.support.android.designlibdemo.model.PetAdoption;
import com.support.android.designlibdemo.model.TextAndImagePetContainer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static utils.Constants.getRandomCheeseDrawable;

public class PetsDetailActivity extends AppCompatActivity {
    private JSONArray object = null;
    public static final String EXTRA_NAME = "cheese_name";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        final String cheeseName = intent.getStringExtra(EXTRA_NAME);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(cheeseName);

        loadBackdrop();
        cargarResultados();
    }

    private void loadBackdrop() {
        final ImageView imageView = (ImageView) findViewById(R.id.backdrop);
        Glide.with(this).load(getRandomCheeseDrawable()).centerCrop().into(imageView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sample_actions, menu);
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
        String nombreItem = getIntent().getStringExtra("nombre");
        String razaItem = getIntent().getStringExtra("raza");
        String sexoItem = getIntent().getStringExtra("sexo");
        String edadItem = getIntent().getStringExtra("edad");
        String tamanioItem = getIntent().getStringExtra("tamanio");
        String ubicacionItem = getIntent().getStringExtra("ubicacion");
        String colorPelajeItem = getIntent().getStringExtra("colorPelaje");
        String colorOjosItem = getIntent().getStringExtra("colorOjos");
        String caracteristicasItem = getIntent().getStringExtra("caracteristicas");
        String descripcionItem = getIntent().getStringExtra("descripcion");
        String conductaItem = getIntent().getStringExtra("conducta");

        TextView nombre = (TextView) findViewById(R.id.nombreAnimal);
        TextView raza = (TextView) findViewById(R.id.razaAnimal);
        TextView sexo = (TextView) findViewById(R.id.sexoAnimal);
        TextView edad = (TextView) findViewById(R.id.edadAnimal);
        TextView tamanio = (TextView) findViewById(R.id.tamanioAnimal);
        TextView ubicacion = (TextView) findViewById(R.id.ubicacionAnimal);
        TextView colorPelaje = (TextView) findViewById(R.id.colorPelajeAnimal);
        TextView colorOjos = (TextView) findViewById(R.id.colorOjosAnimal);
        TextView caracteristicas = (TextView) findViewById(R.id.caracteristicas);
        TextView descripcion = (TextView) findViewById(R.id.description);
        TextView conducta = (TextView) findViewById(R.id.behavior);
        nombre.setText(nombre.getText() + " " + nombreItem);
        raza.setText(raza.getText()+" "+razaItem);
        sexo.setText(sexo.getText() + " " + sexoItem);
        edad.setText(edad.getText()+" "+edadItem);
        tamanio.setText(tamanio.getText() + " " + tamanioItem);
        ubicacion.setText(ubicacion.getText()+" "+ubicacionItem);
        colorPelaje.setText(colorPelaje.getText() + " " + colorPelajeItem);
        colorOjos.setText(colorOjos.getText()+" "+colorOjosItem);
        caracteristicas.setText(caracteristicasItem);
        descripcion.setText(descripcionItem);
        conducta.setText(conductaItem);

    }

    public void nextPage(View view) {
        //Aca llama a la actividad siguiente: donde adopta la mascota o se le da el mail.
    }
}
