package com.example.findapartment.activities.apartments;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.findapartment.R;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }

    public void navigateToAddUser(View view) {
        Intent i=new Intent(getBaseContext(),AddUserActivity.class);
        startActivity(i);
    }
    public void navigateToAllApartments(View view) {
        Intent i=new Intent(getBaseContext(), ApartmentListActivity.class);
        startActivity(i);
    }
    public void navigateToAddApartment(View view) {
        Intent i=new Intent(getBaseContext(), AddApartmentActivity.class);
        startActivity(i);
    }
}