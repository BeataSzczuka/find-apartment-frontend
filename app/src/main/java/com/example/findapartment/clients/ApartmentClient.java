package com.example.findapartment.clients;

import android.content.Context;
import android.util.Log;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

public class ApartmentClient {
    private static  final String API_BASE_URL = "http://10.0.2.2:3000/api";


    public void getApartments(Context applicationContext, IRequestCallback iRequestCallback) {
        String API_APARTMENTS_URL = API_BASE_URL + "/apartments";
        RequestService.makeGetRequest(API_APARTMENTS_URL, applicationContext, iRequestCallback);
    }

    // TODO getApartment by id
    public void getApartment(Context applicationContext, String apartmentId, IRequestCallback iRequestCallback) {
        String API_APARTMENTS_URL = API_BASE_URL + "/apartments/" + apartmentId;
        RequestService.makeGetRequest(API_APARTMENTS_URL, applicationContext, iRequestCallback);
    }
}
