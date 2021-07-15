package com.example.findapartment.fragments;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.findapartment.R;
import com.example.findapartment.activities.AddApartmentActivity;
import com.example.findapartment.activities.ApartmentListActivity;
import com.example.findapartment.activities.LoginActivity;
import com.example.findapartment.activities.MyAccountActivity;
import com.example.findapartment.helpers.AppViewNames;
import com.example.findapartment.helpers.UserSession;


public class ToolbarFragment extends Fragment {

    private ImageButton startMenuBtn;
    private ImageButton addMenuBtn;
    private ImageButton accountMenuBtn;

    private UserSession userSession;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_toolbar, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        userSession = new UserSession(getActivity());

        startMenuBtn = (ImageButton) view.findViewById(R.id.startMenuBtn);
        startMenuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ApartmentListActivity.class);
                startActivity(intent);

            }
        });

        addMenuBtn = (ImageButton) view.findViewById(R.id.addMenuBtn);
        addMenuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddApartmentActivity.class);
                startActivity(intent);

            }
        });

        accountMenuBtn = (ImageButton) view.findViewById(R.id.accountMenuBtn);
        accountMenuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                if (userSession.isLoggedIn()) {
                    intent = new Intent(getActivity(), MyAccountActivity.class);
                }
                else {
                    intent = new Intent(getActivity(), LoginActivity.class);
                }
                startActivity(intent);
            }
        });
    }

    public void setImageTint(String appViewName) {
        startMenuBtn.setEnabled(appViewName != AppViewNames.APARTMENTS_LIST);
        addMenuBtn.setEnabled(appViewName != AppViewNames.ADD_APARTMENT);
        accountMenuBtn.setEnabled(appViewName != AppViewNames.MY_ACCOUNT);
        Drawable buttonDrawable;
        if (appViewName == AppViewNames.ADD_APARTMENT){
            buttonDrawable = addMenuBtn.getDrawable();
        }
        else if (appViewName == AppViewNames.MY_ACCOUNT){
            buttonDrawable = accountMenuBtn.getDrawable();
        }
        else {
            buttonDrawable = startMenuBtn.getDrawable();
        }
        buttonDrawable = DrawableCompat.wrap(buttonDrawable);
        DrawableCompat.setTint(buttonDrawable, getResources().getColor(R.color.colorPrimary));
    }
}