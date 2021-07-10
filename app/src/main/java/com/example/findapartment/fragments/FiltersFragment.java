package com.example.findapartment.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;

import com.example.findapartment.R;
import com.example.findapartment.activities.ApartmentListActivity;
import com.example.findapartment.activities.FiltersActivity;
import com.example.findapartment.activities.MenuActivity;
import com.example.findapartment.helpers.TransactionTypeEnum;
import com.example.findapartment.helpers.UserSession;

public class FiltersFragment extends Fragment {

    private EditText priceFrom;
    private EditText priceTo;
    private EditText propertySizeFrom;
    private EditText propertySizeTo;
    private EditText location;
    private RadioGroup transactionType;
    private Button cancelFilteringBtn;


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
        transactionType = view.findViewById(R.id.transactionType);

        setFilterBtn(view, this);
    }

    private void setFilterBtn(View view, FiltersFragment filtersFragment){
        cancelFilteringBtn = (Button) view.findViewById(R.id.cancelFiltering);
        cancelFilteringBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().remove(filtersFragment).commit();

            }
        });
    }


    public void onFilterClick(View view) {
//        Intent i = new Intent(ApartmentListActivity.class);
//        i.putExtra("priceFrom", priceFrom.getText().toString());
//        i.putExtra("priceTo", priceTo.getText().toString());
//        i.putExtra("propertySizeFrom", propertySizeFrom.getText().toString());
//        i.putExtra("propertySizeTo", propertySizeTo.getText().toString());
//        i.putExtra("location", location.getText().toString());
//        if (transactionType.getCheckedRadioButtonId() == R.id.transactionSale) {
//            i.putExtra("transactionType", TransactionTypeEnum.SALE.name());
//        } else {
//            i.putExtra("transactionType", TransactionTypeEnum.RENT.name());
//        }
//        i.putExtra("onlyMy", onlyMy.isChecked());
//        startActivity(i);
    }
}