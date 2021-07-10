package com.example.findapartment.clients;

import android.content.Context;

import org.json.JSONObject;

public class UserClient {
    String API_USERS_URL = "users";

    public void login( JSONObject loginData, Context applicationContext, IRequestCallback iRequestCallback) {
        String URL = API_USERS_URL + "/login";
        RequestService.makePostRequest(URL, loginData, applicationContext, iRequestCallback);
    }

    public void logout(Context applicationContext, IRequestCallback iRequestCallback) {
        String URL = API_USERS_URL + "/logout";
        RequestService.makePostRequest(URL, null, applicationContext, iRequestCallback);
    }

    public void register( JSONObject newUser, Context applicationContext, IRequestCallback iRequestCallback) {
        String URL = API_USERS_URL + "/signup";
        RequestService.makePostRequest(URL, newUser, applicationContext, iRequestCallback);
    }

}
