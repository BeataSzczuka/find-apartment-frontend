package com.example.findapartment.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.findapartment.R;
import com.example.findapartment.adapters.ApartmentsAdapter;
import com.example.findapartment.clients.ApartmentClient;
import com.example.findapartment.clients.IRequestCallback;
import com.example.findapartment.helpers.ToastService;
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
    private ProgressBar progressBar;
    private TextView noApartmentsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apartment_list);

        lvApartments = (ListView) findViewById(R.id.lvApartments);
        ArrayList<Apartment> apartments = new ArrayList<Apartment>();
        apartmentsAdapter = new ApartmentsAdapter(this, apartments);
        lvApartments.setAdapter(apartmentsAdapter);
        apartmentClient = new ApartmentClient();

        progressBar = findViewById(R.id.apartmentListActivityProgressBar);
        noApartmentsTextView = findViewById(R.id.noApartmentsTextView);

        fetchApartments();
    }


    private void fetchApartments() {
        progressBar.setVisibility(View.VISIBLE);
        apartmentClient.getApartments(getApplicationContext(), new IRequestCallback(){
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    JSONArray data = null;
                    if (response != null) {
                        data = response.getJSONArray("data");
                        final ArrayList<Apartment> apartments = Apartment.fromJSON(data);
                        apartmentsAdapter.clear();
                        for (Apartment apartment: apartments){
                            apartmentsAdapter.add(apartment);
                        }
                        apartmentsAdapter.notifyDataSetChanged();
                        if (apartments.size() == 0) {
                            noApartmentsTextView.setVisibility(View.VISIBLE);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }
            @Override
            public void onError(String result) throws Exception {
                ToastService.showErrorMessage("Nie można załadować ogłoszeń", getApplicationContext());
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }
}