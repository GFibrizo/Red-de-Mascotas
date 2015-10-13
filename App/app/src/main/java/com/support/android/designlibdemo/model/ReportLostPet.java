package com.support.android.designlibdemo.model;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.method.LinkMovementMethod;
import android.util.AttributeSet;
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
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.support.android.designlibdemo.MainActivity;
import com.support.android.designlibdemo.R;
import com.support.android.designlibdemo.data.maps.MapActivity;
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
import java.util.Objects;
import java.util.Vector;

import utils.ImageRequest;
import utils.RequestHandler;
import utils.SpinnerArrayAdapter;

import static utils.Constants.AGES;
import static utils.Constants.CATS;
import static utils.Constants.DOGS;
import static utils.Constants.SIZES;

public class ReportLostPet extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {

    GoogleMap mGoogleMap = null;
    private double lat = -34.603620;
    private double lng = -58.381598;
    public MapView mapView;
    private Button loadVideosButton = null;
    private TextView dateMissing = null;
    private TextView timeMissing = null;
    private String dateString = null;
    private String timeString = null;
    private static List<String> images = new ArrayList<>();
    private static List<String> imagesPaths = new ArrayList<>();
    private static Vector<Bitmap> bitmapList = new Vector<>();
    static int NUM_ITEMS = 5;
    ViewPager viewPager = null;
    ImageFragmentPagerAdapter imageFragmentPagerAdapter = null;
    String objName = null;
    JSONObject object = null;
    Activity activity = null;

    /**********************************************************************************************/
    /**********************************************************************************************/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_lost_pet);

        String userData = getIntent().getStringExtra("data");
        if (userData != null) {
            // Vengo de MainActivity
            try {
                object = new JSONObject(userData);
            } catch (JSONException e) {
                Log.e("Error receiving intent", e.getMessage());
            }
        } else {
            // Vengo de MapActivity
            String strObj = getIntent().getStringExtra("missing");
            if (strObj != null) {
                try {
                    object = new JSONObject(strObj);
                    Log.e("Object received", object.toString());
                } catch (JSONException e) {
                    Log.e("Error receiving intent", e.getMessage());
                    object = new JSONObject();
                }
            } else {
                object = new JSONObject();
            }
        }

        initializeImagesData();
        Spinner hairColor1Spinner = (Spinner) findViewById(R.id.spinner_hair_color1_missing);
        ArrayAdapter<CharSequence> hairColor1Adapter = SpinnerArrayAdapter.createSpinnerArrayAdapter(this, utils.Constants.HAIR_COLORS, "Color de pelaje principal");
        hairColor1Spinner.setAdapter(hairColor1Adapter);
        hairColor1Spinner.setSelection(hairColor1Adapter.getCount());

        Spinner hairColor2Spinner = (Spinner) findViewById(R.id.spinner_hair_color2_missing);
        ArrayAdapter<CharSequence> hairColor2Adapter = SpinnerArrayAdapter.createSpinnerArrayAdapter(this, utils.Constants.HAIR_COLORS, "Color de pelaje secundario");
        hairColor2Spinner.setAdapter(hairColor2Adapter);
        hairColor2Spinner.setSelection(hairColor2Adapter.getCount());

        Spinner eyeColorSpinner = (Spinner) findViewById(R.id.spinner_eye_color_missing);
        ArrayAdapter<CharSequence> eyeColorAdapter = SpinnerArrayAdapter.createSpinnerArrayAdapter(this, utils.Constants.EYE_COLORS, "Color de ojos");
        eyeColorSpinner.setAdapter(eyeColorAdapter);
        eyeColorSpinner.setSelection(eyeColorAdapter.getCount());

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_report_lost_pet);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);

        activity = this;
        setBreed();
        set_age();
        setSize();
        loadMap();
        loadImages();
        loadVideos();
        loadDateAndTime();
    }


    /**********************************************************************************************/
    /**********************************************************************************************/

    private void setBreed() {
        AutoCompleteTextView breed = (AutoCompleteTextView) findViewById(R.id.missing_pet_breed);
        breed.setAdapter(new ArrayAdapter<>(activity, android.R.layout.simple_dropdown_item_1line, CATS));

        Switch type = (Switch) findViewById(R.id.switch_pet_type_missing);
        type.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // do something, the isChecked will be
                // true if the switch is in the On position
                AutoCompleteTextView breed = (AutoCompleteTextView) findViewById(R.id.missing_pet_breed);
                ArrayAdapter<String> adapter = null;

                if (isChecked) {
                    adapter = new ArrayAdapter<>(activity, android.R.layout.simple_dropdown_item_1line, DOGS);
                } else {
                    adapter = new ArrayAdapter<>(activity, android.R.layout.simple_dropdown_item_1line, CATS);
                }
                breed.setAdapter(adapter);
            }
        });
    }

    private void set_age() {
        SeekBar ages = (SeekBar) findViewById(R.id.pet_age_missing);
        ages.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            TextView ageLabel = (TextView) findViewById(R.id.age_label_missing);

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

    private void setSize() {
        SeekBar size = (SeekBar) findViewById(R.id.pet_size_missing);
        size.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            TextView sizeLabel = (TextView) findViewById(R.id.size_label_missing);

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
    /***********************        AUX OF INITIALIZATION           *******************************/
    /**********************************************************************************************/

    private void loadMap() {
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

    private void loadImages() {
        // Carga de imagenes
        final Button loadImagesButton = (Button) findViewById(R.id.load_missing_images_button);
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
        loadVideosButton = (Button) findViewById(R.id.load_missing_video_button);
        loadVideosButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                createDialog("Agregar video", "Copie la url de un video de Youtube").show();
            }
        });
    }


    /**********************************************************************************************/

    private void loadDateAndTime() {
        // Carga de fecha
        dateMissing = (TextView) findViewById(R.id.date_missing);
        timeMissing = (TextView) findViewById(R.id.time_missing);
        final Button buttonDate = (Button) findViewById(R.id.date_missing_button);
        buttonDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        ReportLostPet.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.show(getFragmentManager(), "Datepickerdialog");
                buttonDate.setVisibility(View.GONE);
            }
        });
    }

    /**********************************************************************************************/


    private void initializeImagesData() {
        images = new ArrayList<>();
        imagesPaths = new ArrayList<>();
        bitmapList = new Vector<>();
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

    public void showMapDetails() {
        Intent intent = new Intent(ReportLostPet.this, MapActivity.class);
        intent.putExtra("missing", object.toString());
        startActivity(intent);
        //moveTaskToBack(true);
    }

    /**********************************************************************************************/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_report_lost_pet, menu);
        return true;
    }

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
        viewPager = (ViewPager) findViewById(R.id.missing_pager);
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
                Toast.makeText(getApplicationContext(), "Hubo un problema al cargar las imagenes", Toast.LENGTH_SHORT).show();
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
                TextView videoLink = (TextView) findViewById(R.id.video_report_missing);
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

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
        timeString = hourOfDay+" : "+minute;
        String time = "Hora: "+ timeString;
        timeMissing.setText(time);
        timeMissing.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        dateString = dayOfMonth+"/"+(monthOfYear+1)+"/"+year;
        String date = "Fecha: " + dateString;
        dateMissing.setText(date);
        dateMissing.setVisibility(View.VISIBLE);
        Calendar now = Calendar.getInstance();
        TimePickerDialog dpd = TimePickerDialog.newInstance(
                ReportLostPet.this,
                now.get(Calendar.HOUR),
                now.get(Calendar.MINUTE),
                true
        );
        dpd.show(getFragmentManager(), "Timepickerdialog");
    }

    /**********************************************************************************************/
    /**********************************************************************************************/

    public void request() {
        RequestHandler requestHandler = RequestHandler.getInstance(this);
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                RequestHandler.getServerUrl() + "pet/lost",
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


    /**********************************************************************************************/
    /**********************************************************************************************/

    /*
    {
    "name": "Manchitas",
    "type": "Gato",
    "ownerId": "560af25344aeb8e12aa73bc7",
    "breed": "Angora turco",
    "gender": "Hembra",
    "age": "1 - 3 años",
    "size": "Chico",
    "colors": ["Blanco"],
    "eyeColor": "Verde",
    "images": ["id"],
    "videos": ["link"],
    "isCastrated": false,
    "isOnTemporaryMedicine": false,
    "isOnChronicMedicine": false,
    "description": "Es una gata muy linda",
    "lastSeenLocation": {
    "latitude": "-34.630661",
    "longitude": "-58.413056"
    },
    "lastSeenDate": "2015/09/29",
    "lastSeenHour": "17:23:38"
    }
     */


    public void finish(View view) {

        TextView videos = (TextView) findViewById((R.id.video_report_missing));
        EditText name = (EditText) findViewById(R.id.missing_pet_name);
        Switch pet_type = (Switch) findViewById(R.id.switch_pet_type_missing);
        Switch pet_gender = (Switch) findViewById(R.id.switch_pet_gender_missing);
        EditText breed = (EditText) findViewById(R.id.missing_pet_breed);
        TextView age = (TextView) findViewById(R.id.age_string_missing);
        TextView size = (TextView) findViewById((R.id.size_string_missing));
        CheckBox castrated = (CheckBox) findViewById(R.id.castrated_check_missing);
        RadioButton temp_meds = (RadioButton) findViewById(R.id.radio_temporary_medicine_missing);
        RadioButton chronic_meds = (RadioButton) findViewById(R.id.radio_chronic_medicine_missing);
        EditText desc = (EditText) findViewById(R.id.pet_desc_missing);
        Spinner hairColor1 = (Spinner) findViewById(R.id.spinner_hair_color1_missing);
        Spinner hairColor2 = (Spinner) findViewById(R.id.spinner_hair_color2_missing);
        Spinner eyesColor = (Spinner) findViewById(R.id.spinner_eye_color_missing);

        try {
            JSONArray a = new JSONArray();
            object.put("videos", a.put(videos.getText()));

            JSONArray imgs = new JSONArray();
            for (String img : images) {
                imgs.put(img);
            }
            object.put("images", images);
            object.put("name", name.getText());
            if (pet_type.isChecked()) {
                object.put("type", pet_type.getTextOn());
            } else {
                object.put("type", pet_type.getTextOff());
            }

            if (pet_gender.isChecked()) {
                object.put("gender", pet_gender.getTextOn());
            } else {
                object.put("gender", pet_gender.getTextOff());
            }
            object.put("breed", breed.getText());
            object.put("age", age.getText());
            object.put("size", size.getText());

            JSONArray colors = new JSONArray();
            colors.put(hairColor1.getSelectedItem().toString());
            colors.put(hairColor2.getSelectedItem().toString());
            object.put("colors", colors);
            object.put("eyeColor", eyesColor.getSelectedItem().toString());
            object.put("isCastrated", castrated.getText());
            object.put("isOnTemporaryMedicine", temp_meds.getText());
            object.put("isOnChronicMedicine", chronic_meds.getText());
            object.put("description", desc);
            object.put("lastSeenDate", dateString);
            object.put("lastSeenHour", timeString);

            Log.e("Objeto a enviar", object.toString());
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
