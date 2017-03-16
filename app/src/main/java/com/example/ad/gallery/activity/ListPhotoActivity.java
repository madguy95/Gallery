package com.example.ad.gallery.activity;

import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.GridView;
import android.widget.ListView;

import com.example.ad.gallery.model.GroupImages;
import com.example.ad.gallery.model.ImageItem;
import com.example.ad.gallery.R;
import com.example.ad.gallery.adapter.GroupAdapter;

import java.util.ArrayList;

public class ListPhotoActivity extends AppCompatActivity {
    GridView gridView;
    ListView myList;
    ArrayList<ImageItem> data;
    ArrayList<GroupImages> groupdata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_photo);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        data = getData();
        DispayGroup(data, 2);
    }

    /**
     * Prepare some dummy data for gridview
     */
    private ArrayList<ImageItem> getData() {
        final ArrayList<ImageItem> imageItems = new ArrayList<>();
        TypedArray imgs = getResources().obtainTypedArray(R.array.image_ids);
        int j = 0;
        int group_count = 0;
        for (int i = 0; i < imgs.length(); i++) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), imgs.getResourceId(i, -1));
            ImageItem item = new ImageItem(bitmap, "Image#" + i);
            //
            j++;
            if (j == 5) {
                j = 0;
                group_count++;
            }
            item.setLocation("Location " + group_count);
            //
            imageItems.add(item);
        }
        return imageItems;
    }

    private void DispayGroup(ArrayList<ImageItem> data, int sortType) {
        myList = (ListView) findViewById(R.id.listView);
        groupdata = new ArrayList<>();
        //
        ArrayList<ImageItem> group = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            ImageItem item = data.get(i);
//            if (i < data.size() - 1 && !item.getLocation().equals(data.get(i+1).getLocation())) {
//                groupdata.add(new GroupImages(group));
//                group = new ArrayList<>();
//            }
            if (sortType == 3) {
                if (item.isFavorite()) {
                    group.add(item);
                } else {
                    break;
                }
            } else {
                if (group.size() > 0) {
                    if (!item.isSameGroup(data.get(i - 1), sortType)) {
                        groupdata.add(new GroupImages(sortType, group));
                        group = new ArrayList<>();
                    }
                    group.add(item);
                } else {
                    group.add(item);
                }
            }

        }
        groupdata.add(new GroupImages(sortType, group));
        Log.e("TAG", groupdata.toString());
        //

        GroupAdapter groupAdapter = new GroupAdapter(this, R.layout.group_image_layout, groupdata);
        myList.setAdapter(groupAdapter);
    }
}
