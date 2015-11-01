package com.support.android.designlibdemo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.support.android.designlibdemo.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import utils.MatchRequest;

public class NotificationHandlerActivity extends AppCompatActivity {
    private final String ADOPTION_REQUEST = "ADOPTION_REQUEST";
    private final String PETS_FOUND = "PETS_FOUND";
    private final String NEW_SEARCH_MATCHES = "NEW_SEARCH_MATCHES";
    private SharedPreferences prefs= null;
    private static User loginUser;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_list_view);
        TextView titulo = (TextView) findViewById(R.id.titulo);
        titulo.setText("Resultados del Match");
        context = this;
        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        loginUser = obtenerUsuario(getApplicationContext());

        cargarResultado();

    }

    public void cargarResultado(){
        String userId = loginUser.getId();
        QueryResultTask qTask = new QueryResultTask(userId);
        qTask.execute((Void) null);
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


    public class QueryResultTask extends AsyncTask<Void, Void, Boolean> {
        String userId;
        JSONArray response;

        QueryResultTask(String userId) {
            this.userId = userId;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            MatchRequest request = new MatchRequest(getApplicationContext());
            response = request.getMatch(userId);
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (success) {
                Intent resultIntent = new Intent(context, MatchResultListActivity.class);
                if (response != null) {
                    prefs.edit().putString("searchResponse", response.toString()).commit();
                    context.startActivity(resultIntent);
                }
            }
        }

        @Override
        protected void onCancelled() { }

    }

}
