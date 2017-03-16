package com.example.ad.gallery.model;

import java.util.ArrayList;

/**
 * Created by Anh Nam on 2017-03-09.
 */

public class GroupImages {
    private ArrayList<ImageItem> groupData;
    private int typeOfSort;

    public GroupImages(int typeOfSort, ArrayList<ImageItem> groupData) {
        this.typeOfSort = typeOfSort;
        this.groupData = groupData;
    }

    public GroupImages(ArrayList<ImageItem> groupData) {
        this.groupData = groupData;
        this.typeOfSort = 0;
    }

    public ArrayList<ImageItem> getGroupData() {
        return groupData;
    }

    public void setGroupData(ArrayList<ImageItem> groupData) {
        this.groupData = groupData;
    }

    public int getTypeOfSort() {
        return typeOfSort;
    }

    public void setTypeOfSort(int typeOfSort) {
        this.typeOfSort = typeOfSort;
    }

    public String getGroupTitle() {
        ImageItem image = groupData.get(0);
        switch (typeOfSort) {
            case 1: // Title
                return image.getTitle();
            case 2: // Location
                return image.getLocation();
            case 3: // Favorite
                return "";
            default:
                return String.format("yyyy,MM ", image.getDate());
        }
    }
}
