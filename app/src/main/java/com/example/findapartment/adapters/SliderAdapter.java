package com.example.findapartment.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.findapartment.R;
import com.example.findapartment.activities.ApartmentActivity;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

public class SliderAdapter extends RecyclerView.Adapter<SliderAdapter.SliderViewHolder>  {

    private List<String> sliderItems;
    public ProgressBar imageProgressBar;
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
            if (context instanceof ApartmentActivity && !((ApartmentActivity) context).isGalleryDialogOpened()) {
                holder.setApartmentImage(sliderItems.get(position));
            } else {
                holder.setGalleryDialogImage(sliderItems.get(position));
            }

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
            imageProgressBar = (ProgressBar) itemView.findViewById(R.id.imageProgressBar);
        }

        void setApartmentImage(String sliderItem) {
            imageProgressBar.setVisibility(View.VISIBLE);
            Glide.with(context)
                    .load(sliderItem)
                    .optionalCenterCrop()
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            imageProgressBar.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            imageProgressBar.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .error(R.drawable.imageplaceholder)
                    .into(imageView);

        }

        void setGalleryDialogImage(String sliderItem) {
            imageProgressBar.setVisibility(View.VISIBLE);
            Glide.with(context)
                    .load(sliderItem)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            imageProgressBar.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            imageProgressBar.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .error(R.drawable.imageplaceholder)
                    .into(imageView);

        }
    }

}
