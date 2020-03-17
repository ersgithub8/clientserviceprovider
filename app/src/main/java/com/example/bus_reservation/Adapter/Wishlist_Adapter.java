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
import com.example.bus_reservation.Activity.Provider_Detail;
import com.example.bus_reservation.Constant;
import com.example.bus_reservation.Model.Dasboard_provider_model;
import com.example.bus_reservation.Model.Wishlist_model;
import com.example.bus_reservation.R;
import com.example.bus_reservation.Wishlist;
import com.example.bus_reservation.volley.CustomRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;
import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;
import static com.example.bus_reservation.Activity.Login.MY_PREFS_NAME;

public class Wishlist_Adapter  extends RecyclerView.Adapter<Wishlist_Adapter.MyViewHolder> {


    private List<Wishlist_model> modelList;
    private Context context;
    String language;
    int i=0;
    SharedPreferences preferences;
    private ArrayList<String> wishlistid,dropup;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView image,fav;
        public String wishlist_id;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.category_name);
            image = (ImageView) view.findViewById(R.id.category_img);
            wishlistid= new ArrayList<>();
            dropup= new ArrayList<>();

            fav=(ImageView)view.findViewById(R.id.addfav);

        }
    }

    public Wishlist_Adapter(List<Wishlist_model> modelList) {
        this.modelList = modelList;
    }

    @Override
    public Wishlist_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_wishlist, parent, false);

        context = parent.getContext();

        return new Wishlist_Adapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final Wishlist_Adapter.MyViewHolder holder, int position) {
        final Wishlist_model mList = modelList.get(position);



        Glide.with(context)
                .load(Constant.Base_url_provider_image + mList.getImagelocation())
                .placeholder(R.drawable.app_icon)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(holder.image);
        preferences = context.getSharedPreferences("lan", MODE_PRIVATE);
        language = preferences.getString("language", "");
        if (language.contains("english")) {
            holder.title.setText(mList.getProvider_name());
        } else {
            holder.title.setText(mList.getProvider_name());

        }
        holder.fav.setColorFilter(context.getResources().getColor(R.color.Red));

//            holder.fav.setColorFilter(context.getResources().getColor(R.color.grey));


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String providerid = mList.getProvider_id();

                Intent intent = new Intent(context, Provider_Detail.class);
                intent.putExtra("Provider_id", providerid);
                context.startActivity(intent);
            }
        });
        holder.fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    SharedPreferences sharedPreferences=context.getSharedPreferences(MY_PREFS_NAME,MODE_PRIVATE);
                    String id=sharedPreferences.getString("id",null);



                    JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, Constant.Base_url_Remove_wishlist+id+"&provider_id="+mList.getProvider_id(), new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Boolean status = null;

                            try {

                                status = response.getBoolean("response");
                                if (status) {
                                    makeText(context, "Remove From Wishlist", LENGTH_SHORT).show();
                                }

                            } catch (Exception e) {

                                makeText(context, "No Data Found", LENGTH_SHORT).show();
                            }

                        }
                    }
                            , new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            makeText(context, "Connection Error", LENGTH_LONG).show();
                        }
                    });

                    jsonRequest.setRetryPolicy(new RetryPolicy() {
                        @Override
                        public int getCurrentTimeout() {
                            return 50000;
                        }

                        @Override
                        public int getCurrentRetryCount() {
                            return 50000;
                        }

                        @Override
                        public void retry(VolleyError error) throws VolleyError {

                        }
                    });

                    RequestQueue queue = Volley.newRequestQueue(context);
                    queue.add(jsonRequest);
                    i--;
                    SharedPreferences.Editor editors = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                    editors.putString("wishlist", "0");
                }





        });
    }



    @Override
    public int getItemCount() {
        return modelList.size();
    }

}


