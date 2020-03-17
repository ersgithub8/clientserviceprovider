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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.bus_reservation.Activity.Provider_Activity;
import com.example.bus_reservation.Activity.Provider_Detail;
import com.example.bus_reservation.Constant;

import com.example.bus_reservation.Model.Dasboard_provider_model;
import com.example.bus_reservation.Model.Gallery_model;
import com.example.bus_reservation.Model.Wishlist_model;
import com.example.bus_reservation.R;
import com.example.bus_reservation.volley.CustomRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;
import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;
import static com.example.bus_reservation.Activity.Login.MY_PREFS_NAME;

public class Provider_adapter extends RecyclerView.Adapter<Provider_adapter.MyViewHolder> {

    private List<Dasboard_provider_model> modelList;
    private List<Wishlist_model> modelList1;
    private Context context;
    String language;
    int i=0;
    SharedPreferences preferences;
    private ArrayList<String> wishlistid,dropup;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView image;
        public String wishlist_id;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.category_name);
            image = (ImageView) view.findViewById(R.id.category_img);
            wishlistid= new ArrayList<>();
            dropup= new ArrayList<>();



        }
    }

    public Provider_adapter(List<Dasboard_provider_model> modelList) {
        this.modelList = modelList;
    }

    @Override
    public Provider_adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_dashboard_provider, parent, false);

        context = parent.getContext();

        return new Provider_adapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final Provider_adapter.MyViewHolder holder, int position) {
        final Dasboard_provider_model mList = modelList.get(position);
        SharedPreferences editors = context.getSharedPreferences(MY_PREFS_NAME,MODE_PRIVATE);
        String id=editors.getString("id",null);



        Glide.with(context)
                .load(Constant.Base_url_provider_image + mList.getImage())
                .placeholder(R.drawable.app_icon)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(holder.image);
        preferences = context.getSharedPreferences("lan", MODE_PRIVATE);
        language = preferences.getString("language", "");
        if (language.contains("english")) {
            holder.title.setText(mList.getName());
        } else {
            holder.title.setText(mList.getName());

        }


//            holder.fav.setColorFilter(context.getResources().getColor(R.color.grey));


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String providerid = mList.getProviderid();

                Intent intent = new Intent(context, Provider_Detail.class);
                intent.putExtra("Provider_id", providerid);
                context.startActivity(intent);
            }
        });

    }



    @Override
    public int getItemCount() {
        return modelList.size();
    }

}

