package com.support.android.designlibdemo;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


/*import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpPost;
//import cz.msebera.android.httpclient.entity.mime.HttpMultipartMode;
//import cz.msebera.android.httpclient.entity.mime.content.FileBody;
//import cz.msebera.android.httpclient.entity.mime.content.StringBody;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.protocol.BasicHttpContext;
import cz.msebera.android.httpclient.protocol.HttpContext;
*/

import static utils.Constants.AGES;
import static utils.Constants.CATS;
import static utils.Constants.DOGS;
import static utils.Constants.SIZES;

public class PublishInAdoptionActivity extends AppCompatActivity {

    JSONObject object = new JSONObject();
    Activity activity = null;
    final Context mContext = this;
    String mimeType;
    DataOutputStream dos = null;
    String lineEnd = "\r\n";
    String boundary = "apiclient-" + System.currentTimeMillis();
    String twoHyphens = "--";
    int bytesRead, bytesAvailable, bufferSize;
    byte[] buffer;
    int maxBufferSize = 1024 * 1024;

    /**********************************************************************************************/
    /**********************************************************************************************/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_in_adoption);

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
                    adapter =  new ArrayAdapter<>(activity, android.R.layout.simple_dropdown_item_1line, CATS);
                }
                breed.setAdapter(adapter);
            }
        });


        SeekBar ages = (SeekBar) findViewById(R.id.pet_age);
        SeekBar size = (SeekBar) findViewById(R.id.pet_size);
        ages.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            TextView ageLabel = (TextView) findViewById(R.id.age_label);
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

    public void nextPage(View view) {
        EditText name = (EditText)findViewById(R.id.published_pet_name);
        Switch petType = (Switch)findViewById(R.id.switch_pet_type);
        Switch petGender = (Switch)findViewById(R.id.switch_pet_gender);
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
                object.put("gender", petType.getTextOn());
            } else {
                object.put("gender", petType.getTextOff());
            }

            object.put("breed", breed.getText());
            object.put("age", age.getText());
            object.put("size", size.getText());
            object.put("name", name.getText());

        } catch (JSONException e) {
            Log.e("Error al crear el JSON", e.getMessage());
        }
        Intent intent = new Intent(getApplicationContext(), PublishInAdoptionActivity2.class);
        intent.putExtra("data", object.toString());
        if (intent != null)
            startActivity(intent);
    }

    /**********************************************************************************************/
    /**********************************************************************************************/

    private Bitmap loadImage(Uri uri) {
        Bitmap bitmap = null;

        try {
            bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
            try {
                ArrayList<String> arrayList = (ArrayList) object.get("imagenes");
                arrayList.add(uri.toString());
            } catch (JSONException e) {
                Log.e("Error loading images", e.getMessage());
                return null;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            object.put("imagenes", new ArrayList<String>());
        } catch (JSONException e) { Log.e("Error JSON", e.getMessage());}

        List<String> imageList = new ArrayList<>();
        ImageView view = (ImageView) findViewById(R.id.load_image);
        if (resultCode == RESULT_OK) {
            ClipData clipData = data.getClipData();
            if (clipData == null) {
                Uri uri = data.getData();
                Bitmap bitmap = loadImage(uri);
                view.setImageBitmap(bitmap);
            } else {
                for (int i = 0; i < clipData.getItemCount(); i++) {
                    Bitmap bitmap = loadImage(clipData.getItemAt(i).getUri());
                    if (i == 0) view.setImageBitmap(bitmap);
                }
            }
            Toast.makeText(getApplicationContext(), "Imágenes cargadas", Toast.LENGTH_SHORT).show();
        }
    }
}

