package com.example.findapartment.helpers;

import android.util.Log;

import androidx.annotation.NonNull;

public enum TransactionTypeEnum {
    SALE("sprzedaż"), RENT("wynajem");

    private final String value;

    TransactionTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static String getEnumValueByName(String code){
        for(TransactionTypeEnum e : TransactionTypeEnum.values()){
            if (code.equals(e.name())) {
                return e.getValue();
            }
        }
        return null;
    }
}