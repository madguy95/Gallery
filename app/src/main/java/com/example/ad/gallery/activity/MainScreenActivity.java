package com.example.ad.gallery.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.example.ad.gallery.DAO.ImageDAO;
import com.example.ad.gallery.R;
import com.example.ad.gallery.adapter.AlbumAdapter;
import com.example.ad.gallery.model.Album;
import com.example.ad.gallery.model.ImageItem;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

public class MainScreenActivity extends AppCompatActivity {

    public final static String ALBUM_NAME = "AlbumName";
    public final static String KIND_ALBUM = "KindName";
    int CAMERA_PIC_REQUEST = 1001;
    ArrayList<Album> arr = new ArrayList<>();
    String m_Text;
    FloatingActionButton fab;
    FloatingActionButton fab2;
    private GridView gridView;
    static AlbumAdapter gridAdapter;
    ImageDAO imgDAO;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainscreen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setLogo(R.drawable.ic_action_icon);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        imgDAO = new ImageDAO(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                inputAlbum();
            }
        });
        fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        fab2.setEnabled(false);
        SpinnerAdapter spinnerAdapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.category, R.layout.spinner_dropdown_item);
        Spinner navigationSpinner = new Spinner(getSupportActionBar().getThemedContext());
        navigationSpinner.setAdapter(spinnerAdapter);
        toolbar.addView(navigationSpinner, 0);

        navigationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        getAllAlbum();
                        break;
                    case 1:
                        Intent intent = new Intent(getApplicationContext(), ListPhotoActivity.class);
                        intent.putExtra(MainScreenActivity.ALBUM_NAME, "VIDEO");
                        startActivity(intent);
                        break;
//                    case 2:
//                        setDataToGridView(arr);
//                        break;
                    case 2:
                        getAlbumTime();
                        break;
//                    case 4:
//                        break;
                    default:
                        break;
                }
                Toast.makeText(MainScreenActivity.this,
                        "you selected: " + position,
                        Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        gridView = (GridView) findViewById(R.id.gridViewAlbum);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), "" + position, Toast.LENGTH_SHORT).show();
            }
        });
        Toast.makeText(getApplicationContext(),"onCreate",Toast.LENGTH_SHORT).show();
    }

    void getAllAlbum(){
        imgDAO.getAllImages(this);
        arr.clear();
        for (Map.Entry alb : imgDAO.albumMap.entrySet()) {
            String key = alb.getKey().toString();
            ArrayList<ImageItem> value = (ArrayList<ImageItem>) alb.getValue();
            if (!value.isEmpty()) {
                Collections.sort(value, new Comparator<ImageItem>() {
                    @Override
                    public int compare(ImageItem t1, ImageItem t2) {
                        try {
                            return t2.getDate().compareTo(t1.getDate());
                        } catch (Exception ex) {
                            Log.e("DAO", "Compare date fail", ex);
                            return 0;
                        }
                    }
                });
                Album album = new Album(key, value.get(0).getPath());
                arr.add(album);
            }
        }
        setDataToGridView(arr);
    }
    void getAlbumTime() {
        imgDAO.getAlbumByTime(this);
        arr.clear();
        for (Map.Entry alb : imgDAO.albumMap.entrySet()) {
            String key = alb.getKey().toString();
            ArrayList<ImageItem> value = (ArrayList<ImageItem>) alb.getValue();
            if (!value.isEmpty()) {
                Album album = new Album(key, value.get(0).getPath());
                arr.add(album);
            }
        }
        setDataToGridView(arr);
    }

    void setDataToGridView(ArrayList<Album> arrAlbum) {
        gridAdapter = new AlbumAdapter(this, R.layout.albumitem_layout, arrAlbum);
        gridAdapter.notifyDataSetChanged();
        gridView.setAdapter(gridAdapter);
    }

    @Override
    protected void onResume() {
        getAllAlbum();
        Toast.makeText(getApplicationContext(),"onResume",Toast.LENGTH_SHORT).show();
        super.onResume();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Toast.makeText(getApplicationContext(),"onRestart",Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPause() {
        arr.clear();
        gridAdapter.clear();
        gridAdapter.notifyDataSetChanged();
        super.onPause();
        Toast.makeText(getApplicationContext(),"onPause",Toast.LENGTH_SHORT).show();
    }

    void inputAlbum() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add new album");

// Set up the input
        final EditText input = new EditText(this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

// Set up the buttons
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                m_Text = input.getText().toString();
                Album album = new Album(m_Text, null);
                arr.add(album);
                gridAdapter.notifyDataSetChanged();

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_mainscreen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == R.id.action_select){
            AlbumAdapter.checked = !AlbumAdapter.checked;
            gridAdapter.notifyDataSetChanged();
            return true;
        }
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_camera) {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            this.startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode == CAMERA_PIC_REQUEST && data != null && data.getExtras() != null) {
//            Bitmap image = (Bitmap) data.getExtras().get("data");
////            ImageView imageview = (ImageView) findViewById(R.id.ImageView01);
////            imageview.setImageBitmap(image);
        }
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
