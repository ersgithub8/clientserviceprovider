package com.example.bus_reservation.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.bus_reservation.Adapter.Provider_adapter;
import com.example.bus_reservation.Constant;
import com.example.bus_reservation.Model.Dasboard_provider_model;
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

public class Provider_Activity extends AppCompatActivity {
    TextView back;
    //provider adapter model
    private Provider_adapter provider_adapter;
    private List<Dasboard_provider_model> provider_models = new ArrayList<>();
    RecyclerView rv_privider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider);
        back=findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //Provider Recycler

        String catid=getIntent().getStringExtra("catid");
        rv_privider=(RecyclerView)findViewById(R.id.rv_providers);

        GridLayoutManager gridLayoutManager1 = new GridLayoutManager(Provider_Activity.this, 2);
        rv_privider.setLayoutManager(gridLayoutManager1);
        rv_privider.setItemAnimator(new DefaultItemAnimator());
        rv_privider.setNestedScrollingEnabled(true);
        getProvider(catid);
    }
    private void getProvider(String cat_id) {

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, Constant.Base_url_ProviderBy_category+cat_id ,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Boolean status = null;

                try {

                    status=response.getBoolean("response");
                    Gson gson = new Gson();
                    Type listType = new TypeToken<List<Dasboard_provider_model>>() {
                    }.getType();

                    provider_models = gson.fromJson(response.getString("data"), listType);


                    provider_adapter = new Provider_adapter(provider_models);
                    rv_privider.setAdapter(provider_adapter);
                    provider_adapter.notifyDataSetChanged();



                }
                catch (Exception e){

                    makeText(Provider_Activity.this,"No Data Found", LENGTH_SHORT).show();
                }

            }
        }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                makeText(Provider_Activity.this, "Connection Error", LENGTH_LONG).show();
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
