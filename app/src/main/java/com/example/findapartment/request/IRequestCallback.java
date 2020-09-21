package com.example.findapartment.request;

import org.json.JSONObject;

public interface IRequestCallback {
    public void onSuccess(JSONObject result);
    public abstract void onError(String errorMessage) throws Exception;
}
