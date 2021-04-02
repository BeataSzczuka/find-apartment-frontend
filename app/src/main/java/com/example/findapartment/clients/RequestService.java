package com.example.findapartment.clients;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.findapartment.helpers.UserSession;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RequestService {

    private static  final String API_BASE_URL = "http://10.0.2.2:3000/api";


    public static void makeGetRequest(String url, final Context c, final IRequestCallback requestCallback) {
        JsonObjectRequest newRequest = new JsonObjectRequest(Request.Method.GET, API_BASE_URL + url, null,
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
        }){
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                UserSession userSession = new UserSession(c);
                params.put("authorization", userSession.getLoggedInUserToken());
                return params;
            }
        };
        RequestQueueController.getInstance(c).addToRequestQueue(newRequest);
    }


    public static void makePostRequest(String url, JSONObject data, final Context context, final IRequestCallback requestCallback) {
        JsonObjectRequest newRequest = new JsonObjectRequest(Request.Method.POST, API_BASE_URL + url, data,
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
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                UserSession userSession = new UserSession(context);
                params.put("authorization", userSession.getLoggedInUserToken());
                return params;
            }
        };
        RequestQueueController.getInstance(context).addToRequestQueue(newRequest);
    }
}
