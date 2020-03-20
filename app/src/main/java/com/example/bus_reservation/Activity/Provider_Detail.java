package com.example.bus_reservation.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.example.bus_reservation.Adapter.Message_Adapter;
import com.example.bus_reservation.Adapter.Provider_adapter;
import com.example.bus_reservation.Constant;
import com.example.bus_reservation.Model.Dasboard_provider_model;
import com.example.bus_reservation.Model.Gallery_model;
import com.example.bus_reservation.R;
import com.example.bus_reservation.volley.CustomRequest;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.skydoves.elasticviews.ElasticButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;

public class Provider_Detail extends AppCompatActivity {
    TextView back,name,gender,age,height,haircolor,ethincity,services,tvpbgallery,tvprgallery,continue_request,tagline;
    ImageView providerimage,fav;
    int j=0;
    LinearLayout genderlayout,agelayout,heightlayout,haicolorlayout,ethincitylayout,serviceslayout;
    //provider detail adapter model
    private Gallery_adapter gallery_adapter;
    private List<Gallery_model> gallery_models = new ArrayList<>();
    RecyclerView rv_public_gallery,rv_private_gallery;
    String image1;
    ElasticButton chat;
    String provider_id;
    SharedPreferences sharedPreferences;
    ArrayList<String> list=new ArrayList<String>();
    String client_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider__detail);

         provider_id=getIntent().getStringExtra("Provider_id");
        back=findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        name=findViewById(R.id.name);
        providerimage=findViewById(R.id.provider_img);
        chat=findViewById(R.id.chat);
        age=findViewById(R.id.Age);
        fav=findViewById(R.id.favorite);
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
        sharedPreferences=getSharedPreferences("MyPrefsFile",MODE_PRIVATE);
        client_id=sharedPreferences.getString("id",null);
        GridLayoutManager gridLayoutManager1 = new GridLayoutManager(this, 3);
        rv_public_gallery.setLayoutManager(gridLayoutManager1);
        rv_public_gallery.setItemAnimator(new DefaultItemAnimator());
        rv_public_gallery.setNestedScrollingEnabled(true);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        rv_private_gallery.setLayoutManager(gridLayoutManager);
        rv_private_gallery.setItemAnimator(new DefaultItemAnimator());
        rv_private_gallery.setNestedScrollingEnabled(true);

        getProvider(provider_id);
        getWstatus(client_id);

        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Provider_Detail.this, MessageActivity.class);
                i.putExtra("pid", provider_id);
                i.putExtra("name",name.getText().toString());
                i.putExtra("image",image1);
                startActivity(i);
            }
        });


        fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(j==1){
                    removewl(client_id,provider_id);
                }else{
                    addwl(client_id,provider_id);
                }
            }


        });
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
                            image1=image;
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




    private void getWstatus(String Prov_id) {
        final android.app.AlertDialog loading = new ProgressDialog(Provider_Detail.this);
        loading.setMessage("Loading...");
        loading.setCancelable(false);
        loading.show();
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, Constant.Base_url_Provider_Detail1+Prov_id ,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Boolean status = null;

                try {

                    status=response.getBoolean("wishlist_response");
                    if(status){
                        loading.cancel();
                        JSONArray jsonArray = response.getJSONArray("wishlist");
//                        Toast.makeText(Provider_Detail.this, response.getString("data"), LENGTH_SHORT).show();
                        for (int i=0;i<jsonArray.length();i++){
                            JSONObject object= jsonArray.getJSONObject(i);

//                            list.add(object.getString("provider_id"));
                            if(object.getString("provider_id").equals(provider_id)){
                                fav.setImageResource(R.drawable.fav);
                                j=1;
                            }


                        }



                    }else {
                        loading.cancel();
                        makeText(Provider_Detail.this, "Something went wrong.", LENGTH_SHORT).show();
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



    public void addwl(String c_id,String p_id){

        final android.app.AlertDialog loading = new ProgressDialog(Provider_Detail.this);
        loading.setMessage("Checking...");
        loading.show();

        Map<String, String> params = new Hashtable<String, String>();
        params.put("add_wishlist", "true");
        params.put("client_id", c_id);
        params.put("provider_id", p_id);

        CustomRequest jsonRequest = new CustomRequest(Request.Method.POST, Constant.addwl,params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {

                    Boolean status = response.getBoolean("response");

                    String data=response.getString("data");
                    if (status){
                        loading.dismiss();
                        j=1;
                        fav.setImageResource(R.drawable.fav);
                    }
                    else {
                        loading.dismiss();
                        String error = response.getString("data");
                        j=0;
                        fav.setImageResource(R.drawable.ic_favorite_black_24dp);
                        Toast.makeText(Provider_Detail.this,error, LENGTH_SHORT).show();
                    }

                } catch (JSONException error) {
                    loading.dismiss();
                    Toast.makeText(Provider_Detail.this,"Something Went Wrong", LENGTH_SHORT).show();
                }

            }
        }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                makeText(getApplicationContext(), "Something went wrong" + error, LENGTH_LONG).show();
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

//        MySingleton.getInstance(this).addToRequestQueue(jsonRequest);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jsonRequest);

    }


    private void removewl(String client_id, String Prov_id) {


        final android.app.AlertDialog loading = new ProgressDialog(Provider_Detail.this);
        loading.setMessage("Loading...");
        loading.setCancelable(false);
        loading.show();
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, Constant.removewl
                +client_id+"&provider_id="+Prov_id ,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Boolean status = null;

                try {

                    status=response.getBoolean("response");
                    if(status){
                        loading.cancel();

                        fav.setImageResource(R.drawable.ic_favorite_black_24dp);
                        j=0;
                    }else {
                        loading.cancel();
                        fav.setImageResource(R.drawable.fav);
                        j=1;
                        makeText(Provider_Detail.this, "Something went wrong.", LENGTH_SHORT).show();
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
