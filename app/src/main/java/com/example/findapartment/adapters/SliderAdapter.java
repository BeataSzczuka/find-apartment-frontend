package com.example.findapartment.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.shapes.Shape;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.findapartment.R;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;
import java.util.Objects;

public class SliderAdapter extends RecyclerView.Adapter<SliderAdapter.SliderViewHolder>  {

    private List<Bitmap> sliderItems;


    public SliderAdapter(List<Bitmap> sliderItems) {
        this.sliderItems = sliderItems;
    }

    @NonNull
    @Override
    public SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SliderViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.view_pager_image_item,
                parent,
                false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull SliderViewHolder holder, int position) {
        holder.setImage(sliderItems.get(position));
    }

    @Override
    public int getItemCount() {
        return sliderItems.size();
    }

    class SliderViewHolder extends RecyclerView.ViewHolder{
        private RoundedImageView imageView;

        public SliderViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.itemImageView);
        }

        void setImage(Bitmap sliderItem) {
            imageView.setImageBitmap(sliderItem);
        }

    }

}
