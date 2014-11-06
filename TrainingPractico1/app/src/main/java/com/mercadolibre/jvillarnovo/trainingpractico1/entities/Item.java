package com.mercadolibre.jvillarnovo.trainingpractico1.entities;

import java.io.Serializable;

/**
 * Created by jvillarnovo on 05/11/14.
 */
public class Item implements Serializable {

    private String id;
    private String title;
    private double price;

    public Item(String id, String title, double price){
        this.setId(id);
        this.setTitle(title);
        this.setPrice(price);
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
}
