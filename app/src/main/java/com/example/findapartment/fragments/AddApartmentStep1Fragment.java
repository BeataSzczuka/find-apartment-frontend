package com.example.findapartment.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.example.findapartment.R;
import com.example.findapartment.activities.AddApartmentActivity;

public class AddApartmentStep1Fragment extends Fragment {

    private Button cancelAddingApartment;
    private Button nextStep;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_apartment_step1, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Fragment thisFragment = this;
        cancelAddingApartment = view.findViewById(R.id.cancelAddingApartment);
        cancelAddingApartment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        nextStep = view.findViewById(R.id.nextStep);
        nextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                Fragment nextFragment = fm.findFragmentById(R.id.fragmentStep2);
                fm.beginTransaction().hide(thisFragment).commit();
                fm.beginTransaction().show(nextFragment).commit();
            }
        });

    }
}