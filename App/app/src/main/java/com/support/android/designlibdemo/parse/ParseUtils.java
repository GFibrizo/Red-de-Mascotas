package com.support.android.designlibdemo.parse;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;
import com.parse.Parse;
import com.parse.ParseInstallation;

import utils.Constants;


public class ParseUtils {

    private static String TAG = ParseUtils.class.getSimpleName();

    public static void verifyParseConfiguration(Context context) {
        if (TextUtils.isEmpty(Constants.PARSE_APPLICATION_ID) || TextUtils.isEmpty(Constants.PARSE_CLIENT_KEY)) {
            Toast.makeText(context, "Please configure your Parse Application ID and Client Key in AppConfig.java", Toast.LENGTH_LONG).show();
            ((Activity) context).finish();
        }
    }

    public static void registerParse(Context context) {
        // initializing parse library
        try{
            Parse.initialize(context, Constants.PARSE_APPLICATION_ID, Constants.PARSE_CLIENT_KEY);
            ParseInstallation.getCurrentInstallation().saveInBackground();
        } catch (Exception e) {
            e.printStackTrace();
        }


//        ParsePush.subscribeInBackground(Constants.PARSE_CHANNEL, new SaveCallback() {
//            @Override
//            public void done(ParseException e) {
//                Log.e(TAG, "Successfully subscribed to Parse!");
//            }
//        });
    }

    public static void subscribeWithEmail(String email) {
        ParseInstallation installation = ParseInstallation.getCurrentInstallation();

        installation.put("email", email);

        installation.saveInBackground();
    }
}