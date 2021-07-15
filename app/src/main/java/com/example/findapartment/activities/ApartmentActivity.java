package com.example.findapartment.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.findapartment.R;
import com.example.findapartment.adapters.SliderAdapter;
import com.example.findapartment.clients.ApartmentClient;
import com.example.findapartment.clients.IRequestCallback;
import com.example.findapartment.fragments.GalleryDialogFragment;
import com.example.findapartment.fragments.ImageGallery;
import com.example.findapartment.helpers.ToastService;
import com.example.findapartment.helpers.TransactionTypeEnum;
import com.example.findapartment.models.Apartment;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ApartmentActivity extends AppCompatActivity {

    private ApartmentClient apartmentClient;
    private String apartmentId;

    SliderAdapter mViewPagerAdapter;
    List<Bitmap> bitmaps;

    ImageGallery imageGallery;
    ProgressBar progressBar;

    Apartment apartment;

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

        progressBar = findViewById(R.id.apartmentActivityProgressBar);

        fetchApartment();
    }

    private void fetchApartment() {
        progressBar.setVisibility(View.VISIBLE);
        apartmentClient.getApartment(getApplicationContext(), apartmentId, new IRequestCallback(){
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    JSONObject data = null;
                    if (response != null) {
                        data = response.getJSONObject("data");
                        apartment = Apartment.fromJSON(data);

                        ((TextView) findViewById(R.id.priceTv)).setText(apartment.getPrice().toString() + " zł");
                        ((TextView) findViewById(R.id.propertySizeTv)).setText(Html.fromHtml(apartment.getPropertySize()+ " " + getResources().getString(R.string.m2)));
                        ((TextView) findViewById(R.id.locationTv)).setText(apartment.getLocation());

                        ((TextView) findViewById(R.id.transactionTextView)).setText(TransactionTypeEnum.getEnumValueByName(apartment.getTransactionType()));

                        SimpleDateFormat fromFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                        Date date1= fromFormat.parse(apartment.getPublicationDate());
                        SimpleDateFormat targetFormat = new SimpleDateFormat("dd.MM.yyy HH:mm");
                        String date2 = targetFormat.format(date1);

                        ((TextView) findViewById(R.id.publicationDateTv)).setText(date2);
                        ((TextView) findViewById(R.id.phoneNumberTv)).setText(apartment.getPhoneNumber());
                        ((TextView) findViewById(R.id.emailTv)).setText(apartment.getEmail());
                        ((TextView) findViewById(R.id.descriptionTv)).setText(apartment.getDescription());


                        convertToBitmapAndDisplayImages(apartment);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                } finally {
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }
            @Override
            public void onError(String result) throws Exception {
                ToastService.showErrorMessage(result, getApplicationContext());
                progressBar.setVisibility(View.INVISIBLE);
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

    public void onDeleteApartmentClick(View view) {
        progressBar.setVisibility(View.VISIBLE);
        apartmentClient.deleteApartment(apartmentId, getApplicationContext(), new IRequestCallback(){
            @Override
            public void onSuccess(JSONObject response) {
                Intent i=new Intent(getBaseContext(), ApartmentListActivity.class);
                startActivity(i);
                ToastService.showSuccessMessage("Ogłoszenie zostało usunięte", getApplicationContext());
                progressBar.setVisibility(View.INVISIBLE);
            }
            @Override
            public void onError(String result) throws Exception {
                ToastService.showErrorMessage("Nie można usunąć ogłoszenia", getApplicationContext());
                progressBar.setVisibility(View.INVISIBLE);

            }
        });
    }

    public void onEditApartmentClick(View view) {
        Intent i=new Intent(getBaseContext(), AddApartmentActivity.class);
        i.putExtra("editedApartmentId", apartment.getId());
        i.putExtra("propertySizeEditText", apartment.getPropertySize().toString());
        i.putExtra("locationEditText", apartment.getLocation());
        i.putExtra("descriptionEditText", apartment.getDescription());
        i.putExtra("priceEditText", apartment.getPrice().toString());
        i.putExtra("transactionType", apartment.getTransactionType());
        startActivity(i);
    }

}