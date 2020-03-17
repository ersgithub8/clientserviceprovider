package com.example.bus_reservation.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.bus_reservation.Adapter.Category_Adapter;
import com.example.bus_reservation.Constant;
import com.example.bus_reservation.Model.Categories_model;
import com.example.bus_reservation.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;

public class All_categories extends AppCompatActivity {
    RecyclerView rv_cat;

    //category adapter/model
    private Category_Adapter master_adapter;
    private List<Categories_model> master_models = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_categories);
        //Category Recycler
        rv_cat=(RecyclerView) findViewById(R.id.rv_categories);

        GridLayoutManager gridLayoutManager1 = new GridLayoutManager(this, 2);
        rv_cat.setLayoutManager(gridLayoutManager1);
        rv_cat.setItemAnimator(new DefaultItemAnimator());
        getData();
    }
    //Get Category
    private void getData() {

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, Constant.Base_url_category, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Boolean status = null;

                try {

                    status=response.getBoolean("response");
                    //  makeText(getActivity(), String.valueOf(status), LENGTH_SHORT).show();
                    Gson gson = new Gson();
                    Type listType = new TypeToken<List<Categories_model>>() {
                    }.getType();

                    master_models = gson.fromJson(response.getString("data"), listType);


                    master_adapter = new Category_Adapter(master_models);
                    rv_cat.setAdapter(master_adapter);
                    master_adapter.notifyDataSetChanged();



                }
                catch (Exception e){

                    makeText(All_categories.this,"No Data Found", LENGTH_SHORT).show();
                }

            }
        }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                makeText(All_categories.this, "Connection Error", LENGTH_LONG).show();
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
}}
