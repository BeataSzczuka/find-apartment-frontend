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

import com.example.findapartment.R;
import com.example.findapartment.activities.AddApartmentActivity;

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

        prevStep = view.findViewById(R.id.prevStep);
        prevStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                try {
                    ((AddApartmentActivity) getActivity()).onAddApartmentClick();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }
}