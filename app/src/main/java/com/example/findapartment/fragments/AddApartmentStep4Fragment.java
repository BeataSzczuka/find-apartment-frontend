package com.example.findapartment.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.findapartment.R;
import com.example.findapartment.activities.AddApartmentActivity;
import com.example.findapartment.helpers.SetupHelpers;

import org.json.JSONException;

public class AddApartmentStep4Fragment extends Fragment {

    private Button prevStep;
    private Button addApartmentBtn;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_apartment_step4, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Fragment thisFragment = this;

        AlphaAnimation btnAnimation = new AlphaAnimation(1F, 0.3F);


        prevStep = view.findViewById(R.id.prevStep);
        prevStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(btnAnimation);
                SetupHelpers.hideSoftKeyboard(getActivity());
                FragmentManager fm = getActivity().getSupportFragmentManager();
                Fragment prevFragment = fm.findFragmentById(R.id.fragmentStep3);
                fm.beginTransaction().hide(thisFragment).commit();
                fm.beginTransaction().show(prevFragment).commit();
            }
        });

        addApartmentBtn = view.findViewById(R.id.addApartmentBtn);
        addApartmentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(btnAnimation);
                SetupHelpers.hideSoftKeyboard(getActivity());
                try {
                    ((AddApartmentActivity) getActivity()).onAddApartmentClick();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

        ImageButton imageButton = view.findViewById(R.id.imageButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetupHelpers.hideSoftKeyboard(getActivity());
                ((AddApartmentActivity) getActivity()).uploadImage();
            }
        });
    }
}