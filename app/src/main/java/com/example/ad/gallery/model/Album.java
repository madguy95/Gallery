package com.example.ad.gallery.model;

import android.graphics.Bitmap;

/**
 * Created by AD on 08/03/2017.
 */

public class Album {

    private String name;
    private Bitmap image;

    public Album() {
    }

    public Album(String name, Bitmap image) {
        this.name = name;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}
