package com.example.findapartment.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
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

    private int page = 0;
    private int pageSize = 3;
    private int totalPages = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apartment_list);

        lvApartments = (ListView) findViewById(R.id.lvApartments);
        ArrayList<Apartment> apartments = new ArrayList<Apartment>();
        apartmentsAdapter = new ApartmentsAdapter(this, apartments);
        lvApartments.setAdapter(apartmentsAdapter);
        apartmentClient = new ApartmentClient();

//        progressBar = findViewById(R.id.apartmentListActivityProgressBar);
        noApartmentsTextView = findViewById(R.id.noApartmentsTextView);

        Button filtersBtn = (Button) findViewById(R.id.openFiltersBtn);
        filtersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), FiltersActivity.class);
                startActivity(intent);

            }
        });

        lvApartments.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (totalItemCount != 0 && page < totalPages - 1 && firstVisibleItem + visibleItemCount == totalItemCount && progressBar.getVisibility() != View.VISIBLE) {
                    page++;
                    fetchApartments();
                }
            }
        });


        View footer = getLayoutInflater().inflate(
                R.layout.progress_footer, null);
        progressBar = (ProgressBar)
                footer.findViewById(R.id.pbFooterLoading);
        lvApartments.addFooterView(progressBar);


        apartmentsAdapter.clear();
        fetchApartments();
    }


    private void fetchApartments() {
        progressBar.setVisibility(View.VISIBLE);
        apartmentClient.getApartments(getApplicationContext(), String.valueOf(page), String.valueOf(pageSize), new IRequestCallback(){
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    JSONObject data = null;
                    if (response != null) {
                        data = response.getJSONObject("data");
                        totalPages = Integer.parseInt(data.getString("pages"));
                        final ArrayList<Apartment> apartments = Apartment.fromJSON(data.getJSONArray("apartments"));
                        for (Apartment apartment: apartments){
                            apartmentsAdapter.add(apartment);
                        }
                        apartmentsAdapter.notifyDataSetChanged();
                        if (apartments.size() == 0) {
                            noApartmentsTextView.setVisibility(View.GONE);
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