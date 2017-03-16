package com.example.ad.gallery.DAO;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.example.ad.gallery.model.ImageItem;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by AD on 15/03/2017.
 */

public class ImageDAO {

    String extention = ".jpg";
    ArrayList<ImageItem> allImages = new ArrayList<>();

    public ImageDAO() {
        HashMap<String, ArrayList<ImageItem>> hm = new HashMap<>();

        // get image from sdcard
        File f = android.os.Environment.getExternalStorageDirectory();
        File[] files = f.listFiles();
        ArrayList<ImageItem> arrRoot = new ArrayList<>();
        for (File inFile : files) {
            if (inFile.isDirectory()) {
                // is directory
                ArrayList<ImageItem> arr = new ArrayList<>();
                load_image_files(inFile, arr);
                if (!arr.isEmpty()) {
                    String name = inFile.getName();
                    hm.put(name, arr);
                }
            } else {
                if (inFile.getName().endsWith(extention)) {
                    ImageItem imageItem = new ImageItem();
                    imageItem.setTitle(inFile.getName());
                    imageItem.setDate(new Date(inFile.lastModified()));
                    imageItem.setPath(inFile.getAbsolutePath());
                    arrRoot.add(imageItem);
                }
            }
        }
        if (!arrRoot.isEmpty()) {
            hm.put("Root", arrRoot);
        }

    }

    private void load_image_files(File dir, ArrayList<ImageItem> hm) {


        File[] listFile = dir.listFiles();
        if (listFile != null) {
            for (int i = 0; i < listFile.length; i++) {

                if (listFile[i].isDirectory()) {
                    load_image_files(listFile[i], hm);
                } else {
                    if (listFile[i].getName().endsWith(extention)) {
                        ImageItem imageItem = new ImageItem();
                        imageItem.setTitle(listFile[i].getName());
                        imageItem.setDate(new Date(listFile[i].lastModified()));
                        imageItem.setPath(listFile[i].getAbsolutePath());
                        hm.add(imageItem);
                    }
                }
            }
        }
    }

    public ArrayList<ImageItem> gettAllImages(Activity activity) {

        //Remove older images to avoid copying same image twice
        Uri uri;
        Cursor cursor;
        int column_index_data, column_index_folder_name;

        String absolutePathOfImage = null, imageName;

        //get all images from external storage

        uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String[] projection = {MediaStore.MediaColumns.DATA,
                MediaStore.Images.Media.DISPLAY_NAME};

        cursor = activity.getContentResolver().query(uri, projection, null, null, null);

        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);

        column_index_folder_name = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME);

        while (cursor.moveToNext()) {

            absolutePathOfImage = cursor.getString(column_index_data);

            imageName = cursor.getString(column_index_folder_name);

            ImageItem imageItem = new ImageItem();
            imageItem.setTitle(imageName);
            imageItem.setPath(absolutePathOfImage);
            allImages.add(imageItem);

        }

        // Get all Internal storage images

        uri = android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI;

        cursor = activity.getContentResolver().query(uri, projection, null,
                null, null);

        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);

        column_index_folder_name = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME);

        while (cursor.moveToNext()) {

            absolutePathOfImage = cursor.getString(column_index_data);

            imageName = cursor.getString(column_index_folder_name);

            ImageItem imageItem = new ImageItem();
            imageItem.setTitle(imageName);
            imageItem.setPath(absolutePathOfImage);
            allImages.add(imageItem);
        }

        return allImages;
    }

}
