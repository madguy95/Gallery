package com.example.ad.gallery.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.example.ad.gallery.model.GroupImages;
import com.example.ad.gallery.R;

import java.util.ArrayList;

public class GroupAdapter extends ArrayAdapter<GroupImages> {

    private Context context;
    private int layoutResourceId;
    private ArrayList<GroupImages> data = new ArrayList<>();

    public GroupAdapter(Context context, int layoutResourceId, ArrayList<GroupImages> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(layoutResourceId, parent, false);
        //
        TextView grouptitle = (TextView) row.findViewById(R.id.grouptitle);
        GridView gvGroup = (GridView) row.findViewById(R.id.gvGroup);
        //
        GroupImages groupImages = data.get(position);
        grouptitle.setText(groupImages.getGroupTitle());
        ImageAdapter imageAdapter = new ImageAdapter(context, R.layout.image_layout, groupImages.getGroupData());
        gvGroup.setAdapter(imageAdapter);
        //
        return row;
    }

}