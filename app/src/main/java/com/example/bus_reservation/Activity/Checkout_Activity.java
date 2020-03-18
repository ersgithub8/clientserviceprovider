package com.example.bus_reservation.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.bus_reservation.Constant;
import com.example.bus_reservation.R;
import com.example.bus_reservation.volley.CustomRequest;
import com.example.bus_reservation.volley.VolleyMultipartRequest;
import com.google.android.material.textfield.TextInputEditText;
import com.skydoves.elasticviews.ElasticButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;
import static com.example.bus_reservation.Activity.Login.MY_PREFS_NAME;
import static java.security.AccessController.getContext;

public class Checkout_Activity extends AppCompatActivity {
    TextView back,name,email,phone1,freelancer_id;
    TextInputEditText specialrequest,link1,link2,addres;
    String special_request_text,link1_text,link2_text,Service_id,Call_type,hourly_price,Date,total_hour,total_price,time,provider_id,locationtype,address;
    ElasticButton checkout,browse;
    Bitmap bitmap;
    String Email;
    ImageView pimg;
    String First;
    String phone;
    String freelancid;
    Uri uri;

    int a=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        back=findViewById(R.id.back);
        addres=findViewById(R.id.address);
        specialrequest=findViewById(R.id.special_request);
        browse=findViewById(R.id.id_image);
        checkout=findViewById(R.id.checkout);
        link1=findViewById(R.id.link1);
        link2=findViewById(R.id.link2);
        name=findViewById(R.id.client_name);
        freelancer_id=findViewById(R.id.client_id);
        email=findViewById(R.id.client_email);
        phone1=findViewById(R.id.client_phone);
        pimg=findViewById(R.id.pimg);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        pimg.setVisibility(View.GONE);
        SharedPreferences editors = getSharedPreferences(MY_PREFS_NAME,MODE_PRIVATE);
       Email = editors.getString("email","Null");
       First = editors.getString("firstname","Null");
       phone = editors.getString("phone","Null");
       freelancid = editors.getString("id","Null");
        provider_id=getIntent().getStringExtra("provider_id");

        name.setText(First);
        email.setText(Email);
        phone1.setText(phone);
        freelancer_id.setText(freelancid);
        address=addres.getText().toString();
        locationtype="house";

        special_request_text=specialrequest.getText().toString();
        link1_text=link1.getText().toString().trim();
        link2_text=link2.getText().toString().trim();

        Service_id=getIntent().getStringExtra("Service_id");
        Call_type=getIntent().getStringExtra("Call_type");
        hourly_price=getIntent().getStringExtra("hourly_price");
        Date   =getIntent().getStringExtra("Date");
        total_hour   =getIntent().getStringExtra("total_hours");
        total_price   =getIntent().getStringExtra("total_price");
            time= editors.getString("hours","Null")+":"+ editors.getString("minutes","Null");
        browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pickImage = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickImage, 0);
            }
        });
        pimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pickImage = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickImage, 0);
            }
        });

        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!addres.getText().toString().isEmpty()
                &&!specialrequest.getText().toString().isEmpty()
                && !link1.getText().toString().isEmpty()
                &&!link2.getText().toString().isEmpty()
                ) {
                    if(a!=0){
                        uploadbitmap(bitmap, Service_id, Call_type, hourly_price, Date, time, total_hour, total_price, provider_id, First, Email, phone, locationtype, address, special_request_text, link1_text, link2_text, freelancid);
                    }
                    else {
                        makeText(Checkout_Activity.this, "Select Image first", LENGTH_SHORT).show();
                    }
                    }else {
                    makeText(Checkout_Activity.this, "All fields must be filled.", LENGTH_SHORT).show();
                }
            }
        });








    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            uri = data.getData();
            try {
                Bitmap bit = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                bitmap = bit;
                a=1;
                //uploadbitmap(bitmap,"12");
                browse.setVisibility(View.GONE);
                pimg.setVisibility(View.VISIBLE);
//                Glide.with(Checkout_Activity.this).load(bitmap).into(pimg);

                pimg.setImageBitmap(bitmap);
            } catch (IOException e) {
                Toast.makeText(this,"Error Loading Image", LENGTH_SHORT).show();
            }
        }
    }
    private void uploadbitmap(final Bitmap bitmap, final String service_id, final String service_type,
                              final String hourly_price, final String start_date, final String start_time, final String hours, final String total_price,
                              final String freelancer_id, final String client_name, final String client_email, final String client_phone,
                              final String location_type, final String client_address, final String special_request,
                              final String screening_link1, final String screening_link2, final String client_id) {
        final android.app.AlertDialog loading = new ProgressDialog(Checkout_Activity.this);
        loading.setMessage("Checking...");
        loading.setCancelable(false);
        loading.show();


        VolleyMultipartRequest jsonRequest = new VolleyMultipartRequest(Request.Method.POST, Constant.Base_url, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                Boolean status = null;
                try {
                    JSONObject obj = new JSONObject(new String(response.data));

                    status = obj.getBoolean("response");
                    makeText(Checkout_Activity.this, status+"", LENGTH_SHORT).show();
                    String msg = obj.getString("data");
                    if (status) {
                        loading.dismiss();
                        Toast.makeText(Checkout_Activity.this,msg, Toast.LENGTH_SHORT).show();
                        Intent i=new Intent(Checkout_Activity.this,Payumoney.class);
                        i.putExtra("paymenttype","booking");
                        i.putExtra("price",total_price);
                        startActivity(i);
//

                    }
                    else {

                        Toast.makeText(Checkout_Activity.this,msg, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
//                    Toast.makeText(this,"Connection Error", LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

//                makeText(getContext(), "Something went wrong" + error, LENGTH_LONG).show();
            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new Hashtable<String, String>();

                params.put("add_booking","true");
                params.put("service_id",service_id);
                params.put("service_type",service_type);
                params.put("hourly_price",hourly_price);
                params.put("start_date",start_date);
                params.put("start_time",start_time);
                params.put("hours",hours);
                params.put("total_price",total_price);
                params.put("freelancer_id",freelancer_id);
                params.put("client_name",client_name);
                params.put("client_email",client_email);
                params.put("client_phone",client_phone);
                params.put("location_type",location_type);
                params.put("client_address",client_address);
                params.put("special_request",special_request);
                params.put("screening_link1",screening_link1);
                params.put("screening_link2",screening_link2);
                params.put("client_id",client_id);
                params.put("id_image", String.valueOf(bitmap));
                return params;
            }
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long ima = System.currentTimeMillis();
                String imagename2 = String.valueOf(ima).concat(".jpg");
                params.put("id_image",  new VolleyMultipartRequest.DataPart(imagename2, getFileDataFromDrawable(bitmap)));
                return params;
            }
        };

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
        Volley.newRequestQueue(this).add(jsonRequest);
    }
    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }
//    private void getData(final Bitmap bitmap, final String service_id, final String service_type,
//                         final String hourly_price, final String start_date, final String start_time, final String hours, final String total_price,
//                         final String freelancer_id, final String client_name, final String client_email, final String client_phone,
//                         final String location_type, final String client_address, final String special_request,
//                         final String screening_link1, final String screening_link2, final String client_id) {
//
//        final android.app.AlertDialog loading = new ProgressDialog(Checkout_Activity.this);
//        loading.setMessage("Checking...");
//        loading.show();
//
//        Map<String, String> params = new Hashtable<String, String>();
//
//        params.put("add_booking","true");
//        params.put("service_id",service_id);
//        params.put("service_type",service_type);
//        params.put("hourly_price",hourly_price);
//        params.put("start_date",start_date);
//        params.put("start_time",start_time);
//        params.put("hours",hours);
//        params.put("total_price",total_price);
//        params.put("freelancer_id",freelancer_id);
//        params.put("client_name",client_name);
//        params.put("client_email",client_email);
//        params.put("client_phone",client_phone);
//        params.put("location_type",location_type);
//        params.put("client_address",client_address);
//        params.put("special_request",special_request);
//        params.put("screening_link1",screening_link1);
//        params.put("screening_link2",screening_link2);
//        params.put("client_id",client_id);
//        params.put("id_image", String.valueOf(bitmap));
//
//        CustomRequest jsonRequest = new CustomRequest(Request.Method.POST, Constant.Base_url,params, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//
//                try {
//
//                    Boolean status = response.getBoolean("response");
//
//                    String jsonArray = response.getString("data");
//                    if (status){
//
//
//
//                            Toast.makeText(Checkout_Activity.this,jsonArray , LENGTH_SHORT).show();
//
//                            loading.dismiss();
//
//
//
//
//                    }
//                    else {
//                        loading.dismiss();
//                        String error = response.getString("error");
//
//                        Toast.makeText(Checkout_Activity.this,error, LENGTH_SHORT).show();
//                    }
//
//                } catch (JSONException error) {
//                    loading.dismiss();
//                    Toast.makeText(Checkout_Activity.this,"Something Went Wrong", LENGTH_SHORT).show();
//                }
//
//            }
//        }
//                , new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                loading.dismiss();
//                makeText(getApplicationContext(), "Something went wrong" + error, LENGTH_LONG).show();
//            }
//        });
//
//        jsonRequest.setRetryPolicy(new RetryPolicy() {
//            @Override
//            public int getCurrentTimeout() {
//                return 50000;
//            }
//
//            @Override
//            public int getCurrentRetryCount() {
//                return 50000;
//            }
//
//            @Override
//            public void retry(VolleyError error) throws VolleyError {
//
//            }
//        });
//
////        MySingleton.getInstance(this).addToRequestQueue(jsonRequest);
//        RequestQueue queue = Volley.newRequestQueue(this);
//        queue.add(jsonRequest);
//
//    }

}
