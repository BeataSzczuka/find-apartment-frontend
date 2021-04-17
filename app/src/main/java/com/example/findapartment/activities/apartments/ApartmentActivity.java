package com.example.findapartment.activities.apartments;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.findapartment.R;
import com.example.findapartment.adapters.SliderAdapter;
import com.example.findapartment.clients.ApartmentClient;
import com.example.findapartment.clients.IRequestCallback;
import com.example.findapartment.models.Apartment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ApartmentActivity extends AppCompatActivity {

    private ApartmentClient apartmentClient;
    private String apartmentId;
    private ConstraintLayout apartmentLayout;

    private ViewPager2 mViewPager;
    SliderAdapter mViewPagerAdapter;
    LinearLayout circlesLayout;
    List<Bitmap> bitmaps;

    ImageGallery imageGallery;

    private int prevPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apartment);

        Intent intentNow = getIntent();
        apartmentId = intentNow.getStringExtra("apartmentID");
        apartmentLayout = findViewById(R.id.apartmentLayout);
        apartmentClient = new ApartmentClient();

        bitmaps = new ArrayList<Bitmap>();

        FragmentManager fm = getSupportFragmentManager();
        imageGallery = (ImageGallery) fm.findFragmentById(R.id.apartmentGalleryFragment);
        mViewPagerAdapter = new SliderAdapter(bitmaps);
        imageGallery.setImagesAdapter(mViewPagerAdapter);


//
//        mViewPager = (ViewPager2) findViewById(R.id.viewPagerImageSlider);
//        circlesLayout = (LinearLayout) findViewById(R.id.circlesLayout);
//        mViewPagerAdapter = new SliderAdapter(bitmaps);
//        mViewPager.setAdapter(mViewPagerAdapter);
//
//
//        mViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
//            @Override
//            public void onPageSelected(int position) {
//                super.onPageSelected(position);
//
//                ImageView prevCircle = (ImageView) circlesLayout.getChildAt(prevPosition);
//                prevCircle.setColorFilter(null);
//
//                ImageView circle = (ImageView) circlesLayout.getChildAt(position);
//                PorterDuffColorFilter porterDuffColorFilter = new PorterDuffColorFilter(getResources().getColor(R.color.colorPrimary),PorterDuff.Mode.MULTIPLY);
//                circle.setColorFilter(porterDuffColorFilter);
//
//                prevPosition = position;
//            }
//        });

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

//    private void showCircles(){
//        for (int i = 0; i < bitmaps.size(); i++) {
//            ImageView iv = new ImageView(this);
//            iv.setImageResource(R.drawable.circle_shadow);
//            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(70, 70);
//            lp.setMargins(0,0,20,0);
//            iv.setLayoutParams(lp);
//            circlesLayout.addView(iv);
//        }
//    }
}