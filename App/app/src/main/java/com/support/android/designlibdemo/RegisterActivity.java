package com.support.android.designlibdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.support.android.designlibdemo.model.Password;

import org.json.JSONException;
import org.json.JSONObject;

import utils.SecurityHandler;
import utils.Validators;

public class RegisterActivity extends AppCompatActivity {


    private EditText mUserNameView;
    private EditText mEmailView;
    private EditText mPasswordView;
    private EditText mRePasswordView;
    private Button mNextButton;
    private Button mCancelButton;

    private Password password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_register);
        setSupportActionBar(toolbar);
        mUserNameView = (EditText) findViewById(R.id.input_userName);
        mEmailView = (EditText) findViewById(R.id.input_email);
        mPasswordView = (EditText) findViewById(R.id.input_password);
        mRePasswordView = (EditText) findViewById(R.id.input_re_password);
        mNextButton = (Button) findViewById(R.id.button_next);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptNext();
            }
        });
        mCancelButton = (Button) findViewById(R.id.button_cancel);
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancel();
            }
        });
    }

    private void attemptNext() {
        // Reset errors.
        mUserNameView.setError(null);
        mEmailView.setError(null);
        mPasswordView.setError(null);
        mRePasswordView.setError(null);

        // Store values at the time of the login attempt.
        String userName = mUserNameView.getText().toString();
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        String rePassword = mRePasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;
        Validators validator = new Validators();
        // Check for a valid userName.
        if (TextUtils.isEmpty(userName)) {
            mUserNameView.setError(getString(R.string.error_field_required));
            focusView = mUserNameView;
            cancel = true;
        }


        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        }else {
            if (!validator.validateEmail(email)){
                mEmailView.setError(getString(R.string.error_invalid_email));
                focusView = mEmailView;
                cancel = true;
            }
        }
        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password) || !validator.isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }
        // Check for a valid repassword
        if (TextUtils.isEmpty(password) || !password.equals(rePassword)) {
            mRePasswordView.setError(getString(R.string.error_invalid_re_password));
            focusView = mRePasswordView;
            cancel = true;
        }
        SecurityHandler securityHandler = new SecurityHandler();
        this.password = securityHandler.createPassword(mPasswordView.getText().toString());

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            nextPage();
        }
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
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

    public void cancel() {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
    }

    public void nextPage() {
        JSONObject object = new JSONObject();
        try {
            object.put("userName", mUserNameView.getText());
            object.put("email", mEmailView.getText());
            object.put("password", password.toJson());
        } catch (JSONException e) {
            Log.e("Error al crear el JSON", e.getMessage());
        }
        Intent intent = new Intent(getApplicationContext(), Register2Activity.class);
        intent.putExtra("data", object.toString());
        if (intent != null)
            startActivity(intent);
    }
}
