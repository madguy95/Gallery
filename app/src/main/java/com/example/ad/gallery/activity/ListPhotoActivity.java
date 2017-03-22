package com.example.ad.gallery.activity;

import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.ad.gallery.DAO.ImageDAO;
import com.example.ad.gallery.adapter.AlbumAdapter;
import com.example.ad.gallery.adapter.ImageAdapter;
import com.example.ad.gallery.model.GroupImages;
import com.example.ad.gallery.model.ImageItem;
import com.example.ad.gallery.R;
import com.example.ad.gallery.adapter.GroupAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class ListPhotoActivity extends AppCompatActivity {
    private final String className = "ListPoto";
    GridView gridView;
    ArrayList<ImageItem> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_photo);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // get List Images
        data = getData();
        //
        DispayGroup();
    }

    /**
     * Prepare some dummy data for gridview
     */
    private ArrayList<ImageItem> getData() {
        String albumName = getIntent().getStringExtra(MainScreenActivity.ALBUM_NAME);
        ImageDAO imageDAO = new ImageDAO(this);
        imageDAO.getAllImages(this);
        ArrayList<ImageItem> value = (ArrayList<ImageItem>) imageDAO.albumMap.get(albumName);
        Log.i(className, "Load data succesfull");
        return value;
    }

    private void DispayGroup() {

        // Sort by time :
        Collections.sort(data, new Comparator<ImageItem>() {
            @Override
            public int compare(ImageItem imageItem, ImageItem t1) {
                try {
                    Date imageDate = imageItem.getDate();
                    Date tDate = imageItem.getDate();
                    return imageDate.compareTo(tDate);
                } catch (Exception e) {
                    Log.e(className, "Compare exception :", e);
                }
                return 0;
            }
        });
        // Display to GridView
        GridView gridView = (GridView) findViewById(R.id.gridview);
        //
        ImageAdapter gridAdapter = new ImageAdapter(this, R.layout.image_layout, data);
        gridView.setAdapter(gridAdapter);
        // Set onclick Listener
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), "" + position, Toast.LENGTH_SHORT).show();
                Log.e(className, "Click Image : " + position);
            }
        });
        //
        Log.i(className, "Display data succesfull");
    }
}
