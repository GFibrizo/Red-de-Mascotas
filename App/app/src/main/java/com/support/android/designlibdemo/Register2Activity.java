package com.support.android.designlibdemo;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.support.android.designlibdemo.model.SearchForAdoptionFilters;
import com.support.android.designlibdemo.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import utils.SearchRequest;
import utils.UserRequest;

public class Register2Activity extends AppCompatActivity {
    private JSONObject object = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);
        try {
            object = new JSONObject(getIntent().getStringExtra("data"));
        } catch (JSONException e) {
            Log.e("Error receiving intent", e.getMessage());
        }
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
    public void nextPage(View view) {

        EditText name = (EditText)findViewById(R.id.input_name);
        EditText mail = (EditText)findViewById(R.id.input_telephone);
        EditText password = (EditText)findViewById(R.id.input_password);
        EditText rePassword = (EditText)findViewById(R.id.input_re_password);

        try {
            object.put("nombre", name.getText());
            object.put("mail", mail.getText());
            object.put("password", password.getText());
            object.put("rePassword", rePassword.getText());



        } catch (JSONException e) {
            Log.e("Error al crear el JSON", e.getMessage());
        }
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        Toast.makeText(getApplicationContext(), "Usuario creado", Toast.LENGTH_SHORT).show();
        if (intent != null)
            startActivity(intent);
    }


    public class UserTask extends AsyncTask<Void, Void, Boolean> {

        User user;
        JSONArray response;

        UserTask( User user) {
            this.user = user;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            UserRequest request = new UserRequest(getApplicationContext());
            response = request.create(user);
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (success) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("data", response.toString());
                startActivity(intent);
            }
        }

        @Override
        protected void onCancelled() { }

    }
}
