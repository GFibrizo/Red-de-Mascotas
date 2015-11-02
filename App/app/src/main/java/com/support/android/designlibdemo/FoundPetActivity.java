package com.support.android.designlibdemo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.support.android.designlibdemo.data.maps.MapActivity;
import com.support.android.designlibdemo.model.MapCoordenates;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import info.hoang8f.android.segmented.SegmentedGroup;
import utils.FoundPetRequest;
import utils.ImageRequest;
import utils.SpinnerArrayAdapter;

import static utils.Constants.CATS;
import static utils.Constants.DOGS;
import static utils.Constants.SIZES;


public class FoundPetActivity extends AppCompatActivity implements
        TimePickerDialog.OnTimeSetListener,
        DatePickerDialog.OnDateSetListener {

    JSONObject object = null;
    Activity activity = null;
    JSONObject userData = null;
    private TextView timeTextView;
    private TextView dateTextView;
    ViewPager viewPager = null;
    ImageFragmentPagerAdapter imageFragmentPagerAdapter = null;
    private TextView dateFound = null;
    private TextView timeFound = null;
    private String dateString = null;
    private String timeString = null;
    private Button loadVideosButton = null;

    GoogleMap mGoogleMap = null;
    private double lat = -34.603620;
    private double lng = -58.381598;
    public MapView mapView;

    private int ID_TYPE_DOG = 2131558617;
    private int ID_GENDER_MALE = 2131558621;
    private ProgressDialog progress;

    private static List<String> images = new ArrayList<>();
    private static List<String> imagesPaths = new ArrayList<>();
    private static Vector<Bitmap> bitmapList = new Vector<>();

    /**********************************************************************************************/
    /**********************************************************************************************/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_found_pet);

        activity = this;
        initObject();
        initializeImagesData();
        initSpinners();
        initUserData();
        initToolBar();
        initActionBar();
        //initImage();
        loadImages();
        loadVideos();
        initBreed();
        initType();
        initSize();
        loadDateAndTime();
        // Show a timepicker when the timeButton is clicked
        //initDateTime();
        // Mapa
        initMap();
    }

    /**********************************************************************************************/
    /**********************************************************************************************/

    private void loadImages() {
        // Carga de imagenes
        final Button loadImagesButton = (Button) findViewById(R.id.load_found_images_button);
        loadImagesButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                takePictureFromGallery();
                loadImagesButton.setVisibility(View.GONE);

            }
        });
    }

    private void loadVideos() {
        // Carga de videos
        loadVideosButton = (Button) findViewById(R.id.load_found_video_button);
        loadVideosButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                createDialog("Agregar video", "Copie la url de un video de Youtube").show();
            }
        });
    }

    /**********************************************************************************************/
    /**********************************************************************************************/

    private void initObject() {
        object = new JSONObject();
    }

    /**********************************************************************************************/
    /**********************************************************************************************/

    private void initToolBar() {
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_found);
        setSupportActionBar(toolbar);
    }

    /**********************************************************************************************/
    /**********************************************************************************************/

    private void initUserData() {
        try {
            userData = new JSONObject(getIntent().getStringExtra("data"));
        } catch (JSONException e) {
            Log.e("Error receiving intent", e.getMessage());
        }
    }

    /**********************************************************************************************/
    /**********************************************************************************************/

    private void initActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);
    }

    /**********************************************************************************************/
    /**********************************************************************************************/

    private void initImage() {
        ImageView image = (ImageView) findViewById(R.id.load_image);
        image.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                takePictureFromGallery();
               /* Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 0);*/
            }
        });
    }

    /**********************************************************************************************/
    /**********************************************************************************************/

    private void initBreed() {
//        AutoCompleteTextView breed = (AutoCompleteTextView) findViewById(R.id.breed);
//        breed.setAdapter(new ArrayAdapter<>(activity, android.R.layout.simple_dropdown_item_1line, CATS));
        AutoCompleteTextView breed = (AutoCompleteTextView) findViewById(R.id.breed);
        breed.setAdapter(new ArrayAdapter<>(activity, android.R.layout.simple_dropdown_item_1line, DOGS));
    }

    /**********************************************************************************************/
    /**********************************************************************************************/

    private void initSpinners() {
        Spinner hairColor1Spinner = (Spinner) findViewById(R.id.spinner_hair_color1);
        ArrayAdapter<CharSequence> hairColor1Adapter = SpinnerArrayAdapter.createSpinnerArrayAdapter(this, utils.Constants.HAIR_COLORS, "Color principal");
        hairColor1Spinner.setAdapter(hairColor1Adapter);
        hairColor1Spinner.setSelection(hairColor1Adapter.getCount());

        Spinner hairColor2Spinner = (Spinner) findViewById(R.id.spinner_hair_color2);
        ArrayAdapter<CharSequence> hairColor2Adapter = SpinnerArrayAdapter.createSpinnerArrayAdapter(this, utils.Constants.HAIR_COLORS, "Color secundario");
        hairColor2Spinner.setAdapter(hairColor2Adapter);
        hairColor2Spinner.setSelection(hairColor2Adapter.getCount());

        Spinner eyeColorSpinner = (Spinner) findViewById(R.id.spinner_eye_color);
        ArrayAdapter<CharSequence> eyeColorAdapter = SpinnerArrayAdapter.createSpinnerArrayAdapter(this, utils.Constants.EYE_COLORS, "Color de ojos");
        eyeColorSpinner.setAdapter(eyeColorAdapter);
        eyeColorSpinner.setSelection(eyeColorAdapter.getCount());
    }

    /**********************************************************************************************/
    /**********************************************************************************************/


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
                TextView videoLink = (TextView) findViewById(R.id.video_report_found);
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


    private void initType() {
        //SwitchCompat type = (SwitchCompat) findViewById(R.id.switch_pet_type);
//        type.setOn
//
// ChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                // do something, the isChecked will be
//                // true if the switch is in the On position
//                AutoCompleteTextView breed = (AutoCompleteTextView) findViewById(R.id.breed);
//                ArrayAdapter<String> adapter = null;
//
//                if (isChecked) {
//                    adapter = new ArrayAdapter<>(activity, android.R.layout.simple_dropdown_item_1line, DOGS);
//                } else {
//                    adapter = new ArrayAdapter<>(activity, android.R.layout.simple_dropdown_item_1line, CATS);
//                }
//                breed.setAdapter(adapter);
//            }
//        });
        SegmentedGroup type = (SegmentedGroup) findViewById(R.id.segmented_pet_type_found);
        type.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                AutoCompleteTextView breed = (AutoCompleteTextView) findViewById(R.id.breed);
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
    }

    /**********************************************************************************************/
    /**********************************************************************************************/

    private void initSize() {
        SeekBar size = (SeekBar) findViewById(R.id.pet_size);

        size.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            TextView sizeLabel = (TextView) findViewById(R.id.size_label);

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

    /*private void initDateTime() {
        timeTextView = (EditText) findViewById(R.id.horaEncuentro);
        dateTextView = (EditText) findViewById(R.id.fechaEncuentro);

        timeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                TimePickerDialog tpd = TimePickerDialog.newInstance(
                        FoundPetActivity.this,
                        now.get(Calendar.HOUR_OF_DAY),
                        now.get(Calendar.MINUTE),
                        true//mode24Hours.isChecked()
                );
                tpd.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        Log.d("TimePicker", "Dialog was cancelled");
                    }
                });
                tpd.show(getFragmentManager(), "Timepickerdialog");
            }
        });

        dateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        FoundPetActivity.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );

                dpd.show(getFragmentManager(), "Datepickerdialog");
            }
        });
    }*/

    /**********************************************************************************************/
    /**********************************************************************************************/

    private void loadDateAndTime() {
        // Carga de fecha
        dateFound = (TextView) findViewById(R.id.date_found);
        timeFound = (TextView) findViewById(R.id.time_found);
        final Button buttonDate = (Button) findViewById(R.id.date_found_button);
        buttonDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        FoundPetActivity.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.show(getFragmentManager(), "Datepickerdialog");
                //buttonDate.setVisibility(View.GONE);
            }
        });
    }


    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
        timeString = String.format("%02d:%02d",hourOfDay,minute);

        String time = "Hora: "+ timeString;
        timeFound.setText(time);
        timeFound.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        dateString = String.format("%04d/%02d/%02d", year, monthOfYear+1, dayOfMonth);// dateString;

        String date = "Fecha: " + dayOfMonth+"/"+(monthOfYear+1)+"/"+year;
        //Toast.makeText(getApplicationContext(), date, Toast.LENGTH_SHORT).show();
        dateFound.setText(date);
        dateFound.setVisibility(View.VISIBLE);
        Calendar now = Calendar.getInstance();
        TimePickerDialog dpd = TimePickerDialog.newInstance(
                FoundPetActivity.this,
                now.get(Calendar.HOUR),
                now.get(Calendar.MINUTE),
                true
        );
        dpd.show(getFragmentManager(), "Timepickerdialog");
    }

    /**********************************************************************************************/
    /**********************************************************************************************/

    private void initMap() {
        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(null);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                MapsInitializer.initialize(getApplicationContext());
                mGoogleMap = googleMap;
                if (mGoogleMap != null) {
                    mGoogleMap.clear();
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 10f);
                    mGoogleMap.moveCamera(cameraUpdate);
                }
            }
        });

        GoogleMap map = mapView.getMap();
        if ( map != null){
            map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    showMapDetails();
                }
            });
        }
    }

    /**********************************************************************************************/
    /**********************************************************************************************/

    public void showMapDetails() {
        //Intent intent = new Intent(getApplicationContext(), MapActivity.class);
        //intent.putExtra("missing", object.toString());
        Intent intent = new Intent(FoundPetActivity.this, MapActivity.class);
        //intent.putExtra("missing", object.toString());
        intent.putExtra("From", FoundPetActivity.class);
        startActivity(intent);
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

    public void finish(View view) {

        SegmentedGroup pet_type = (SegmentedGroup) findViewById(R.id.segmented_pet_type_found);
        SegmentedGroup pet_gender = (SegmentedGroup) findViewById(R.id.segmented_pet_gender_found);
        AutoCompleteTextView breed = (AutoCompleteTextView) findViewById(R.id.breed);
        TextView size = (TextView) findViewById(R.id.size_label);
        Spinner hairColor1Spinner = (Spinner) findViewById(R.id.spinner_hair_color1);
        Spinner hairColor2Spinner = (Spinner) findViewById(R.id.spinner_hair_color2);
        Spinner eyeColorSpinner = (Spinner) findViewById(R.id.spinner_eye_color);
        CheckBox transitHome = (CheckBox)findViewById(R.id.transit_home_check);

        TextView petTypeLabel = (TextView) findViewById(R.id.pet_type);
        TextView petGenderLabel = (TextView) findViewById(R.id.pet_gender);
        TextView colorLabel = (TextView) findViewById(R.id.colors_found_title);
        TextView description = (TextView) findViewById(R.id.pet_desc);
        TextView video = (TextView) findViewById(R.id.video_report_found);
        TextView dateTitle = (TextView) findViewById(R.id.date_found_title);

        SharedPreferences preferences;
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        JSONObject userData;
        JSONObject foundLocation = new JSONObject();
        MapCoordenates mapCoordenates = MapCoordenates.getInstance();
        try {
            foundLocation.put("latitude", mapCoordenates.getLatitude());
            foundLocation.put("longitude",mapCoordenates.getLongitude());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String finderId = "";

        try {
            userData = new JSONObject(preferences.getString("userData", "{}"));
            finderId = userData.getString("id").toString();
        } catch (JSONException e) {
            Toast.makeText(getApplicationContext(), "Error en datos de usuario", Toast.LENGTH_SHORT).show();
        }



        // Reset errors.
        petTypeLabel.setError(null);
        petGenderLabel.setError(null);
        breed.setError(null);
        size.setError(null);
        timeFound.setError(null);
        dateFound.setError(null);
        colorLabel.setError(null);


        boolean cancel = false;
        View focusView = null;
        String petTypeString;
        String petGenderString;

        Log.e("PERRO", Integer.toString(pet_type.getCheckedRadioButtonId()));
        Log.e("MACHO", Integer.toString(pet_gender.getCheckedRadioButtonId()));

        petTypeString = (pet_type.getCheckedRadioButtonId() == ID_TYPE_DOG) ?  "Perro": "Gato";
        petGenderString = (pet_gender.getCheckedRadioButtonId() == ID_GENDER_MALE) ? "Macho" : "Hembra";


        String hairColor = hairColor1Spinner.getSelectedItem().toString();
        String eyeColor = eyeColorSpinner.getSelectedItem().toString();

        // Check for a valid petGender
        if (TextUtils.isEmpty(hairColor)) {
            colorLabel.setError(getString(R.string.error_field_required));
            focusView = colorLabel;
            cancel = true;
        }
        // Check for a valid petGender
        if (TextUtils.isEmpty(eyeColor)) {
            colorLabel.setError(getString(R.string.error_field_required));
            focusView = colorLabel;
            cancel = true;
        }

        // Check for a valid breed
//        if (TextUtils.isEmpty(breed.getText().toString())) {
//            breed.setError(getString(R.string.error_field_required));
//            focusView = breed;
//            cancel = true;
//        }
        // Check for a valid size
        if (TextUtils.isEmpty(size.getText().toString())) {
            size.setError(getString(R.string.error_field_required));
            focusView = size;
            cancel = true;
        }
        // Check for a valid timeTextView
        if (TextUtils.isEmpty(timeFound.getText().toString())) {
            dateTitle.setError(getString(R.string.error_field_required));
            focusView = dateTitle;
            cancel = true;
        }
        // Check for a valid dateTextView
        if (TextUtils.isEmpty(dateFound.getText().toString())) {
            dateTitle.setError(getString(R.string.error_field_required));
            focusView = dateTitle;
            cancel = true;
        }



        if (cancel) {
            focusView.requestFocus();
        } else {

            try {
                object.put("finderId", finderId);
                object.put("type", petTypeString);
                object.put("gender", petGenderString);
                object.put("breed", breed.getText());
                object.put("colors", hairColor);
                object.put("eyeColor", eyeColor);
                object.put("needsTransitHome", transitHome.isChecked());
                object.put("foundHour", timeString);
                object.put("foundDate", dateString);
                object.put("size", size.getText());
                object.put("foundLocation",foundLocation);
                object.put("description",description.getText());


                JSONArray videos = new JSONArray();
                JSONArray imgs = new JSONArray();
                for (String img : images) {
                    imgs.put(img);
                }
                videos.put(video.getText());
                object.put("images", imgs);
                object.put("videos", videos);
                Log.e("FOUND PET", object.toString());
            } catch (JSONException e) {
                Log.e("Error al crear el JSON", e.getMessage());
            }
            AlertDialog dialogo = crearDialogo("Confirmar hallazgo",
                    "En caso de coincidencia con una mascota reportada como  perdida," +
                    "Se le enviará una notificación a usted y al dueño de esta publicación");
            dialogo.show();

        }

    }

    /**********************************************************************************************/
    /**********************************************************************************************/

    private AlertDialog crearDialogo(String titulo, String mensaje) {
        // Instanciamos un nuevo AlertDialog Builder y le asociamos titulo y mensaje
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(titulo);
        alertDialogBuilder.setMessage(mensaje);

        // Creamos un nuevo OnClickListener para el boton OK que realice la conexion
        DialogInterface.OnClickListener listenerOk = new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                progress = ProgressDialog.show(FoundPetActivity.this, "Publicando", "Se está publicando la mascota", true);
                FoundPetTask foundPetTask = new FoundPetTask();
                foundPetTask.execute((Void) null);
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
/*
    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = year+ "/" + (++monthOfYear) + "/" + dayOfMonth;
        dateTextView.setText(date);
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
        String hourString = hourOfDay < 10 ? "0" + hourOfDay : "" + hourOfDay;
        String minuteString = minute < 10 ? "0" + minute : "" + minute;
        String time = hourString + ":" + minuteString;
        timeTextView.setText(time);
    }
*/
    /**********************************************************************************************/
    /**********************************************************************************************/

    public class FoundPetTask extends AsyncTask<Void, Void, Boolean> {

        JSONObject response;

        @Override
        protected Boolean doInBackground(Void... params) {
            FoundPetRequest request = new FoundPetRequest(getApplicationContext());
            try {

                response = request.post(object);
            } catch (InterruptedException | ExecutionException  | TimeoutException e) {
                // exception handling
                Log.e("Found Error",e.getMessage());
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            progress.dismiss();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            if (success) {
                Toast.makeText(getApplicationContext(), "Exito", Toast.LENGTH_SHORT).show();
            }else{
//                Toast.makeText(getApplicationContext(), "Error de conexion", Toast.LENGTH_SHORT).show();
            }
            Log.e("Exit Found", "EXITING FOUND");
            startActivity(intent);
        }

        @Override
        protected void onCancelled() { }

    }

    /**********************************************************************************************/
    /**********************************************************************************************/

    public class ImageRequestTask extends AsyncTask<Void, Void, Boolean> {
        File image;
        String response;

        ImageRequestTask(File image) {
            this.image = image;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            ImageRequest request = new ImageRequest(getApplicationContext());
            response = request.upload(image);
            Log.e("Response", response);
            images.add(response);
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            //
        }

        @Override
        protected void onCancelled() {
        }

    }





    /**********************************************************************************************/
    /********************           IMAGES UPLOAD AND SENDING TO SERVER         *******************/
    /**********************************************************************************************/

    private void initializeImagesData() {
        images = new ArrayList<>();
        imagesPaths = new ArrayList<>();
        bitmapList = new Vector<>();
    }


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
        viewPager = (ViewPager) findViewById(R.id.found_pager);
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
            Log.e("Response", response);
            images.add(response);
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


}

