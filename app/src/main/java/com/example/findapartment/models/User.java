package com.example.findapartment.models;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {
    private String id;
    private String phoneNumber;
    private String email;
    private String role;
    private List<String> apartmentsIds;



    public static User fromJSON(JSONObject jsonObj) {
        User user = new User();
        try {
            user.id = jsonObj.getString("_id");
            user.phoneNumber = jsonObj.optString("phoneNumber");
            user.email = jsonObj.optString("email");
            user.role = jsonObj.optString("role");
            JSONArray apartments = jsonObj.optJSONArray("apartments");
            if (apartments != null) {
                for (int i = 0; i < apartments.length(); i++){
                    user.apartmentsIds.add(apartments.getString(i));
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return user;
    }

}
