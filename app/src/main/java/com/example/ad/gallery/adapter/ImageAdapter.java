package com.example.ad.gallery.adapter;

import android.content.Context;
import android.content.Intent;
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
        TextView imageTitle = (TextView) row.findViewById(R.id.text);
        ImageView image = (ImageView) row.findViewById(R.id.image);
        imageTitle.setText(item.getTitle());
        imageTitle.setText("");
        image.setImageBitmap(item.getImage());
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
}