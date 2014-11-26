package com.mercadolibre.jvillarnovo.trainingpractico1.task;

import android.os.AsyncTask;
import android.util.Log;

import com.mercadolibre.jvillarnovo.trainingpractico1.entities.Item;
import com.mercadolibre.jvillarnovo.trainingpractico1.util.Closure;
import com.mercadolibre.jvillarnovo.trainingpractico1.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.LinkedList;

/**
 * Created by jvillarnovo on 10/11/14.
 */
public class AsyncSearch extends AsyncTask<Object, Void, JSONObject> {

    private static int offset = 0;
    private Closure<LinkedList<Item>> closure;

    public AsyncSearch(String itemSearch, int limit, int offset, Closure<LinkedList<Item>> closure) {
        new AsyncSearch(closure, offset).execute(itemSearch, limit, getOffset());
    }

    private AsyncSearch(Closure<LinkedList<Item>> closure, int offset) {
        this.closure = closure;
        this.offset = offset;
    }

    public synchronized static int getOffset() {
        return offset;
    }

    @Override
    protected JSONObject doInBackground(Object... objects) {
        Integer limit = (Integer) objects[1];
        Integer offset = (Integer) objects[2];

        try {
            return Util.parseJsonOb(Util.sendRequest(getURL(objects[0].toString(), limit, offset)));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(JSONObject jsonResponse) {
        LinkedList<Item> listResults = new LinkedList<Item>();
        try {
            JSONArray results = jsonResponse.getJSONArray("results");
            for (int i = 0; i < results.length(); i++) {
                listResults.add(Util.generateItem(results.getJSONObject(i)));
            }
            closure.executeOnSuccess(listResults);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String getURL(String item, int limit, int offset) throws UnsupportedEncodingException {
        StringBuilder builder = new StringBuilder();
        builder.append("https://api.mercadolibre.com/sites/MLA/search?q=");
        builder.append(URLEncoder.encode(item, "UTF-8"));
        builder.append("&limit=").append(String.valueOf(limit));
        builder.append("&offset=").append(String.valueOf(offset));

        Log.d("AsyncSearch.getURL()", "Item= " + item + "&limit= " + limit + "&offset= " + offset);

        return builder.toString();

    }

    public static Item getItem(String itemId) {
        String url = "https://api.mercadolibre.com/items/" + itemId;
        try {
            return Util.generateItem(Util.parseJsonOb(Util.sendRequest(url)));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
