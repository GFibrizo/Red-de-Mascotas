package com.support.android.designlibdemo;


import android.content.Intent;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;

import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
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
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.share.Sharer;
import com.facebook.share.widget.ShareDialog;
import com.support.android.designlibdemo.model.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import utils.LoginRequest;
import utils.UserRegisterRequest;

import static java.util.Arrays.*;

public class LoginActivity extends FragmentActivity {


    private ShareDialog shareDialog;
    private CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;
    //private ProfileTracker profileTracker;
    SharedPreferences preferences = null;
    private FacebookCallback<LoginResult> callback;

    {
        callback = new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
            AccessToken accessToken = loginResult.getAccessToken();
            GraphRequest.newMeRequest(
                loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject json, GraphResponse response) {
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        if (response.getError() != null) {
                            // handle error
                            Toast.makeText(getApplicationContext(), "Error de Conexion", Toast.LENGTH_SHORT).show();
                        } else {
                            UserRegisterRequest userRegisterRequest = new UserRegisterRequest(getApplicationContext());
                            JSONObject jUser = null;
                            try {
                                jUser = userRegisterRequest.registerFacebookUser(json);
                            }catch (TimeoutException | ExecutionException | InterruptedException e) {
                                Toast.makeText(getApplicationContext(), "Error de Conexion", Toast.LENGTH_SHORT).show();
                            }
                            if (jUser!= null){
                                User user = new User(jUser);
                                intent.putExtra("user", user.toJson().toString());
                            }
                        }
                        startActivity(intent);
                    }
                }).executeAsync();
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
        }

        callbackManager = CallbackManager.Factory.create();
        accessTokenTracker= new AccessTokenTracker() {
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
        List<String> permissions = Arrays.asList("email", "public_profile", "user_friends");
        loginButton.setReadPermissions(permissions);
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
        if(profile != null) {
            UserFacebookTask userFacebookTask = new UserFacebookTask(profile);
            userFacebookTask.execute((Void) null);
        }else{
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

        UserFacebookTask(Profile profile ) {
            this.profile= profile;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            LoginRequest loginRequest = new LoginRequest(getApplicationContext());
            try {
                response = loginRequest.getFacebookUser(profile.getId());
            } catch (TimeoutException | ExecutionException | InterruptedException e) {
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (success) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                if (response != null){
                    //intent.putExtra("user", response.toString());
                    preferences.edit().putString("userData", response.toString()).commit();
                }else{
                    Toast.makeText(getApplicationContext(), "Error de Conexion", Toast.LENGTH_SHORT).show();
                }
                startActivity(intent);
            }
        }

        @Override
        protected void onCancelled() { }

    }
}
