package com.example.findapartment.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;

import com.example.findapartment.R;
import com.example.findapartment.helpers.SetupHelpers;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;

public class AddApartmentStep2Fragment extends Fragment {

    private Button nextStep;
    private Button prevStep;

    private EditText priceEditText;
    private EditText propertySizeEditText;
    private EditText locationEditText;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_apartment_step2, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        priceEditText = view.findViewById(R.id.priceEditText);
        propertySizeEditText = view.findViewById(R.id.propertySizeEditText);
        locationEditText = view.findViewById(R.id.locationEditText);
        setNotEmptyValidator(priceEditText);
        setNotEmptyValidator(propertySizeEditText);
        setNotEmptyValidator(locationEditText);

        setFocusChangedListener(priceEditText);
        setFocusChangedListener(propertySizeEditText);

        AlphaAnimation btnAnimation = new AlphaAnimation(1F, 0.3F);

        Fragment thisFragment = this;
        nextStep = view.findViewById(R.id.nextStep);
        nextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(btnAnimation);
                SetupHelpers.hideSoftKeyboard(getActivity());
                if (hasErrors()) {
                    checkErrors(priceEditText);
                    checkErrors(propertySizeEditText);
                    checkErrors(locationEditText);
                } else {
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    Fragment nextFragment = fm.findFragmentById(R.id.fragmentStep3);
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
                Fragment prevFragment = fm.findFragmentById(R.id.fragmentStep1);
                fm.beginTransaction().hide(thisFragment).commit();
                fm.beginTransaction().show(prevFragment).commit();
            }
        });
    }

    private void setFocusChangedListener(EditText editText) {
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                try {
                    String content  = editText.getText().toString();
                    if (!hasFocus && content.length() > 0) {
                        String cleanString = content.replaceAll(",", ".");
                        cleanString = cleanString.replaceAll("\\s", "");
                        double parsed = Double.parseDouble(cleanString);
                        if (cleanString.indexOf('.') != cleanString.lastIndexOf('.')) {
                            editText.setError("Niepoprawna wartość");
                            setNextStepEnabled(false);
                        } else if (!hasErrors()) {
                            setNextStepEnabled(true);
                        }
                        DecimalFormat formatter = (DecimalFormat) NumberFormat.getCurrencyInstance();
                        DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
                        symbols.setCurrencySymbol("");
                        formatter.setDecimalFormatSymbols(symbols);
                        String formato = formatter.format(parsed);
                        editText.setText(formato);
                    }
                }catch (Exception e) {
                    editText.setError("Niepoprawna wartość");
                    setNextStepEnabled(false);
                }
            }
        });
    }

    private void setNotEmptyValidator(EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.d("before", s.toString());
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                editText.removeTextChangedListener(this);
//
//                String cleanString = s.toString().replaceAll(",", ".");
//
////                double parsed = Double.parseDouble(cleanString);
////                String formatted = NumberFormat.getCurrencyInstance().format((parsed/100));
//                BigDecimal parsed = new BigDecimal(cleanString).setScale(2,BigDecimal.ROUND_FLOOR).divide(new BigDecimal(100),BigDecimal.ROUND_FLOOR);
//                String formato = NumberFormat.getCurrencyInstance().format(parsed);
////
//////                current = formatted;
//                editText.setText(formato);
////                editText.setSelection(formatted.length());
//
//                editText.addTextChangedListener(this);

            }

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
        return priceEditText.getText().length() == 0 || propertySizeEditText.getText().length() == 0 || locationEditText.getText().length() == 0 || priceEditText.getError() != null;
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