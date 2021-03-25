package com.example.findapartment.activities.apartments;

import android.annotation.SuppressLint;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;

import com.example.findapartment.R;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        Thread background = new Thread() {
            public void run() {
                try {
                    sleep(3*1000);
                    Intent i=new Intent(getBaseContext(),ApartmentListActivity.class);
                    startActivity(i);
                    finish();
                } catch (Exception e) {
                }
            }
        };
        background.start();

    }


}