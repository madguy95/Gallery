package com.example.ad.gallery.model;

import android.graphics.Bitmap;

import java.util.Date;

public class ImageItem {
    private Bitmap image;
    private String title;
    private Date date;
    private String path;
    private String location;
    private boolean isFavorite;

    public ImageItem() {

    }

    public ImageItem(Bitmap image, String title) {
        super();
        this.image = image;
        this.title = title;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public boolean isSameGroup(ImageItem nItem, int sortType) {
        switch (sortType) {
            case 1: // Sort by title
                return this.title.equals(nItem.getTitle());
            case 2: // Sort by location
                return this.location.equals(nItem.getLocation());
            default: //Sort by time
                Date nDate = nItem.getDate();
                return (date.getYear() == nDate.getYear()) && (date.getMonth() == nDate.getMonth());
        }
    }

}
