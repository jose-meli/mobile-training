package com.mercadolibre.jvillarnovo.trainingpractico1.handler;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;

import com.mercadolibre.jvillarnovo.trainingpractico1.Adapters.ListViewAdapter;

/**
 * Created by jvillarnovo on 13/11/14.
 */
public class MainThreadHandler extends Handler {

    private static final MainThreadHandler INSTANCE=new MainThreadHandler();
    private ListViewAdapter adapter;

    private MainThreadHandler(){}

    public static MainThreadHandler getInstance(ListViewAdapter adapter){
        INSTANCE.adapter=adapter;
        return INSTANCE;
    }

    @Override
    public void handleMessage(Message msg) {
        Bitmap image = (Bitmap) msg.obj;
        int itemIndex = msg.arg1;
        adapter.updateImgItem(itemIndex,image);
    }


}
