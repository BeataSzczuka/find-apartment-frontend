package com.example.findapartment.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class Apartment implements Serializable {
    private String title;
    private Integer price;
    private Integer propertySize;
    private String location;
    private String description;

    public String getTitle() {
        return title;
    }

    public Integer getPrice() {
        return price;
    }

    public Integer getPropertySize() {
        return propertySize;
    }


    public String getLocation() {
        return location;
    }

    public String getDescription() {
        return description;
    }

    public static Apartment fromJSON(JSONObject jsonObj) {
        Apartment apartment = new Apartment();
        try {
            apartment.description = jsonObj.getString("description");
            apartment.location = jsonObj.getString("location");
            apartment.propertySize = jsonObj.getInt("propertySize");
            apartment.price = jsonObj.getInt("price");
            apartment.title = jsonObj.getString("title");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return apartment;
    }

    public static ArrayList<Apartment> fromJSON(JSONArray jsonArr) {
        ArrayList<Apartment> apartments = new ArrayList<Apartment>(jsonArr.length());
        for (int i=0; i<jsonArr.length(); i++){
            JSONObject jsonObj = null;
            try {
                jsonObj = jsonArr.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }
            Apartment apartment = Apartment.fromJSON(jsonObj);
            if (apartment != null) {
                apartments.add(apartment);
            }
        }
        return apartments;
    }
}
