package com.example.bus_reservation;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.bus_reservation.Activity.Provider_Activity;
import com.example.bus_reservation.Adapter.Provider_adapter;
import com.example.bus_reservation.Model.Dasboard_provider_model;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;
import static com.example.bus_reservation.Activity.Login.MY_PREFS_NAME;

public class Wishlist extends Fragment
{
    private Provider_adapter provider_adapter;
    private List<Dasboard_provider_model> provider_models = new ArrayList<>();
    RecyclerView rv_privider;


    @Override
public View onCreateView(LayoutInflater inflater, ViewGroup container,
                         Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_wishlist, container, false);
        rv_privider=(RecyclerView)view.findViewById(R.id.rv_providers);

        GridLayoutManager gridLayoutManager1 = new GridLayoutManager(getActivity(), 2);
        rv_privider.setLayoutManager(gridLayoutManager1);
        rv_privider.setItemAnimator(new DefaultItemAnimator());
        rv_privider.setNestedScrollingEnabled(true);
        SharedPreferences sharedPreferences=getActivity().getSharedPreferences(MY_PREFS_NAME,MODE_PRIVATE);
        String id=sharedPreferences.getString("id",null);


        getProvider(id);
        return view;
    }
    private void getProvider(String cat_id) {

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, Constant.Base_url_Wihslist+cat_id ,new Response.Listener<JSONObject>() {
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

                    makeText(getActivity(),"No Data Found", LENGTH_SHORT).show();
                }

            }
        }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                makeText(getActivity(), "Connection Error", LENGTH_LONG).show();
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

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(jsonRequest);
    }
}
