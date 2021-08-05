package com.example.findapartment.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.findapartment.R;

import java.util.ArrayList;

public class UploadedImagesAdapter extends ArrayAdapter<Uri> {

    private Context c;
    private ArrayList<Uri> uris;

    private static class ViewHolder {
        public ImageView uploadedImageIV;
        public ProgressBar imageProgressBar;
    }

    public UploadedImagesAdapter(Context context, ArrayList<Uri> uris) {
        super(context,0,  uris);
        this.c = context;
        this.uris = uris;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Uri uri = getItem(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.fragment_uploaded_image, parent, false);
            viewHolder.uploadedImageIV = (ImageView) convertView.findViewById(R.id.uploadedImageIV);
            viewHolder.imageProgressBar = (ProgressBar) convertView.findViewById(R.id.imageProgressBar);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.imageProgressBar.setVisibility(View.VISIBLE);
        Glide.with(c)
                .load(uri)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        viewHolder.imageProgressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        viewHolder.imageProgressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .error(R.drawable.imageplaceholder)
                .into(viewHolder.uploadedImageIV);

        return convertView;
    }


}
