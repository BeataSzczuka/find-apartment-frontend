package com.example.findapartment.fragments;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.example.findapartment.R;
import com.example.findapartment.activities.ApartmentListActivity;
import com.example.findapartment.helpers.SetupHelpers;

public class FiltersFragment extends Fragment {

    private EditText priceFrom;
    private EditText priceTo;
    private EditText propertySizeFrom;
    private EditText propertySizeTo;
    private EditText location;
    private CheckBox transactionSale;
    private CheckBox transactionRent;
    private Button cancelFilteringBtn;
    private Button filterBtn;

    private LinearLayout noResultsTryAgain;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_filters, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        priceFrom = view.findViewById(R.id.priceFrom);
        priceTo = view.findViewById(R.id.priceTo);
        propertySizeFrom = view.findViewById(R.id.propertySizeFrom);
        propertySizeTo = view.findViewById(R.id.propertySizeTo);
        location = view.findViewById(R.id.location);
        transactionSale = view.findViewById(R.id.transactionSale);
        transactionRent = view.findViewById(R.id.transactionRent);

        noResultsTryAgain = view.findViewById(R.id.noResultsTryAgain);

        setFilterBtn(view, this);
        setValidators();

        SetupHelpers.setKeyboardHideListener(getActivity());
    }

    private void setFilterBtn(View view, FiltersFragment filtersFragment){
        cancelFilteringBtn = (Button) view.findViewById(R.id.cancelFiltering);
        cancelFilteringBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetupHelpers.hideSoftKeyboard(getActivity());
                getActivity().getSupportFragmentManager().beginTransaction().remove(filtersFragment).commit();

            }
        });

        filterBtn = (Button) view.findViewById(R.id.filterBtn);
        filterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetupHelpers.hideSoftKeyboard(getActivity());
                Uri.Builder builder = new Uri.Builder();
                if (priceFrom.getText().toString().length() > 0) builder.appendQueryParameter("priceFrom", priceFrom.getText().toString());
                if (priceTo.getText().toString().length() > 0) builder.appendQueryParameter("priceTo", priceTo.getText().toString());
                if (propertySizeFrom.getText().toString().length() > 0) builder.appendQueryParameter("propertySizeFrom", propertySizeFrom.getText().toString());
                if (propertySizeTo.getText().toString().length() > 0) builder.appendQueryParameter("propertySizeTo", propertySizeTo.getText().toString());
                if (location.getText().toString().length() > 0) builder.appendQueryParameter("location", location.getText().toString());
                if (transactionSale.isChecked() && !transactionRent.isChecked()) builder.appendQueryParameter("transactionType", "SALE");
                if (transactionRent.isChecked() && !transactionSale.isChecked()) builder.appendQueryParameter("transactionType", "RENT");
                String queryParams =  builder.build().toString();

                ((ApartmentListActivity) getActivity()).setFilters(queryParams);

            }
        });

    }

    private void setValidators() {
        setNumberFieldValidator();
        setTransactionTypeValidators();
    }


    private boolean isNotEmpty(EditText editText) {
        return editText.getText().toString().trim().length() > 0;
    }

    private int getNumber(EditText editText) {
        return Integer.parseInt(editText.getText().toString());
    }

    private void setNumberFieldValidator() {
        TextWatcher proceTW = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                if (isNotEmpty(priceTo) && isNotEmpty(priceFrom) && getNumber(priceTo) < getNumber(priceFrom)){
                    priceFrom.setError("Niepoprawny zakres liczb");
                    priceTo.setError("Niepoprawny zakres liczb");
                    filterBtn.setEnabled(false);
                } else {
                    priceFrom.setError(null);
                    priceTo.setError(null);
                    if (propertySizeFrom.getError() == null) {
                        filterBtn.setEnabled(true);
                    }
                }

            }
        };
        priceFrom.addTextChangedListener( proceTW );
        priceTo.addTextChangedListener ( proceTW );

        TextWatcher propertySizeTW = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                if (isNotEmpty(propertySizeTo) && isNotEmpty(propertySizeFrom) && getNumber(propertySizeTo) < getNumber(propertySizeFrom)){
                    propertySizeFrom.setError("Niepoprawny zakres liczb");
                    propertySizeTo.setError("Niepoprawny zakres liczb");
                    filterBtn.setEnabled(false);
                } else {
                    propertySizeFrom.setError(null);
                    propertySizeTo.setError(null);
                    if (priceFrom.getError() == null) {
                        filterBtn.setEnabled(true);
                    }
                }

            }
        };
        propertySizeFrom.addTextChangedListener( propertySizeTW );
        propertySizeTo.addTextChangedListener ( propertySizeTW );
    }

    public void setTransactionTypeValidators() {
        transactionRent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                if (!isChecked && !transactionSale.isChecked()) {
                    transactionSale.setChecked(true);
                }
            }
        });

        transactionSale.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                if (!isChecked && !transactionRent.isChecked()) {
                    transactionRent.setChecked(true);
                }
            }
        });

    }

    public void showNoResultsMessage(boolean visible) {
        if (noResultsTryAgain != null) {
            if (visible) {
                noResultsTryAgain.setVisibility(View.VISIBLE);
            } else {
                noResultsTryAgain.setVisibility(View.GONE);
            }
        }
    }
}