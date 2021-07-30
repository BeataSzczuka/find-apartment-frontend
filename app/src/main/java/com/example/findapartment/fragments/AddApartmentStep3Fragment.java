package com.example.findapartment.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;

import com.example.findapartment.R;
import com.example.findapartment.helpers.SetupHelpers;

public class AddApartmentStep3Fragment extends Fragment {

    private Button nextStep;
    private Button prevStep;

    private EditText descriptionEditText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_apartment_step3, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        descriptionEditText = view.findViewById(R.id.descriptionEditText);
        setNotEmptyValidator(descriptionEditText);


        AlphaAnimation btnAnimation = new AlphaAnimation(1F, 0.3F);

        Fragment thisFragment = this;
        nextStep = view.findViewById(R.id.nextStep);
        nextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(btnAnimation);
                SetupHelpers.hideSoftKeyboard(getActivity());
                if (hasErrors()) {
                    checkErrors(descriptionEditText);
                } else {
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    Fragment nextFragment = fm.findFragmentById(R.id.fragmentStep4);
                    fm.beginTransaction().hide(thisFragment).commit();
                    fm.beginTransaction().show(nextFragment).commit();
                }
            }
        });

        prevStep = view.findViewById(R.id.prevStep);
        prevStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(btnAnimation);
                SetupHelpers.hideSoftKeyboard(getActivity());
                FragmentManager fm = getActivity().getSupportFragmentManager();
                Fragment prevFragment = fm.findFragmentById(R.id.fragmentStep2);
                fm.beginTransaction().hide(thisFragment).commit();
                fm.beginTransaction().show(prevFragment).commit();
            }
        });
    }

    private void setNotEmptyValidator(EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                checkErrors(editText);
            }
        });
    }

    private void checkErrors(EditText editText) {
        if (editText.getText().toString().length() == 0) {
            editText.setError("To pole nie może być puste");
            setNextStepEnabled(false);
        } else {
            editText.setError(null);
            if (!hasErrors()) {
                setNextStepEnabled(true);
            }
        }
    }

    private boolean hasErrors() {
        return descriptionEditText.getText().length() == 0;
    }

    private void setNextStepEnabled(boolean enabled) {
        if (enabled) {
            nextStep.setEnabled(true);
            nextStep.setTextColor(getResources().getColor(R.color.colorPrimary));
        } else {
            nextStep.setEnabled(false);
            nextStep.setTextColor(getResources().getColor(R.color.colorPrimaryTransparent));
        }
    }
}