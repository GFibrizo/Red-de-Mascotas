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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.RadioButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SearchInAdoptionActivity extends AppCompatActivity {

    private static final String AGE_0_TO_6_MONTHS = "0-6meses";
    private static final String AGE_6_TO_12_MONTHS = "6-12meses";
    private static final String AGE_1_TO_3_YEARS = "1-3anios";
    private static final String AGE_3_TO_7_YEARS = "3-7anios";
    private static final String AGE_7_OR_MORE_YEARS = "masDe7Anios";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_in_adoption);

        AutoCompleteTextView breedTextView = (AutoCompleteTextView) findViewById(R.id.autocomplete_breed);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.autocomplete_list_item, BREEDS);
        breedTextView.setAdapter(adapter);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_search);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search_in_adoption, menu);
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

    @Override
    public void onResume() {
        super.onResume();

    }

    public void nextPage(View view) {

        JSONObject object = new JSONObject();
        RadioButton petTypeDog = (RadioButton)findViewById(R.id.radio_dog);
        RadioButton petTypeCat = (RadioButton)findViewById(R.id.radio_cat);
        CheckBox petGenderMale = (CheckBox)findViewById(R.id.check_male);
        CheckBox petGenderFemale = (CheckBox)findViewById(R.id.check_female);
        AutoCompleteTextView breed = (AutoCompleteTextView)findViewById(R.id.autocomplete_breed);
        CheckBox age0to6m = (CheckBox)findViewById(R.id.check_0to6m_age);
        CheckBox age6to12m = (CheckBox)findViewById(R.id.check_6to12m_age);
        CheckBox age1to3y = (CheckBox)findViewById(R.id.check_1to3y_age);
        CheckBox age3to7y = (CheckBox)findViewById(R.id.check_3to7y_age);
        CheckBox age7toMorey = (CheckBox)findViewById(R.id.check_7toMorey_age);


        // TODO: Faltan validaciones
        try {
            if (petTypeDog.isChecked()) {
                object.put("tipo", petTypeDog.getText());
            } else if (petTypeCat.isChecked()) {
                object.put("tipo", petTypeCat.getText());
            }

            JSONArray genders = new JSONArray();
            if (petGenderMale.isChecked()) genders.put(petGenderMale.getText());
            if (petGenderFemale.isChecked()) genders.put(petGenderFemale.getText());
            object.put("sexos", genders);

            object.put("raza", breed.getText().toString().replaceAll(" ", "_"));

            JSONArray ages = new JSONArray();
            if (age0to6m.isChecked()) ages.put(AGE_0_TO_6_MONTHS);
            if (age6to12m.isChecked()) ages.put(AGE_6_TO_12_MONTHS);
            if (age1to3y.isChecked()) ages.put(AGE_1_TO_3_YEARS);
            if (age3to7y.isChecked()) ages.put(AGE_3_TO_7_YEARS);
            if (age7toMorey.isChecked()) ages.put(AGE_7_OR_MORE_YEARS);
            object.put("edades", ages);

        } catch (JSONException e) {
            Log.e("Error al crear el JSON", e.getMessage());
        }
        Intent intent = new Intent(getApplicationContext(), SearchInAdoptionActivity2.class);
        intent.putExtra("data", object.toString());
        if (intent != null)
            startActivity(intent);

    }

    static final String[] BREEDS = new String[] {
            //Dog breeds
        "Affenpinscher", "Afgano", "Airedale Terrier", "Akita", "Alaskan Malamute", "American Foxhound", "American Staffordshire Terrier",
            "Antiguo perro pastor inglés", "Basenji", "Basset Hound", "Beagle", "Beauceron", "Bedlington Terrier", "Bichon Frise", "Bichón habanero",
            "Bichón maltés", "Bloodhound / Perro de San Huberto", "Bluetick Coonhound", "Border Collie", "Border Terrier", "Borzoi", "Boston Terrier",
            "Boxer", "Boyero de Berna", "Boyero de Entlebuch", "Boyero de Flandes", "Boykin Spaniel", "Braco alemán de pelo corto / Kurzhaar",
            "Braco alemán de pelo duro / Drahthaar", "Braco de Weimar / Weimaraner", "Buhund noruego", "Bull Terrier", "Bull Terrier Miniatura",
            "Bulldog", "Bulldog francés", "Bullmastiff", "Cairn Terrier", "Cane Corso", "Caniche", "Cavalier King Charles Spaniel", "Cazador de alces noruego",
            "Chihuahua", "Chinook", "Chow Chow", "Clumber Spaniel", "Cocker Spaniel", "Cocker spaniel inglés", "Collie", "Collie barbudo",
            "Coonhound Inglés Americano", "Coonhound negro y bronce", "Corgi galés de Cardigan", "Corgi galés de Pembroke", "Crestado Chino", "Dachshund",
            "Dandie Dinmont Terrier", "Doberman", "Dogo de Burdeos", "Dogo del Tíbet", "Dálmata", "Finnish Lapphund", "Finnish Spitz", "Fox terrier de pelo duro",
            "Foxhound inglés", "Galgo inglés", "Glen of Imaal Terrier", "Golden Retriever", "Gordon Setter", "Gran boyero suizo", "Gran danés", "Grifón de Bruselas",
            "Grifón Korthal", "Grifón vandeano basset pequeño", "Harrier", "Husky siberiano", "Jack Russell terrier", "Keeshond", "Kerry Blue Terrier",
            "Komondor", "Kuvasz", "Labrador Retriever", "Lakeland Terrier", "Lebrel escocés", "Lebrel italiano", "Leonberger", "Lhasa Apso", "Lobero irlandés",
            "Lundehund", "Manchester Terrier", "Mastín inglés", "Mastín napolitano", "Mestizo", "Montaña de los Pirineos / Gran Pirineo", "Otterhound", "Papillon",
            "Parson Russell Terrier", "Pastor alemán / Overjero Alemán", "Pastor belga", "Pastor belga Malinois", "Pastor belga Tervuerense", "Pastor de Anatolia",
            "Pastor de Brie", "Pastor de los Pirineos", "Pastor de Valée", "Pastor ganadero autraliano", "Pastor Islandés", "Pastor ovejero australiano", "Pekinés",
            "Pequeño perro león", "Perro crestado rodesiano", "Perro de agua americano", "Perro de agua irlandés", "Perro de Agua Portugués", "Perro de Canaán",
            "Perro esquimal americano", "Perro pastor de las islas Shetland", "Pharaoh Hound", "Pinscher alemán", "Pinscher miniatura", "Plott", "Podenco ibicenco",
            "Podenco portugués", "Pointer", "Pomerania", "Pug", "Puli", "Redbone Coonhound", "Retriever de Chesapeake", "Retriever de Nueva Escocia",
            "Retriever de Pelo Liso", "Retriever de pelo rizado", "Rottweiler", "Saluki", "Samoyedo", "San Bernardo", "Schipperke", "Schnauzer estándar",
            "Schnauzer gigante", "Schnauzer miniatura", "Sealyham Terrier", "Setter inglés", "Setter irlandés", "Setter irlandés rojo y blanco", "Shar Pei",
            "Shiba Inu", "Shih Tzu", "Skye Terrier", "Soft Coated Wheaten Terrier", "Spaniel bretón", "Spaniel de campo", "Spaniel japonés", "Spaniel tibetano",
            "Spinone", "Springer spaniel galés", "Springer spaniel inglés", "Staffordshire Bull Terrier", "Sussex Spaniel", "Terranova", "Terrier australiano",
            "Terrier checo", "Terrier de Australia", "Terrier de Norfolk", "Terrier de Norwich", "Terrier escocés", "Terrier galés", "Terrier irlandés",
            "Terrier ruso negro", "Terrier tibetano", "Toy Fox Terrier", "Toy spaniel inglés", "Treeing Walker Coonhound", "Vallhund sueco", "Vizsla",
            "West Highland White Terrier", "Whippet", "Xoloitzcuintli", "Yorkshire Terrier",

            // Cat breeds
         "Abisinio", "Aphrodite's Giants", "Australian Mist", "American Curl", "Azul ruso", "American shorthair", "American wirehair", "Angora turco",
            "Africano doméstico", "Bengala", "Bobtail japonés", "Bombay", "Bosque de Noruega", "Brazilian Shorthair", "Brivon de pelo corto", "Brivon de pelo largo",
            "British Shorthair", "Burmés", "Burmilla", "Cornish rexx", "California Spangled", "Ceylon", "Cymric", "Chartreux", "Deutsch Langhaar", "Devon rex",
            "Dorado africano", "Don Sphynx", "Dragon Li", "Europeo Común", "Exótico de Pelo Corto", "FoldEx", "German Rex", "Habana brown", "Himalayo", "Korat",
            "Khao Manee", "Lituli", "Maine Coon", "Manx", "Mau egipcio", "Munchkin", "Ocicat", "Oriental", "Oriental de pelo largo", "Ojos azules", "PerFold1",
            "Persa Americano o Moderno", "Persa Clásico o Tradicional", "Peterbald", "Pixie Bob", "Ragdoll", "Sagrado de Birmania", "Scottish Fold", "Selkirk rex",
            "Serengeti", "Seychellois", "Siamés", "Siamés Moderno", "Siamés Tradicional", "Siberiano", "Snowshoe", "Sphynx", "Tonkinés", "Van Turco"
    };

}
