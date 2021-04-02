package com.example.findapartment.activities.apartments;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.findapartment.R;
import com.example.findapartment.adapters.ApartmentsAdapter;
import com.example.findapartment.clients.ApartmentClient;
import com.example.findapartment.clients.IRequestCallback;
import com.example.findapartment.models.Apartment;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ApartmentListActivity extends AppCompatActivity {
    private ApartmentClient apartmentClient;
    private ListView lvApartments;
    private ApartmentsAdapter apartmentsAdapter;

    private String userToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apartment_list);

        Intent intentNow = getIntent();
        userToken =intentNow.getStringExtra("TOKEN");

        lvApartments = (ListView) findViewById(R.id.lvApartments);
        ArrayList<Apartment> apartments = new ArrayList<Apartment>();
        apartmentsAdapter = new ApartmentsAdapter(this, apartments);
        lvApartments.setAdapter(apartmentsAdapter);
        apartmentClient = new ApartmentClient();
        fetchApartments();
        setOrderSpinner();
    }

    private void setOrderSpinner(){
        Spinner spinner = (Spinner) findViewById(R.id.orderSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.orders_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void fetchApartments() {
        Log.e("tag", "FETCH APARTMENTS");
        apartmentClient.getApartments(getApplicationContext(), new IRequestCallback(){
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    JSONArray data = null;
                    if (response != null) {
                        data = response.getJSONArray("data");
                        final ArrayList<Apartment> apartments = Apartment.fromJSON(data);
                        apartmentsAdapter.clear(); // remove all objects from adapter
                        // load fetched data into adapter
                        for (Apartment apartment: apartments){
                            apartmentsAdapter.add(apartment);
                        }
                        apartmentsAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onError(String result) throws Exception {
                Log.e("tag", result);
            }
        });
    }
}