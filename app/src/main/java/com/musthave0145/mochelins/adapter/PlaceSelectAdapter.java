package com.musthave0145.mochelins.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.musthave0145.mochelins.R;
import com.musthave0145.mochelins.model.PlaceSelect;

import java.util.ArrayList;

public class PlaceSelectAdapter extends RecyclerView.Adapter<PlaceSelectAdapter.ViewHolder>{

    Context context;
    ArrayList<PlaceSelect> placeSelectArrayList;

    public PlaceSelectAdapter(Context context, ArrayList<PlaceSelect> placeSelectArrayList) {
        this.context = context;
        this.placeSelectArrayList = placeSelectArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.place_select_row, parent, false);

        return new PlaceSelectAdapter.ViewHolder(view) ;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PlaceSelect placeSelect = placeSelectArrayList.get(position);


        if (placeSelect.name == null){
            holder.txtNewAddress.setText("상점명 없음");
        } else {
            holder.txtStoreName.setText(placeSelect.name);
        }

        if (placeSelect.vicinity == null) {
            holder.txtNewAddress.setText("주소 없음");
        } else {
            holder.txtNewAddress.setText(placeSelect.vicinity);
        }

    }

    @Override
    public int getItemCount() {
        return placeSelectArrayList.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView txtStoreName;
        TextView txtNewAddress;
        TextView txtOldAddress;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.cardView);
            txtStoreName = itemView.findViewById(R.id.txtStoreName);
            txtNewAddress = itemView.findViewById(R.id.txtNewAddress);
            txtOldAddress = itemView.findViewById(R.id.txtOldAddress);
        }
    }
}
