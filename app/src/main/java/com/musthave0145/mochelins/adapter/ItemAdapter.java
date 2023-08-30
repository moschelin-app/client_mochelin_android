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

import java.util.ArrayList;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.viewHolder> {

    Context context;
    ArrayList<String> imgUrl;

    public ItemAdapter(Context context, ArrayList<String> imgUrl) {
        this.context = context;
        this.imgUrl = imgUrl;
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
        for (int i = 0; i < imgUrl.size(); i++){
            Glide.with(context).load(imgUrl).into(holder.imageView);
        }
    }

    @Override
    public int getItemCount() {
        return imgUrl.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;


        public viewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}
