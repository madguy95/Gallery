package com.example.ad.gallery.DAO;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.example.ad.gallery.model.ImageItem;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.FormatFlagsConversionMismatchException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by AD on 15/03/2017.
 */

public class ImageDAO {

    String extention = ".jpg";
    Context context;
    public HashMap<String, ArrayList<ImageItem>> albumMap = new HashMap<>();
    public HashMap<String, String> mapLocation = new HashMap<>();

    public ImageDAO(Context context) {

        //get image from sdcard

        this.context = context;

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

    public ArrayList<ImageItem> getAllImages(Activity activity) {

        //Remove older images to avoid copying same image twice
        Uri uri;
        Cursor cursor;
        int column_index_data, column_index_folder_name;

        String absolutePathOfImage = null, imageName;

        String albumName = "Camera";
        String[] projection = {MediaStore.MediaColumns.DATA,
                MediaStore.Images.Media.DISPLAY_NAME};

        // Get all Internal storage images
        ArrayList<ImageItem> allImages = new ArrayList<>();

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
            imageItem.setDate(new Date(new File(absolutePathOfImage).lastModified()));
            allImages.add(imageItem);
        }

        albumMap.put(albumName, allImages);

        // get all image from external storage
        File f = android.os.Environment.getExternalStorageDirectory();
        File[] files = f.listFiles();
        ArrayList<ImageItem> arrRoot = new ArrayList<>();
        if (files != null)
            for (File inFile : files) {
                if (inFile.isDirectory()) {
                    // is directory
                    ArrayList<ImageItem> arr = new ArrayList<>();
                    load_image_files(inFile, arr);
                    if (!arr.isEmpty()) {
                        String name = inFile.getName();
                        allImages.addAll(arr);
                        albumMap.put(name, arr);
                    }
                } else {
                    if (inFile.getName().endsWith(extention)) {
                        ImageItem imageItem = new ImageItem();
                        imageItem.setTitle(inFile.getName());
                        imageItem.setDate(new Date(inFile.lastModified()));
                        imageItem.setPath(inFile.getAbsolutePath());
                        arrRoot.add(imageItem);
                        allImages.add(imageItem);
                    }
                }
            }
        if (!arrRoot.isEmpty()) {
            albumMap.put("Root", arrRoot);
        }

//        //get all images from external storage
//
//        uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
//        cursor = activity.getContentResolver().query(uri, projection, null, null, null);
//
//        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
//
//        column_index_folder_name = cursor
//                .getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME);
//
//        while (cursor.moveToNext()) {
//
//            absolutePathOfImage = cursor.getString(column_index_data);
//
//            imageName = cursor.getString(column_index_folder_name);
//
//            ImageItem imageItem = new ImageItem();
//            imageItem.setTitle(imageName);
//            imageItem.setPath(absolutePathOfImage);
//            String albumNameEX = absolutePathOfImage.replace(android.os.Environment.getExternalStorageDirectory().getAbsolutePath(),"");
//            albumNameEX = albumNameEX.substring(0,albumNameEX.indexOf("/"));
//            if(albumNameEX.isEmpty()){
//                albumNameEX = "Root";
//            }
//            if(albumMap.containsKey(albumNameEX)){
//                albumMap.get(albumNameEX).add(imageItem);
//            }else{
//                ArrayList<ImageItem> arr = new ArrayList<>();
//                arr.add(imageItem);
//                albumMap.put(albumNameEX,arr);
//            }
//        }
        return allImages;
    }

    void getImageLocation(String path) {
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(path);
            float[] latLong = new float[2];
            boolean hasLatLong = exif.getLatLong(latLong);

            if (hasLatLong) {
                String country = getCountryName(new Float(latLong[0]).doubleValue(), new Float(latLong[1]).doubleValue());
                System.out.println("Latitude: " + new Float(latLong[0]).doubleValue());
                System.out.println("Longitude: " + new Float(latLong[1]).doubleValue());
                if (!mapLocation.containsKey(country)) {
                    mapLocation.put(country, path);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    String ReadExif(String file) {
        String exif = "Exif: " + file;
        try {
            ExifInterface exifInterface = new ExifInterface(file);

            exif += "\nIMAGE_LENGTH: " + exifInterface.getAttribute(ExifInterface.TAG_IMAGE_LENGTH);
            exif += "\nIMAGE_WIDTH: " + exifInterface.getAttribute(ExifInterface.TAG_IMAGE_WIDTH);
            exif += "\n DATETIME: " + exifInterface.getAttribute(ExifInterface.TAG_DATETIME);
            exif += "\n TAG_MAKE: " + exifInterface.getAttribute(ExifInterface.TAG_MAKE);
            exif += "\n TAG_MODEL: " + exifInterface.getAttribute(ExifInterface.TAG_MODEL);
            exif += "\n TAG_ORIENTATION: " + exifInterface.getAttribute(ExifInterface.TAG_ORIENTATION);
            exif += "\n TAG_WHITE_BALANCE: " + exifInterface.getAttribute(ExifInterface.TAG_WHITE_BALANCE);
            exif += "\n TAG_FOCAL_LENGTH: " + exifInterface.getAttribute(ExifInterface.TAG_FOCAL_LENGTH);
            exif += "\n TAG_FLASH: " + exifInterface.getAttribute(ExifInterface.TAG_FLASH);
            exif += "\nGPS related:";
            exif += "\n TAG_GPS_DATESTAMP: " + exifInterface.getAttribute(ExifInterface.TAG_GPS_DATESTAMP);
            exif += "\n TAG_GPS_TIMESTAMP: " + exifInterface.getAttribute(ExifInterface.TAG_GPS_TIMESTAMP);
            exif += "\n TAG_GPS_LATITUDE: " + exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
            exif += "\n TAG_GPS_LATITUDE_REF: " + exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF);
            exif += "\n TAG_GPS_LONGITUDE: " + exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);
            exif += "\n TAG_GPS_LONGITUDE_REF: " + exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF);
            exif += "\n TAG_GPS_PROCESSING_METHOD: " + exifInterface.getAttribute(ExifInterface.TAG_GPS_PROCESSING_METHOD);


        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return exif;
    }

    // get location image follow latitude and longitude
    public String getCountryName(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            Address result;

            if (addresses != null && !addresses.isEmpty()) {
                return addresses.get(0).getCountryName();
            }
            return null;
        } catch (IOException ignored) {
            //do something
        }

        return null;
    }

    //
    public void getAlbumByTime(Activity activity) {
        ArrayList<ImageItem>  allImages = getAllImages(activity);
        //Sort by date time
        Collections.sort(allImages, new Comparator<ImageItem>() {
            @Override
            public int compare(ImageItem imageItem, ImageItem t1) {
                try {
                    return imageItem.getDate().compareTo(t1.getDate());
                } catch (Exception ex) {
                    Log.e("DAO", "Compare date fail", ex);
                    return 0;
                }
            }
        });
        // Group to HashMap
        albumMap = new HashMap<>();
        ArrayList<ImageItem> arrOfKey = new ArrayList<>();
        for (ImageItem item : allImages) {
            if (arrOfKey.size() == 0) {
                arrOfKey.add(item);
            } else {
                if (!item.isSameGroup(arrOfKey.get(0), 0)) {
                    SimpleDateFormat df = new SimpleDateFormat("yyyy, MMM");
                    String key = df.format(arrOfKey.get(0).getDate());
                    //
                    albumMap.put(key, arrOfKey);
                    //
                    arrOfKey = new ArrayList<>();
                }
                arrOfKey.add(item);
            }
        }
        if (arrOfKey.size() > 0) {
            SimpleDateFormat df = new SimpleDateFormat("yyyy, MMM");
            String key = df.format(arrOfKey.get(0).getDate());
            //
            albumMap.put(key, arrOfKey);
        }
    }


}
