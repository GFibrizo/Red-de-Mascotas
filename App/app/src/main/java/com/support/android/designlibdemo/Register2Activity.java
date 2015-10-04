package com.support.android.designlibdemo;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.support.android.designlibdemo.model.Address;
import com.support.android.designlibdemo.model.User;

import org.json.JSONException;
import org.json.JSONObject;

import utils.UserRegisterRequest;

public class Register2Activity extends AppCompatActivity {

    private EditText mNameView;
    private EditText mAddressView;
    private EditText mPhoneView;

    private Button mNextButton;
    private Button mBackButton;

    private UserTask userTask  = null;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);
        try {
            JSONObject object = new JSONObject(getIntent().getStringExtra("data"));
            this.user = new User();
            this.user.setUserName(object.getString("userName"));
            this.user.setPassword(object.getJSONObject("password"));
            this.user.setEmail(object.getString("email"));
        } catch (JSONException e) {
            Log.e("Error receiving intent", e.getMessage());
        }


        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_publish);
        setSupportActionBar(toolbar);
        mNameView = (EditText)findViewById(R.id.input_name);
        mAddressView = (EditText)findViewById(R.id.input_address);
        mPhoneView = (EditText)findViewById(R.id.input_phone);
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

        Address address = new Address();
        address.setStreet(mAddressView.getText().toString());
        //Validate
        user.setAddress(address);

        user.setName(mNameView.getText().toString());
        user.setLastName(mNameView.getText().toString());
        user.setPhone(mPhoneView.getText().toString());


        //User user = new User();
        userTask = new UserTask(user);
        userTask.execute((Void) null);

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
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (success) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("user", user.toJson().toString());
                Toast.makeText(getApplicationContext(), "Usuario creado", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        }

        @Override
        protected void onCancelled() { }

    }
}
