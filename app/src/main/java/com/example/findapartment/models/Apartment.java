package com.example.findapartment.models;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.util.Base64;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;


public class Apartment implements Serializable {

    private static class ImageObject {

        private byte[] data;

        public ImageObject(byte[] data) {
            this.data = data;
        }

        public byte[] getData() {
            return data;
        }

        public void setData(byte[] data) {
            this.data = data;
        }

    }

    private String id;
    private String transactionType;
    private Float price;
    private Float propertySize;
    private String location;
    private String description;
    private String publicationDate;
    private String phoneNumber;
    private String email;
    private boolean isAuthor;
    private List<ImageObject> images;

    public List<ImageObject> getImages() {
        return images;
    }

    public byte[] getImage(int i) {
        return images.get(i).getData();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Float getPrice() {
        return price;
    }

    public Float getPropertySize() {
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

    public String getPublicationDate() {
        return publicationDate;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }


    public boolean getIsAuthor() {
        return isAuthor;
    }


    public static Apartment fromJSON(JSONObject jsonObj) {
        Apartment apartment = new Apartment();
        try {
            apartment.id = jsonObj.getString("_id");
            apartment.description = jsonObj.optString("description");
            apartment.location = jsonObj.optString("location");
            apartment.propertySize = Float.parseFloat(jsonObj.optString("propertySize"));
            apartment.price =  Float.parseFloat(jsonObj.optString("price"));
            apartment.transactionType = jsonObj.optString("transactionType");
            apartment.publicationDate = jsonObj.optString("publicationDate");
            apartment.phoneNumber = jsonObj.optString("phoneNumber");
            apartment.email = jsonObj.optString("email");
            apartment.isAuthor = jsonObj.optBoolean("isAuthor");

            JSONArray arr =  jsonObj.optJSONArray("images");
            List<ImageObject> images = new ArrayList<ImageObject>();

            for (int i = 0; i < arr.length(); i++) {
                String decoded = ((JSONObject) arr.get(i)).getJSONObject("data").getString("data");
                Gson gson = new Gson();
                Type bytesType = new TypeToken<byte[]>(){}.getType();
                byte[] bytes = gson.fromJson(decoded, bytesType );

                ImageObject io = new ImageObject(bytes);
                images.add(io);
            }
            apartment.images = images;
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
