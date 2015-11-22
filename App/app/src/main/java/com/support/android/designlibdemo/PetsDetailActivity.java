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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import com.facebook.share.ShareApi;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.sromku.simple.fb.Permission;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.entities.Feed;
import com.sromku.simple.fb.listeners.OnPublishListener;
import com.support.android.designlibdemo.data.communications.ImageUrlView;
import com.support.android.designlibdemo.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import utils.AdoptionRequest;
import utils.BasicOwnerPetRequest;
import utils.Constants;
import utils.ReportComplainRequest;
import utils.RequestHandler;
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
    private String publicationType;
    private Menu menu = null;
    OnPublishListener listener;
    private static Bitmap sharedImage;

    Permission[] permissions = new Permission[] {
            Permission.USER_PHOTOS,
            Permission.EMAIL,
            Permission.PUBLISH_ACTION
    };

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

        String necesitaTransito = getIntent().getStringExtra("transitHome");
        Log.e("Intent transit", necesitaTransito.toString());
        if (necesitaTransito.equals("false")) {
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

        cargarResultados();


        listener = new OnPublishListener() {
            @Override
            public void onComplete(String postId) {
                Log.i("PUBLISH", "Published successfully. The new post id = " + postId);
                Toast.makeText(getApplicationContext(), "Publicación compartida exitosamente", Toast.LENGTH_SHORT).show();
            }

     /*
      * You can override other methods here:
      * onThinking(), onFail(String reason), onException(Throwable throwable)
      */
        };


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
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        this.menu = menu;
        ArrayList<String> informers = getIntent().getStringArrayListExtra("informers");
        if (informers.contains(loginUser.getId()))
            menu.findItem(R.id.report_complain).setVisible(false);

        if (loginUser.getId().equals(getIntent().getStringExtra("ownerId"))) {
            menu.findItem(R.id.report_complain).setVisible(false);
            if (!publicationType.equals("LOST")){
                menu.findItem(R.id.share).setVisible(false);
            }
        } else {
            menu.findItem(R.id.share).setVisible(false);
        }
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
        //Log.e("MENU ID", Integer.toString(id));
        //Log.e("COMPLAIN ID", Integer.toString((R.id.report_complain)));
        //noinspection SimplifiableIfStatement
        if (id == R.id.report_complain) {
            AlertDialog dialog = createReportDialog("Denunciar publicación", "Escriba la causa de la denuncia");
            dialog.show();
            if (menu != null) menu.findItem(R.id.report_complain).setVisible(false);
            return true;
        } else if (id == R.id.share) {
            AlertDialog dialog = createPublicationDialog("Compartir Publicación", "Haz un comentario");
            dialog.show();
//            shareOnFacebook();
        }
        return super.onOptionsItemSelected(item);
    }

    /**********************************************************************************************/
    /**********************************************************************************************/

    public void shareOnFacebook(String texto) {
        /*Feed feed = new Feed.Builder()
                .setMessage("Ayúdenme a encontrar mi mascota compartiendo esta publicación")
                .setName(getIntent().getStringExtra("nombre"))
                .setCaption("")
                .setDescription(buildDescription())
                .setPicture(Constants.IP_SERVER + "/pet/image/" + imagesItem[0])
                .build();

        SimpleFacebook mSimpleFacebook = SimpleFacebook.getInstance(this);
        mSimpleFacebook.publish(feed, true, listener);*/
        SharePhoto photo = new SharePhoto.Builder()
                .setBitmap(sharedImage)//BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher)
                .setCaption(texto)
                .build();

        SharePhotoContent content = new SharePhotoContent.Builder()
                .addPhoto(photo)
                .build();
        ShareApi.share(content, null);
    }

    private String buildDescription() {
        String description = "\n\nSu nombre es " + getIntent().getStringExtra("nombre") + " y se encuentra perdido.\nSus caracteristicas son:\n" +
                "Raza: "            + getIntent().getStringExtra("raza")            + "\n" +
                "Género: "          + getIntent().getStringExtra("sexo")            + "\n" +
                "Edad: "            + getIntent().getStringExtra("edad")            + "\n" +
                "Tamaño: "          + getIntent().getStringExtra("tamanio")         + "\n" +
                "Color de pelaje: " + getIntent().getStringExtra("colorPelaje")     + "\n";
//                + "Otros datos:"      + getIntent().getStringExtra("caracteristicas");
        return description;
    }

    /**********************************************************************************************/
    /**********************************************************************************************/

    private AlertDialog createPublicationDialog(String titulo, String message) {
        // Instanciamos un nuevo AlertDialog Builder y le asociamos titulo y mensaje
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(titulo);
        alertDialogBuilder.setMessage(message);
        RelativeLayout linearLayout = new RelativeLayout(this);
        final EditText link = new EditText(this);
        link.setText(buildDescription());
        link.setTextSize(12);
        link.setWidth(750);
        linearLayout.addView(link);
        linearLayout.setPadding(70, 0, 0, 0);
        alertDialogBuilder.setView(linearLayout);
        link.invalidate();
        linearLayout.invalidate();
//        final String petId = getIntent().getStringExtra("id");

        // Creamos un nuevo OnClickListener para el boton OK que realice la conexion
        DialogInterface.OnClickListener listenerOk = new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                String texto = link.getText().toString();
                shareOnFacebook(texto);
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
        alertDialogBuilder.setNegativeButton(R.string.dialogPublish, listenerOk);

        return alertDialogBuilder.create();
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

        // Creamos un nuevo OnClickListener para el boton OK que realice la conexion
        DialogInterface.OnClickListener listenerOk = new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                reportComplain(petId, loginUser.getId(), link.getText().toString());
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
        publicationType = getIntent().getStringExtra("publicationType");

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

        if (!publicationType.equals(Constants.FOR_ADOPTION)){
            CardView cardConducta = (CardView) findViewById(R.id.cardConducta);
            CardView cardCaracteristicas = (CardView) findViewById(R.id.cardCaracteristicas);
            CardView cardDescripcion = (CardView) findViewById(R.id.cardDescripcion);
            cardConducta.setVisibility(View.GONE);
            cardCaracteristicas.setVisibility(View.GONE);
            cardDescripcion.setVisibility(View.GONE);
            ubicacion.setVisibility(View.GONE);
        }
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

            Thread thread;
            View swipeView = null;
            Bundle bundle = getArguments();
            int position = bundle.getInt("position");
            if (position < imagesItem.length) {
                swipeView = inflater.inflate(R.layout.swipe_fragment, container, false);
                ImageView imageView = (ImageView) swipeView.findViewById(R.id.imageView);
                String imageFileName = imagesItem[position];
                baseUrlForImage = IP_EMULADOR + "/pet/image/" + imageFileName;
                if (position == 0) {
                    loadImageFromUrl(baseUrlForImage);
                }
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

    /**********************************************************************************************/
    /**********************************************************************************************/

    private static void loadImageFromUrl(final String url) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                sharedImage = getBitmapFromURL(url);
            }
        });
    }


    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }



}
