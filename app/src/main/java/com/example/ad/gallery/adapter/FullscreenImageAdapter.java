package com.example.ad.gallery.adapter;

import java.sql.SQLOutput;
import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.ad.gallery.R;
import com.example.ad.gallery.activity.ViewPhotoActivity;
import com.example.ad.gallery.model.ImageItem;

public class FullscreenImageAdapter extends PagerAdapter {

    private Activity activity;
    private ArrayList<ImageItem> imageList;
    private LayoutInflater inflater;

    // constructor
    public FullscreenImageAdapter(Activity activity, ArrayList<ImageItem> imageItem) {
        this.activity = activity;
        this.imageList = imageItem;
    }

    @Override
    public int getCount() {
        return this.imageList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewLayout = inflater.inflate(R.layout.fragment_viewphoto, container, false);

        System.out.println("Test - instantiateItem(): " + position);
        ImageView img = (ImageView) viewLayout.findViewById(R.id.imageViewLD);
        img.setImageBitmap(BitmapFactory.decodeFile(imageList.get(position).getPath()));
//        img.setBackgroundColor(Color.BLACK);

        //setup for fullscreen image
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ViewPhotoActivity.vdecoder.getVisibility() != View.GONE) {
                    ViewPhotoActivity.FullScreencall();
                } else {
                    ViewPhotoActivity.exitFullScreen();
                }
            }
        });


        ((ViewPager) container).addView(viewLayout);

        return viewLayout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((RelativeLayout) object);

    }
}
