package com.support.android.designlibdemo;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.parse.Parse;
import com.parse.ParseInstallation;
import com.support.android.designlibdemo.model.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import utils.Constants;
import utils.LoginRequest;
import utils.UserRegisterRequest;


public class LoginActivity extends AppCompatActivity {


    private CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;
    SharedPreferences preferences = null;
    private FacebookCallback<LoginResult> callback;
    private Activity activity;

    {
        callback = new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject json, GraphResponse response) {

                                if (response.getError() != null) {
                                    // handle error
                                    Toast.makeText(getApplicationContext(), "Error de Conexion", Toast.LENGTH_SHORT).show();
                                } else {
                                    UserRegisterRequest userRegisterRequest = new UserRegisterRequest(getApplicationContext());
                                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                    userRegisterRequest.setPreferences(preferences);
                                    userRegisterRequest.setContext(getApplicationContext());
                                    userRegisterRequest.setCallerActivity(LoginActivity.this);

                                    try {
                                        userRegisterRequest.registerFacebookUser(json);
                                    } catch (TimeoutException | ExecutionException | InterruptedException e) {
                                        Toast.makeText(getApplicationContext(), "Error de Conexion", Toast.LENGTH_SHORT).show();
                                    }
                                }

                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,link,gender,birthday,email");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException e) {
            }

        };
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        setContentView(R.layout.activity_login);

        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        JSONObject object = null;
        try {
            object = new JSONObject(preferences.getString("userData", "{}")); //getIntent().getStringExtra("user")
        } catch (JSONException e) {
            Toast.makeText(getApplicationContext(), "Error en datos de usuario", Toast.LENGTH_SHORT).show();
        }
        if (object.length() != 0) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }

        //Inicializo Parse
        try{
            Parse.enableLocalDatastore(getApplication());
            Parse.initialize(this, Constants.PARSE_APPLICATION_ID, Constants.PARSE_CLIENT_KEY);
            ParseInstallation.getCurrentInstallation().saveInBackground();
        } catch (Exception e) {
            e.printStackTrace();
        }

        callbackManager = CallbackManager.Factory.create();
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken newToken) {
            }
        };
        accessTokenTracker.startTracking();
        final Button button = (Button) findViewById(R.id.btn_login);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, LoginUserActivity.class);
                startActivity(intent);
            }
        });
        LoginButton loginButton = (LoginButton) findViewById(R.id.fb_login_button);
        List<String> readPermissions =  Arrays.asList("publish_actions");//Arrays.asList("email", "public_profile", "user_friends");
        loginButton.setPublishPermissions(readPermissions);
        loginButton.registerCallback(callbackManager, callback);


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onStop() {
        super.onStop();
        accessTokenTracker.stopTracking();
        //profileTracker.stopTracking();
    }

    @Override
    public void onResume() {
        super.onResume();

        Profile profile = Profile.getCurrentProfile();
        if (profile != null) {
            UserFacebookTask userFacebookTask = new UserFacebookTask(profile);
            userFacebookTask.execute((Void) null);
        } else {
            //TODO: Lanzar mensaje de error
        }
    }

    public void toRegister(View view) {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    public class UserFacebookTask extends AsyncTask<Void, Void, Boolean> {

        Profile profile;
        JSONObject response;

        UserFacebookTask(Profile profile) {
            this.profile = profile;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            LoginRequest loginRequest = new LoginRequest(getApplicationContext());
            try {
                response = loginRequest.getFacebookUser(profile.getId());
            } catch (TimeoutException | ExecutionException | InterruptedException e) {
                return false;
            }

            return (response != null);
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (success) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                //LoginManager manager = LoginManager.getInstance();
                //manager.logOut();
                //Collection<String> publishPermissions = Arrays.asList("publish_actions");
                //manager.logInWithPublishPermissions(activity, publishPermissions);


                if (response != null) {
                    //intent.putExtra("user", response.toString());
                    preferences.edit().putString("userData", response.toString()).commit();
                } else {
                    Toast.makeText(getApplicationContext(), "Usuario o contraseña invalidos", Toast.LENGTH_SHORT).show();
                }
                startActivity(intent);
                finish();
            }
        }

        @Override
        protected void onCancelled() {
        }

    }
}
