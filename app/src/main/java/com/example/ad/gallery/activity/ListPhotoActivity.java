package com.example.ad.gallery.activity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.ad.gallery.DAO.ImageDAO;
import com.example.ad.gallery.R;
import com.example.ad.gallery.adapter.AlbumAdapter;
import com.example.ad.gallery.adapter.ImageAdapter;
import com.example.ad.gallery.model.ImageItem;

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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.memu_listphoto, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_selectImage){
            // code show checkbox here
            return true;
        }
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Prepare some dummy data for gridview
     */
    private ArrayList<ImageItem> getData() {
        String albumName = getIntent().getStringExtra(MainScreenActivity.ALBUM_NAME);
        ImageDAO imageDAO = new ImageDAO(this);
        if (albumName.equals("VIDEO")) {
            return imageDAO.getAllMedia();
        } else if (albumName.contains(",")) {
            imageDAO.getAlbumByTime(this);
        } else {
            imageDAO.getAllImages(this);
        }
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
        GridView gridView = (GridView) findViewById(R.id.gridViewImage);
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
