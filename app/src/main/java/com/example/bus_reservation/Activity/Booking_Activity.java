package com.example.bus_reservation.Activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
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
import com.example.bus_reservation.Constant;
import com.example.bus_reservation.Model.Gallery_model;
import com.example.bus_reservation.R;
import com.example.bus_reservation.volley.Time_picker;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;
import static com.example.bus_reservation.Activity.Login.MY_PREFS_NAME;

public class Booking_Activity extends AppCompatActivity implements Time_picker.TimePickerListener {
    TextView back,name,tagline,distance,hourlyprice;
    TextInputEditText dob,time;
    ImageView providerimage,plus,minus;
    ArrayList<String> Select_service,dropup,facility,priceincall,priceoutcall;
    Spinner service;
    Button btncheck;
    RadioGroup radioGroup;
    RadioButton in,out;
    LinearLayout li;
    String calltype="null",date="null",Timeget="null";
    TextView hours,total_price;

    int i=1,j,k;
    private DatePickerDialog.OnDateSetListener mDateSetListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        back=findViewById(R.id.back);
        total_price=findViewById(R.id.total_price);
        plus=findViewById(R.id.plus);
        minus=findViewById(R.id.minus);
        hours=findViewById(R.id.hours);
        time=findViewById(R.id.time);
        in=findViewById(R.id.incall);
        out=findViewById(R.id.outcall);
        dob=findViewById(R.id.date);
        btncheck=findViewById(R.id.request_time);
        li=findViewById(R.id.distancelayout);
        name=findViewById(R.id.provider_name);
        hourlyprice=findViewById(R.id.hourly_price);
        distance=findViewById(R.id.distance);
        tagline=findViewById(R.id.tagline);
        service=findViewById(R.id.select_service);
        providerimage=findViewById(R.id.provider_img);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Select_service= new ArrayList<>();
        priceincall= new ArrayList<>();
        priceoutcall= new ArrayList<>();
        dropup= new ArrayList<>();
        Select_service.add("Select Service");
        dropup.add("null");
        priceincall.add("null");
        priceoutcall.add("null");
        final String provider_id=getIntent().getStringExtra("Provider_id");
        getProvider(provider_id);



        hourlyprice.setText(" ");
        radioGroup=(RadioGroup)findViewById(R.id.radio_call);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {


                    switch (i) {
                        case R.id.incall:

                            String spin;
                            spin=String.valueOf(service.getSelectedItem());
                            if (spin.equals("Select Service")){
                                in.setChecked(false);
                                makeText(Booking_Activity.this, "Select Service First", LENGTH_SHORT).show();
                            }else {
                                calltype = "incall";
                                int one = (int) service.getSelectedItemId();
                                String two = priceincall.get(one);
                                j = Integer.parseInt(two);
                                hourlyprice.setText("$" + two);
                                total_price.setText("$" + two);
                            }


                            break;
                        case R.id.outcall:

                            String spin1;
                            spin1=String.valueOf(service.getSelectedItem());
                            if (String.valueOf(service.getSelectedItem()).equals("Select Service")){
                                out.setChecked(false);
                                makeText(Booking_Activity.this, "Select Service First", LENGTH_SHORT).show();
                            }else {
                                calltype = "outcall";
                                int on = (int) service.getSelectedItemId();
                                String tw = priceoutcall.get(on);
                                j = Integer.parseInt(tw);
                                hourlyprice.setText("$" + tw);
                                total_price.setText("$" + tw);
                            }

                            break;

                        default:
                            break;

                    }

            }
        });
        dob.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        Booking_Activity.this,
                        mDateSetListener,
                        year,month,day);
                dialog.show();
            }
        });
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                date = year + "-" + month + "-" + day;
                dob.setText(date);
            }
        };
        hours.setText(String.valueOf(i));
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i++;
                hours.setText(String.valueOf(i));
                hourlyprice.getText();
                k=i*j;
                total_price.setText("$"+String.valueOf(k));
            }
        });
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i--;
                hours.setText(String.valueOf(i));
                k=i*j;
                total_price.setText("$"+String.valueOf(k));
            }
        });
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment timePickerFragment = new Time_picker();
                timePickerFragment.setCancelable(false);
                timePickerFragment.show(getSupportFragmentManager(), "timePicker");
            }
        });



        btncheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (String.valueOf(service.getSelectedItem()) == "Select Service")
                {
                    makeText(Booking_Activity.this, "Select service first", LENGTH_SHORT).show();
                }else if (calltype.equals("null")){
                    makeText(Booking_Activity.this, "Select Call type first", LENGTH_SHORT).show();
                }else if (date.equals("null")){
                    makeText(Booking_Activity.this, "Select date first", LENGTH_SHORT).show();
                } else if (Timeget.equals("null")) {
                    makeText(Booking_Activity.this, "Select Time  first", LENGTH_SHORT).show();
                }else {

                    Intent intent = new Intent(Booking_Activity.this, Checkout_Activity.class);
                    intent.putExtra("Service_id", String.valueOf(service.getSelectedItemId()));
                    intent.putExtra("Call_type", calltype);
                    intent.putExtra("hourly_price", String.valueOf(j));
                    intent.putExtra("Date", date);
                    intent.putExtra("total_hours", hours.getText().toString());
                    intent.putExtra("total_price", String.valueOf(k));
                    intent.putExtra("provider_id", provider_id);
                    startActivity(intent);
                }
            }
        });




    }
    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
        time.setText( hour +":" + minute);
        Timeget=String.valueOf(hour)+":"+String.valueOf(minute);

    }
    private void getProvider(String Prov_id) {
        final AlertDialog loading=new ProgressDialog(Booking_Activity.this);
        loading.setCancelable(false);
        loading.setTitle("Loading...");
        loading.show();
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, Constant.Base_url_Provider_Detail+Prov_id+"&get_single_provider=true" ,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Boolean status = null;

                try {

                    status=response.getBoolean("response");
                    if(status){
                        loading.dismiss();
                        JSONArray jsonArray = response.getJSONArray("data");
//                        Toast.makeText(Provider_Detail.this, response.getString("data"), LENGTH_SHORT).show();
                        for (int i=0;i<jsonArray.length();i++){
                            JSONObject object= jsonArray.getJSONObject(i);

                            name.setText(object.getString("name"));
                            if(object.getString("distance").equals("")){
                                li.setVisibility(View.GONE);
                            }else{
                                distance.setText(object.getString("distance"));
                            }

                            if(object.getString("tagline").equals("")){
                                tagline.setVisibility(View.GONE);
                            }else {
                                tagline.setText(object.getString("tagline"));
                            }
                            String image=Constant.Base_url_provider_image+object.getString("image");
                            Glide.with(Booking_Activity.this).load(image).into(providerimage);


                            JSONArray jsonArray1 = response.getJSONArray("services");

                            JSONArray jsonArray2 = response.getJSONArray("services");

//
                                for (int j = 0; j < jsonArray1.length(); j++) {
                                    JSONObject object1 = jsonArray1.getJSONObject(j);
                                    String temp = object1.getString("title");
                                    String temp2=object1.getString("serviceid");
                                    String temp3=object1.getString("incall_rate");
                                    String temp4=object1.getString("outcall_rate");
                                    Select_service.add(temp);
                                    dropup.add(temp2);
                                    priceincall.add(temp3);
                                    priceoutcall.add(temp4);

                                }
                                service.setAdapter(new ArrayAdapter<String>(Booking_Activity.this, android.R.layout.simple_spinner_dropdown_item, Select_service));
//                                Drop.setAdapter(new ArrayAdapter<String>(Pick_Drop.this, android.R.layout.simple_spinner_dropdown_item, dropup));




                            }

                    }

                }
                catch (Exception e){

                    makeText(Booking_Activity.this,"No Data Found", LENGTH_SHORT).show();
                }

            }
        }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                makeText(Booking_Activity.this, "Connection Error", LENGTH_LONG).show();
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
