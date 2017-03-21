package com.example.ad.gallery.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.media.ThumbnailUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ad.gallery.model.ImageItem;
import com.example.ad.gallery.R;
import com.example.ad.gallery.activity.ViewPhotoActivity;

import java.util.ArrayList;

public class ImageAdapter extends ArrayAdapter<ImageItem> {

    private Context context;
    private int layoutResourceId;
    private ArrayList<ImageItem> data = new ArrayList<ImageItem>();

    public ImageAdapter(Context context, int layoutResourceId, ArrayList<ImageItem> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(layoutResourceId, parent, false);
        //
        ImageItem item = data.get(position);
        //
        //TextView imageTitle = (TextView) row.findViewById(R.id.text);
        ImageView image = (ImageView) row.findViewById(R.id.imageView);
        //
        Log.i("ImageAdapter", "Item : " +position + ":\n\t"+ item.getPath());
        String extention = ".jpg";
        if (item.getPath().endsWith(extention)) {
            image.setImageBitmap(resizeBitmap(item.getPath(),image.getWidth(),image.getHeight()));
        } else {
            // Set Image of Video :
            try {

                Bitmap thumb = ThumbnailUtils.createVideoThumbnail(item.getPath(), MediaStore.Images.Thumbnails.MINI_KIND);
                thumb = ThumbnailUtils.extractThumbnail(thumb, image.getWidth(),image.getHeight(), ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
                image.setImageBitmap(thumb);
            } catch (Exception ex) {
                Log.e("ImageAdapter", "", ex);
            }
        }


        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ViewPhotoActivity.class);
                context.startActivity(intent);
            }
        });
        //
        return row;
    }
    public Bitmap resizeBitmap(String photoPath, int targetW, int targetH) {
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(photoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        int scaleFactor = 2;
        if ((targetW > 0) || (targetH > 0)) {
            scaleFactor = Math.min(photoW/targetW, photoH/targetH);
        }

        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true; //Deprecated API 21

        return BitmapFactory.decodeFile(photoPath, bmOptions);
    }
}