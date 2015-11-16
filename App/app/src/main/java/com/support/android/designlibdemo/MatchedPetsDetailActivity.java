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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.support.android.designlibdemo.data.communications.ImageUrlView;
import com.support.android.designlibdemo.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import utils.AdoptionRequest;
import utils.Constants;
import utils.ReportComplainRequest;


public class MatchedPetsDetailActivity extends AppCompatActivity implements View.OnClickListener{
    private JSONArray object = null;
    public static final String EXTRA_NAME = "cheese_name";
    public static int IMAGE_MAX = 5;
    ImageFragmentPagerAdapter imageFragmentPagerAdapter;
    ViewPager viewPager;
    private Button button;
    private CardView contacto;
    private SharedPreferences prefs;
    private User loginUser;
    public static  String imagesItem[] = {};
    private String publicationType;
    private Menu menu = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_match);
        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        try {
            JSONObject object = new JSONObject(prefs.getString("userData", "{}"));
            Log.e("USER DATA DETAIL", prefs.getString("userData", "{}"));
            if (object.length() == 0) {
                Toast.makeText(getApplicationContext(), "Error cargando datos de usuario", Toast.LENGTH_SHORT).show();
                return;
            }
            this.loginUser = new User(object);
            Log.e("USER", this.loginUser.getId());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        imageFragmentPagerAdapter = new ImageFragmentPagerAdapter(getSupportFragmentManager());

        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(imageFragmentPagerAdapter);

//        button = (Button) findViewById(R.id.botonAdoptar);
//        button.setOnClickListener(this);
//        contacto = (CardView) findViewById(R.id.cardContacto);
//        contacto.setVisibility(View.GONE);

        Intent intent = getIntent();
        final String cheeseName = intent.getStringExtra("nombre");

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(cheeseName);

        cargarResultados();
    }

    @Override
    public void onClick(View button) {
        //show dialog
        AlertDialog dialogo = crearDialogo("Confirmar Adopción",
                "Se le enviará una notificación al dueño de esta publicación, ¿Está seguro de que desea adoptarlo?");
        dialogo.show();
    }

    private void adoptar(){
        String petId = getIntent().getStringExtra("id");
        String adopterId = this.loginUser.getId();
        QueryResultTask qTask = new QueryResultTask(petId, adopterId);
        qTask.execute((Void) null);
        contacto.setVisibility(View.VISIBLE);
        button.setVisibility(View.GONE);
    }

    private AlertDialog crearDialogo(String titulo, String mensaje) {
        // Instanciamos un nuevo AlertDialog Builder y le asociamos titulo y mensaje
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(titulo);
        alertDialogBuilder.setMessage(mensaje);

        // Creamos un nuevo OnClickListener para el boton OK que realice la conexion
        DialogInterface.OnClickListener listenerOk = new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                adoptar();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        this.menu = menu;
        ArrayList<String> informers = getIntent().getStringArrayListExtra("informers");
        if (informers.contains(this.loginUser.getId()))
            menu.findItem(R.id.report_complain).setVisible(false);
        //menu.setGroupVisible(R.id.report_complain, false);
        if (this.loginUser.getId().equals(getIntent().getStringExtra("ownerId")))
            menu.findItem(R.id.report_complain).setVisible(false);
        //menu.setGroupVisible(R.id.report_complain, false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //Log.e("MENU ID", Integer.toString(id));
        //Log.e("COMPLAIN ID", Integer.toString((R.id.report_complain)));
        //noinspection SimplifiableIfStatement
        if (id == R.id.report_complain) {
            AlertDialog dialog = createReportDialog("Denunciar publicación", "Escriba la causa de la denuncia");
            dialog.show();
            if (menu != null) menu.findItem(R.id.report_complain).setVisible(false);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**********************************************************************************************/
    /**********************************************************************************************/

    private AlertDialog createReportDialog(String titulo, String message) {
        // Instanciamos un nuevo AlertDialog Builder y le asociamos titulo y mensaje
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(titulo);
        alertDialogBuilder.setMessage(message);
        RelativeLayout linearLayout = new RelativeLayout(this);
        final EditText link = new EditText(this);
        link.setHint("Causa de la denuncia");
        link.setWidth(750);
        linearLayout.addView(link);
        linearLayout.setPadding(70, 0, 0, 0);
        alertDialogBuilder.setView(linearLayout);
        link.invalidate();
        linearLayout.invalidate();
        final String petId = getIntent().getStringExtra("id");
        final String reporterId = this.loginUser.getId();

        // Creamos un nuevo OnClickListener para el boton OK que realice la conexion
        DialogInterface.OnClickListener listenerOk = new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                reportComplain(petId, reporterId, link.getText().toString());
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
        alertDialogBuilder.setPositiveButton(R.string.dialogCancel, listenerCancelar);
        alertDialogBuilder.setNegativeButton(R.string.dialogSend, listenerOk);

        return alertDialogBuilder.create();
    }

    /**********************************************************************************************/
    /**********************************************************************************************/

    private void reportComplain(String petId, String reporterId, String text) {
        ReportComplainRequest request = new ReportComplainRequest(getApplicationContext());
        request.send(petId, reporterId, text, publicationType);
    }



    private void cargarResultados(){
        String tipoItem = getIntent().getStringExtra("tipo");
        String fechaItem = getIntent().getStringExtra("lastSeenOrFoundDate");
        String razaItem = getIntent().getStringExtra("raza");
        String sexoItem = getIntent().getStringExtra("sexo");
        String tamanioItem = getIntent().getStringExtra("tamanio");
        String colorPelajeItem = getIntent().getStringExtra("colorPelaje");
        String colorOjosItem = getIntent().getStringExtra("colorOjos");
        String contactoItem = getIntent().getStringExtra("contactEmail");
        imagesItem = getIntent().getStringExtra("images").split(", ");
        publicationType = getIntent().getStringExtra("publicationType");


        TextView contacto = (TextView) findViewById(R.id.contacto);
        TextView tipo = (TextView) findViewById(R.id.tipoAnimal);
        TextView fecha = (TextView) findViewById(R.id.fechaAnimal);
        TextView raza = (TextView) findViewById(R.id.razaAnimal);
        TextView sexo = (TextView) findViewById(R.id.sexoAnimal);
        TextView tamanio = (TextView) findViewById(R.id.tamanioAnimal);
        TextView colorPelaje = (TextView) findViewById(R.id.colorPelajeAnimal);
        TextView colorOjos = (TextView) findViewById(R.id.colorOjosAnimal);
        tipo.setText(tipo.getText()+" "+tipoItem);
        fecha.setText(fecha.getText()+" "+fechaItem);
        raza.setText(raza.getText()+" "+razaItem);
        sexo.setText(sexo.getText() + " " + sexoItem);
        contacto.setText(contactoItem);
        tamanio.setText(tamanio.getText() + " " + tamanioItem);
        colorPelaje.setText(colorPelaje.getText() + " " + colorPelajeItem);
        colorOjos.setText(colorOjos.getText()+" "+colorOjosItem);

//
//        Button botonAdoptar = (Button) findViewById(R.id.botonAdoptar);

    }


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

    public class QueryResultTask extends AsyncTask<Void, Void, Boolean> {
        String petId;
        String adopterId;
        JSONArray response;

        QueryResultTask(String petId, String adopterId) {
            this.petId = petId;
            this.adopterId = adopterId;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            AdoptionRequest request = new AdoptionRequest(getApplicationContext());
            request.send(petId, adopterId);
            return true;
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
