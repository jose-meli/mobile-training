package com.mercadolibre.jvillarnovo.trainingpractico1.entities;

import android.database.Cursor;
import android.graphics.Bitmap;

import com.mercadolibre.jvillarnovo.trainingpractico1.storage.DataBaseContract;
import com.mercadolibre.jvillarnovo.trainingpractico1.util.Util;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by jvillarnovo on 05/11/14.
 */
public class Item implements Serializable {

    private String id;
    private String title;
    private double price;
    private String stock;
    private String subtitle;
    private String thumbnailUrl;
    private transient Bitmap thumbnail;
    private transient boolean downloadImg;
    private String condition;
    private String imageUrl;
    private transient Bitmap image;
    private boolean tracking;
    private transient Date date;

    public Item(String id, String title, double price, String stock, String subtitle, String thumbnailUrl, String condition) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.stock = stock;
        this.subtitle = subtitle;
        this.thumbnailUrl = thumbnailUrl;
        this.condition = condition;
        this.tracking = false;
    }

    public static final Item convertCursorToItem(Cursor cursor) {
        Item item = new Item("", "", 0, "", "", "", "");
        item.setId(cursor.getString(cursor.getColumnIndex(DataBaseContract.TrackerColumns.ID)));
        item.setPrice(cursor.getDouble(cursor.getColumnIndex(DataBaseContract.TrackerColumns.PRICE)));

        return item;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public Bitmap getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Bitmap img) {
        this.thumbnail = img;
    }

    public boolean isDownloadImg() {
        return downloadImg;
    }

    public void setDownloadImg(boolean downloadImg) {
        this.downloadImg = downloadImg;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public boolean isTracking() {
        return tracking;
    }

    public void setTracking(boolean tracking) {
        this.tracking = tracking;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = Util.parseDate(date);
    }

    @Override
    public String toString() {
        return id;
    }
}
