package com.kuroi.contract.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.kuroi.contract.R;

import java.io.File;

public class ShowActivity extends ActionBarActivity {
    private static final String ACTIVITY_TAG="LogDemo";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
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
            pic.setImageBitmap(bm);}
    }


}
