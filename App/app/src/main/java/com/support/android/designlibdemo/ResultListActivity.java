package com.support.android.designlibdemo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.support.android.designlibdemo.model.Address;
import com.support.android.designlibdemo.model.PetAdoption;
import com.support.android.designlibdemo.model.TextAndImagePetContainer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**  Esta clase se va a encargar de enviarle los resultados de las busquedas en tuplas de imagen
 *   y texto al adapter ResultImageAndTextArrayAdapter para que haga su display.
 */

public class ResultListActivity extends AppCompatActivity {
    private JSONArray object = null;
    private ListView listView ;
    private List<PetAdoption> mascotas = null;
    SharedPreferences prefs = null;

    // Defined Array values to show in ListView
    String[] values = new String[]{"Fiona", "Simba", "Homero", "Casandra", "Cleopatra"};

    /**********************************************************************************************/
    /**********************************************************************************************/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_list_view);
        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_result);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);

        cargarResultados();
        saveSearchIfNoResults();
    }

    /**********************************************************************************************/
    /**********************************************************************************************/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_result_list, menu);
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

    @Override
    public void finish() {
        Log.e("BACK", "BACK TO MAIN");
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        super.finish();
    }

    /**********************************************************************************************/
    /**********************************************************************************************/

    private AlertDialog createDialog(String titulo, String mensaje) {
        // Instanciamos un nuevo AlertDialog Builder y le asociamos titulo y mensaje
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(titulo);
        alertDialogBuilder.setMessage(mensaje);

        // Creamos un nuevo OnClickListener para el boton OK que realice la conexion
        DialogInterface.OnClickListener listenerOk = new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
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
        alertDialogBuilder.setPositiveButton("OK", listenerOk);

        return alertDialogBuilder.create();
    }


    /**********************************************************************************************/
    /**********************************************************************************************/


    private void saveSearchIfNoResults() {
        if (mascotas != null)
            return;
        AlertDialog dialog = createDialog("No se han encontrado resultados",
                "Se ha guardado la búsqueda realizada y se le notificará cuando haya nuevos resultados para la misma.");
        dialog.show();
    }

    /**********************************************************************************************/
    /**********************************************************************************************/

    private void cargarResultados(){
        // Get ListView object from xml
        listView = (ListView) findViewById(R.id.list);

        try {
            //object = new JSONArray(getIntent().getStringExtra("data"));
            object = new JSONArray(prefs.getString("searchResponse", "[]"));
            if (object.length() != 0){
                mascotas = fromJSONArrayToListMascotas(object);
            } else {

            }
        } catch (JSONException e) {
            Log.e("Error receiving intent", e.getMessage());
        }

        ArrayList<TextAndImage> textAndImageArray = new ArrayList<TextAndImage>();

        if (mascotas != null) {
            for (int i = 0; i < mascotas.size(); i++) {
                textAndImageArray.add(new TextAndImagePetContainer(mascotas.get(i)));
            }
        }

        // ListView Item Click Listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
               @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                   Intent intent = new Intent(getApplicationContext(), PetsDetailActivity.class);
                   TextAndImagePetContainer petContainer = new TextAndImagePetContainer(mascotas.get(position));
                   intent.putExtra("id", petContainer.getId());
                   intent.putExtra("ownerId", petContainer.getOwnerId());
                   intent.putExtra("nombre", petContainer.getNombre());
                   intent.putExtra("raza", petContainer.getRaza());
                   intent.putExtra("sexo", petContainer.getSexo());
                   intent.putExtra("edad", petContainer.getEdad());
                   intent.putExtra("tamanio", petContainer.getTamanio());
                   intent.putExtra("colorPelaje", petContainer.getColorPelaje());
                   intent.putExtra("colorOjos", petContainer.getColorOjos());
                   intent.putExtra("ubicacion", petContainer.getBarrio());
                   intent.putExtra("caracteristicas", petContainer.getCaracteristicas());
                   intent.putExtra("descripcion", petContainer.getDescripcion());
                   intent.putExtra("conducta", petContainer.getConducta());
                   intent.putExtra("images", petContainer.getImages());
                   intent.putExtra("publicationType", "FOR_ADOPTION");
                   Boolean transitHome = petContainer.getTransito();
                   String transitHomeUser = petContainer.getTransitHomeUser();
                   Log.e("Result TransitHome", transitHome + ", " + transitHomeUser);
                   intent.putExtra("informers", mascotas.get(position).getInformers());

                   if ((transitHome != false) && (transitHomeUser.equals("null"))) {
                       Log.e("TRUE", "transit");
                       intent.putExtra("transitHome", "true");
                   } else {
                       intent.putExtra("transitHome", "false");
                       Log.e("FALSE", "transit");
                   }
                   startActivity(intent);
                }

        });

        ResultImageAndTextArrayAdapter adapter = new ResultImageAndTextArrayAdapter(this, R.layout.mock_image_and_text_single_row ,
                    null, (ArrayList<TextAndImage>) textAndImageArray);
        listView.setAdapter(adapter);

    }

    /**********************************************************************************************/
    /**********************************************************************************************/

    private ArrayList<String> getInformers(JSONObject object) {
        ArrayList<String> informers = new ArrayList<>();
        try {
            JSONArray reports = object.getJSONArray("reports");
            int length = reports.length();
            for (int i = 0; i < length; i++) {
                JSONObject report = (JSONObject) reports.get(i);
                if (report.getString("status").equals("PENDING")) {
                    informers.add(report.getString("informer"));
                }
            }
        } catch (JSONException e) {}
        return informers;
    }


    /**********************************************************************************************/
    /**********************************************************************************************/

//    public PetAdoption(String name, String type, String ownerId, Address address, String breed,
//                       String gender, String age, String size, List<String> colors, String eyeColor,
//                       List<String> behavior, List<String> images, Boolean needsTransitHome,
//                       Boolean isCastrated, Boolean isOnTemporaryMedicine, Boolean isOnChronicMedicine, String description) {
    private List<PetAdoption> fromJSONArrayToListMascotas(JSONArray jsonArray) {
        List<PetAdoption> list = new ArrayList<>();
        try {
            for (int i=0; i<jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                Address address = new Address();
                String barrio = ((JSONObject) object.get("address")).getString("neighbourhood");
                address.setNeighbourhood(barrio);
                List<String> colors = new ArrayList<>();
                List<String> behavior = new ArrayList<>();
                List<String> images = new ArrayList<>();
                if (!object.get("colors").toString().equals("null")) {
                    for (int j = 0; j < object.getJSONArray("colors").length(); j++) {
                        colors.add((String) object.getJSONArray("colors").get(j));
                    }
                }
                if (!object.get("behavior").toString().equals("null")) {
                    for (int j = 0; j < object.getJSONArray("behavior").length(); j++) {
                        behavior.add((String) object.getJSONArray("behavior").get(j));
                    }
                }
                JSONArray imageArray = object.getJSONArray("images");
                if (!imageArray.toString().equals("null")) {
                    for (int j = 0; j < imageArray.length(); j++) {
                        //images.add(((String) object.getJSONArray("images").get(j)).replace("[","").replace("]", ""));
                        images.add(imageArray.getString(j));
                    }
                }
                PetAdoption mascota = new PetAdoption(object.getString("id"),
                        object.getString("name"),
                        object.getString("type"),
                        object.getString("ownerId"),
                        address,
                        object.getString("breed"),
                        object.getString("gender"),
                        object.getString("age"),
                        object.getString("size"),
                        colors,
                        object.getString("eyeColor"),
                        behavior,
                        images,
                        object.getBoolean("needsTransitHome"),
                        object.getBoolean("isCastrated"),
                        object.getBoolean("isOnTemporaryMedicine"),
                        object.getBoolean("isOnChronicMedicine"),
                        object.getString("description"),
                        object.getString("transitHomeUser"),
                        getInformers(object));
                list.add(mascota);
            }
        } catch (JSONException e) {
            Log.e("Error al crear el JSON", e.getMessage());
        }
        return list;
    }

}
