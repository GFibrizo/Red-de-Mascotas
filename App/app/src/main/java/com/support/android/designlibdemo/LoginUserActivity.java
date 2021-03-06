package com.support.android.designlibdemo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import utils.Constants;
import utils.LoginRequest;

import com.parse.Parse;
import com.parse.ParseInstallation;
import com.support.android.designlibdemo.model.Password;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import utils.SecurityHandler;

/**
 * A login screen that offers login via email/password.
 */
public class LoginUserActivity extends AppCompatActivity {


    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView userView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    SharedPreferences preferences = null;
    final private boolean BLOCKED = false;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_user);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_login);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);

        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        // Set up the login form.
        userView = (AutoCompleteTextView) findViewById(R.id.user);
        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        //Inicializo Parse
        try{
            Parse.initialize(this, Constants.PARSE_APPLICATION_ID, Constants.PARSE_CLIENT_KEY);
            ParseInstallation.getCurrentInstallation().saveInBackground();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    public void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        userView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String user = userView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(user)) {
            userView.setError(getString(R.string.error_field_required));
            focusView = userView;
            cancel = true;
        } else if (!isUserValid(user)) {
            userView.setError(getString(R.string.error_invalid_user));
            focusView = userView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(user, password);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isUserValid(String user) {
        //TODO: Longitud mínima: 6 caracteres - Longitud máxima: 12 caracteres
        //return ((user.length() >= 6 && user.length() <= 12) && user.matches("[a-zA-Z][0-9]+"));
        return ((user.length() >= 3 && user.length() <= 12));
    }

    private boolean isPasswordValid(String password) {
        //TODO: Longitud mínima: 6 caracteres - Longitud máxima: 12 caracteres
        //*return ((password.length() >= 6 && password.length() <= 12) && password.matches("[a-zA-Z][0-9]+"));
        return ((password.length() >= 6 && password.length() <= 12));

    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }


    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mUser;
        private final String mPassword;
        private boolean mStatus;
        private boolean userValid;
        private boolean passValid;
        private boolean apiError;
        JSONObject responseUserLogin;

        UserLoginTask(String user, String password) {
            mUser = user;
            mPassword = password;
            mStatus = false;
            userValid = false;
            passValid = false;
            apiError = false;
        }


        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            SecurityHandler securityHandler = new SecurityHandler();
            LoginRequest loginRequest = new LoginRequest(getApplicationContext());
            String salt = null;
            try {
                salt = loginRequest.getUserSalt(mUser);
                userValid = (salt != null);
                if (userValid) {
                    Password encryptedPassword = securityHandler.createPassword(mPassword, salt);
                    userValid = true;
                    responseUserLogin = loginRequest.isValidUserPassword(mUser, encryptedPassword);
                    if (responseUserLogin != null) {
                        mStatus = true;
                    }
                    passValid = mStatus;
                } else {
                    userValid = false;
                    mStatus = userValid;
                }
            } catch (TimeoutException | ExecutionException | InterruptedException e) {
                apiError = true;
                return false;
            }
            return mStatus;
        }


        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (!success) {
                if (apiError){
                    Toast.makeText(getApplicationContext(), "Usuario o contraseña invalidos", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!userValid) {
                    userView.setError(getString(R.string.error_incorrect_password));
                    userView.requestFocus();
                }
                if (!passValid) {
                    mPasswordView.setError(getString(R.string.error_incorrect_password));
                    mPasswordView.requestFocus();
                }
                return;
            }

            try {
                if (responseUserLogin.getBoolean("active") == BLOCKED) {
                    Toast.makeText(getApplicationContext(), "Su usuario ha sido bloqueado debido a una gran cantidad de denuncias", Toast.LENGTH_LONG).show();
                    return;
                }
            } catch (JSONException e) {}
            Intent intent = new Intent(LoginUserActivity.this, MainActivity.class);
            //intent.putExtra("user", responseUserLogin.toString());
            preferences.edit().putString("userData", responseUserLogin.toString()).commit();
            startActivity(intent);
            finish();
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }


    }


}

