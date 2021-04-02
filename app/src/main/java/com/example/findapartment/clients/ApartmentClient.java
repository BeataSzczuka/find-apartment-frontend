package com.example.findapartment.clients;

import android.content.Context;
import android.util.Log;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

public class ApartmentClient {

    public void getApartments(Context applicationContext, IRequestCallback iRequestCallback) {
        String API_APARTMENTS_URL = "/apartments";
        RequestService.makeGetRequest(API_APARTMENTS_URL, applicationContext, iRequestCallback);
    }

    public void getApartment(Context applicationContext, String apartmentId, IRequestCallback iRequestCallback) {
        String API_APARTMENTS_URL = "/apartments/" + apartmentId;
        RequestService.makeGetRequest(API_APARTMENTS_URL, applicationContext, iRequestCallback);
    }
}
