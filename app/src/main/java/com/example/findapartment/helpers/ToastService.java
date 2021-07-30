package com.example.findapartment.helpers;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import  android.widget.LinearLayout.LayoutParams;

import com.example.findapartment.R;
import com.example.findapartment.activities.MyAccountActivity;
import com.google.android.material.snackbar.Snackbar;

public class ToastService {

    public static void showSuccessMessage(String message, Context context) {
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, message, duration);
        toast.getView().setBackgroundColor(Color.parseColor("#68955B"));
        TextView toastText = (TextView) toast.getView().findViewById(android.R.id.message);
        toastText.setTextColor(Color.WHITE);

        View v = toast.getView();
        v.setPadding(50,50,50,50);
        toast.setGravity(Gravity.FILL_HORIZONTAL | Gravity.BOTTOM, 0, 150);
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, 10);
        v.setLayoutParams(lp);

        toast.show();
    }


    public static void showErrorMessage(String message, View view) {
        View bottomMenu = view.findViewById(R.id.menuFragment);
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG).setAnchorView(bottomMenu);
        snackbar.setBackgroundTint(Color.parseColor("#B55353"));
        snackbar.show();
    }

    public static void showSnackbar(String message, MyAccountActivity myAccountActivity, String apartmentId, int position) {
        View view = myAccountActivity.findViewById(R.id.lvMyApartments);
        View bottomMenu = myAccountActivity.findViewById(R.id.menuFragment);
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG).setAnchorView(bottomMenu);
        snackbar.setAction("Cofnij", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myAccountActivity.onRestoreApartmentClick(apartmentId, position);
            }
        });
        snackbar.show();
    }

}
