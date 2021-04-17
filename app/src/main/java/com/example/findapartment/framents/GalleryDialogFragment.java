package com.example.findapartment.framents;

import android.app.Dialog;
import android.os.Bundle;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.example.findapartment.R;
import com.example.findapartment.adapters.SliderAdapter;

public class GalleryDialogFragment extends DialogFragment {

    public static SliderAdapter mViewPagerAdapter;

    public static GalleryDialogFragment newInstance(SliderAdapter sliderAdapter) {
        GalleryDialogFragment frag = new GalleryDialogFragment();
        mViewPagerAdapter = sliderAdapter;
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), android.R.style.Theme_Material_Light_NoActionBar_Fullscreen);
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.dialog_fragment_gallery, null, false);


        FragmentManager fm = getParentFragmentManager();
        ImageGallery imageGallery = (ImageGallery) fm.findFragmentById(R.id.galleryDialogFragment);
        imageGallery.setImagesAdapter(mViewPagerAdapter);
        imageGallery.showCircles();

        builder.setView(view);
        Dialog dialog = builder.create();
        return dialog;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ImageGallery f = (ImageGallery) getParentFragmentManager()
                .findFragmentById(R.id.galleryDialogFragment);
        if (f != null)
            getParentFragmentManager().beginTransaction().remove(f).commit();
    }
}
