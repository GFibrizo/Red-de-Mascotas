package com.support.android.designlibdemo.model;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.support.android.designlibdemo.R;
import com.support.android.designlibdemo.data.maps.MapActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import utils.ImageRequest;
import utils.SpinnerArrayAdapter;

public class ReportLostPet extends AppCompatActivity {

    GoogleMap mGoogleMap = null;
    private double lat = -34.603620;
    private double lng = -58.381598;
    public MapView mapView;
    private Button loadVideosButton = null;
    private static List<String> images = new ArrayList<>();
    private static List<String> imagesPaths = new ArrayList<>();
    private static Vector<Bitmap> bitmapList = new Vector<>();
    static int NUM_ITEMS = 5;
    ViewPager viewPager = null;
    ImageFragmentPagerAdapter imageFragmentPagerAdapter = null;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_lost_pet);

        initializeImagesData();
        Spinner hairColor1Spinner = (Spinner) findViewById(R.id.spinner_hair_color1);
        ArrayAdapter<CharSequence> hairColor1Adapter = SpinnerArrayAdapter.createSpinnerArrayAdapter(this, utils.Constants.HAIR_COLORS, "Color de pelaje principal");
        hairColor1Spinner.setAdapter(hairColor1Adapter);
        hairColor1Spinner.setSelection(hairColor1Adapter.getCount());

        Spinner hairColor2Spinner = (Spinner) findViewById(R.id.spinner_hair_color2);
        ArrayAdapter<CharSequence> hairColor2Adapter = SpinnerArrayAdapter.createSpinnerArrayAdapter(this, utils.Constants.HAIR_COLORS, "Color de pelaje secundario");
        hairColor2Spinner.setAdapter(hairColor2Adapter);
        hairColor2Spinner.setSelection(hairColor2Adapter.getCount());

        Spinner eyeColorSpinner = (Spinner) findViewById(R.id.spinner_eye_color);
        ArrayAdapter<CharSequence> eyeColorAdapter = SpinnerArrayAdapter.createSpinnerArrayAdapter(this, utils.Constants.EYE_COLORS, "Color de ojos");
        eyeColorSpinner.setAdapter(eyeColorAdapter);
        eyeColorSpinner.setSelection(eyeColorAdapter.getCount());

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_report_lost_pet);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);


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


        // Carga de imagenes
        final Button loadImagesButton = (Button) findViewById(R.id.load_missing_images_button);
        loadImagesButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                takePictureFromGallery();
                loadImagesButton.setVisibility(View.GONE);

            }
        });

        // Carga de videos
        loadVideosButton = (Button) findViewById(R.id.load_missing_video_button);
        loadImagesButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                createDialog("Agregar video", "Copie la url de un video de Youtube").show();
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


    private void takePictureFromGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(Intent.createChooser(intent, "Elije una imagen"), 1);
    }

    /**********************************************************************************************/

    public void showMapDetails() {
        Intent intent = new Intent(getApplicationContext(), MapActivity.class);
        startActivity(intent);
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
            Log.e("Response", response);
            images.add(response);
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (success) {
                /*Intent intent = new Intent(getApplicationContext(), ResultListActivity.class);
                if (response != null) {
                    intent.putExtra("data", response);
                    startActivity(intent);
                }*/
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
        TextView link = new TextView(this);
        link.setMovementMethod(LinkMovementMethod.getInstance());
        alertDialogBuilder.setView(link);

        // Creamos un nuevo OnClickListener para el boton OK que realice la conexion
        DialogInterface.OnClickListener listenerOk = new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                loadVideosButton.setVisibility(View.INVISIBLE);
                TextView videoLayout = (TextView) findViewById(R.id.video_report_missing);
                videoLayout.setVisibility(View.VISIBLE);
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

}
