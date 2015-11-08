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

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.support.android.designlibdemo.data.communications.ImageUrlView;
import com.support.android.designlibdemo.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import utils.AdoptionRequest;
import utils.BasicOwnerPetRequest;
import utils.Constants;
import utils.TransitHomeRequest;


public class PetsDetailActivity extends AppCompatActivity {
    private JSONArray object = null;
    public static final String EXTRA_NAME = "cheese_name";
    public static int IMAGE_MAX = 5;
    ImageFragmentPagerAdapter imageFragmentPagerAdapter;
    ViewPager viewPager;
    private Button buttonAdopt;
    private Button buttonTransitHome;
    private CardView contacto;
    private SharedPreferences prefs;
    private User loginUser;
    public static  String imagesItem[] = {};
    public static final String[] IMAGE_NAME = {"orange_kitten", "black_cat", "grey_cat",  "pardo_cat", "tiger_cat", "tiger_kitten"};


    /**********************************************************************************************/
    /**********************************************************************************************/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        try {
            JSONObject object = new JSONObject(prefs.getString("userData", "{}"));
            Log.e("USER DATA DETAIL", prefs.getString("userData", "{}"));
            if (object.length() == 0) {
                Toast.makeText(getApplicationContext(), "Error cargando datos de usuario", Toast.LENGTH_SHORT).show();
                return;
            }
            this.loginUser = new User(object);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        imageFragmentPagerAdapter = new ImageFragmentPagerAdapter(getSupportFragmentManager());

        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(imageFragmentPagerAdapter);

        buttonAdopt = (Button) findViewById(R.id.botonAdoptar);
        buttonAdopt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialogo = crearDialogo("Confirmar Adopción",
                        "Se le enviará una notificación al dueño de esta publicación para la evaluación de su solicitud",
                        true);
                dialogo.show();
            }
        });

        buttonTransitHome = (Button) findViewById(R.id.botonOfrecerHogar);
        buttonTransitHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialogo = crearDialogo("Confirmar ofrecimiento de hogar de tránsito",
                        "Se le enviará una notificación al dueño de esta publicación para la evaluación de su solicitud",
                        false);
                dialogo.show();
            }
        });


        if (loginUser.getId().equals(getIntent().getStringExtra("ownerId"))) {
            Log.e("Intent transit", "el usuario es owner, no puede ofrecer transito");
            buttonAdopt.setVisibility(View.GONE);
            buttonTransitHome.setVisibility(View.GONE);
        }

        Boolean necesitaTransito = getIntent().getBooleanExtra("necesitaTransito", false);
        Log.e("Intent transit", necesitaTransito.toString());
        if (necesitaTransito == false) {
            buttonTransitHome.setVisibility(View.GONE);
        }

        contacto = (CardView) findViewById(R.id.cardContacto);
        contacto.setVisibility(View.GONE);

        Intent intent = getIntent();
        final String cheeseName = intent.getStringExtra("nombre");

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(cheeseName);

//        loadBackdrop();
        cargarResultados();
//        TODO: sacar la exampleNotification de acá
//        ExampleNotification notification = new ExampleNotification(getResources(),
//                                (NotificationManager) getSystemService(NOTIFICATION_SERVICE),
//                                this, "esta es una notificacion");
//        notification.sendNotification();
    }

/*    @Override
    public void onClick(View button) {
        //show dialog
        AlertDialog dialogo = crearDialogo("Confirmar Adopción",
                "Se le enviará una notificación al dueño de esta publicación para la evaluación de su solicitud");
        dialogo.show();
    }
*/
//    private void loadBackdrop() {
//        final ImageView imageView = (ImageView) findViewById(R.id.backdrop);
//        Glide.with(this).load(getRandomCheeseDrawable()).centerCrop().into(imageView);
//    }


    /**********************************************************************************************/
    /**********************************************************************************************/

    private void ofrecerHogarDeTransito() {
        String petId = getIntent().getStringExtra("id");
        String homeOwnerId = loginUser.getId();
        QueryResultTask qTask = new QueryResultTask(petId, homeOwnerId, false);
        qTask.execute((Void) null);
        //contacto.setVisibility(View.VISIBLE);
        buttonTransitHome.setVisibility(View.GONE);
    }

    /**********************************************************************************************/
    /**********************************************************************************************/

    private void adoptar(){
        String petId = getIntent().getStringExtra("id");
        String adopterId = loginUser.getId();
        QueryResultTask qTask = new QueryResultTask(petId, adopterId, true);
        qTask.execute((Void) null);
        //contacto.setVisibility(View.VISIBLE);
        buttonAdopt.setVisibility(View.GONE);
    }

    /**********************************************************************************************/
    /**********************************************************************************************/

    private AlertDialog crearDialogo(String titulo, String mensaje, final boolean isAdoption) {
        // Instanciamos un nuevo AlertDialog Builder y le asociamos titulo y mensaje
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(titulo);
        alertDialogBuilder.setMessage(mensaje);

        // Creamos un nuevo OnClickListener para el boton OK que realice la conexion
        DialogInterface.OnClickListener listenerOk = new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (isAdoption) adoptar();
                else ofrecerHogarDeTransito();
            }
        };

        // Creamos un nuevo OnClickListener para el boton Cancelar
        DialogInterface.OnClickListener listenerCancelar = new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        };

        // Asignamos los botones positivo y negativo a sus respectivos listeners
        //OJO: estan al reves para que sea display si - no en vez de no - si
        alertDialogBuilder.setPositiveButton(R.string.dialogNo, listenerCancelar);
        alertDialogBuilder.setNegativeButton(R.string.dialogSi, listenerOk);

        return alertDialogBuilder.create();
    }

    /**********************************************************************************************/
    /**********************************************************************************************/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sample_actions, menu);
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
        imagesItem = getIntent().getStringExtra("images").split(", ");

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
//
//        Button botonAdoptar = (Button) findViewById(R.id.botonAdoptar);

    }

    /**********************************************************************************************/
    /**********************************************************************************************/

    public static class ImageFragmentPagerAdapter extends FragmentPagerAdapter {
        public ImageFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return IMAGE_MAX;
        }

        @Override
        public Fragment getItem(int position) {
            SwipeFragment fragment = new SwipeFragment();
            return SwipeFragment.newInstance(position);
        }
    }

    /**********************************************************************************************/
    /**********************************************************************************************/

    public static class SwipeFragment extends Fragment {
        protected String baseUrlForImage;
        private String IP_EMULADOR = Constants.IP_SERVER;//"http://10.0.2.2:9000"; //ip generica del emulador

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View swipeView = null;
            Bundle bundle = getArguments();
            int position = bundle.getInt("position");
            if (position < imagesItem.length) {
                swipeView = inflater.inflate(R.layout.swipe_fragment, container, false);
                ImageView imageView = (ImageView) swipeView.findViewById(R.id.imageView);
                String imageFileName = imagesItem[position];
                baseUrlForImage = IP_EMULADOR + "/pet/image/" + imageFileName;
                new ImageUrlView(baseUrlForImage, imageView).connect();
            }
            return swipeView;

        }

        static SwipeFragment newInstance(int position) {
            SwipeFragment swipeFragment = new SwipeFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("position", position);
            swipeFragment.setArguments(bundle);
            return swipeFragment;
        }
    }

    /**********************************************************************************************/
    /**********************************************************************************************/

    public class QueryResultTask extends AsyncTask<Void, Void, Boolean> {
        String petId;
        String ownerId;
        JSONArray response;
        Boolean adoption;

        QueryResultTask(String petId, String ownerId, Boolean adoption) {
            this.petId = petId;
            this.ownerId = ownerId;
            this.adoption = adoption;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            BasicOwnerPetRequest request = buildRequest(); //new AdoptionRequest(getApplicationContext());
            request.send(petId, ownerId);
            return true;
        }

        private BasicOwnerPetRequest buildRequest() {
            if (adoption) {
                Log.e("PetRequest","Adoption");
                return new AdoptionRequest(getApplicationContext());
            }
            Log.e("PetRequest","TransitHome");
            return new TransitHomeRequest(getApplicationContext());
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (success) {
                if (response != null) {
                    //TODO: poner algun dialog de confirmación
                }
            }
        }

        @Override
        protected void onCancelled() { }

    }
}
