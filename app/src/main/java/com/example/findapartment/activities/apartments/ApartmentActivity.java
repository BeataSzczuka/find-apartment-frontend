package com.example.findapartment.activities.apartments;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.findapartment.R;
import com.example.findapartment.clients.ApartmentClient;
import com.example.findapartment.clients.IRequestCallback;
import com.example.findapartment.models.Apartment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ApartmentActivity extends AppCompatActivity {

    private ApartmentClient apartmentClient;
    private String apartmentId;
    private ProgressDialog pDialog;
    private ConstraintLayout apartmentLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apartment);
        Intent intentNow = getIntent();
        apartmentId = intentNow.getStringExtra("apartmentID");
        apartmentLayout = findViewById(R.id.apartmentLayout);
        apartmentClient = new ApartmentClient();

        fetchApartment();
    }

    private void fetchApartment() {
        Log.e("tag", "FETCH APARTMENTS");
        apartmentClient.getApartment(getApplicationContext(), apartmentId, new IRequestCallback(){
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    JSONObject data = null;
                    if (response != null) {
                        data = response.getJSONObject("data");
                        final Apartment apartment = Apartment.fromJSON(data);

                        ((TextView) findViewById(R.id.priceTv)).setText(apartment.getPrice().toString() + " zł");
                        ((TextView) findViewById(R.id.propertySizeTv)).setText(apartment.getPropertySize().toString() + " m2");
                        ((TextView) findViewById(R.id.locationTv)).setText(apartment.getLocation());
                        ((TextView) findViewById(R.id.transactionTextView)).setText(apartment.getTransactionType());
                        ((TextView) findViewById(R.id.publicationDateTv)).setText(apartment.getPublicationDate());
                        ((TextView) findViewById(R.id.phoneNumberTv)).setText(apartment.getPhoneNumber());
                        ((TextView) findViewById(R.id.emailTv)).setText(apartment.getEmail());
                        ((TextView) findViewById(R.id.descriptionTv)).setText(apartment.getDescription());

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