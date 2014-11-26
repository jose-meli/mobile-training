package com.mercadolibre.jvillarnovo.trainingpractico1;

import android.app.Fragment;
import android.content.ContentValues;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.mercadolibre.jvillarnovo.trainingpractico1.entities.Item;
import com.mercadolibre.jvillarnovo.trainingpractico1.manager.ImageDownloadManager;
import com.mercadolibre.jvillarnovo.trainingpractico1.storage.DataBaseContract;
import com.mercadolibre.jvillarnovo.trainingpractico1.tracker.TrackerService;

import java.text.NumberFormat;

/**
 * Created by jvillarnovo on 14/11/14.
 */
public class ItemDetailActivity extends Fragment {

    public static final String ITEM = "item";
    private Item item;
    private TextView viewTitle;
    private TextView viewCondition;
    private TextView viewStock;
    private TextView viewPrice;
    private ImageView imageView;
    private ToggleButton toggleButton;
    private TrackerService trackerService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_item_detail, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initComponents(view, savedInstanceState);
    }

    private void initComponents(View view, Bundle savedInstanceState) {
        viewTitle = (TextView) view.findViewById(R.id.txtTitle);
        viewCondition = (TextView) view.findViewById(R.id.txtCondition);
        viewStock = (TextView) view.findViewById(R.id.txtStock);
        viewPrice = (TextView) view.findViewById(R.id.txtPrice);
        imageView = (ImageView) view.findViewById(R.id.imageView);
        imageView.setImageDrawable(getResources().getDrawable(R.drawable.icon_loading));
        toggleButton = (ToggleButton) view.findViewById(R.id.toggleButton);
        toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (toggleButton.isChecked()) {
                    addItemTracker();
                } else {
                    removeItemTracker();
                }

            }
        });
        if (savedInstanceState == null) {
            item = (Item) getArguments().getSerializable(ITEM);
        } else {
            item = (Item) savedInstanceState.getSerializable(ITEM);
        }
        trackerService = new TrackerService();
        showItemOnView();
    }

    private void requestItemDetailData() {
        ImageHandler handler = new ImageHandler();
        if (item.getImageUrl() == null) {
            ImageDownloadManager.getInstance().getUrlImage(item.getId(), handler);
        } else {
            if (item.getImage() == null) {
                ImageDownloadManager.getInstance().getImage(item.getImageUrl(), 0, handler);
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
        item.setTracking(trackerService.isItemTracked(getActivity().getApplicationContext(), item));
        toggleButton.setChecked(item.isTracking());
    }

    private void addItemTracker() {
        Log.d("ItemDetailFragment", "addItemTracker");
        ContentValues values = new ContentValues();
        values.put(DataBaseContract.TrackerColumns.ID, item.getId());
        values.put(DataBaseContract.TrackerColumns.PRICE, item.getPrice());

        getActivity().getContentResolver().insert(DataBaseContract.CONTENT_URI, values);
        item.setTracking(true);
    }

    private void removeItemTracker() {
        Log.d("ItemDetailFragment", "removeItemTracker");
        getActivity().getContentResolver().delete(DataBaseContract.CONTENT_URI,
                DataBaseContract.TrackerColumns.ID + "= ?", new String[]{item.getId()});
        item.setTracking(false);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(ITEM, item);
    }

    public class ImageHandler extends Handler {
        public static final int URL = 222;
        public static final int BITMAP = 333;

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case URL:
                    item.setImageUrl((String) msg.obj);
                    ImageDownloadManager.getInstance().getImage(item.getImageUrl(), 0, this);
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
