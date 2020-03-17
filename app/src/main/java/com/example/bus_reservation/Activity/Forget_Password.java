package com.example.bus_reservation.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
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
import com.example.bus_reservation.volley.CustomRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Hashtable;
import java.util.Map;

import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;

public class Forget_Password extends AppCompatActivity {

    TextView back;
    Button contnue;
    EditText Email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget__password);

        back=findViewById(R.id.back);
        contnue=findViewById(R.id.contnue);
        Email=findViewById(R.id.forgotemail);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        contnue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(Email.getText().toString())){
                    Toast.makeText(Forget_Password.this, "Eamil is required", Toast.LENGTH_SHORT).show();
                }
                else {
                    getData(Email.getText().toString().trim());
                }
            }
        });
    }
    private void getData(final String Email) {

        final android.app.AlertDialog loading = new ProgressDialog(Forget_Password.this);
        loading.setMessage("Checking...");
        loading.show();

        Map<String, String> params = new Hashtable<String, String>();
        params.put("email", Email);
        params.put("forgot_password", "true");

        CustomRequest jsonRequest = new CustomRequest(Request.Method.POST, Constant.Base_url,params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Boolean status = null;
                try {

                    status = response.getBoolean("response");
                    String msg=response.getString("error");
                    String msg1=response.getString("data");

                    if (status) {

                        Toast.makeText(Forget_Password.this,"Successfully !!!", LENGTH_SHORT).show();

                        loading.dismiss();
                        makeText(Forget_Password.this,msg1, LENGTH_SHORT).show();
                        Intent intent=new Intent(Forget_Password.this,Login.class);
                        startActivity(intent);

                    }


                    else {
                        loading.dismiss();
                        Toast.makeText(Forget_Password.this,msg, LENGTH_SHORT).show();
                    }
                } catch (JSONException error) {
                    loading.dismiss();
                    Toast.makeText(Forget_Password.this,"Something Went Wrong", LENGTH_SHORT).show();
                }

            }
        }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                Toast.makeText(Forget_Password.this, "Something went wrong" + error, Toast.LENGTH_LONG).show();
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
