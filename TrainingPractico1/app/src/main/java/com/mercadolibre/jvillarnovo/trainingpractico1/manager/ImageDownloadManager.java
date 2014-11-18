package com.mercadolibre.jvillarnovo.trainingpractico1.manager;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.LruCache;

import com.mercadolibre.jvillarnovo.trainingpractico1.ItemDetailActivity;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by jvillarnovo on 12/11/14.
 */
public class ImageDownloadManager {

    private static final ImageDownloadManager INSTANCE=new ImageDownloadManager();
    private ThreadPoolExecutor thread;
    private LruCache<String, Bitmap> imageCache;


    private ImageDownloadManager(){
        thread=new ThreadPoolExecutor(2,10,20, TimeUnit.SECONDS,new LinkedBlockingDeque<Runnable>());
        int cacheSizeBytes= 16 * 1024 * 1024;
        imageCache=new LruCache<String, Bitmap>(cacheSizeBytes);
    }

    public static ImageDownloadManager getInstance(){
        return INSTANCE;
    }

    public void getImage(final String imgUrl, final int itemIndex, final Handler mainThread) {
        thread.execute(new Runnable() {
            @Override
            public void run() {
                try{
                    Bitmap img=imageCache.get(imgUrl);
                    if(img==null){
                        byte[] content = EntityUtils.toByteArray(requestData(imgUrl));
                        img = BitmapFactory.decodeByteArray(content, 0, content.length);
                        imageCache.put(imgUrl, img);
                    }
                    Message message = new Message();
                    message.arg1 = itemIndex;
                    message.obj = img;
                    message.what= ItemDetailActivity.ImageHandler.BITMAP;
                    mainThread.sendMessage(message);
                } catch (IOException e){
                    Log.e("ImageDownloadManager", e.toString());
                }
            }
        });
    }

    public void getUrlImage(final String itemId, final ItemDetailActivity.ImageHandler notifier){
        thread.execute(new Runnable() {
            @Override
            public void run() {
                try{
                    JSONObject result=new JSONObject(EntityUtils.toString(requestData("https://api.mercadolibre.com/items/" + itemId), HTTP.UTF_8));
                    JSONArray images= result.getJSONArray("pictures");
                    String urlImage= images.getJSONObject(0).getString("url");
                    Message message = new Message();
                    message.obj=urlImage;
                    message.what= ItemDetailActivity.ImageHandler.URL;
                    notifier.sendMessage(message);
                } catch (IOException e){
                    Log.e("ImageDownloadManager", e.toString());
                } catch (JSONException e) {
                    Log.e("ImageDownloadManager", e.toString());
                }
            }
        });
    }

    private HttpEntity requestData(String url) throws IOException {
        HttpClient httpclient = new DefaultHttpClient();
        HttpRequestBase request = new HttpGet(url);
        HttpResponse response = httpclient.execute(request);
        HttpEntity entity = response.getEntity();
        if (entity == null) {
            return null;
        }
        return entity;
    }
}
