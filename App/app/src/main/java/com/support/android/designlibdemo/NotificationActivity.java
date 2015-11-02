package com.support.android.designlibdemo;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.support.android.designlibdemo.model.InquirerNotification;
import com.support.android.designlibdemo.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import utils.Constants;
import utils.NotificationRequest;


public class NotificationActivity extends AppCompatActivity {
    private JSONArray object = null;
    private ListView listView ;
    private String dataServer;
    private ArrayList<InquirerNotification> textAndImageArray;
    private List<InquirerNotification> notifications = null;
    private NotificationImageAndTextArrayAdapter adapter;
    private SharedPreferences prefs;
    private User loginUser;
    protected String baseUrlForImage;
    private String IP_EMULADOR = Constants.IP_SERVER;//"http://10.0.2.2:9000"; //ip generica del emulador


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_result);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);

        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        try {
            JSONObject object = new JSONObject(prefs.getString("userData", "{}"));
            Log.e("USER DATA DETAIL", prefs.getString("userData", "{}"));
            if (object.length() == 0) {
                Toast.makeText(getApplicationContext(), "Error cargando datos de usuario", Toast.LENGTH_SHORT).show();
                return;
            }
            this.loginUser = new User(object);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        cargarResultados();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_result_list, menu);
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


    private void cargarResultados(){
        // Get ListView object from xml
        listView = (ListView) findViewById(R.id.list);
        textAndImageArray = new ArrayList<InquirerNotification>();
        adapter = new NotificationImageAndTextArrayAdapter(this, R.layout.notification_image_and_text ,
                null, (ArrayList<InquirerNotification>) textAndImageArray);

        // ListView Item Click Listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("item","position " + position + " id " + id);

            }
        });

        String userId = loginUser.getId();
        QueryResultTask qTask = new QueryResultTask(userId);
        qTask.execute((Void) null);

    }


    public class QueryResultTask extends AsyncTask<Void, Void, Boolean> {
        String userId;
        JSONArray response;

        QueryResultTask(String userId) {
            this.userId = userId;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            NotificationRequest request = new NotificationRequest(getApplicationContext());
            response = request.search(userId);
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (success) {
                if (response != null) {
                    dataServer = response.toString();
                    try {
                        if (dataServer != null) {
                            object = new JSONArray(dataServer);
                        }
                        if (object != null){
                            notifications = fromJSONArrayToListNotifications(object);
                        }
                    } catch (JSONException e) {
                        Log.e("Error receiving intent", e.getMessage());
                    }

                    if (notifications != null) {
                        for (int i = 0; i < notifications.size(); i++) {
                            textAndImageArray.add(notifications.get(i));
                        }
                    }

                    adapter.setElements(textAndImageArray);
                    listView.setAdapter(adapter);
                }
            }
        }

        @Override
        protected void onCancelled() { }

    }

    private List<InquirerNotification> fromJSONArrayToListNotifications(JSONArray jsonArray) {
        List<InquirerNotification> list = new ArrayList<>();
        try {
            if (jsonArray != null) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    InquirerNotification notification = new InquirerNotification(
                            object.getString("notificationType"),
                            object.getString("petId"),
                            object.getString("inquirerId"),
                            object.getString("inquirerEmail"),
                            object.getString("requestDate"),
                            object.getString("petName"),
                            object.getString("petImageId"),
                            object.getString("status") //"PENDING" o "ACCEPTED"
                    );
                    list.add(notification);
                }
            }
        } catch (JSONException e) {
            Log.e("Error al crear el JSON", e.getMessage());
        }
        return list;
    }


}
