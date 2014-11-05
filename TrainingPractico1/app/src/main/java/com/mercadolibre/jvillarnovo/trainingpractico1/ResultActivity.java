package com.mercadolibre.jvillarnovo.trainingpractico1;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


public class ResultActivity extends Activity {

    public static final String ITEM_SEARCH="item_search";
    private AsyncSearch searchThread;
    private ListView listResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        initComponents();
    }

    public void initComponents(){
        listResults=(ListView) findViewById(R.id.listResults);
        if(searchThread==null) {
            searchThread = new AsyncSearch();
            searchThread.execute(getIntent().getStringExtra(ITEM_SEARCH));
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_result, menu);
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

    private void insertItemList(){
        
    }

    private class AsyncSearch extends AsyncTask<String,Void,JSONObject> {

        private String url;

        @Override
        protected JSONObject doInBackground(String... strings) {
            url = "https://api.mercadolibre.com/sites/MLA/search?q=" + strings[0] + "&limit=100";
            return parseJsonOb(sendRequest(url));
        }

        private HttpResponse sendRequest(String url) {
            HttpGet request = new HttpGet(url);
            request.setHeader(new BasicHeader("Content-Type","application/json"));
            HttpClient client = new DefaultHttpClient();

            try {
                return client.execute(request);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        private JSONObject parseJsonOb(HttpResponse response) {
            if(response.getEntity()!=null)
                Log.d("AsyncSearch", response.getEntity().toString());

            try {
                String result = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
                JSONObject object = new JSONObject(result);
                return object;
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(JSONObject jsonResponse) {
            try {
                JSONArray results=new JSONArray(jsonResponse.getString("result"));

                searchThread=null;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
