package com.example.findapartment.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.findapartment.R;
import com.example.findapartment.activities.apartments.ApartmentActivity;
import com.example.findapartment.models.Apartment;

import java.util.ArrayList;

public class ApartmentsAdapter extends ArrayAdapter<Apartment> {

    private Context c;
    private static class ViewHolder {
        public TextView tvTitle;
        public TextView tvPrice;
        public TextView tvPropertySize;
        public TextView tvLocation;
        public TextView tvDescription;
    }

    public ApartmentsAdapter(Context context, ArrayList<Apartment> apartments) {
        super(context,0,  apartments);
        this.c = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Apartment apartment = getItem(position);
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
        viewHolder.tvPrice.setText(Integer.toString(apartment.getPrice()));
       /* viewHolder.tvPropertySize.setText(Integer.toString(apartment.getPropertySize()));
        viewHolder.tvLocation.setText(apartment.getLocation());
        viewHolder.tvDescription.setText(apartment.getDescription());*/


        RelativeLayout employeeItem = (RelativeLayout) convertView.findViewById(R.id.apartmentLayout);
        employeeItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(c, ApartmentActivity.class);
                intent.putExtra("apartmentID", apartment.getId());
                c.startActivity(intent);

            }
        });

        return convertView;
    }


}
