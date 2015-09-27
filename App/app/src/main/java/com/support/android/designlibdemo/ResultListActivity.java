package com.support.android.designlibdemo;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

/**  Esta clase se va a encargar de enviarle los resultados de las busquedas en tuplas de imagen
 *   y texto al adapter ImageAndTextArrayAdapter para que haga su display.
 */

public class ResultListActivity extends AppCompatActivity {

    private ListView listView ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_list_view);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_result);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);

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
        ArrayList<TextAndImage> textAndImageArray = new ArrayList<TextAndImage>();
        textAndImageArray.add(new TextAndImageContainer());
        textAndImageArray.add(new TextAndImageContainer());
        textAndImageArray.add(new TextAndImageContainer());
        textAndImageArray.add(new TextAndImageContainer());
        textAndImageArray.add(new TextAndImageContainer());
        textAndImageArray.add(new TextAndImageContainer());
        textAndImageArray.add(new TextAndImageContainer());
        textAndImageArray.add(new TextAndImageContainer());
        textAndImageArray.add(new TextAndImageContainer());
        textAndImageArray.add(new TextAndImageContainer());
        textAndImageArray.add(new TextAndImageContainer());
        textAndImageArray.add(new TextAndImageContainer());

        // ListView Item Click Listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
//               @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    Intent intent = new Intent(thisActivity, WallActivity.class);
//                    intent.putExtra("ownProfile", false);
//                    int friendId = friends.get(position).getId();
//                    intent.putExtra("friend_id", friendId);
//                    startActivity(intent);
//                }
            }
        });

//                            String urlBaseForImage = IpConfig.LOCAL_IP.url() + "/getstudentpicture/";

        ImageAndTextArrayAdapter adapter = new ImageAndTextArrayAdapter(this, R.layout.mock_image_and_text_single_row ,
                    null, (ArrayList<TextAndImage>) textAndImageArray);
        listView.setAdapter(adapter);

    }


    public class TextAndImageContainer implements TextAndImage {
        private int id;
        private String text;

        TextAndImageContainer(){

        }

        TextAndImageContainer(int id, String text){
            this.id = id;
            this.text = text;
        }

        // Defined Array values to show in ListView
        String[] values = new String[]{"Fiona", "Simba", "Homero", "Casandra", "Cleopatra"};

        @Override
        public int getId() {
//            return id;
            return 0; //TODO: cambiar url - new ImageUrlView(IpConfig.LOCAL_IP.url() + "/getstudentpicture/" + friendId, profilePricture).connect();
        }

        @Override
        public String getText() {
//            return text;
            return values[0]; //TODO: cambiar origen - mValues.get(position)
        }
    }

}
