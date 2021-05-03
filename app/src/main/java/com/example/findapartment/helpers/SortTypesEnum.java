package com.example.findapartment.helpers;

import androidx.annotation.NonNull;

import java.util.List;

public enum SortTypesEnum {
    NEWEST("Od najnowyszych"), PRICE_ASC("Cena rosnąco"), PRICE_DESC("Cena malejąco");

    private final String value;

    SortTypesEnum(String value) {
        this.value = value;
    }

    @NonNull
    @Override
    public String toString() {
        return value;
    }
}