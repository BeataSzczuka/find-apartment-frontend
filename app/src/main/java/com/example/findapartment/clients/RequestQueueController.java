package com.example.findapartment.clients;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 *  A singleton class for the RequestQueue
 */
public class RequestQueueController {
    private static RequestQueueController INSTANCE;
    private RequestQueue requestQueue;
    private static Context context;

    private RequestQueueController(Context ctxt) {
        this.context = ctxt;
        this.requestQueue = getRequestQueue();
    }

    public RequestQueue getRequestQueue(){
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }

    public static synchronized RequestQueueController getInstance(Context ctxt) {
        if (INSTANCE == null) {
            INSTANCE = new RequestQueueController(ctxt);
        }
        return INSTANCE;
    }

    public void addToRequestQueue(Request request){
        getRequestQueue().add(request);
    }
}

