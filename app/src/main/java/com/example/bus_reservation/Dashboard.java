package com.example.bus_reservation;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.example.bus_reservation.Activity.All_categories;
import com.example.bus_reservation.Activity.Menu;
import com.example.bus_reservation.Adapter.Category_Adapter;
import com.example.bus_reservation.Adapter.Provider_adapter;
import com.example.bus_reservation.Model.Categories_model;
import com.example.bus_reservation.Model.Dasboard_provider_model;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;
import static com.example.bus_reservation.Activity.Login.MY_PREFS_NAME;

public class Dashboard extends Fragment {
    private SliderLayout mDemoSlider,imgSlider;
    ArrayList<String> image;
    RecyclerView rv_cat,rv_privider;
    TextView view_all;


    //category adapter/model
    private Category_Adapter master_adapter;
    private List<Categories_model> master_models = new ArrayList<>();

    //provider adapter model
    private Provider_adapter provider_adapter;
    private List<Dasboard_provider_model> provider_models = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);


        image=new ArrayList<>();
        //Category Recycler
        rv_cat=(RecyclerView) view.findViewById(R.id.rv_categories);

        LinearLayoutManager layoutManager3 = new LinearLayoutManager(getActivity());
        layoutManager3.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv_cat.setLayoutManager(layoutManager3);
        rv_cat.setItemAnimator(new DefaultItemAnimator());
        view_all=view.findViewById(R.id.view_All);
        view_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getActivity(), All_categories.class);
                startActivity(i);
            }
        });


//Provider Recycler
        rv_privider=(RecyclerView) view.findViewById(R.id.rv_providers);

        GridLayoutManager gridLayoutManager1 = new GridLayoutManager(getActivity(), 2);
        rv_privider.setLayoutManager(gridLayoutManager1);
        rv_privider.setItemAnimator(new DefaultItemAnimator());
        rv_privider.setNestedScrollingEnabled(true);

        mDemoSlider = view.findViewById(R.id.slider);

        SharedPreferences editors = getContext().getSharedPreferences(MY_PREFS_NAME,MODE_PRIVATE);
        String id=editors.getString("id",null);


//Slider function
        showData();
        //category function
        getData();
        //provider function
        getProvider(id);

        //category Click listener


        return view;
    }

//Slider Function

    public void showData(){

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, Constant.Base_url_Slider, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Boolean status = null;

                try {
                    status = response.getBoolean("response");

                    if (status){

                        JSONArray jsonArray = response.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            String temp = object.getString("image");
                            image.add(temp);
                        }
                        Slider_view(image);
                    }
                    else {
                        Toast.makeText(getActivity(),"Error", Toast.LENGTH_SHORT).show();
                    }

                }
                catch (Exception e) {

                    Toast.makeText(getActivity(),"Something Went Wrong", Toast.LENGTH_SHORT).show();
                }

            }
        }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getContext(), "Connection Error", Toast.LENGTH_LONG).show();
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
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(jsonRequest);
    }

    public void Slider_view(ArrayList<String> images){

        //from image link
        HashMap<String,String> url_maps = new HashMap<String, String>();
        for (int k = 0; k < images.size(); k++){
           url_maps.put(String.valueOf(k), Constant.Base_url_image.concat(images.get(k)));
        }

        for(String name : url_maps.keySet()){
            TextSliderView textSliderView = new TextSliderView(getActivity());
            textSliderView
                    .image(url_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit);
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra",name);
            mDemoSlider.addSlider(textSliderView);
        }
        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.FlipHorizontal);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(4000);
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

                    makeText(getContext(),"No Data Found", LENGTH_SHORT).show();
                }

            }
        }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                makeText(getContext(), "Connection Error", LENGTH_LONG).show();
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

        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(jsonRequest);
    }
    //Get Provider
    private void getProvider(String id) {

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, Constant.Base_url_dashboard_provider+id ,new Response.Listener<JSONObject>() {
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

                    makeText(getContext(),"No Data Found", LENGTH_SHORT).show();
                }

            }
        }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                makeText(getContext(), "Connection Error", LENGTH_LONG).show();
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

        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(jsonRequest);
    }
}
