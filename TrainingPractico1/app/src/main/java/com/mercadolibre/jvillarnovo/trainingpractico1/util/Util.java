package com.mercadolibre.jvillarnovo.trainingpractico1.util;

import com.mercadolibre.jvillarnovo.trainingpractico1.entities.Item;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by jvillarnovo on 25/11/14.
 */
public class Util {

    public interface PreferencesConstants {
        public static final String LAST_QUERY = "last_query";
        public static final String PREF_FILE = "tp1_pref_file";
        public static final String IS_ALARM_SET = "is_alarm_set";
    }

    public static HttpResponse sendRequest(String url) {
        HttpGet request = new HttpGet(url);
        request.setHeader(new BasicHeader("Content-Type", "application/json"));
        HttpClient client = new DefaultHttpClient();
        try {
            return client.execute(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static JSONObject parseJsonOb(HttpResponse response) {
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

    public static Item generateItem(JSONObject jsonObject) throws JSONException {
        Item item = new Item(jsonObject.getString("id"), jsonObject.getString("title"), jsonObject.getDouble("price"), jsonObject.getString("available_quantity"), jsonObject.getString("subtitle"), jsonObject.getString("thumbnail"), jsonObject.getString("condition"));
        item.setDate(jsonObject.getString("stop_time"));
        return item;
    }

    public static long getTime(String date) {
        return parseDate(date).getTime();
    }

    public static Date parseDate(String date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        try {
            Date date1 = format.parse(date);
            return date1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
