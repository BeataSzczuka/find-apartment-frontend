package com.example.findapartment.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.findapartment.R;
import com.example.findapartment.adapters.SliderAdapter;
import com.example.findapartment.clients.ApartmentClient;
import com.example.findapartment.clients.IRequestCallback;
import com.example.findapartment.framents.GalleryDialogFragment;
import com.example.findapartment.framents.ImageGallery;
import com.example.findapartment.models.Apartment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ApartmentActivity extends AppCompatActivity {

    private ApartmentClient apartmentClient;
    private String apartmentId;

    SliderAdapter mViewPagerAdapter;
    List<Bitmap> bitmaps;

    ImageGallery imageGallery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apartment);

        Intent intentNow = getIntent();
        apartmentId = intentNow.getStringExtra("apartmentID");
        apartmentClient = new ApartmentClient();

        bitmaps = new ArrayList<Bitmap>();

        mViewPagerAdapter = new SliderAdapter(bitmaps){
            @Override
            public void onBindViewHolder(@NonNull SliderAdapter.SliderViewHolder holder, int position) {
                super.onBindViewHolder(holder, position);
                holder.itemView.findViewById(R.id.itemImageView).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DialogFragment newFragment = GalleryDialogFragment.newInstance(mViewPagerAdapter);
                        newFragment.show(getSupportFragmentManager(), "newFragment");
                    }
                });
            }
        };

        FragmentManager fm = getSupportFragmentManager();
        imageGallery = (ImageGallery) fm.findFragmentById(R.id.apartmentGalleryFragment);
        imageGallery.setImagesAdapter(mViewPagerAdapter);

        fetchApartment();
    }

    private void fetchApartment() {
        apartmentClient.getApartment(getApplicationContext(), apartmentId, new IRequestCallback(){
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    JSONObject data = null;
                    if (response != null) {
                        data = response.getJSONObject("data");
                        final Apartment apartment = Apartment.fromJSON(data);

                        ((TextView) findViewById(R.id.priceTv)).setText(apartment.getPrice().toString() + " zł");
                        ((TextView) findViewById(R.id.propertySizeTv)).setText(apartment.getPropertySize().toString() + " m2");
                        ((TextView) findViewById(R.id.locationTv)).setText(apartment.getLocation());
                        ((TextView) findViewById(R.id.transactionTextView)).setText(apartment.getTransactionType());
                        ((TextView) findViewById(R.id.publicationDateTv)).setText(apartment.getPublicationDate());
                        ((TextView) findViewById(R.id.phoneNumberTv)).setText(apartment.getPhoneNumber());
                        ((TextView) findViewById(R.id.emailTv)).setText(apartment.getEmail());
                        ((TextView) findViewById(R.id.descriptionTv)).setText(apartment.getDescription());

                        convertToBitmapAndDisplayImages(apartment);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onError(String result) throws Exception {
                Log.e("error", result);
            }
        });
    }

    private void convertToBitmapAndDisplayImages(Apartment apartment){

        for (int i = 0; i < apartment.getImages().size(); i++) {
            byte[] bytes = apartment.getImage(i);
            Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            bitmaps.add(bm);
        }
        imageGallery.showCircles();
        mViewPagerAdapter.notifyDataSetChanged();
    }
}