package com.release.aeonaudio.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.release.aeonaudio.R;
import com.release.aeonaudio.utils.Util;

public class Blank extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blank);


        if(Util.MianActivityDestoryed)
        {

            Util.MianActivityDestoryed = false;

            Intent startIntent = new Intent(Blank.this, MainActivity.class);
            startActivity(startIntent);
            finish();
        }
        else
        {
            Util.MianActivityDestoryed = false;
            finish();
        }



    }
}
