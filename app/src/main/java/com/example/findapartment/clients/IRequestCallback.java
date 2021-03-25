package com.example.findapartment.clients;

import org.json.JSONObject;

public interface IRequestCallback {
    public void onSuccess(JSONObject result);
    public abstract void onError(String errorMessage) throws Exception;
}
