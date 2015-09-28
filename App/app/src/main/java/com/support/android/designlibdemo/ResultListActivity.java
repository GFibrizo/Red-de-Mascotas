package com.support.android.designlibdemo;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

/**  Esta clase se va a encargar de enviarle los resultados de las busquedas en tuplas de imagen
 *   y texto al adapter ImageAndTextArrayAdapter para que haga su display.
 */

public class ResultListActivity extends AppCompatActivity {

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
        ArrayList<TextAndImage> textAndImageArray = new ArrayList<TextAndImage>();
        textAndImageArray.add(new TextAndImageContainer(1, values[0], "hembra", "0 a 6 meses", "chico", "Belgrano, Capital Federal"));
        textAndImageArray.add(new TextAndImageContainer(2, values[1], "macho", "1 a 3 años", "chico", "Flores, Capital Federal"));
        textAndImageArray.add(new TextAndImageContainer(3, values[2], "hembra", "6 a 12 meses", "grande", "Palermo, Capital Federal"));
        textAndImageArray.add(new TextAndImageContainer(4, values[3], "macho", "0 a 6 meses", "chico", "Paternal, Capital Federal"));
        textAndImageArray.add(new TextAndImageContainer(5, values[4], "macho", "1 a 3 años", "chico", "Villa Urquiza, Capital Federal"));
        textAndImageArray.add(new TextAndImageContainer(6, values[0], "hembra", "0 a 6 meses", "mediano", "Villa Crespo, Capital Federal"));
        textAndImageArray.add(new TextAndImageContainer(7, values[1], "hembra", "0 a 6 meses", "chico", "San Telmo, Capital Federal"));
        textAndImageArray.add(new TextAndImageContainer(8, values[2], "hembra", "0 a 6 meses", "chico", "Palermo, Capital Federal"));
        textAndImageArray.add(new TextAndImageContainer(9, values[4], "hembra", "más de 7 años", "chico", "Flores, Capital Federal"));
        textAndImageArray.add(new TextAndImageContainer(10, values[1],"macho", "0 a 6 meses", "mediano", "Palermo, Capital Federal"));
        textAndImageArray.add(new TextAndImageContainer(11, values[2], "hembra", "0 a 6 meses", "chico", "Caballito, Capital Federal"));
        textAndImageArray.add(new TextAndImageContainer(12, values[3], "hembra", "1 a 3 años", "chico", "San Telmo, Capital Federal"));
        textAndImageArray.add(new TextAndImageContainer(13, values[4], "hembra", "0 a 6 meses", "chico", "Nuñez, Capital Federal"));
        textAndImageArray.add(new TextAndImageContainer(14, values[0], "macho", "1 a 3 años", "grande", "Colegiales, Capital Federal"));


        // ListView Item Click Listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
//               @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    Intent intent = new Intent(thisActivity, WallActivity.class);
//                    intent.putExtra("ownProfile", false);
//                    int friendId = friends.get(position).getId();
//                    intent.putExtra("friend_id", friendId);
//                    startActivity(intent);
//                }
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

}
