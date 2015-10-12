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
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.support.android.designlibdemo.model.User;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import utils.FoundPetRequest;
import utils.ImageRequest;
import utils.SpinnerArrayAdapter;
import utils.UserRegisterRequest;

import static utils.Constants.CATS;
import static utils.Constants.DOGS;
import static utils.Constants.SIZES;


public class FoundPetActivity extends AppCompatActivity implements
        TimePickerDialog.OnTimeSetListener,
        DatePickerDialog.OnDateSetListener {

    JSONObject object = null;
    Activity activity = null;
    List<String> images = new ArrayList<>();
    JSONObject userData = null;
    private TextView timeTextView;
    private TextView dateTextView;

    GoogleMap mGoogleMap = null;
    private double lat = -34.603620;
    private double lng = -58.381598;
    public MapView mapView;

    /**********************************************************************************************/
    /**********************************************************************************************/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_found_pet);

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

        try {
            userData = new JSONObject(getIntent().getStringExtra("data"));
        } catch (JSONException e) {
            Log.e("Error receiving intent", e.getMessage());
        }

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_publish);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);

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

        activity = this;
        AutoCompleteTextView breed = (AutoCompleteTextView) findViewById(R.id.breed);
        breed.setAdapter(new ArrayAdapter<>(activity, android.R.layout.simple_dropdown_item_1line, CATS));

        Switch type = (Switch) findViewById(R.id.switch_pet_type);
        type.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // do something, the isChecked will be
                // true if the switch is in the On position
                AutoCompleteTextView breed = (AutoCompleteTextView) findViewById(R.id.breed);
                ArrayAdapter<String> adapter = null;

                if (isChecked) {
                    adapter = new ArrayAdapter<>(activity, android.R.layout.simple_dropdown_item_1line, DOGS);
                } else {
                    adapter = new ArrayAdapter<>(activity, android.R.layout.simple_dropdown_item_1line, CATS);
                }
                breed.setAdapter(adapter);
            }
        });


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
        timeTextView = (EditText) findViewById(R.id.horaEncuentro);
        dateTextView = (EditText) findViewById(R.id.fechaEncuentro);
//        Button timeButton = (Button) findViewById(R.id.time_button);
//        Button dateButton = (Button) findViewById(R.id.date_button);
        // Show a timepicker when the timeButton is clicked
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

        // Mapa
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
        mapView.getMap().setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                showMapDetails();
            }
        });



    }

/**********************************************************************************************/
    /**********************************************************************************************/

    public void showMapDetails() {
        Intent intent = new Intent(getApplicationContext(), MapActivity.class);
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

        Switch petType = (Switch) findViewById(R.id.switch_pet_type);
        Switch petGender = (Switch) findViewById(R.id.switch_pet_gender);
        AutoCompleteTextView breed = (AutoCompleteTextView) findViewById(R.id.breed);
        TextView size = (TextView) findViewById(R.id.size_label);
        Spinner hairColor1Spinner = (Spinner) findViewById(R.id.spinner_hair_color1);
        //Spinner hairColor2Spinner = (Spinner) findViewById(R.id.spinner_hair_color2);
        Spinner eyeColorSpinner = (Spinner) findViewById(R.id.spinner_eye_color);
        CheckBox transitHome = (CheckBox)findViewById(R.id.transit_home_check);

        TextView petTypeLabel = (TextView) findViewById(R.id.pet_type);
        TextView petGenderLabel = (TextView) findViewById(R.id.pet_gender);
        TextView colorLabel = (TextView) findViewById(R.id.label_hair_color);
        TextView description = (TextView) findViewById(R.id.pet_desc);
        TextView video = (TextView) findViewById(R.id.videoEncuentro);

        SharedPreferences preferences;
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        JSONObject userData;
        JSONObject foundLocation = new JSONObject();
        try {
            foundLocation.put("latitude", "-34.630661");
            foundLocation.put("longitude","-58.413056");
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

        object = new JSONObject();

        // Reset errors.
        petTypeLabel.setError(null);
        petGenderLabel.setError(null);
        breed.setError(null);
        size.setError(null);
        timeTextView.setError(null);
        dateTextView.setError(null);
        colorLabel.setError(null);


        boolean cancel = false;
        View focusView = null;

        String petTypeString = (petType.isChecked()) ? petType.getTextOn().toString() : petType.getTextOff().toString();
        String petGenderString = (petGender.isChecked()) ? petGender.getTextOn().toString() : petGender.getTextOff().toString();
        String hairColor = hairColor1Spinner.getSelectedItem().toString();
        String eyeColor = eyeColorSpinner.getSelectedItem().toString();

        // Check for a valid petType
        if (TextUtils.isEmpty(petTypeString)) {
            petTypeLabel.setError(getString(R.string.error_field_required));
            focusView = petTypeLabel;
            cancel = true;
        }

        // Check for a valid petGender
        if (TextUtils.isEmpty(petGenderString)) {
            petGenderLabel.setError(getString(R.string.error_field_required));
            focusView = petGenderLabel;
            cancel = true;
        }
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
        if (TextUtils.isEmpty(breed.getText().toString())) {
            breed.setError(getString(R.string.error_field_required));
            focusView = breed;
            cancel = true;
        }
        // Check for a valid size
        if (TextUtils.isEmpty(size.getText().toString())) {
            size.setError(getString(R.string.error_field_required));
            focusView = size;
            cancel = true;
        }
        // Check for a valid timeTextView
        if (TextUtils.isEmpty(timeTextView.getText().toString())) {
            timeTextView.setError(getString(R.string.error_field_required));
            focusView = timeTextView;
            cancel = true;
        }
        // Check for a valid dateTextView
        if (TextUtils.isEmpty(dateTextView.getText().toString())) {
            dateTextView.setError(getString(R.string.error_field_required));
            focusView = dateTextView;
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
                object.put("foundHour", timeTextView.getText());
                object.put("foundDate", dateTextView.getText());
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


    private void sendImage(Bitmap bitmap, int index) {
        try {
            File f = new File(this.getCacheDir(), "tmp-" + Integer.toString(index));
            f.createNewFile();

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 10, bos);
            byte[] bitmapdata = bos.toByteArray();

            FileOutputStream fos = new FileOutputStream(f);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();

            ImageRequestTask qTask = new ImageRequestTask(f);
            qTask.execute((Void) null);
        } catch (IOException ioe) {
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        ImageView view = (ImageView) findViewById(R.id.load_image);
        if (resultCode == RESULT_OK) {
            ClipData clipData = data.getClipData();
            if (clipData == null) {
                Uri uri = data.getData();
                Bitmap bitmap = loadImage(uri);
                view.setImageBitmap(bitmap);
                sendImage(bitmap, 0);

            } else {
                for (int i = 0; i < clipData.getItemCount(); i++) {
                    Bitmap bitmap = loadImage(clipData.getItemAt(i).getUri());
                    Log.e("Uri", clipData.getItemAt(i).getUri().getPath());
                    if (i == 0) view.setImageBitmap(bitmap);
                    sendImage(bitmap, i);
                }
            }
            Toast.makeText(getApplicationContext(), "Imágenes cargadas", Toast.LENGTH_SHORT).show();
        }
    }

    /**********************************************************************************************/
    /**********************************************************************************************/

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
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (success) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                Toast.makeText(getApplicationContext(), "Exito", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }else{
                Toast.makeText(getApplicationContext(), "Error de conexion", Toast.LENGTH_SHORT).show();
            }
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

}

