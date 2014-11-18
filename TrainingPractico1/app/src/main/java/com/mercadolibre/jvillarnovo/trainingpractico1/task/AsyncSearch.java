package com.mercadolibre.jvillarnovo.trainingpractico1.task;

import android.os.AsyncTask;
import android.util.Log;

import com.mercadolibre.jvillarnovo.trainingpractico1.entities.Item;
import com.mercadolibre.jvillarnovo.trainingpractico1.util.Closure;

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
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.LinkedList;

/**
 * Created by jvillarnovo on 10/11/14.
 */
public class AsyncSearch extends AsyncTask<Object,Void,JSONObject> {

    private Closure<LinkedList<Item>> closure;

    public AsyncSearch(String itemSearch, int limit, int offset, Closure<LinkedList<Item>> closure){
        new AsyncSearch(closure).execute(itemSearch,limit,offset);
    }

    private AsyncSearch(Closure<LinkedList<Item>> closure) {
        this.closure = closure;
    }

    @Override
    protected JSONObject doInBackground(Object... objects) {
        Integer limit=(Integer) objects[1];
        Integer offset=(Integer) objects[2];

        try {
            return parseJsonOb(sendRequest(getURL(objects[0].toString(),limit,offset)));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
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
        LinkedList<Item> listResults=new LinkedList<Item>();
        try {
            JSONArray results= jsonResponse.getJSONArray("results");
            for(int i=0;i<results.length();i++){
                listResults.add(generateItem(results.getJSONObject(i)));
            }
            closure.executeOnSuccess(listResults);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private Item generateItem(JSONObject item) throws JSONException {
        return new Item(item.getString("id"), item.getString("title"), item.getDouble("price"), item.getString("available_quantity"), item.getString("subtitle"), item.getString("thumbnail"),item.getString("condition"));
    }

    private String getURL(String item, int limit, int offset) throws UnsupportedEncodingException{
        StringBuilder builder=new StringBuilder();
        builder.append("https://api.mercadolibre.com/sites/MLA/search?q=");
        builder.append(URLEncoder.encode(item,"UTF-8"));
        builder.append("&limit=").append(String.valueOf(limit));
        builder.append("&offset=").append(String.valueOf(offset));

        Log.d("AsyncSearch.getURL()","Item= "+item+"&limit= "+limit+"&offset= "+offset);

        return builder.toString();

    }
}
