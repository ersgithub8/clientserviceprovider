package com.example.bus_reservation;

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
import com.example.bus_reservation.Adapter.Membership_Adapter;
import com.example.bus_reservation.Adapter.Provider_adapter;
import com.example.bus_reservation.Model.Dasboard_provider_model;
import com.example.bus_reservation.Model.Membership_model;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;

public class Membership extends Fragment {
    //provider adapter model
    private Membership_Adapter membership_adapter;
    private List<Membership_model> membership_models = new ArrayList<>();
    RecyclerView rv_membership;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.membership_fragment, container, false);
        rv_membership=(RecyclerView)view.findViewById(R.id.rv_membership);
        GridLayoutManager gridLayoutManager1 = new GridLayoutManager(getActivity(), 2);
        rv_membership.setLayoutManager(gridLayoutManager1);
        rv_membership.setItemAnimator(new DefaultItemAnimator());
        rv_membership.setNestedScrollingEnabled(true);

        getmemberships();

        return view;
    }
    private void getmemberships() {

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, Constant.Base_url_dashboard_Membership,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Boolean status = null;

                try {

                    status=response.getBoolean("response");
                    Gson gson = new Gson();
                    Type listType = new TypeToken<List<Membership_model>>() {
                    }.getType();

                    membership_models = gson.fromJson(response.getString("data"), listType);


                    membership_adapter = new Membership_Adapter(membership_models);
                    rv_membership.setAdapter(membership_adapter);
                    membership_adapter.notifyDataSetChanged();



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
