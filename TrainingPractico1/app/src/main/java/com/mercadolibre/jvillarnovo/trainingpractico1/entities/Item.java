package com.mercadolibre.jvillarnovo.trainingpractico1.entities;

import android.graphics.Bitmap;

import java.io.Serializable;

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

    public Item(String id, String title, double price, String stock, String subtitle, String thumbnailUrl, String condition) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.stock = stock;
        this.subtitle = subtitle;
        this.thumbnailUrl = thumbnailUrl;
        this.condition=condition;
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

    @Override
    public String toString(){
        return id;
    }
}
