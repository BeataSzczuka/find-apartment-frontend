package com.example.findapartment.clients;

import android.content.Context;
import android.util.Log;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ApartmentClient {
    String API_APARTMENTS_URL = "apartments";

    public void getApartments(Context applicationContext, String pageParam, String pageSizeParam, IRequestCallback iRequestCallback) {
        String url = String.format(API_APARTMENTS_URL + "?page=%1$s&pageSize=%2$s", pageParam, pageSizeParam);
        RequestService.makeGetRequest(url, applicationContext, iRequestCallback);
    }

    public void getApartment(Context applicationContext, String apartmentId, IRequestCallback iRequestCallback) {
        RequestService.makeGetRequest(API_APARTMENTS_URL + "/" + apartmentId, applicationContext, iRequestCallback);
    }

    public void deleteApartment( String apartmentId, Context applicationContext, IRequestCallback iRequestCallback) {
        RequestService.makeDeleteRequest(API_APARTMENTS_URL + "/" + apartmentId, applicationContext, iRequestCallback);
    }

    private Map<String, String> setQueryParams() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("page", "0");
        params.put("pageSize", "5");
        return params;
    }
}
