package com.support.android.designlibdemo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.support.android.designlibdemo.model.Address;
import com.support.android.designlibdemo.model.User;

import org.json.JSONException;
import org.json.JSONObject;

import utils.UserRegisterRequest;

import static utils.Constants.NEIGHBOURHOODS;

public class Register2Activity extends AppCompatActivity {

    private EditText mNameView;
    private EditText mLastNameView;
    private EditText mAddressNumView;
    private EditText mAddressView;
    AutoCompleteTextView neighbourhoodTextView;
    private EditText mPhoneView;

    private Button mNextButton;
    private Button mBackButton;

    private UserTask userTask  = null;
    private User user;

    SharedPreferences preferences = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);

        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        try {
            JSONObject object = new JSONObject(getIntent().getStringExtra("data"));
            this.user = new User();
            this.user.setUserName(object.getString("userName"));
            this.user.setPassword(object.getJSONObject("password"));
            this.user.setEmail(object.getString("email"));
        } catch (JSONException e) {
            Log.e("Error receiving intent", e.getMessage());
        }


        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_register);
        setSupportActionBar(toolbar);
        mNameView = (EditText)findViewById(R.id.input_name);
        mLastNameView = (EditText)findViewById(R.id.input_lastName);
        mAddressNumView = (EditText)findViewById(R.id.input_addressNum);
        mAddressView = (EditText)findViewById(R.id.input_address);
        mPhoneView = (EditText)findViewById(R.id.input_phone);

        neighbourhoodTextView = (AutoCompleteTextView) findViewById(R.id.input_neighbourhood);
        ArrayAdapter<String> neighbourhoodAdapter = new ArrayAdapter<>(this, R.layout.autocomplete_list_item, NEIGHBOURHOODS);
        neighbourhoodTextView.setAdapter(neighbourhoodAdapter);

        mNextButton = (Button) findViewById(R.id.button_finish);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptFinish();
            }
        });
        mBackButton = (Button) findViewById(R.id.button_back);
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register2, menu);
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

    public void back(){
        Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
        startActivity(intent);
    }

    public void attemptFinish() {

        View focusView = null;
        boolean cancel = false;

        mAddressNumView.setError(null);
        mAddressView.setError(null);
        neighbourhoodTextView.setError(null);
        mNameView.setError(null);
        mLastNameView.setError(null);
        mPhoneView.setError(null);

        if (TextUtils.isEmpty(mAddressNumView.getText())) {
            mAddressNumView.setError(getString(R.string.error_field_required));
            focusView = mAddressNumView;
            cancel = true;
        }
        if (TextUtils.isEmpty(mAddressView.getText())) {
            mAddressView.setError(getString(R.string.error_field_required));
            focusView = mAddressView;
            cancel = true;
        }

        if (TextUtils.isEmpty(neighbourhoodTextView.getText())) {
            neighbourhoodTextView.setError(getString(R.string.error_field_required));
            focusView = neighbourhoodTextView;
            cancel = true;
        }

        if (TextUtils.isEmpty(mNameView.getText())) {
            mNameView.setError(getString(R.string.error_field_required));
            focusView = mNameView;
            cancel = true;
        }

        if (TextUtils.isEmpty(mLastNameView.getText())) {
            mLastNameView.setError(getString(R.string.error_field_required));
            focusView = mLastNameView;
            cancel = true;
        }

        if (TextUtils.isEmpty(mPhoneView.getText())) {
            mPhoneView.setError(getString(R.string.error_field_required));
            focusView = mPhoneView;
            cancel = true;
        }



        if (cancel) {
            focusView.requestFocus();
        } else {
            Address address = new Address();
            address.setNumber(mAddressNumView.getText().toString());
            address.setStreet(mAddressView.getText().toString());
            address.setNeighbourhood(neighbourhoodTextView.getText().toString());
            address.setCity("Ciudad Autónoma de Buenos Aires");
            address.setProvince("Ciudad Autónoma de Buenos Aires");
            address.setCountry("Argentina");
            //Validate
            user.setAddress(address);

            user.setName(mNameView.getText().toString());
            user.setLastName(mLastNameView.getText().toString());
            user.setPhone(mPhoneView.getText().toString());
            userTask = new UserTask(user);
            userTask.execute((Void) null);
        }
    }


    public class UserTask extends AsyncTask<Void, Void, Boolean> {

        User user;
        JSONObject response;

        UserTask( User user) {
            this.user = user;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            UserRegisterRequest request = new UserRegisterRequest(getApplicationContext());
            response = request.registerUser(user.toJson());
            if (response == null)
                return false;

            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (success) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                //intent.putExtra("user", user.toJson().toString());
                preferences.edit().putString("userData", response.toString()).commit();
                Toast.makeText(getApplicationContext(), "Usuario creado", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }else{
                Toast.makeText(getApplicationContext(), "Error de conexion", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onCancelled() { }

    }
}
