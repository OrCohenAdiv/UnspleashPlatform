package com.example.finalproject_seminar.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject_seminar.Activities.PhotoDetailsActivity;
import com.example.finalproject_seminar.R;
import com.kc.unsplash.models.Photo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class PhotoRecyclerViewAdapter extends RecyclerView.Adapter<PhotoRecyclerViewAdapter.ViewHolder> {
    private List<Photo> photos;
    private Context context;

    public PhotoRecyclerViewAdapter(Context context, List<Photo> photoList) {

        photos = photoList;
        this.context = context;
    }

    public PhotoRecyclerViewAdapter(Context context) {

        photos = new ArrayList<>();
        this.context = context;
    }

    @NonNull
    @Override
    public PhotoRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photo, parent, false);

        return new ViewHolder(view,context);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Photo photo = photos.get(position);

        Picasso.get().load(photo.getUrls().getSmall()).into(holder.imageView);
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageView;

        public ViewHolder(View itemView, Context ctx) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            context = ctx;

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Photo photo = photos.get(getAdapterPosition());
                    Intent intent = new Intent (context, PhotoDetailsActivity.class);
                    intent.putExtra("photo",photo);
                    context.startActivity(intent);
                }
            });

        }

        @Override
        public void onClick(View v) {

        }
    }
}
