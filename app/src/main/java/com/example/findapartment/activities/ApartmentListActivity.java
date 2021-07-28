package com.example.findapartment.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.app.Fragment;

import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.findapartment.R;
import com.example.findapartment.adapters.ApartmentsAdapter;
import com.example.findapartment.clients.ApartmentClient;
import com.example.findapartment.clients.IRequestCallback;
import com.example.findapartment.fragments.FiltersFragment;
import com.example.findapartment.fragments.ToolbarFragment;
import com.example.findapartment.helpers.AppViewNames;
import com.example.findapartment.helpers.SortTypesEnum;
import com.example.findapartment.helpers.ToastService;
import com.example.findapartment.helpers.UserSession;
import com.example.findapartment.models.Apartment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class ApartmentListActivity extends AppCompatActivity {
    private ApartmentClient apartmentClient;
    private ListView lvApartments;
    private ApartmentsAdapter apartmentsAdapter;
    private LinearLayout progressBarLayout;
    private TextView noApartmentsTextView;

    private FiltersFragment filtersFragment;

    private int page = 0;
    private int pageSize = 20;
    private int totalPages = 1;

    private String sortBy;
    private String priceFrom;
    private String priceTo;
    private String propertySizeFrom;
    private String propertySizeTo;
    private String location;
    private String transactionType;
    private Boolean onlyMy;

    private UserSession userSession;

    private String queryParams = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apartment_list);

        userSession = new UserSession(ApartmentListActivity.this);

        lvApartments = (ListView) findViewById(R.id.lvApartments);
        ArrayList<Apartment> apartments = new ArrayList<Apartment>();
        apartmentsAdapter = new ApartmentsAdapter(this, apartments, R.layout.item_apartment);
        lvApartments.setAdapter(apartmentsAdapter);
        apartmentClient = new ApartmentClient();

        noApartmentsTextView = findViewById(R.id.noApartmentsTextView);

        filtersFragment = new FiltersFragment();

        ToolbarFragment toolbarFragment = (ToolbarFragment) getSupportFragmentManager().findFragmentById(R.id.menuFragment);
        toolbarFragment.setImageTint(AppViewNames.APARTMENTS_LIST);


        lvApartments.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (totalItemCount != 0 && page < totalPages - 1 && firstVisibleItem + visibleItemCount == totalItemCount && progressBarLayout.getVisibility() != View.VISIBLE) {
                    page++;
                    fetchApartments();
                }
            }
        });


        View footer = getLayoutInflater().inflate(
                R.layout.progress_footer, null);
        progressBarLayout = (LinearLayout)
                footer.findViewById(R.id.pbFooterLoading);
        lvApartments.addFooterView(progressBarLayout);

//        setOrderSpinner();
        fetchApartments();


    }


//    private void setOrderSpinner(){
//        Spinner spinner = (Spinner) findViewById(R.id.orderSpinner);
//
//        ArrayAdapter<SortTypesEnum> adapter = new ArrayAdapter<SortTypesEnum>(this, android.R.layout.simple_spinner_item, SortTypesEnum.values());
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinner.setAdapter(adapter);
//
//        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                apartmentsAdapter.clear();
//                page = 0;
//                SortTypesEnum selected = SortTypesEnum.values()[position];
//                sortBy = selected.name();
//                fetchApartments();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//    }


    public void onFiltersClick(View view) throws JSONException {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (filtersFragment.isAdded()) {
            ft.remove(filtersFragment);
        } else {
            ft.add(R.id.filtersContainer, filtersFragment);
        }
        ft.commit();
    }

    public void setFilters(String filters) {
        this.queryParams = filters;
        fetchApartments();
    }

    private void fetchApartments() {
        progressBarLayout.setVisibility(View.VISIBLE);
        noApartmentsTextView.setVisibility(View.GONE);

        Uri.Builder builder = new Uri.Builder();
        builder.appendQueryParameter("page", String.valueOf(page));
        builder.appendQueryParameter("pageSize", String.valueOf(pageSize));
        builder.appendQueryParameter("sort", sortBy);
//        builder.appendPath(queryParams);
        String paginationParams =  builder.build().toString();


        apartmentClient.getApartments(getApplicationContext(), paginationParams, new IRequestCallback(){
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    JSONObject data = null;
                    if (response != null) {
                        data = response.getJSONObject("data");
                        totalPages = Integer.parseInt(data.getString("pages"));
                        final ArrayList<Apartment> apartments = Apartment.fromJSON(data.getJSONArray("apartments"));
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                for (Apartment apartment: apartments){
                                    apartmentsAdapter.add(apartment);
                                }
                                apartmentsAdapter.notifyDataSetChanged();

                                if (apartments.size() == 0) {
                                    noApartmentsTextView.setVisibility(View.VISIBLE);
                                } else {
                                    noApartmentsTextView.setVisibility(View.GONE);
                                }

                                if (filtersFragment.isAdded()) {
                                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                                    if (apartments.size() == 0) {
                                        filtersFragment.showNoResultsMessage(true);
                                    } else {
                                        ft.remove(filtersFragment);
                                        filtersFragment.showNoResultsMessage(false);
                                    }
                                    ft.commit();
                                }

                            }
                        });
                        progressBarLayout.setVisibility(View.GONE);


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    progressBarLayout.setVisibility(View.GONE);
                }
            }
            @Override
            public void onError(String result) throws Exception {
                ToastService.showErrorMessage("Nie można załadować ogłoszeń", findViewById(R.id.rootView));
                progressBarLayout.setVisibility(View.GONE);
                noApartmentsTextView.setVisibility(View.VISIBLE);
                if (filtersFragment.isAdded()) {
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.remove(filtersFragment);
                    ft.commit();
                }
            }
        });
    }
}