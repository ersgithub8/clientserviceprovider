package com.example.bus_reservation.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.bus_reservation.Constant;
import com.example.bus_reservation.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;

public class Pick_Drop extends AppCompatActivity {
Button button;
TextView back,Facility;
ArrayList<String> pickup,dropup,facility;
Spinner Pick,Drop;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick__drop);

        pickup= new ArrayList<>();
        dropup= new ArrayList<>();
        facility= new ArrayList<>();
        Pick=findViewById(R.id.pickup);
        Drop=findViewById(R.id.dropup);
        Facility=findViewById(R.id.facil);
        back=findViewById(R.id.back);
        button=findViewById(R.id.next);
        pickup.add("Select Pickup Location");
        dropup.add("Select DropUp Location");

        final String trip = getIntent().getStringExtra("trip_id");
        final String fleet = getIntent().getStringExtra("fleet_reg_no");
        String rout = getIntent().getStringExtra("rout_id");
        final String price = getIntent().getStringExtra("price");
        final String routn = getIntent().getStringExtra("routn");

        showdata(rout,fleet);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Pick.getSelectedItem().toString().equals("Select Pickup Location")){
                    Toast.makeText(Pick_Drop.this,"Please Select Start Point", LENGTH_SHORT).show();
                }
                else if (Drop.getSelectedItem().toString().equals("Select DropUp Location")){
                    Toast.makeText(Pick_Drop.this,"Please Select End Point", LENGTH_SHORT).show();
                }
                else {
                    String pick = Pick.getSelectedItem().toString();
                    String drop = Drop.getSelectedItem().toString();
//                    Intent i = new Intent(Pick_Drop.this, Seat_Layout.class);
//                    i.putExtra("trip_id", trip);
//                    i.putExtra("routn", routn);
//                    i.putExtra("fleet_id", fleet);
//                    i.putExtra("Pick",pick);
//                    i.putExtra("Drop",drop);
//                    i.putExtra("price",price);
//                    startActivity(i);
                }
                }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void showdata(String Trip, String Fleet) {
            final android.app.AlertDialog loading = new ProgressDialog(Pick_Drop.this);
            loading.setMessage("Getting Data...");
            loading.show();
            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, Constant.Base_url_Search_location+"trip_route_id="+Trip+"&fleet_registration_id="+Fleet, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Boolean status = null;
                    try {
                        status = response.getBoolean("response");
                        JSONArray jsonArray = response.getJSONArray("locations");
                        JSONArray jsonArray2 = response.getJSONArray("facilities");
                        if (status) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                String temp = object.getString("scalar");
                                pickup.add(temp);
                                dropup.add(temp);
                            }
                            Pick.setAdapter(new ArrayAdapter<String>(Pick_Drop.this, android.R.layout.simple_spinner_dropdown_item, pickup));
                            Drop.setAdapter(new ArrayAdapter<String>(Pick_Drop.this, android.R.layout.simple_spinner_dropdown_item, dropup));

                            for (int i = 0; i < jsonArray2.length(); i++) {
                                JSONObject object = jsonArray2.getJSONObject(i);
                                String temp = object.getString("scalar");
                                facility.add(temp);
                            }

                            StringBuilder builder = new StringBuilder();
                            for (String details : facility) {
                                builder.append(details + "\n");
                            }

                            Facility.setText(builder.toString());
                            loading.dismiss();

                        }else {
                            Toast.makeText(Pick_Drop.this,"No Data Available", LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        loading.dismiss();
                        makeText(Pick_Drop.this, "No Data Found", LENGTH_SHORT).show();
                    }
                }
            }
                    , new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    loading.dismiss();
                    makeText(Pick_Drop.this, "Connection Error", LENGTH_LONG).show();
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

            RequestQueue queue = Volley.newRequestQueue(Pick_Drop.this);
            queue.add(jsonRequest);
        }

}
