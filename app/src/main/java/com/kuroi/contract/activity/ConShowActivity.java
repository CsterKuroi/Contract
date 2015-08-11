package com.kuroi.contract.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;

import com.kuroi.contract.R;

import java.io.File;
import java.io.IOException;

public class ConShowActivity extends Activity {
    private static final String ACTIVITY_TAG="LogDemo";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_con);
        ActionBar actionBar=getActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
//        actionBar.setTitle("      照片");

        Log.d(ACTIVITY_TAG,"show");
        Intent intent = getIntent();
        String picName= intent.getStringExtra("picName");
        Log.d(ACTIVITY_TAG,picName);
        if(new File(picName).isFile()){
                BitmapFactory.Options option = new BitmapFactory.Options();
            option.inSampleSize = 4;
            Bitmap bm = BitmapFactory.decodeFile(picName,option);
            ImageView pic = (ImageView) findViewById(R.id.show_pic);
            Log.d(ACTIVITY_TAG, "ok");
            pic.setImageBitmap(bm);
        }
    }

    public static int getExifOrientation(String filepath) {
        int degree = 0;
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(filepath);
        } catch (IOException ex) {
            // MmsLog.e(ISMS_TAG, "getExifOrientation():", ex);
        }
        if (exif != null) {
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);
            if (orientation != -1) {
                // We only recognize a subset of orientation tag values.
                switch (orientation) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        degree = 90;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        degree = 180;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        degree = 270;
                        break;
                    default:
                        break;
                }
            }
        }
        return degree;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home)  // 返回
        {
            finish();
        }
        return true;
    }
}
