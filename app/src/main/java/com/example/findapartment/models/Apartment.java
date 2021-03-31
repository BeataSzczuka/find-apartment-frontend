package com.example.findapartment.models;


import android.widget.TextView;

import com.example.findapartment.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class Apartment implements Serializable {
    private String id;
    private String transactionType;
    private Integer price;
    private Integer propertySize;
    private String location;
    private String description;
    private String publicationDate;
    private String phoneNumber;
    private String email;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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


    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public void setPropertySize(Integer propertySize) {
        this.propertySize = propertySize;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(String publicationDate) {
        this.publicationDate = publicationDate;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }



    public static Apartment fromJSON(JSONObject jsonObj) {
        Apartment apartment = new Apartment();
        try {
            apartment.id = jsonObj.getString("_id");
            apartment.description = jsonObj.optString("description");
            apartment.location = jsonObj.optString("location");
            apartment.propertySize = jsonObj.optInt("propertySize");
            apartment.price = jsonObj.optInt("price");
            apartment.transactionType = jsonObj.optString("transactionType");
            apartment.publicationDate = jsonObj.optString("publicationDate");
            apartment.phoneNumber = jsonObj.optString("phoneNumber");
            apartment.email = jsonObj.optString("email");
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
