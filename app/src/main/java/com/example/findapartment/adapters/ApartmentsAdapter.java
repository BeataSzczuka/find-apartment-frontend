package com.example.findapartment.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.appcompat.view.menu.MenuBuilder;

import com.example.findapartment.R;
import com.example.findapartment.activities.AddApartmentActivity;
import com.example.findapartment.activities.ApartmentActivity;
import com.example.findapartment.activities.ApartmentListActivity;
import com.example.findapartment.activities.MyAccountActivity;
import com.example.findapartment.clients.IRequestCallback;
import com.example.findapartment.helpers.ToastService;
import com.example.findapartment.models.Apartment;

import org.json.JSONObject;

import java.util.ArrayList;

public class ApartmentsAdapter extends ArrayAdapter<Apartment> {

    private Context c;
    private @LayoutRes int layout;

    private static class ViewHolder {
        public TextView tvLocation;
        public TextView tvPrice;
        public TextView tvTransactionType;
        public TextView tvPropertySize;
        public ImageView ivImage;
    }

    public ApartmentsAdapter(Context context, ArrayList<Apartment> apartments, @LayoutRes int resource) {
        super(context,0,  apartments);
        this.c = context;
        this.layout = resource;
    }

    private void openApartmentDetails(Apartment apartment) {
        Intent intent = new Intent(c, ApartmentActivity.class);
        intent.putExtra("apartmentID", apartment.getId());
        c.startActivity(intent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Apartment apartment = getItem(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout, parent, false);
            viewHolder.tvLocation = (TextView) convertView.findViewById(R.id.tvLocation);
            viewHolder.tvPrice = (TextView) convertView.findViewById(R.id.tvPrice);
            viewHolder.tvTransactionType = (TextView) convertView.findViewById(R.id.tvTransactionType);
            viewHolder.tvPropertySize = (TextView) convertView.findViewById(R.id.tvPropertySize);
            viewHolder.ivImage = (ImageView) convertView.findViewById(R.id.ivImage);
            convertView.setTag(viewHolder);
        } else {
            // Existing view
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvPrice.setText(Integer.toString(apartment.getPrice()) + " zł");
        viewHolder.tvLocation.setText(apartment.getLocation());
        viewHolder.tvTransactionType.setText(apartment.getTransactionType().equals("SALE") ? "sprzedaż" : "wynajem");
        viewHolder.tvPropertySize.setText(Html.fromHtml(apartment.getPropertySize()+ " " + c.getResources().getString(R.string.m2)) );

        if (apartment.getImages().size() > 0) {
            byte[] bytes = apartment.getImage(0);
            Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            viewHolder.ivImage.setImageBitmap(bm);
        }

        RelativeLayout item = (RelativeLayout) convertView.findViewById(R.id.apartmentLayout);
        if (item != null) {
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openApartmentDetails(apartment);
                }
            });
        }

        if (c instanceof MyAccountActivity) {

            ImageButton moreActionsButton = (ImageButton) convertView.findViewById(R.id.moreActionsBtn);
            if (moreActionsButton != null) {
                moreActionsButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PopupMenu popup = new PopupMenu(c, v);
                        MenuInflater inflater = popup.getMenuInflater();
                        inflater.inflate(R.menu.manage_announcement, popup.getMenu());
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                if (item.getTitle().equals("zobacz ogłoszenie")) {
                                    openApartmentDetails(apartment);
                                } else if (item.getTitle().equals("edytuj ogłoszenie")) {
                                    onEditApartmentClick(apartment);
                                } else if (item.getTitle().equals("usuń ogłoszenie")) {
                                    ((MyAccountActivity) c).onDeleteApartmentClick(apartment.getId());
                                }
                                return true;
                            }
                        });

                        popup.show();
                    }
                });
            }
        }

        return convertView;
    }

    private void onEditApartmentClick(Apartment apartment) {
        Intent i=new Intent(c, AddApartmentActivity.class);
        i.putExtra("editedApartmentId", apartment.getId());
        i.putExtra("propertySizeEditText", apartment.getPropertySize().toString());
        i.putExtra("locationEditText", apartment.getLocation());
        i.putExtra("descriptionEditText", apartment.getDescription());
        i.putExtra("priceEditText", apartment.getPrice().toString());
        i.putExtra("transactionType", apartment.getTransactionType());
        c.startActivity(i);
    }
}
