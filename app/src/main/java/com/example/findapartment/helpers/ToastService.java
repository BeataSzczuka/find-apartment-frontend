package com.example.findapartment.helpers;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import  android.widget.LinearLayout.LayoutParams;

public class ToastService {

    private static void showMessage(String message, String color, Context context) {
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, message, duration);
        toast.getView().setBackgroundColor(Color.parseColor(color));
        TextView toastText = (TextView) toast.getView().findViewById(android.R.id.message);
        toastText.setTextColor(Color.WHITE);

        View v = toast.getView();
        v.setPadding(50,50,50,50);
        LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lp.setMargins(50,50,10,50);
        v.setLayoutParams(lp);

        toast.show();
    }

    public static void showSuccessMessage(String message, Context context) {
        showMessage(message, "#4CAF50", context);
    }

    public static void showErrorMessage(String message, Context context) {
        showMessage(message, "#A61A1A", context);
    }

}
