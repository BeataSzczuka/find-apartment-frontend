package com.example.findapartment.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.findapartment.R;
import com.example.findapartment.adapters.ApartmentsAdapter;
import com.example.findapartment.clients.ApartmentClient;
import com.example.findapartment.clients.IRequestCallback;
import com.example.findapartment.clients.UserClient;
import com.example.findapartment.fragments.NavigationbarFragment;
import com.example.findapartment.fragments.ToolbarFragment;
import com.example.findapartment.helpers.AppViewNames;
import com.example.findapartment.helpers.ToastService;
import com.example.findapartment.helpers.UserSession;
import com.example.findapartment.models.Apartment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MyAccountActivity extends AppCompatActivity {

    private ApartmentClient apartmentClient;
    private ListView lvMyApartments;
    private ApartmentsAdapter apartmentsAdapter;

    private UserClient userClient;
    private UserSession userSession;

    private LinearLayout myAnnLoading;
    private TextView myAnnEmpty;
    private LinearLayout myAnnData;
    private ProgressBar logoutProgressBar;

    private int page = 0;
    private int pageSize = 3;
    private int totalPages = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);

        myAnnLoading = findViewById(R.id.myAnnLoading);
        myAnnEmpty = findViewById(R.id.myAnnEmpty);
        myAnnData = findViewById(R.id.myAnnData);
        logoutProgressBar = findViewById(R.id.myAccountActivityProgressBar);

        userSession = new UserSession(MyAccountActivity.this);
        userClient = new UserClient();

        lvMyApartments = (ListView) findViewById(R.id.lvMyApartments);
        ArrayList<Apartment> apartments = new ArrayList<Apartment>();
        apartmentsAdapter = new ApartmentsAdapter(this, apartments, R.layout.item_my_apartment);
        lvMyApartments.setAdapter(apartmentsAdapter);
        apartmentClient = new ApartmentClient();
        fetchMyApartments();

        ToolbarFragment toolbarFragment = (ToolbarFragment) getSupportFragmentManager().findFragmentById(R.id.menuFragment);
        toolbarFragment.setImageTint(AppViewNames.MY_ACCOUNT);

        NavigationbarFragment navigationbarfragment = (NavigationbarFragment) getSupportFragmentManager().findFragmentById(R.id.navigationbar);
        navigationbarfragment.setTitle("Moje konto");
    }


    private void fetchMyApartments() {
        myAnnLoading.setVisibility(View.VISIBLE);
        myAnnEmpty.setVisibility(View.GONE);
        myAnnData.setVisibility(View.GONE);

        Uri.Builder builder = new Uri.Builder();
        builder.appendQueryParameter("page", String.valueOf(page));
        builder.appendQueryParameter("pageSize", String.valueOf(pageSize));
        builder.appendQueryParameter("onlyMy", "true");
        String queryParams =  builder.build().toString();


        apartmentClient.getApartments(getApplicationContext(), queryParams, new IRequestCallback(){
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
                            myAnnEmpty.setVisibility(View.VISIBLE);
                        } else {
                            myAnnData.setVisibility(View.VISIBLE);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    myAnnLoading.setVisibility(View.GONE);
                }
            }
            @Override
            public void onError(String result) throws Exception {
                ToastService.showErrorMessage("Nie można załadować ogłoszeń", getApplicationContext());
                myAnnLoading.setVisibility(View.GONE);
                myAnnEmpty.setVisibility(View.VISIBLE);
            }
        });
    }

    public void onLogoutButtonClick(View view) {
        logoutProgressBar.setVisibility(View.VISIBLE);
        userSession.deleteSession();
        userClient.logout(getApplicationContext(), new IRequestCallback(){
            @Override
            public void onSuccess(JSONObject response) {
                if (response != null) {
                    Intent i=new Intent(getBaseContext(), ApartmentListActivity.class);
                    startActivity(i);
                    ToastService.showSuccessMessage("Zostałeś wylogowany.", getApplicationContext());
                }
                logoutProgressBar.setVisibility(View.INVISIBLE);
            }
            @Override
            public void onError(String result) throws Exception {
                ToastService.showErrorMessage("Wystąpił błąd podczas wylogowywania.", getApplicationContext());
                logoutProgressBar.setVisibility(View.INVISIBLE);
            }
        });
    }
}