package com.support.android.designlibdemo;

import android.app.Activity;
import android.app.Notification;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.SyncStateContract;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.design.widget.TabLayout.Tab;
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

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class PublishInAdoptionActivity extends AppCompatActivity {

    private TabLayout mTabLayout;
    JSONObject object = new JSONObject();
    Activity activity = null;

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
        breed.setAdapter(new ArrayAdapter<String>(activity, android.R.layout.simple_dropdown_item_1line, Constants.cats));

        Switch type = (Switch) findViewById(R.id.switch_pet_type);
        type.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // do something, the isChecked will be
                // true if the switch is in the On position
                AutoCompleteTextView breed = (AutoCompleteTextView) findViewById(R.id.breed);
                ArrayAdapter<String> adapter = null;

                if (isChecked) {
                    adapter = new ArrayAdapter<String>(activity, android.R.layout.simple_dropdown_item_1line, Constants.dogs);
                } else {
                    adapter =  new ArrayAdapter<String>(activity, android.R.layout.simple_dropdown_item_1line, Constants.cats);
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
                if (progress < 33) {
                    ageLabel.setText(Constants.ages[0]);
                } else if ((progress >= 33) && (progress < 66)) {
                    ageLabel.setText(Constants.ages[1]);
                } else {
                    ageLabel.setText(Constants.ages[2]);
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
                if (progress < 25) {
                    sizeLabel.setText(Constants.sizes[0]);
                } else if ((progress >= 25) && (progress < 50)) {
                    sizeLabel.setText(Constants.sizes[1]);
                } else if ((progress >= 50) && (progress < 75)) {
                    sizeLabel.setText(Constants.sizes[2]);
                } else {
                    sizeLabel.setText(Constants.sizes[3]);
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
            object.put("nombre", name.getText());

            if (petType.isChecked()) {
                object.put("tipo", petType.getTextOn());
            } else {
                object.put("tipo", petType.getTextOff());
            }

            if (petGender.isChecked()) {
                object.put("genero", petType.getTextOn());
            } else {
                object.put("genero", petType.getTextOff());
            }

            object.put("raza", breed.getText());
            object.put("edad", age.getText());
            object.put("tamaño", size.getText());
            object.put("nombre", name.getText());

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
            Toast.makeText(getApplicationContext(), "Imagenes cargadas", Toast.LENGTH_SHORT).show();
        }
    }
}

