package com.example.bus_reservation.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.bus_reservation.Adapter.Gallery_adapter;
import com.example.bus_reservation.Adapter.Provider_adapter;
import com.example.bus_reservation.Constant;
import com.example.bus_reservation.Model.Dasboard_provider_model;
import com.example.bus_reservation.Model.Gallery_model;
import com.example.bus_reservation.R;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;

public class Provider_Detail extends AppCompatActivity {
    TextView back,name,gender,age,height,haircolor,ethincity,services,tvpbgallery,tvprgallery,continue_request,tagline;
    ImageView providerimage;
    LinearLayout genderlayout,agelayout,heightlayout,haicolorlayout,ethincitylayout,serviceslayout;
    //provider detail adapter model
    private Gallery_adapter gallery_adapter;
    private List<Gallery_model> gallery_models = new ArrayList<>();
    RecyclerView rv_public_gallery,rv_private_gallery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider__detail);

        final String provider_id=getIntent().getStringExtra("Provider_id");
        back=findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        name=findViewById(R.id.name);
        providerimage=findViewById(R.id.provider_img);

        age=findViewById(R.id.Age);
        tagline=findViewById(R.id.tagline);
        continue_request=findViewById(R.id.request_booking);
        height=findViewById(R.id.height);
        haircolor=findViewById(R.id.haircolor);
        gender=findViewById(R.id.Gender);
        ethincity=findViewById(R.id.ethincity);
        services=findViewById(R.id.Services);
        heightlayout=findViewById(R.id.height_layout);
        agelayout=findViewById(R.id.age_layout);
        genderlayout=findViewById(R.id.gender_layout);
        haicolorlayout=findViewById(R.id.haircolor_layout);
        ethincitylayout=findViewById(R.id.ethincity_layout);
        serviceslayout=findViewById(R.id.services_layout);
        rv_public_gallery=findViewById(R.id.publicgallery);
        rv_private_gallery=findViewById(R.id.privategallery);
        tvpbgallery=findViewById(R.id.tv_pbgallery);
        tvprgallery=findViewById(R.id.tv_prgallery);
//        makeText(this, provider_id, LENGTH_SHORT).show();

        GridLayoutManager gridLayoutManager1 = new GridLayoutManager(this, 3);
        rv_public_gallery.setLayoutManager(gridLayoutManager1);
        rv_public_gallery.setItemAnimator(new DefaultItemAnimator());
        rv_public_gallery.setNestedScrollingEnabled(true);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        rv_private_gallery.setLayoutManager(gridLayoutManager);
        rv_private_gallery.setItemAnimator(new DefaultItemAnimator());
        rv_private_gallery.setNestedScrollingEnabled(true);

        getProvider(provider_id);

        continue_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(Provider_Detail.this,Booking_Activity.class);
                i.putExtra("Provider_id", provider_id);
                startActivity(i);
            }
        });

    }
    private void getProvider(String Prov_id) {

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, Constant.Base_url_Provider_Detail+Prov_id+"&get_single_provider=true" ,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Boolean status = null;

                try {

                    status=response.getBoolean("response");
                    if(status){
                        JSONArray jsonArray = response.getJSONArray("data");
//                        Toast.makeText(Provider_Detail.this, response.getString("data"), LENGTH_SHORT).show();
                        for (int i=0;i<jsonArray.length();i++){
                            JSONObject object= jsonArray.getJSONObject(i);

                            name.setText(object.getString("name"));
                            if(object.getString("gender").equals("")){
                                genderlayout.setVisibility(View.GONE);
                            }else{
                            gender.setText(object.getString("gender"));
                            }
                            if(object.getString("age").equals("")){
                                agelayout.setVisibility(View.GONE);
                            }else{
                                age.setText(object.getString("age"));
                            }
                            if(object.getString("height").equals("")){
                                heightlayout.setVisibility(View.GONE);
                            }else{
                                height.setText(object.getString("height"));
                            }
                            if(object.getString("hair_color").equals("")){
                                haicolorlayout.setVisibility(View.GONE);
                            }else{
                                haircolor.setText(object.getString("hair_color"));
                            }
                            if(object.getString("hair_color").equals("")){
                                haicolorlayout.setVisibility(View.GONE);
                            }else{
                                haircolor.setText(object.getString("hair_color"));
                            }
                            if(object.getString("ethnicity").equals("")){
                                ethincitylayout.setVisibility(View.GONE);
                            }else {
                                ethincity.setText(object.getString("ethnicity"));
                            }
                            if(object.getString("services").equals("")){
                                serviceslayout.setVisibility(View.GONE);
                            }else {
                                services.setText(object.getString("services"));
                            }
                            if(object.getString("tagline").equals("")){
                                tagline.setVisibility(View.GONE);
                            }else {
                                tagline.setText(object.getString("tagline"));
                            }
                            String image=Constant.Base_url_provider_image+object.getString("image");
                            Glide.with(Provider_Detail.this).load(image).into(providerimage);

                            //public gallery
                            Gson gson = new Gson();
                            Type listType = new TypeToken<List<Gallery_model>>() {
                            }.getType();
                            String gallery=response.getString("public_gallery");
                            if(gallery.equals("")){
                                tvpbgallery.setVisibility(View.GONE);
                                rv_public_gallery.setVisibility(View.GONE);

                            }else {

                                gallery_models = gson.fromJson(response.getString("public_gallery"), listType);
                                gallery_adapter = new Gallery_adapter(gallery_models);
                                rv_public_gallery.setAdapter(gallery_adapter);
                                gallery_adapter.notifyDataSetChanged();
                            }

                            String gallery1=response.getString("private_gallery");
                            if(gallery.equals("")){
                                tvprgallery.setVisibility(View.GONE);
                                rv_private_gallery.setVisibility(View.GONE);

                            }else {

                                gallery_models = gson.fromJson(response.getString("private_gallery"), listType);
                                gallery_adapter = new Gallery_adapter(gallery_models);
                                rv_private_gallery.setAdapter(gallery_adapter);
                                gallery_adapter.notifyDataSetChanged();
                            }


                        }

                    }

                }
                catch (Exception e){

                    makeText(Provider_Detail.this,"No Data Found", LENGTH_SHORT).show();
                }

            }
        }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                makeText(Provider_Detail.this, "Connection Error", LENGTH_LONG).show();
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

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jsonRequest);
    }
}
