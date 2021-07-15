package com.example.findapartment.clients;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class ApartmentClient {
    String API_APARTMENTS_URL = "apartments";

    private final OkHttpClient client = new OkHttpClient();
    public void getApartments(Context applicationContext, String queryParams, IRequestCallback iRequestCallback) {
        RequestService.makeGetRequest(API_APARTMENTS_URL + queryParams, applicationContext, iRequestCallback);
    }

    public void getApartment(Context applicationContext, String apartmentId, IRequestCallback iRequestCallback) {
        RequestService.makeGetRequest(API_APARTMENTS_URL + "/" + apartmentId, applicationContext, iRequestCallback);
    }

    public void deleteApartment( String apartmentId, Context applicationContext, IRequestCallback iRequestCallback) {
        RequestService.makeDeleteRequest(API_APARTMENTS_URL + "/" + apartmentId, applicationContext, iRequestCallback);
    }
}
