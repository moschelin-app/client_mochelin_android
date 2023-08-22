package com.musthave0145.mochelins.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.musthave0145.mochelins.R;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.viewHolder> {

    Context context;

    int[] images;

    public ItemAdapter(int[] images) {
        this.images = images;
    }

    @NonNull
    @Override
    public ItemAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_layout,
                viewGroup, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemAdapter.viewHolder holder, int position) {
        Glide.with(context).load(images[position]).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return images.length;
    }

    public class viewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;


        public viewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}
