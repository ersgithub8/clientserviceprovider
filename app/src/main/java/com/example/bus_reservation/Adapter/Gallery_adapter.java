package com.example.bus_reservation.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.bus_reservation.Activity.Provider_Activity;
import com.example.bus_reservation.Constant;
import com.example.bus_reservation.Model.Categories_model;
import com.example.bus_reservation.Model.Gallery_model;
import com.example.bus_reservation.R;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class Gallery_adapter  extends RecyclerView.Adapter<Gallery_adapter.MyViewHolder> {

    private List<Gallery_model> modelList;
    private Context context;


    public class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView image;

        public MyViewHolder(View view) {
            super(view);

            image = (ImageView) view.findViewById(R.id.category_img);


        }
    }

    public Gallery_adapter(List<Gallery_model> modelList) {
        this.modelList = modelList;
    }

    @Override
    public Gallery_adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_gallery, parent, false);

        context = parent.getContext();

        return new Gallery_adapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(Gallery_adapter.MyViewHolder holder, int position) {
        final Gallery_model mList = modelList.get(position);
        Glide.with(context)
                .load(Constant.Base_url_provider_image + mList.getImage())
                .placeholder(R.drawable.app_icon)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(holder.image);



    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

}
