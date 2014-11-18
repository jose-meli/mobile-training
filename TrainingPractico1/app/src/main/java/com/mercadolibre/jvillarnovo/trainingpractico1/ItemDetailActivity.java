package com.mercadolibre.jvillarnovo.trainingpractico1;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;
import android.widget.TextView;

import com.mercadolibre.jvillarnovo.trainingpractico1.entities.Item;
import com.mercadolibre.jvillarnovo.trainingpractico1.manager.ImageDownloadManager;

import java.text.NumberFormat;

/**
 * Created by jvillarnovo on 14/11/14.
 */
public class ItemDetailActivity extends Activity {

    public static final String ITEM = "item";
    private Item item;
    private TextView viewTitle;
    private TextView viewCondition;
    private TextView viewStock;
    private TextView viewPrice;
    private ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);
        initComponents(savedInstanceState);
    }

    private void initComponents(Bundle savedInstanceState) {
        viewTitle = (TextView) findViewById(R.id.txtTitle);
        viewCondition = (TextView) findViewById(R.id.txtCondition);
        viewStock = (TextView) findViewById(R.id.txtStock);
        viewPrice = (TextView) findViewById(R.id.txtPrice);
        imageView= (ImageView) findViewById(R.id.imageView);
        imageView.setImageDrawable(getResources().getDrawable(R.drawable.icon_loading));
        if (savedInstanceState == null) {
            item = (Item) getIntent().getSerializableExtra(ITEM);
        } else {
            item = (Item) savedInstanceState.getSerializable(ITEM);
        }
        showItemOnView();
    }



    private void requestItemDetailData() {
        ImageHandler handler=new ImageHandler();
        if(item.getImageUrl()==null) {
            ImageDownloadManager.getInstance().getUrlImage(item.getId(), handler);
        } else {
            if(item.getImage()==null){
                ImageDownloadManager.getInstance().getImage(item.getImageUrl(),0,handler);
            } else {
                imageView.setImageBitmap(item.getImage());
            }
        }
    }

    private void showItemOnView() {
        requestItemDetailData();
        viewTitle.setText(item.getTitle());
        viewCondition.setText(item.getCondition());
        viewStock.setText(item.getStock());
        viewPrice.setText("$ " + NumberFormat.getNumberInstance().format(item.getPrice()));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(ITEM, item);
    }

    public class ImageHandler extends Handler {
        public static final int URL=222;
        public static final int BITMAP=333;

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case URL:
                    item.setImageUrl((String) msg.obj);
                    ImageDownloadManager.getInstance().getImage(item.getImageUrl(),0,this);
                    break;
                case BITMAP:
                    Bitmap image = (Bitmap) msg.obj;
                    item.setImage(image);
                    imageView.setImageBitmap(item.getImage());
                    break;
            }
        }
    }
}
