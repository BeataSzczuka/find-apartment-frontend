package com.example.findapartment.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.example.findapartment.R;
import com.example.findapartment.adapters.UploadedImagesAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UploadedImageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UploadedImageFragment extends Fragment {

    public UploadedImageFragment() {
        // Required empty public constructor
    }

    public static UploadedImageFragment newInstance(String param1, String param2) {
        UploadedImageFragment fragment = new UploadedImageFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_uploaded_image, container, false);
    }

}