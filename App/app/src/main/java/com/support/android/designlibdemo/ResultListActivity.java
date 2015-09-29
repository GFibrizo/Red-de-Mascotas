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
import android.widget.ListView;

import com.support.android.designlibdemo.model.Domicilio;
import com.support.android.designlibdemo.model.MascotaAdopcion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**  Esta clase se va a encargar de enviarle los resultados de las busquedas en tuplas de imagen
 *   y texto al adapter ImageAndTextArrayAdapter para que haga su display.
 */

public class ResultListActivity extends AppCompatActivity {
    private JSONArray object = null;
    private ListView listView ;

    // Defined Array values to show in ListView
    String[] values = new String[]{"Fiona", "Simba", "Homero", "Casandra", "Cleopatra"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_list_view);

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
//        String data = getIntent().getExtras().getString("data");
        String data = getIntent().getStringExtra("data");
//        String nombre = "nombre";
        List<MascotaAdopcion> mascotas = null;

        try {
            object = new JSONArray(getIntent().getStringExtra("data"));
            if (object != null){
                mascotas = fromJSONArrayToListMascotas(object);
            }
        } catch (JSONException e) {
            Log.e("Error receiving intent", e.getMessage());
        }


        ArrayList<TextAndImage> textAndImageArray = new ArrayList<TextAndImage>();

        if (mascotas != null) {
            for (int i = 0; i < mascotas.size(); i++) {
                String nombre = mascotas.get(i).getNombre();
                String sexo = mascotas.get(i).getSexo();
                String edad = mascotas.get(i).getEdad();
                String tam = mascotas.get(i).getTamanio();
                String barrio = mascotas.get(i).getDomicilio().getBarrio();
                textAndImageArray.add(new TextAndImageContainer(20+i, nombre, sexo, edad,tam, barrio));
            }

        }


        // ListView Item Click Listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
               @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                   Intent intent = new Intent(getApplicationContext(), PetsDetailActivity.class);
                   intent.putExtra("id", id);
//                    intent.putExtra("ownProfile", false);
//                    int friendId = friends.get(position).getId();
//                    intent.putExtra("friend_id", friendId);
                    startActivity(intent);
                }

        });

//                            String urlBaseForImage = IpConfig.LOCAL_IP.url() + "/getstudentpicture/";

        ImageAndTextArrayAdapter adapter = new ImageAndTextArrayAdapter(this, R.layout.mock_image_and_text_single_row ,
                    null, (ArrayList<TextAndImage>) textAndImageArray);
        listView.setAdapter(adapter);

    }


    public class TextAndImageContainer implements TextAndImage {
        private int id;
        private String nombre;
        private String sexo;
        private String edad;
        private String tamanio;
        private String ubicacion;

        TextAndImageContainer(){

        }

        TextAndImageContainer(int id, String nombre, String sexo, String edad, String tamanio, String ubicacion){
            this.id = id;
            this.nombre = nombre;
            this.sexo = sexo;
            this.edad = edad;
            this.tamanio = tamanio;
            this.ubicacion = ubicacion;
        }

        @Override
        public int getId() {
            return id;
        }

        @Override
        public String getText(){
            return "";
        }

        public String getNombre() {
            return nombre;
        }

        public String getSexo() {
            return sexo;
        }

        public String getEdad() {
            return edad;
        }

        public String getTamanio() {
            return tamanio;
        }

        public String getUbicacion() {
            return ubicacion;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public void setSexo(String sexo) {
            this.sexo = sexo;
        }

        public void setEdad(String edad) {
            this.edad = edad;
        }

        public void setTamanio(String tamanio) {
            this.tamanio = tamanio;
        }

        public void setUbicacion(String ubicacion) {
            this.ubicacion = ubicacion;
        }
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

    private List<MascotaAdopcion> fromJSONArrayToListMascotas(JSONArray jsonArray) {
        List<MascotaAdopcion> list = new ArrayList<>();
        try {
            for (int i=0; i<jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                Domicilio domicilio = new Domicilio();
                String barrio = ((JSONObject) object.get("domicilio")).getString("barrio");
                domicilio.setBarrio(barrio);
                MascotaAdopcion mascota = new MascotaAdopcion(object.getString("nombre"),
                        "",
                        "",
                        domicilio,
                        "",
                        object.getString("sexo"),
                        object.getString("edad"),
                        object.getString("tamanio"),
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
