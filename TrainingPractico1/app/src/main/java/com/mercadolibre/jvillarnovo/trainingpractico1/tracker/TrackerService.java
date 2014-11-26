package com.mercadolibre.jvillarnovo.trainingpractico1.tracker;

import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;

import com.mercadolibre.jvillarnovo.trainingpractico1.ItemDetailActivity;
import com.mercadolibre.jvillarnovo.trainingpractico1.R;
import com.mercadolibre.jvillarnovo.trainingpractico1.ResultActivity;
import com.mercadolibre.jvillarnovo.trainingpractico1.entities.Item;
import com.mercadolibre.jvillarnovo.trainingpractico1.storage.DataBaseContract;
import com.mercadolibre.jvillarnovo.trainingpractico1.task.AsyncSearch;

import java.util.concurrent.TimeUnit;

public class TrackerService extends IntentService {

    private static final long TIME_TO_EXPIRE = TimeUnit.MILLISECONDS.convert(30, TimeUnit.MINUTES);
    private TrackerProvider providerDb = new TrackerProvider();

    public TrackerService() {
        super(TrackerService.class.getName());
    }

    private void checkItems() {
        Cursor cursor = getContentResolver().query(DataBaseContract.CONTENT_URI, null, null, null, null);
        String msj = (cursor == null ? "cursor null" : "" + cursor.getCount());
        Log.d("TrackerService", msj);
        if (cursor.moveToFirst()) {
            do {
                Item itemLocal = Item.convertCursorToItem(cursor);
                Item itemServer = getItemFromServer(itemLocal.getId());
                if (itemLocal.getPrice() != itemServer.getPrice()) {
                    updatePrice(itemServer);
                    sendNotification(getString(R.string.notification_ticket_price), getString(R.string.notification_title_price), getString(R.string.notification_content_price) + itemServer.getPrice(), itemServer);
                }
                if (isDateCloseToToday(itemServer)) {
                    sendNotification(getString(R.string.notification_ticket_date), getString(R.string.notification_title_date), getString(R.string.notification_content_date), itemServer);
                }
            } while (cursor.moveToNext());
        }
    }

    private boolean isDateCloseToToday(Item item) {
        long timeLocal = System.currentTimeMillis();
        long timeItemServer = item.getDate().getTime() - TIME_TO_EXPIRE;
        if (timeItemServer <= timeLocal) {
            return true;
        }
        return false;
    }

    private Item getItemFromServer(String id) {
        return AsyncSearch.getItem(id);
    }

    private void sendNotification(String ticket, String title, String contentMessage, Item item) {
        Intent intent = new Intent(getApplicationContext(), ResultActivity.class);
        intent.putExtra(ItemDetailActivity.ITEM, item);
        PendingIntent pIntent = PendingIntent.getActivity(getApplicationContext(), 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification builder = new Notification.Builder(getApplicationContext())
                .setTicker(ticket)
                .setContentTitle(title)
                .setContentText(contentMessage)
                .setSmallIcon(R.drawable.ic_launcher)
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentIntent(pIntent).build();
    }

    private void updatePrice(Item item) {
        ContentValues values = new ContentValues();
        values.put(DataBaseContract.TrackerColumns.PRICE, item.getPrice());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        checkItems();
    }

    public boolean isItemTracked(Context context, Item item) {
        Cursor cursor = context.getContentResolver().query(DataBaseContract.CONTENT_URI,
                null, DataBaseContract.TrackerColumns.ID + "=?",
                new String[]{item.getId()}, null);

        Item auxItem;
        if (cursor.moveToFirst()) {
            auxItem = Item.convertCursorToItem(cursor);
            Log.d("TrackerService.isItemTracked", item.getId() + " = " + auxItem.getId());
            return true;
        }
        return false;
    }
}