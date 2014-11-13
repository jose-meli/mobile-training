package com.mercadolibre.jvillarnovo.trainingpractico1.manager;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.LruCache;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

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
    private Handler handler;


    private ImageDownloadManager(){
        thread=new ThreadPoolExecutor(2,10,20, TimeUnit.SECONDS,new LinkedBlockingDeque<Runnable>());
        int cacheSizeBytes= 16 * 1024 * 1024;
        imageCache=new LruCache<String, Bitmap>(cacheSizeBytes);
    }

    public static ImageDownloadManager getInstance(Handler handler){
        INSTANCE.handler=handler;
        return INSTANCE;
    }

    public void getImage(final String imgUrl, final int itemIndex) {
        thread.execute(new Runnable() {
            @Override
            public void run() {
                try{
                    Bitmap img=imageCache.get(imgUrl);

                    if(img==null){
                        byte[] content = requestData(imgUrl);
                        img = BitmapFactory.decodeByteArray(content, 0, content.length);
                        imageCache.put(imgUrl, img);
                        Message message = new Message();
                        message.arg1 = itemIndex;
                        message.obj = img;
                        handler.sendMessage(message);
                    }
                } catch (IOException e){
                    Log.e("ImageDownloadManager", e.toString());
                }
            }
        });
    }

    private byte[] requestData(String url) throws IOException {
        Log.d("ImageDownloadManager.requestData",url);
        HttpClient httpclient = new DefaultHttpClient();
        HttpRequestBase request = new HttpGet(url);
        HttpResponse response = httpclient.execute(request);
        HttpEntity entity = response.getEntity();
        if (entity == null) {
            return null;
        }
        return EntityUtils.toByteArray(entity);
    }
}
