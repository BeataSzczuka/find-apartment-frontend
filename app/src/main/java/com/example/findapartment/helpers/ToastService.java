package com.example.findapartment.helpers;

import android.content.Context;
import android.graphics.Color;
import android.widget.TextView;
import android.widget.Toast;

public class ToastService {

    private static void showMessage(String message, String color, Context context) {
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, message, duration);
        toast.getView().setBackgroundColor(Color.parseColor(color));

        TextView toastText = (TextView) toast.getView().findViewById(android.R.id.message);
        toastText.setTextColor(Color.WHITE);
        toast.show();
    }

    public static void showSuccessMessage(String message, Context context) {
        showMessage(message, "#4CAF50", context);
    }

    public static void showErrorMessage(String message, Context context) {
        showMessage(message, "#A61A1A", context);
    }

}
