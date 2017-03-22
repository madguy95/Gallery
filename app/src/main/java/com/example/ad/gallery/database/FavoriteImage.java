package com.example.ad.gallery.database;

/**
 * Created by truongbonba on 3/21/2017.
 */

public class FavoriteImage {
    private String path;
    private boolean isLiked;

    public FavoriteImage() {
    }

    public FavoriteImage(String path, boolean isLiked) {
        this.path = path;
        this.isLiked = isLiked;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }

    @Override
    public String toString() {
        return path + " | " + (isLiked ? "true" : "false");
    }
}
