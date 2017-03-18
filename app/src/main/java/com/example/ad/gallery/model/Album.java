package com.example.ad.gallery.model;

import android.graphics.Bitmap;

/**
 * Created by AD on 08/03/2017.
 */

public class Album {

    private String name;
    private String title;

    public Album() {
    }

    public Album(String name, String title) {
        this.name = name;
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
