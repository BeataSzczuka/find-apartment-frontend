package com.example.findapartment.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.findapartment.R;
import com.example.findapartment.models.Apartment;

import java.util.ArrayList;

public class ApartmentAdapter extends ArrayAdapter<Apartment> {
    private static class ViewHolder {
        public TextView tvTitle;
        public TextView tvPrice;
        public TextView tvPropertySize;
        public TextView tvLocation;
        public TextView tvDescription;
    }

    public ApartmentAdapter(Context context, ArrayList<Apartment> apartments) {
        super(context,0,  apartments);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Apartment apartment = getItem(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_apartment, parent, false);
            viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
            viewHolder.tvPrice = (TextView) convertView.findViewById(R.id.tvPrice);
         /*   viewHolder.tvPropertySize = (TextView) convertView.findViewById(R.id.tvPropertySize);
            viewHolder.tvLocation = (TextView) convertView.findViewById(R.id.tvLocation);
            viewHolder.tvDescription = (TextView) convertView.findViewById(R.id.tvDescription);*/
            convertView.setTag(viewHolder);
        } else {
            // Existing view
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvTitle.setText(apartment.getTitle());
        viewHolder.tvPrice.setText(Integer.toString(apartment.getPrice()));
       /* viewHolder.tvPropertySize.setText(Integer.toString(apartment.getPropertySize()));
        viewHolder.tvLocation.setText(apartment.getLocation());
        viewHolder.tvDescription.setText(apartment.getDescription());*/
        return convertView;
    }


}
