package com.example.ad.gallery.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ad.gallery.activity.MainScreenActivity;
import com.example.ad.gallery.model.Album;
import com.example.ad.gallery.R;
import com.example.ad.gallery.activity.ListPhotoActivity;

import java.util.ArrayList;

import static com.example.ad.gallery.R.*;

/**
 * Created by AD on 08/03/2017.
 */

public class AlbumAdapter extends ArrayAdapter {
    private Context context;
    private int layoutResourceId;
    private ArrayList<Album> arrayList = new ArrayList<>();
    static int defaultImage = drawable.ic_action_icon;
    public static boolean checked = false;
    public AlbumAdapter(Context context, int resource, ArrayList<Album> objects) {
        super(context, resource, objects);
        this.context = context;
        this.arrayList = objects;
        this.layoutResourceId = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View row = inflater.inflate(layoutResourceId, parent, false);
        TextView imageTitle = (TextView) row.findViewById(id.txtAlbum);
        ImageView image = (ImageView) row.findViewById(id.albumView);
        if(checked){
            CheckBox cb = (CheckBox) row.findViewById(id.cbAlbum);
            cb.setVisibility(View.VISIBLE);
        }else {
            CheckBox cb = (CheckBox) row.findViewById(id.cbAlbum);
            cb.setVisibility(View.INVISIBLE);
        }
        final Album item = arrayList.get(position);
        imageTitle.setText(item.getName());
        if(item.getTitle()!= null) {
            image.setImageBitmap(resizeBitmap(item.getTitle(), image.getWidth(), image.getWidth()));

        }else{
            image.setImageBitmap(decodeSampledBitmapFromResource(context.getResources(),defaultImage,image.getWidth(),200));
        }
        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ListPhotoActivity.class);
                intent.putExtra(MainScreenActivity.ALBUM_NAME, item.getName());
                intent.putExtra(MainScreenActivity.KIND_ALBUM, item.getName());
                context.startActivity(intent);
            }
        });
        return row;
    }
    public Bitmap resizeBitmap(String photoPath, int targetW, int targetH) {
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(photoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        int scaleFactor = 1;
        if ((targetW > 0) || (targetH > 0)) {
            scaleFactor = Math.min(photoW/targetW, photoH/targetH);
        }

        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true; //Deprecated API 21

        return BitmapFactory.decodeFile(photoPath, bmOptions);
    }
    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
}
