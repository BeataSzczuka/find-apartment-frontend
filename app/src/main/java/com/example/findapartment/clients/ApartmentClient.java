package com.example.findapartment.clients;

import android.content.Context;
import android.util.Log;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

public class ApartmentClient {
    String API_APARTMENTS_URL = "/apartments";

    public void getApartments(Context applicationContext, IRequestCallback iRequestCallback) {
        RequestService.makeGetRequest(API_APARTMENTS_URL, applicationContext, iRequestCallback);
    }

    public void getApartment(Context applicationContext, String apartmentId, IRequestCallback iRequestCallback) {
        RequestService.makeGetRequest(API_APARTMENTS_URL + "/" + apartmentId, applicationContext, iRequestCallback);
    }

    public void addApartment( JSONObject apartment, Context applicationContext, IRequestCallback iRequestCallback) {
        RequestService.makePostRequest(API_APARTMENTS_URL, apartment, applicationContext, iRequestCallback);
    }
}
