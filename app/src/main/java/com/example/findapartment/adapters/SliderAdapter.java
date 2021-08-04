package com.example.findapartment.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.example.findapartment.R;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

public class SliderAdapter extends RecyclerView.Adapter<SliderAdapter.SliderViewHolder>  {

    private List<String> sliderItems;
    private Context context;

    public SliderAdapter(List<String> sliderItems) {
        this.sliderItems = sliderItems;
    }

    @NonNull
    @Override
    public SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        return new SliderViewHolder(LayoutInflater.from(this.context).inflate(
                R.layout.view_pager_image_item,
                parent,
                false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull SliderViewHolder holder, int position) {
        if (sliderItems.size() > position) {
            holder.setImage(sliderItems.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return sliderItems.size();
    }

    public class SliderViewHolder extends RecyclerView.ViewHolder{
        private RoundedImageView imageView;


        public SliderViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.itemImageView);
        }

        void setImage(String sliderItem) {
            Glide.with(context)
                    .load(sliderItem)
                    .placeholder(new CircularProgressDrawable(context))
                    .error(R.drawable.imageplaceholder)
                    .into(imageView);

        }

//        void setPlaceholder() {
//            imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.imageplaceholder));
//        }

    }

}
