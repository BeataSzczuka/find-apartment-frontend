package com.example.findapartment.helpers;

import android.app.Activity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.example.findapartment.R;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class SetupHelpers {

    public static void hideSoftKeyboard(Activity activity) {
        View rootView = activity.findViewById(R.id.rootView);
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(
                    rootView.getWindowToken(),
                    0
            );
        }
    }
    public static void setKeyboardHideListener(Activity activity) {
        View rootView = activity.findViewById(R.id.rootView);
        rootView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideSoftKeyboard(activity);
                return true;
            }
        });
    }
}
