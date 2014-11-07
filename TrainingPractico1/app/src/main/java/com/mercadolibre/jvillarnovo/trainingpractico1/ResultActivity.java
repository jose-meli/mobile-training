package com.mercadolibre.jvillarnovo.trainingpractico1;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.mercadolibre.jvillarnovo.trainingpractico1.Adapters.ListViewAdapter;
import com.mercadolibre.jvillarnovo.trainingpractico1.entities.Item;

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
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.LinkedList;


public class ResultActivity extends Activity {

    public static final String ITEM_SEARCH="item_search";
    private AsyncSearch searchThread;
    private ListView listViewResults;
    private ListViewAdapter listViewAdapter;
    private LinkedList<Item> listResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        initComponents();

        if(savedInstanceState!=null){
            Serializable ob=savedInstanceState.getSerializable("listResults");
            if(ob instanceof LinkedList){
                listResults.addAll((LinkedList<Item>) savedInstanceState.getSerializable("listResults"));
            }
        }
    }

    public void initComponents(){
        listViewResults=(ListView) findViewById(R.id.listResults);
        listResults=new LinkedList<Item>();
        listViewAdapter=new ListViewAdapter(getLayoutInflater(),listResults);
        listViewResults.setAdapter(listViewAdapter);
        if(searchThread==null) {
            searchThread = new AsyncSearch();
            searchThread.execute(getIntent().getStringExtra(ITEM_SEARCH));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("listResults",listResults);
    }

    @Override
    protected void onResume() {
        super.onResume();
        showResultsView();
    }

    private void showResultsView(){
        listViewAdapter.notifyDataSetChanged();
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

    private void insertItemList(Item item)
    {
        listResults.add(item);
    }

    private Item generateItem(JSONObject item) throws JSONException {
        return new Item(item.getString("id"), item.getString("title"), item.getDouble("price"), item.getString("available_quantity"), item.getString("subtitle"));
    }

    private class AsyncSearch extends AsyncTask<String,Void,JSONObject> {

        private String url;

        @Override
        protected JSONObject doInBackground(String... strings) {
            try {
                url = "https://api.mercadolibre.com/sites/MLA/search?q=" + URLEncoder.encode(strings[0], "UTF-8") + "&limit=100";
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
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
            }
            return null;
        }

        private JSONObject parseJsonOb(HttpResponse response) {
            try {
                String result = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
                return new JSONObject(result);
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
                JSONArray results= jsonResponse.getJSONArray("results");
                for(int i=0;i<results.length();i++){
                    insertItemList(generateItem(results.getJSONObject(i)));
                }
                showResultsView();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
