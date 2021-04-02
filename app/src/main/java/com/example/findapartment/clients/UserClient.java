package com.example.findapartment.clients;

import android.content.Context;

import org.json.JSONObject;

public class UserClient {

    public void login(Context applicationContext, IRequestCallback iRequestCallback) {
        String API_USERS_URL = "/users/login";
        RequestService.makeGetRequest(API_USERS_URL, applicationContext, iRequestCallback);
    }

    public void logout(Context applicationContext, IRequestCallback iRequestCallback) {
        String API_USERS_URL = "/users/logout";
        RequestService.makePostRequest(API_USERS_URL, null, applicationContext, iRequestCallback);
    }

    public void register( JSONObject newUser, Context applicationContext, IRequestCallback iRequestCallback) {
        String API_USERS_URL = "/users/signup";
        RequestService.makePostRequest(API_USERS_URL, newUser, applicationContext, iRequestCallback);
    }

    public void getUser(Context applicationContext, String userId, IRequestCallback iRequestCallback) {
        String API_USERS_URL = "/users/" + userId;
        RequestService.makeGetRequest(API_USERS_URL, applicationContext, iRequestCallback);
    }
}
