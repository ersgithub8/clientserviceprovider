package com.example.bus_reservation.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.bus_reservation.Activity.MainActivity;
import com.example.bus_reservation.Activity.Menu;
import com.example.bus_reservation.Activity.Provider_Activity;
import com.example.bus_reservation.Constant;
import com.example.bus_reservation.Dashboard;
import com.example.bus_reservation.Model.Categories_model;
import com.example.bus_reservation.R;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class Category_Adapter extends RecyclerView.Adapter<Category_Adapter.MyViewHolder> {

    private List<Categories_model> modelList;
    private Context context;
    String language;
    SharedPreferences preferences;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView image;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.category_name);
            image = (ImageView) view.findViewById(R.id.category_img);


        }
    }

    public Category_Adapter(List<Categories_model> modelList) {
        this.modelList = modelList;
    }

    @Override
    public Category_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_category, parent, false);

        context = parent.getContext();

        return new Category_Adapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(Category_Adapter.MyViewHolder holder, int position) {
        final Categories_model mList = modelList.get(position);
        Glide.with(context)
                .load(Constant.Base_url_Category_image + mList.getImage())
                .placeholder(R.drawable.app_icon)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(holder.image);
        preferences = context.getSharedPreferences("lan", MODE_PRIVATE);
        language=preferences.getString("language","");
        if (language.contains("english")) {
            holder.title.setText(mList.getName());
        }
        else {
            holder.title.setText(mList.getName());

        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               String cat_id= mList.getCatid();
                Intent i=new Intent(context, Provider_Activity.class);
                i.putExtra("catid",cat_id);
                context.startActivity(i);

            }
        });
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

}
