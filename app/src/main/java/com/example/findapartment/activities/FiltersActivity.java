package com.example.findapartment.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.example.findapartment.R;
import com.example.findapartment.helpers.TransactionTypeEnum;
import com.example.findapartment.helpers.UserSession;

public class FiltersActivity extends AppCompatActivity {

    private EditText priceFrom;
    private EditText priceTo;
    private EditText propertySizeFrom;
    private EditText propertySizeTo;
    private EditText location;
    private RadioGroup transactionType;
    private CheckBox onlyMy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filters);

        priceFrom = findViewById(R.id.priceFrom);
        priceTo = findViewById(R.id.priceTo);
        propertySizeFrom = findViewById(R.id.propertySizeFrom);
        propertySizeTo = findViewById(R.id.propertySizeTo);
        location = findViewById(R.id.location);
        transactionType = findViewById(R.id.transactionType);
        onlyMy = findViewById(R.id.onlyMy);

        UserSession userSession = new UserSession(FiltersActivity.this);
        if (!userSession.isLoggedIn()) {
            onlyMy.setVisibility(View.GONE);
        }
    }

    public void onFilterClick(View view) {
        Intent i = new Intent(getBaseContext(), ApartmentListActivity.class);
        i.putExtra("priceFrom", priceFrom.getText().toString());
        i.putExtra("priceTo", priceTo.getText().toString());
        i.putExtra("propertySizeFrom", propertySizeFrom.getText().toString());
        i.putExtra("propertySizeTo", propertySizeTo.getText().toString());
        i.putExtra("location", location.getText().toString());
        if (transactionType.getCheckedRadioButtonId() == R.id.transactionSale) {
            i.putExtra("transactionType", TransactionTypeEnum.SALE.name());
        } else {
            i.putExtra("transactionType", TransactionTypeEnum.RENT.name());
        }
        i.putExtra("onlyMy", onlyMy.isChecked());
        startActivity(i);
    }
}