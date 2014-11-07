package com.mercadolibre.jvillarnovo.trainingpractico1.entities;

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

    public Item(String id, String title, double price, String stock, String subtitle) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.stock = stock;
        this.subtitle = subtitle;
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
}
