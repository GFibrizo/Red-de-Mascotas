package com.support.android.designlibdemo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.support.android.designlibdemo.model.MapCoordenates;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import info.hoang8f.android.segmented.SegmentedGroup;
import utils.ImageRequest;
import utils.RequestHandler;
import utils.SpinnerArrayAdapter;

import static utils.Constants.AGES;
import static utils.Constants.CATS;
import static utils.Constants.CITIES;
import static utils.Constants.DOGS;
import static utils.Constants.NEIGHBOURHOODS;
import static utils.Constants.NONE_COLORS;
import static utils.Constants.SIZES;

public class PublishInAdoptionActivity extends AppCompatActivity {

    JSONObject userData = null;
    private Button loadVideosButton = null;
    private String breedSelected = null;
    private static List<String> images = new ArrayList<>();
    private static List<String> imagesPaths = new ArrayList<>();
    private static Vector<Bitmap> bitmapList = new Vector<>();
    static int NUM_ITEMS = 5;
    ViewPager viewPager = null;
    ImageFragmentPagerAdapter imageFragmentPagerAdapter = null;
    String objName = null;
    JSONObject object = null;
    Activity activity = null;
    private int ID_TYPE_DOG = 2131558676;
    private int ID_GENDER_MALE = 2131558679;



    /**********************************************************************************************/
    /**********************************************************************************************/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_in_adoption);

        try {
            object = new JSONObject(getIntent().getStringExtra("data"));
        } catch (JSONException e) {
            Log.e("Error receiving intent", e.getMessage());
        }

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_publish);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);


        activity = this;

        initializeImagesData();
        Spinner hairColor1Spinner = (Spinner) findViewById(R.id.spinner_hair_color1_publish);
        ArrayAdapter<CharSequence> hairColor1Adapter = SpinnerArrayAdapter.createSpinnerArrayAdapter(this, utils.Constants.HAIR_COLORS, NONE_COLORS[0]);
        hairColor1Spinner.setAdapter(hairColor1Adapter);
        hairColor1Spinner.setSelection(hairColor1Adapter.getCount());

        Spinner hairColor2Spinner = (Spinner) findViewById(R.id.spinner_hair_color2_publish);
        ArrayAdapter<CharSequence> hairColor2Adapter = SpinnerArrayAdapter.createSpinnerArrayAdapter(this, utils.Constants.HAIR_COLORS, NONE_COLORS[1]);
        hairColor2Spinner.setAdapter(hairColor2Adapter);
        hairColor2Spinner.setSelection(hairColor2Adapter.getCount());

        Spinner eyeColorSpinner = (Spinner) findViewById(R.id.spinner_eye_color_publish);
        ArrayAdapter<CharSequence> eyeColorAdapter = SpinnerArrayAdapter.createSpinnerArrayAdapter(this, utils.Constants.EYE_COLORS, NONE_COLORS[2]);
        eyeColorSpinner.setAdapter(eyeColorAdapter);
        eyeColorSpinner.setSelection(eyeColorAdapter.getCount());

        setBreed();
        set_age();
        setSize();
        loadImages();
        loadVideos();
        setLocation();
    }

    /**********************************************************************************************/
    /**********************************************************************************************/


    private void initializeImagesData() {
        images = new ArrayList<>();
        imagesPaths = new ArrayList<>();
        bitmapList = new Vector<>();
    }


    /**********************************************************************************************/
    /**********************************************************************************************/

    private void setBreed() {
        AutoCompleteTextView breed = (AutoCompleteTextView) findViewById(R.id.publish_pet_breed);
        breed.setAdapter(new ArrayAdapter<>(activity, android.R.layout.simple_dropdown_item_1line, DOGS));

        SegmentedGroup type = (SegmentedGroup) findViewById(R.id.segmented_pet_type_publish);
        type.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                AutoCompleteTextView breed = (AutoCompleteTextView) findViewById(R.id.publish_pet_breed);
                ArrayAdapter<String> adapter = null;

                Log.e("CHECKED", Integer.toString(group.getCheckedRadioButtonId()));
                if (group.getCheckedRadioButtonId() == ID_TYPE_DOG) {
                    adapter = new ArrayAdapter<>(activity, android.R.layout.simple_dropdown_item_1line, DOGS);
                } else {
                    adapter = new ArrayAdapter<>(activity, android.R.layout.simple_dropdown_item_1line, CATS);
                }
                breed.setAdapter(adapter);
            }
        });

        breed.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                breedSelected = (String) parent.getItemAtPosition(position);
                Toast.makeText(getApplicationContext(), breedSelected, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**********************************************************************************************/
    /**********************************************************************************************/

    private void set_age() {
        SeekBar ages = (SeekBar) findViewById(R.id.pet_age_publish);
        ages.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            TextView ageLabel = (TextView) findViewById(R.id.age_label_publish);

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress < 20) {
                    ageLabel.setText(AGES[0]);
                } else if ((progress >= 20) && (progress < 40)) {
                    ageLabel.setText(AGES[1]);
                } else if ((progress >= 40) && (progress < 60)) {
                    ageLabel.setText(AGES[2]);
                } else if ((progress >= 60) && (progress < 80)) {
                    ageLabel.setText(AGES[3]);
                } else {
                    ageLabel.setText(AGES[4]);
                }
                ageLabel.invalidate();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    /**********************************************************************************************/
    /**********************************************************************************************/

    private void setSize() {
        SeekBar size = (SeekBar) findViewById(R.id.pet_size_publish);
        size.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            TextView sizeLabel = (TextView) findViewById(R.id.size_label_publish);

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress < 33) {
                    sizeLabel.setText(SIZES[0]);
                } else if ((progress >= 33) && (progress < 66)) {
                    sizeLabel.setText(SIZES[1]);
                } else {
                    sizeLabel.setText(SIZES[2]);
                }
                sizeLabel.invalidate();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }



    /**********************************************************************************************/
    /**********************************************************************************************/

    private void loadImages() {
        // Carga de imagenes
        final Button loadImagesButton = (Button) findViewById(R.id.load_publish_image);
        loadImagesButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                takePictureFromGallery();
                loadImagesButton.setVisibility(View.GONE);

            }
        });
    }

    /**********************************************************************************************/

    private void loadVideos() {
        // Carga de videos
        loadVideosButton = (Button) findViewById(R.id.load_publish_video_button);
        loadVideosButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                createDialog("Agregar video", "Copie la url de un video de Youtube").show();
            }
        });
    }

    /**********************************************************************************************/
    /**********************************************************************************************/

    private void setLocation() {
        AutoCompleteTextView neighbourhood = (AutoCompleteTextView) findViewById(R.id.publish_pet_neighbourhood);
        AutoCompleteTextView city = (AutoCompleteTextView) findViewById(R.id.publish_pet_city);
        ArrayAdapter<String> neighbourhoodAdapter = new ArrayAdapter<>(this, R.layout.autocomplete_list_item, NEIGHBOURHOODS);
        neighbourhood.setAdapter(neighbourhoodAdapter);
        ArrayAdapter<String> cityAdapter = new ArrayAdapter<>(this, R.layout.autocomplete_list_item, CITIES);
        city.setAdapter(cityAdapter);
    }





    /**********************************************************************************************/
    /**********************************************************************************************/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_publish_in_adoption, menu);
        return true;
    }

    /**********************************************************************************************/
    /**********************************************************************************************/

    private void takePictureFromGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(Intent.createChooser(intent, "Elije una imagen"), 1);
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

    @Override
    public void onResume() {
        super.onResume();

    }

    /**********************************************************************************************/
    /**********************************************************************************************/


    /**********************************************************************************************/
    /********************           IMAGES UPLOAD AND SENDING TO SERVER         *******************/
    /**********************************************************************************************/

    private Bitmap loadImage(Uri uri) {
        Bitmap bitmap = null;

        try {
            bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }


    /**********************************************************************************************/

    private void sendImage(Bitmap bitmap, int index) {
        try {
            String currentPath = this.getCacheDir().getAbsolutePath() + "tmp-" + Integer.toString(index);
            imagesPaths.add(currentPath);
            bitmapList.add(bitmap);
            File f = new File(currentPath); //this.getCacheDir(), "tmp-" + Integer.toString(index)
            f.createNewFile();

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 10, bos);
            byte[] bitmapdata = bos.toByteArray();

            FileOutputStream fos = new FileOutputStream(f);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();

            QueryResultTask qTask = new QueryResultTask(f);
            qTask.execute((Void) null);
        } catch (IOException ioe) { }
    }

    /**********************************************************************************************/


    private void addImagesToSlider() {
        imageFragmentPagerAdapter = new ImageFragmentPagerAdapter(getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.publish_pager);
        viewPager.setAdapter(imageFragmentPagerAdapter);
        viewPager.setVisibility(View.VISIBLE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            ClipData clipData = data.getClipData();
            if (clipData == null) {
                Uri uri = data.getData();
                Bitmap bitmap = loadImage(uri);
                sendImage(bitmap, 0);
            } else {
                for (int i = 0; i < clipData.getItemCount(); i++) {
                    Bitmap bitmap = loadImage(clipData.getItemAt(i).getUri());
                    Log.e("Uri", clipData.getItemAt(i).getUri().getPath());
                    sendImage(bitmap, i);
                }
            }
            Toast.makeText(getApplicationContext(), "Imágenes cargadas", Toast.LENGTH_SHORT).show();
            addImagesToSlider();
        }
    }

    /**********************************************************************************************/

    public class QueryResultTask extends AsyncTask<Void, Void, Boolean> {
        File image;
        String response;

        QueryResultTask(File image) {
            this.image = image;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            ImageRequest request = new ImageRequest(getApplicationContext());
            response = request.upload(image);
            if (response == null) return false;
            images.add(response);
            Log.e("Response", response);
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (success == false) {
                //Toast.makeText(getApplicationContext(), "Hubo un problema al cargar las imagenes", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onCancelled() { }

    }

    /**********************************************************************************************/
    /**********************************************************************************************/

    @Override
    protected void onStop() {
        super.onStop();
    }

    /**********************************************************************************************/
    /**********************************************************************************************/


    private static class ImageFragmentPagerAdapter extends FragmentPagerAdapter {
        public ImageFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return imagesPaths.size();
        }

        @Override
        public Fragment getItem(int position) {
            SwipeFragment fragment = new SwipeFragment();
            return fragment.newInstance(position);
        }
    }

    public static class SwipeFragment extends Fragment {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View swipeView = inflater.inflate(R.layout.swipe_fragment, container, false);
            ImageView imageView = (ImageView) swipeView.findViewById(R.id.imageView);

            Bundle bundle = getArguments();
            int position = bundle.getInt("position");
            imageView.setImageBitmap(bitmapList.elementAt(position));
            return swipeView;

        }

        public SwipeFragment newInstance(int position) {
            SwipeFragment swipeFragment = new SwipeFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("position", position);
            swipeFragment.setArguments(bundle);
            return swipeFragment;
        }
    }

    /**********************************************************************************************/
    /**********************************************************************************************/

    private AlertDialog createDialog(String titulo, String message) {
        // Instanciamos un nuevo AlertDialog Builder y le asociamos titulo y mensaje
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(titulo);
        alertDialogBuilder.setMessage(message);
        RelativeLayout linearLayout = new RelativeLayout(this);
        final EditText link = new EditText(this);
        link.setHint("URL de Youtube");
        link.setWidth(750);
        linearLayout.addView(link);
        linearLayout.setPadding(70, 0, 0, 0);
        alertDialogBuilder.setView(linearLayout);
        link.invalidate();
        linearLayout.invalidate();

        // Creamos un nuevo OnClickListener para el boton OK que realice la conexion
        DialogInterface.OnClickListener listenerOk = new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                //loadVideosButton.setVisibility(View.GONE);
                TextView videoLink = (TextView) findViewById(R.id.video_report_publish);
                videoLink.setText(link.getText());
                videoLink.setVisibility(View.VISIBLE);
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
        alertDialogBuilder.setNegativeButton(R.string.dialogDone, listenerOk);

        return alertDialogBuilder.create();
    }

    /**********************************************************************************************/
    /**********************************************************************************************/



    public void finish(View view) {

        TextView videos = (TextView) findViewById((R.id.video_report_publish));
        EditText name = (EditText) findViewById(R.id.publish_pet_name);
        SegmentedGroup pet_type = (SegmentedGroup) findViewById(R.id.segmented_pet_type_publish);
        SegmentedGroup pet_gender = (SegmentedGroup) findViewById(R.id.segmented_pet_gender_publish);
        EditText breed = (EditText) findViewById(R.id.publish_pet_breed);
        TextView age = (TextView) findViewById(R.id.age_label_publish);
        TextView size = (TextView) findViewById((R.id.size_label_publish));
        CheckBox castrated = (CheckBox) findViewById(R.id.castrated_check_publish);
        RadioButton temp_meds = (RadioButton) findViewById(R.id.radio_temporary_medicine_publish);
        RadioButton chronic_meds = (RadioButton) findViewById(R.id.radio_chronic_medicine_publish);
        EditText desc = (EditText) findViewById(R.id.pet_desc_publish);
        Spinner hairColor1 = (Spinner) findViewById(R.id.spinner_hair_color1_publish);
        Spinner hairColor2 = (Spinner) findViewById(R.id.spinner_hair_color2_publish);
        Spinner eyesColor = (Spinner) findViewById(R.id.spinner_eye_color_publish);
        TextView colorsTitle = (TextView) findViewById(R.id.colors_publish_title);
        TextView imagesTitle = (TextView) findViewById(R.id.images_publish_title);
        TextView videosTitle = (TextView) findViewById(R.id.video_publish_title);
        CheckBox sociable = (CheckBox)findViewById(R.id.sociable_check);
        CheckBox good_to_others = (CheckBox)findViewById(R.id.other_animals_check);
        CheckBox good_with_kids = (CheckBox)findViewById(R.id.with_kids_check);
        CheckBox companion = (CheckBox)findViewById(R.id.companion_check);
        CheckBox transitHome = (CheckBox) findViewById(R.id.transit_home_publish_check);
        EditText neighbourhood = (EditText) findViewById(R.id.publish_pet_neighbourhood);
        EditText city = (EditText) findViewById(R.id.publish_pet_city);

        CheckBox funny = (CheckBox)findViewById(R.id.plays_check);
        CheckBox quiet = (CheckBox)findViewById(R.id.quiet_check);
        CheckBox guardian = (CheckBox)findViewById(R.id.guardian_check);
        CheckBox agressive = (CheckBox)findViewById(R.id.agressive_check);

        TextView sociableText = (TextView)findViewById(R.id.sociable_label);
        TextView good_to_othersText = (TextView)findViewById(R.id.other_animals_label);
        TextView good_with_kidsText = (TextView)findViewById(R.id.with_kids_label);
        TextView companionText = (TextView)findViewById(R.id.companion_label);
        TextView funnyText = (TextView)findViewById(R.id.plays_label);
        TextView quietText = (TextView)findViewById(R.id.quiet_label);
        TextView guardianText = (TextView)findViewById(R.id.guardian_label);
        TextView agressiveText = (TextView)findViewById(R.id.agressive_label);



        SharedPreferences preferences;
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        JSONObject userData;
        String ownerId = "";

        try {
            userData = new JSONObject(preferences.getString("userData", "{}"));
            ownerId = userData.getString("id").toString();
            object.put("ownerId", ownerId);
        } catch (JSONException e) {
            Toast.makeText(getApplicationContext(), "Error en datos de usuario", Toast.LENGTH_SHORT).show();
        }

        // Reset errors.
        breed.setError(null);
        colorsTitle.setError(null);
        imagesTitle.setError(null);


        boolean cancel = false;
        View focusView = null;                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                              

        String hairColorStr1 = hairColor1.getSelectedItem().toString();
        String hairColorStr2 = hairColor1.getSelectedItem().toString();
        String eyesColorStr = eyesColor.getSelectedItem().toString();



        // Check for a valid dateTextView
        if (imagesPaths.size() == 0) {
            imagesTitle.setError(getString(R.string.error_field_required));
            focusView = imagesTitle;
            cancel = true;
        }

        // Check for a valid petHairColor
        if (hairColorStr1 == NONE_COLORS[0]) {
            colorsTitle.setError(getString(R.string.error_field_required));
            focusView = hairColor1;
            cancel = true;
        }

        // Check for a valid petHairColor
        if (hairColorStr2 == NONE_COLORS[1]) {
            colorsTitle.setError(getString(R.string.error_field_required));
            focusView = hairColor2;
            cancel = true;
        }

        // Check for a valid petHairColor
        if (eyesColorStr == NONE_COLORS[2]) {
            colorsTitle.setError(getString(R.string.error_field_required));
            focusView = eyesColor;
            cancel = true;
        }

        // Check for a valid breed
        if (TextUtils.isEmpty(name.getText().toString())) {
            name.setError(getString(R.string.error_field_required));
            focusView = name;
            cancel = true;
        }

        // Check for a valid breed
        if (breedSelected == null) {
            breed.setError(getString(R.string.error_field_required));
            focusView = breed;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
            Toast.makeText(getApplicationContext(), R.string.error_required_fields_left, Toast.LENGTH_SHORT).show();
        } else {

            try {
                JSONArray a = new JSONArray();
                object.put("videos", a.put(videos.getText().toString()));

                JSONArray imgs = new JSONArray();
                for (String img : images) {
                    imgs.put(img);
                }
                object.put("images", images);
                object.put("name", name.getText());

                Log.e("ID Perro", Integer.toString(pet_type.getCheckedRadioButtonId()));
                if (pet_type.getCheckedRadioButtonId() == ID_TYPE_DOG) {
                    object.put("type", "Perro");
                } else {
                    object.put("type", "Gato");
                }

                Log.e("ID Macho", Integer.toString(pet_gender.getCheckedRadioButtonId()));
                if (pet_gender.getCheckedRadioButtonId() == ID_GENDER_MALE) {
                    object.put("gender", "Macho");
                } else {
                    object.put("gender", "Hembra");
                }
                object.put("breed", breed.getText());
                object.put("age", age.getText());
                object.put("size", size.getText());

                JSONArray colors = new JSONArray();
                colors.put(hairColor1.getSelectedItem().toString());
                colors.put(hairColor2.getSelectedItem().toString());
                object.put("colors", colors);
                object.put("eyeColor", eyesColor.getSelectedItem().toString());
                object.put("isCastrated", Boolean.toString(castrated.isChecked()));

                JSONObject address = new JSONObject();
                address.put("neighbourhood", neighbourhood.getText().toString());
                address.put("city", city.getText().toString());
                object.put("address", address);

                object.put("isOnTemporaryMedicine", temp_meds.isChecked());
                object.put("isOnChronicMedicine", chronic_meds.isChecked());
                object.put("description", desc.getText());
                object.put("needsTransitHome", transitHome.isChecked());
                Log.e("Objeto a enviar", object.toString());


                JSONArray behavior = new JSONArray();
                if (sociable.isChecked())
                    behavior.put(sociableText.getText());
                if (good_to_others.isChecked())
                    behavior.put(good_to_othersText.getText());
                if (good_with_kids.isChecked())
                    behavior.put(good_with_kidsText.getText());
                if (companion.isChecked())
                    behavior.put(companionText.getText());
                if (funny.isChecked())
                    behavior.put(funnyText.getText());
                if (quiet.isChecked())
                    behavior.put(funnyText.getText());
                if (guardian.isChecked())
                    behavior.put(guardianText.getText());
                if (agressive.isChecked())
                    behavior.put(agressiveText.getText());
                object.put("behavior", behavior);




            } catch (JSONException e) {
                Log.e("Error al crear el JSON", e.getMessage());
            }

            request();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            Toast.makeText(getApplicationContext(), "Publicación creada", Toast.LENGTH_SHORT).show();
            if (intent != null)
                startActivity(intent);

        }
    }



    public void request() {
        RequestHandler requestHandler = RequestHandler.getInstance(this);
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                RequestHandler.getServerUrl() + "/pet/adoption",
                object.toString(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Manejo de la respuesta
                        System.out.println(response);
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Manejo de errores
                    }
                });
        requestHandler.addToRequestQueue(request);
    }





    /*

    public void nextPage(View view) {
        EditText name = (EditText)findViewById(R.id.published_pet_name);
        SwitchCompat petType = (SwitchCompat)findViewById(R.id.switch_pet_type);
        SwitchCompat petGender = (SwitchCompat)findViewById(R.id.switch_pet_gender);
        AutoCompleteTextView breed = (AutoCompleteTextView)findViewById(R.id.breed);
        TextView age = (TextView) findViewById(R.id.age_label);
        TextView size = (TextView) findViewById(R.id.size_label);

        try {
            object.put("name", name.getText());

            if (petType.isChecked()) {
                object.put("type", petType.getTextOn());
            } else {
                object.put("type", petType.getTextOff());
            }

            if (petGender.isChecked()) {
                object.put("gender", petGender.getTextOn());
            } else {
                object.put("gender", petGender.getTextOff());
            }

            object.put("breed", breed.getText());
            object.put("age", age.getText());
            object.put("size", size.getText());
            object.put("name", name.getText());

            JSONArray imgs = new JSONArray();
            for (String img : images) {
                imgs.put(img);
            }

            object.put("images", imgs);


        } catch (JSONException e) {
            Log.e("Error al crear el JSON", e.getMessage());
        }



        Intent intent = new Intent(getApplicationContext(), PublishInAdoptionActivity2.class);
        intent.putExtra("data", object.toString());
        if (intent != null)
            startActivity(intent);
    }*/

}

