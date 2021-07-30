package com.example.findapartment.models;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;

public class FilterRanges implements Serializable {
    public float propertySizeFrom;
    public float propertySizeTo;
    public float priceFrom;
    public float priceTo;



    public static FilterRanges fromJSON(JSONObject jsonObj) {
        FilterRanges filterRanges = new FilterRanges();
        filterRanges.propertySizeFrom = (float) jsonObj.optDouble("propertySizeFrom", 0);
        filterRanges.propertySizeTo = (float) jsonObj.optDouble("propertySizeTo", 1000);
        filterRanges.priceFrom = (float) jsonObj.optDouble("priceFrom", 0);
        filterRanges.priceTo = (float) jsonObj.optDouble("priceTo", 1000000);
        return filterRanges;
    }

}
