package com.example.findapartment.framents;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.findapartment.R;
import com.example.findapartment.activities.MenuActivity;
import com.example.findapartment.adapters.SliderAdapter;

import java.util.ArrayList;
import java.util.List;


public class ImageGallery extends Fragment {
    private ViewPager2 mViewPager;
    private SliderAdapter mViewPagerAdapter;
    private LinearLayout circlesLayout;

    private int prevPosition = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_image_gallery, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mViewPager = (ViewPager2) view.findViewById(R.id.viewPagerImageSlider);
        circlesLayout = (LinearLayout) view.findViewById(R.id.circlesLayout);
        super.onViewCreated(view, savedInstanceState);
    }

    public void setImagesAdapter(SliderAdapter sliderAdapter){
        mViewPagerAdapter = sliderAdapter;
        mViewPager.setAdapter(mViewPagerAdapter);

        mViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                if (circlesLayout.getChildCount() > 0) {
                    ImageView prevCircle = (ImageView) circlesLayout.getChildAt(prevPosition);
                    prevCircle.setColorFilter(null);

                    ImageView circle = (ImageView) circlesLayout.getChildAt(position);
                    PorterDuffColorFilter porterDuffColorFilter = new PorterDuffColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.MULTIPLY);
                    circle.setColorFilter(porterDuffColorFilter);

                    prevPosition = position;
                }
            }
        });
    }

    public void showCircles(){
        for (int i = 0; i < mViewPagerAdapter.getItemCount(); i++) {
            ImageView iv = new ImageView(getActivity());
            iv.setImageResource(R.drawable.circle_shadow);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(70, 70);
            lp.setMargins(0,0,20,0);
            iv.setLayoutParams(lp);
            circlesLayout.addView(iv);
        }
    }

}