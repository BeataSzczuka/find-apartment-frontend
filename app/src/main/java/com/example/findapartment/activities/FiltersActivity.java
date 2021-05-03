package com.example.findapartment.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.findapartment.R;

public class FiltersActivity extends AppCompatActivity {

    private EditText priceFrom;
    private EditText priceTo;
    private EditText propertySizeFrom;
    private EditText propertySizeTo;
    private EditText location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filters);

        priceFrom = findViewById(R.id.priceFrom);
        priceTo = findViewById(R.id.priceTo);
        propertySizeFrom = findViewById(R.id.propertySizeFrom);
        propertySizeTo = findViewById(R.id.propertySizeTo);
        location = findViewById(R.id.location);
    }

    public void onFilterClick(View view) {
        Intent i = new Intent(getBaseContext(), ApartmentListActivity.class);
        i.putExtra("priceFrom", priceFrom.getText().toString());
        i.putExtra("priceTo", priceTo.getText().toString());
        i.putExtra("propertySizeFrom", propertySizeFrom.getText().toString());
        i.putExtra("propertySizeTo", propertySizeTo.getText().toString());
        i.putExtra("location", location.getText().toString());
        startActivity(i);
    }
}