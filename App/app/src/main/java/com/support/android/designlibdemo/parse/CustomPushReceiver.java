package com.support.android.designlibdemo.parse;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.parse.ParsePushBroadcastReceiver;
import com.support.android.designlibdemo.MainActivity;
import com.support.android.designlibdemo.NotificationActivity;
import com.support.android.designlibdemo.NotificationHandlerActivity;
import com.support.android.designlibdemo.ResultListActivity;
import com.support.android.designlibdemo.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import utils.MatchRequest;
import utils.NotificationRequest;


public class CustomPushReceiver extends ParsePushBroadcastReceiver {
    private final String ADOPTION_REQUEST = "ADOPTION_REQUEST";
    private final String ADOPTION_ACCEPTED = "ADOPTION_ACCEPTED";
    private final String TAKE_IN_TRANSIT_REQUEST = "TAKE_IN_TRANSIT_REQUEST";
    private final String TAKE_IN_TRANSIT_ACCEPTED = "TAKE_IN_TRANSIT_ACCEPTED";
    private final String NEW_SEARCH_MATCHES = "NEW_SEARCH_MATCHES";
    private final String PETS_FOUND = "PETS_FOUND";
    private final String TAG = CustomPushReceiver.class.getSimpleName();
    private Context context;
    private SharedPreferences prefs = null;
    private String message;
    private String notificationType;

    private static User loginUser;

    private NotificationUtils notificationUtils;

    private Intent parseIntent;

    public CustomPushReceiver() {
        super();
    }

    @Override
    protected void onPushReceive(Context context, Intent intent) {
        super.onPushReceive(context, intent);
        this.context = context;
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        loginUser = obtenerUsuario(context);

        if (intent == null)
            return;

        try {
            JSONObject json = new JSONObject(intent.getExtras().getString("com.parse.Data"));
            Log.e(TAG, "Push received: " + json);
            parseIntent = intent;
            parsePushJson(context, json);
        } catch (JSONException e) {
            Log.e(TAG, "Push message json exception: " + e.getMessage());
        }
    }

    @Override
    protected void onPushDismiss(Context context, Intent intent) {
        super.onPushDismiss(context, intent);
    }
//    private final String ADOPTION_REQUEST = "ADOPTION_REQUEST"; me lleva a notificaciones
//    private final String ADOPTION_ACCEPTED = "ADOPTION_ACCEPTED"; me lleva a notificaciones
//    private final String TAKE_IN_TRANSIT_REQUEST = "TAKE_IN_TRANSIT_REQUEST"; me lleva a notificaciones
//    private final String TAKE_IN_TRANSIT_ACCEPTED = "TAKE_IN_TRANSIT_ACCEPTED"; me lleva a notificaciones
//    private final String PETS_FOUND = "PETS_FOUND"; me lleva a los resultados
//private final String NEW_SEARCH_MATCHES = "NEW_SEARCH_MATCHES"; me lleva a los resultados
    @Override
    protected void onPushOpen(Context context, Intent intent) {
        super.onPushOpen(context, intent);
        try{
            if (intent != null) {
                JSONObject json = new JSONObject(intent.getExtras().getString("com.parse.Data"));
                parsePushJson(context, json);
                if (this.notificationType.equals(PETS_FOUND)) {
                    Intent resultIntent = new Intent(context, NotificationHandlerActivity.class);
                    if (resultIntent != null) {
                        showNotificationMessage(context, this.notificationType, this.message, resultIntent);
                    }
                } else if (this.notificationType.equals(NEW_SEARCH_MATCHES)) {
                    Intent resultIntent = new Intent(context, NotificationHandlerActivity.class);
                    if (resultIntent != null) { //TODO: ver si tengo que pasar algun otro dato
                        showNotificationMessage(context, this.notificationType, this.message, resultIntent);
                    }
                } else {
                    Intent resultIntent = new Intent(context, NotificationHandlerActivity.class);
                    if (resultIntent != null) {
                        showNotificationMessage(context, this.notificationType, this.message, resultIntent);
                    }
                }
            }
        } catch (JSONException e) {
            Log.e(TAG, "Push message json exception: " + e.getMessage());
        }
    }

    /**
     * Parses the push notification json
     *
     * @param context
     * @param json
     */
    private void parsePushJson(Context context, JSONObject json) {
        try {
            this.message = json.getString("alert"); //TODO: va a depender del notificationType
            this.notificationType = json.getString("notificationType");

        } catch (JSONException e) {
            Log.e(TAG, "Push message json exception: " + e.getMessage());
        }
    }


    private User obtenerUsuario(Context context){
        User loginUser = null;
        try {
            JSONObject object = new JSONObject(prefs.getString("userData", "{}"));
            Log.e("USER DATA DETAIL", prefs.getString("userData", "{}"));
            if (object.length() == 0) {
                Toast.makeText(context, "Error cargando datos de usuario", Toast.LENGTH_SHORT).show();
                return null;
            }
            loginUser = new User(object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return loginUser;
    }


    /**
     * Shows the notification message in the notification bar
     * If the app is in background, launches the app
     *
     * @param context
     * @param notificationType
     * @param message
     * @param intent
     */
    private void showNotificationMessage(Context context, String notificationType, String message, Intent intent) {

        notificationUtils = new NotificationUtils(context);
        if (parseIntent != null){
            intent.putExtras(parseIntent.getExtras());
        }
        intent.putExtra("notificationType", notificationType);

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        notificationUtils.showNotificationMessage(message, intent);
    }

}