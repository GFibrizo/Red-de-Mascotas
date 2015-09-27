package com.support.android.designlibdemo;


import android.content.Intent;

import android.support.v4.app.FragmentActivity;

import android.os.Bundle;

import android.view.View;
import android.widget.Button;


import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.share.Sharer;
import com.facebook.share.widget.ShareDialog;

import java.util.Arrays;

public class LoginActivity extends FragmentActivity {


    private ShareDialog shareDialog;
    private CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;

    private FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            AccessToken accessToken = loginResult.getAccessToken();
            Profile profile = Profile.getCurrentProfile();
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onError(FacebookException e) {

        }

    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(this.getApplicationContext());
        setContentView(R.layout.activity_login);


        callbackManager = CallbackManager.Factory.create();


        accessTokenTracker= new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken newToken) {

            }
        };

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {
//                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                startActivity(intent);
                //showResult(newProfile.getFirstName(),newProfile.getLastName());
            }
        };

        accessTokenTracker.startTracking();
        profileTracker.startTracking();


        final Button button = (Button) findViewById(R.id.btn_login);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, LoginUserActivity.class);
                startActivity(intent);
            }
        });
        LoginButton loginButton = (LoginButton) findViewById(R.id.fb_login_button);
        loginButton.setReadPermissions("user_friends");
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
        profileTracker.stopTracking();
    }

    @Override
    public void onResume() {
        super.onResume();

        Profile profile = Profile.getCurrentProfile();
        if(profile != null){
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra("PROFILE_IMG_ID", profile.getProfilePictureUri(10,10));
            intent.putExtra("PROFILE_LAST_NAME", profile.getLastName());
            intent.putExtra("PROFILE_FIRST_NAME", profile.getFirstName());
            startActivity(intent);
        }else{
            //finish();
        }
    }

}
