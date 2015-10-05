package com.support.android.designlibdemo;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
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

import utils.ImageRequest;

import static utils.Constants.AGES;
import static utils.Constants.CATS;
import static utils.Constants.DOGS;
import static utils.Constants.SIZES;

public class PublishInAdoptionActivity extends AppCompatActivity {

    JSONObject object = null;
    Activity activity = null;
    List<String> images = new ArrayList<>();
    JSONObject userData = null;

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

            JSONArray imgs = new JSONArray();
            for (String img : images) {
                imgs.put(img);
            }

            object.put("images", images);


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

            QueryResultTask qTask = new QueryResultTask(f);
            qTask.execute((Void) null);
        } catch (IOException ioe) { }
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

}

