package com.example.findapartment.fragments;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import com.example.findapartment.clients.ApartmentClient;
import com.example.findapartment.clients.IRequestCallback;
import com.example.findapartment.helpers.SetupHelpers;
import com.example.findapartment.helpers.SortTypesEnum;
import com.example.findapartment.helpers.ToastService;
import com.example.findapartment.models.Apartment;
import com.example.findapartment.models.FilterRanges;
import com.google.android.material.slider.RangeSlider;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

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

    private RangeSlider priceRangeSlider;
    private RangeSlider propertySizeRangeSlider;

    private LinearLayout noResultsTryAgain;

    private ApartmentClient apartmentClient;

    private boolean rangesUploaded = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        apartmentClient = new ApartmentClient();
        getFilterRanges();
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

        priceRangeSlider = view.findViewById(R.id.priceRangeSlider);
        propertySizeRangeSlider = view.findViewById(R.id.propertySizeRangeSlider);
        setRangeSliderListeners();

        noResultsTryAgain = view.findViewById(R.id.noResultsTryAgain);

        setFocusChangeListener(priceFrom);
        setFocusChangeListener(priceTo);
        setFocusChangeListener(propertySizeFrom);
        setFocusChangeListener(propertySizeTo);

        setFilterBtn(view, this);
        setValidators();

        SetupHelpers.setKeyboardHideListener(getActivity());
    }

    private String formatAsNumber(double number) {
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getCurrencyInstance();
        DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
        symbols.setCurrencySymbol("");
        formatter.setDecimalFormatSymbols(symbols);
        return formatter.format(number);
    }

    private void setFocusChangeListener(EditText editText) {
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
                            filterBtn.setEnabled(false);
                        } else {
                            filterBtn.setEnabled(true);
                        }

                        editText.setText(formatAsNumber(parsed));
                    }
                }catch (Exception e) {
                    editText.setError("Niepoprawna wartość");
                    filterBtn.setEnabled(false);
                }
            }
        });
    }

    private void setRangeSliderListeners() {
        priceRangeSlider.addOnChangeListener(new RangeSlider.OnChangeListener() {
            @Override
            public void onValueChange(RangeSlider slider, float value, boolean fromUser) {
                if ( fromUser ) {
                    List<Float> values = slider.getValues();

                    DecimalFormat formatter = (DecimalFormat) NumberFormat.getCurrencyInstance();
                    DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
                    symbols.setCurrencySymbol("");
                    formatter.setDecimalFormatSymbols(symbols);
                    priceFrom.setText(formatter.format(values.get(0)));
                    priceTo.setText(formatter.format(values.get(1)));
                }
            }
        });
        propertySizeRangeSlider.addOnChangeListener(new RangeSlider.OnChangeListener() {
            @Override
            public void onValueChange(RangeSlider slider, float value, boolean fromUser) {
                if ( fromUser ) {
                    List<Float> values = slider.getValues();

                    DecimalFormat formatter = (DecimalFormat) NumberFormat.getCurrencyInstance();
                    DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
                    symbols.setCurrencySymbol("");
                    formatter.setDecimalFormatSymbols(symbols);
                    propertySizeFrom.setText(formatter.format(values.get(0)));
                    propertySizeTo.setText(formatter.format(values.get(1)));
                }
            }
        });
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
                if (priceFrom.getText().toString().length() > 0) builder.appendQueryParameter("priceFrom", parseFloatValue(priceFrom));
                if (priceTo.getText().toString().length() > 0) builder.appendQueryParameter("priceTo", parseFloatValue(priceTo));
                if (propertySizeFrom.getText().toString().length() > 0) builder.appendQueryParameter("propertySizeFrom", parseFloatValue(propertySizeFrom));
                if (propertySizeTo.getText().toString().length() > 0) builder.appendQueryParameter("propertySizeTo", parseFloatValue(propertySizeTo));
                if (location.getText().toString().length() > 0) builder.appendQueryParameter("location", location.getText().toString());
                if (transactionSale.isChecked() && !transactionRent.isChecked()) builder.appendQueryParameter("transactionType", "SALE");
                if (transactionRent.isChecked() && !transactionSale.isChecked()) builder.appendQueryParameter("transactionType", "RENT");
                String queryParams =  builder.build().toString();

                ((ApartmentListActivity) getActivity()).setFilters(queryParams);

            }
        });

    }

    private String parseFloatValue(EditText editText) {
        String value = editText.getText().toString();
        value = value.replaceAll(",", ".");
        value = value.replaceAll("\\s", "");
        return value;
    }

    private void setValidators() {
        setNumberFieldValidator();
        setTransactionTypeValidators();
    }


    private boolean isNotEmpty(EditText editText) {
        String text = editText.getText().toString().trim();
        return text.length() > 0 && Character.isDigit(text.charAt(0));
    }

    private float getNumber(EditText editText) {
        String text = editText.getText().toString();
        text = text.replaceAll("\\s", "");
        text = text.replaceAll(",", ".");
        return Float.parseFloat(text);
    }


    private void setNumberFieldValidator() {
        TextWatcher priceTW = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 0 && !Character.isDigit(s.toString().charAt(0))) {
                    filterBtn.setEnabled(false);
                }
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


                    List<Float> values = priceRangeSlider.getValues();
                    if ( isNotEmpty(priceFrom) ) {
                        float fromValue = getNumber(priceFrom);
                        if (fromValue < priceRangeSlider.getValueFrom())  fromValue = priceRangeSlider.getValueFrom();
                        values.set(0, fromValue);
                    }
                    if ( isNotEmpty(priceTo) ) {
                        float toValue = getNumber(priceTo);
                        if (toValue > priceRangeSlider.getValueTo())  toValue = priceRangeSlider.getValueTo();
                        values.set(1, toValue);
                    }
                    priceRangeSlider.setValues( values );

                }
            }
        };
        priceFrom.addTextChangedListener( priceTW );
        priceTo.addTextChangedListener( priceTW );

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




                    List<Float> values = propertySizeRangeSlider.getValues();
                    if ( isNotEmpty(propertySizeFrom) ) {
                        float fromValue = getNumber(propertySizeFrom);
                        if (fromValue < propertySizeRangeSlider.getValueFrom())  fromValue = propertySizeRangeSlider.getValueFrom();
                        values.set(0, fromValue);
                    }
                    if ( isNotEmpty(propertySizeTo) ) {
                        float toValue = getNumber(propertySizeTo);
                        if (toValue > propertySizeRangeSlider.getValueTo())  toValue = propertySizeRangeSlider.getValueTo();
                        values.set(1, toValue);
                    }
                    propertySizeRangeSlider.setValues( values );
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

    private void getFilterRanges() {
        if (!rangesUploaded) {
            apartmentClient.getFilterRanges(getActivity().getApplicationContext(), new IRequestCallback() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        JSONObject data = null;
                        if (response != null) {
                            data = response.getJSONObject("data");
                            FilterRanges filterRanges = FilterRanges.fromJSON(data);
                            priceRangeSlider.setValueFrom(filterRanges.priceFrom);
                            if (filterRanges.priceFrom == filterRanges.priceTo) {
                                priceRangeSlider.setValueTo(filterRanges.priceTo + 1);
                            } else {
                                priceRangeSlider.setValueTo(filterRanges.priceTo);
                            }
                            priceRangeSlider.setValues(filterRanges.priceFrom, filterRanges.priceTo);
                            priceFrom.setText(String.valueOf(formatAsNumber(filterRanges.priceFrom)));
                            priceTo.setText(String.valueOf(formatAsNumber(filterRanges.priceTo)));


                            propertySizeRangeSlider.setValueFrom(filterRanges.propertySizeFrom);
                            if (filterRanges.propertySizeFrom == filterRanges.propertySizeTo) {
                                propertySizeRangeSlider.setValueTo(filterRanges.propertySizeTo + 1);
                            } else {
                                propertySizeRangeSlider.setValueTo(filterRanges.propertySizeTo);
                            }
                            propertySizeRangeSlider.setValues(filterRanges.propertySizeFrom, filterRanges.propertySizeTo);
                            propertySizeFrom.setText(String.valueOf(formatAsNumber(filterRanges.propertySizeFrom)));
                            propertySizeTo.setText(String.valueOf(formatAsNumber(filterRanges.propertySizeTo)));
                            rangesUploaded = true;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(String result) throws Exception {
                    Log.d("ranges error", result);
                }
            });
        }
    }
}