package com.example.findapartment.clients;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

public class RequestService {
    public static void makeGetRequest(String url, Context context, final IRequestCallback requestCallback) {
        JsonObjectRequest newRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        requestCallback.onSuccess(response);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    requestCallback.onError(error.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        RequestQueueController.getInstance(context).addToRequestQueue(newRequest);
    }
}
