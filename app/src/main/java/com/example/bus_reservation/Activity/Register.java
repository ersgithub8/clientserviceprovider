package com.example.bus_reservation.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.bus_reservation.Constant;
import com.example.bus_reservation.R;
import com.example.bus_reservation.volley.CustomRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Hashtable;
import java.util.Map;

import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;
import static com.example.bus_reservation.Activity.Login.MY_PREFS_NAME;

public class Register extends AppCompatActivity {
  TextView back;
  LinearLayout Successful;
  Button Register;
  EditText first,last,email,password,phone,dob,pincode;
  RadioGroup radioGroup;
  String gender;



  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_register);

    back=findViewById(R.id.back);
    Register=findViewById(R.id.register);
    radioGroup=(RadioGroup)findViewById(R.id.gender_radio);
    first=(EditText)findViewById(R.id.first);
    phone=(EditText)findViewById(R.id.phone);
    dob =(EditText)findViewById(R.id.dob);
    pincode=(EditText)findViewById(R.id.pincode);
    last=findViewById(R.id.last);
    Successful=(LinearLayout) findViewById(R.id.successful_register);
    email=findViewById(R.id.email);

    password=findViewById(R.id.password);
    radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(RadioGroup radioGroup, int i) {
       switch (i){
         case R.id.male:
           gender="male";

           break;
         case R.id.Female:
           gender="Female";

           break;
         case R.id.others:
           gender="others";

           break;
       }
      }
    });

    Register.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if (TextUtils.isEmpty(first.getText().toString())){
          makeText(Register.this, "First Name is required", LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(last.getText().toString())){
          makeText(Register.this, "Last Name is required", LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(email.getText().toString())){
          makeText(Register.this, "Email is required", LENGTH_SHORT).show();
        }

        else if (TextUtils.isEmpty(password.getText().toString())){
          makeText(Register.this, "Password is required", LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(phone.getText().toString())){
          makeText(Register.this, "Phone is required", LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(pincode.getText().toString())){
          makeText(Register.this, "Pincode is required", LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(dob.getText().toString())){
          makeText(Register.this, "Date of birth is required", LENGTH_SHORT).show();
        }


        else {

          getData(first.getText().toString(),last.getText().toString(),email.getText().toString(),password.getText().toString(),phone.getText().toString(),pincode.getText().toString(),dob.getText().toString(),gender);

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
  private void getData(final String First,final String Last,final String Email,final String Password,final String phone,final String pincode,final String dob,final String Gender) {

//        Toast.makeText(Register.this,Phone, LENGTH_LONG).show();

    final android.app.AlertDialog loading = new ProgressDialog(this);
    loading.setMessage("Checking...");
    loading.show();

    Map<String, String> params = new Hashtable<String, String>();
    params.put("name", First);
    params.put("registration", "true");
    params.put("username", Last);

    params.put("email", Email);
    params.put("phone", phone);
    params.put("pin_code", pincode);
    params.put("dob", dob);
    params.put("password", Password);
    params.put("gender", Gender);



    CustomRequest jsonRequest = new CustomRequest(Request.Method.POST, Constant.Base_url,params, new Response.Listener<JSONObject>() {
      @Override
      public void onResponse(JSONObject response) {

        Boolean status = null;
        String msg = "";

        try {
          status = response.getBoolean("response");
          if (status) {
            loading.dismiss();
            SharedPreferences.Editor editors = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
            editors.putString("phone", phone);

            Successful.setVisibility(View.VISIBLE);

            finish();

          }
          else {
            loading.dismiss();
            String error = response.getString("error");
            Toast.makeText(Register.this,error, LENGTH_SHORT).show();
          }
        }
        catch (JSONException e) {
          loading.dismiss();
          makeText(Register.this,"Something Went Wrong", LENGTH_SHORT).show();
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
}
