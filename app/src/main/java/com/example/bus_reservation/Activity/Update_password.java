package com.example.bus_reservation.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.example.bus_reservation.Update;
import com.example.bus_reservation.volley.CustomRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Hashtable;
import java.util.Map;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;
import static com.example.bus_reservation.Activity.Login.MY_PREFS_NAME;

public class Update_password extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        TextView back;
        Button contnue;
        final EditText oldpassword,newpassword;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);
        back=findViewById(R.id.back);
        contnue=findViewById(R.id.contnue);
        oldpassword=findViewById(R.id.oldpasswordd);
        newpassword=findViewById(R.id.newpasswordd);
        SharedPreferences sharedPreferences=getSharedPreferences(MY_PREFS_NAME,MODE_PRIVATE);
        final String id=sharedPreferences.getString("id",null);
        makeText(this,id, LENGTH_SHORT).show();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        contnue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(oldpassword.getText().toString())){
                    Toast.makeText(Update_password.this, "Old password is required", Toast.LENGTH_SHORT).show();
                }else if (TextUtils.isEmpty(newpassword.getText().toString())){
                    Toast.makeText(Update_password.this, "New  password is required", Toast.LENGTH_SHORT).show();
                }
                else {
                    getData(oldpassword.getText().toString().trim(),newpassword.getText().toString().trim(),id);
                }
            }
        });
    }
    private void getData(final String oldpassword,final String newpassword,String id) {

        final android.app.AlertDialog loading = new ProgressDialog(Update_password.this);
        loading.setMessage("Checking...");
        loading.show();

        Map<String, String> params = new Hashtable<String, String>();
        params.put("change_password", "true");
        params.put("old_password", oldpassword);
        params.put("password", newpassword);
        params.put("client_id", id);


        CustomRequest jsonRequest = new CustomRequest(Request.Method.POST, Constant.Base_url,params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Boolean status = null;
                try {

                    status = response.getBoolean("response");

                    String msg1=response.getString("data");

                    if (status) {

                        Toast.makeText(Update_password.this,"Successfully !!!", LENGTH_SHORT).show();

                        loading.dismiss();
                        makeText(Update_password.this,msg1, LENGTH_SHORT).show();
                        Intent intent=new Intent(Update_password.this,Menu.class);
                        startActivity(intent);

                    }


                    else {
                        loading.dismiss();
                        Toast.makeText(Update_password.this,msg1, LENGTH_SHORT).show();
                    }
                } catch (JSONException error) {
                    loading.dismiss();
                    Toast.makeText(Update_password.this,"Something Went Wrong", LENGTH_SHORT).show();
                }

            }
        }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                Toast.makeText(Update_password.this, "Something went wrong" + error, Toast.LENGTH_LONG).show();
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

