package com.example.bus_reservation.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.BoringLayout;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.bus_reservation.Adapter.Chat_Adapter;
import com.example.bus_reservation.Adapter.Provider_adapter;
import com.example.bus_reservation.Constant;
import com.example.bus_reservation.Model.Chat_model;
import com.example.bus_reservation.Model.Dasboard_provider_model;
import com.example.bus_reservation.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static android.widget.Toast.LENGTH_SHORT;

public class Chat_Activity extends AppCompatActivity {
    RecyclerView chat;
    SharedPreferences sharedPreferences;
    TextView back;
    private Chat_Adapter chat_adapter;
    private List<Chat_model> chat_models = new ArrayList<>();

    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
    chat=(RecyclerView)findViewById(R.id.chatrv);
    chat.setLayoutManager(new LinearLayoutManager(Chat_Activity.this));
    sharedPreferences=getSharedPreferences("MyPrefsFile",MODE_PRIVATE);
    id=sharedPreferences.getString("id",null);
    back=findViewById(R.id.backch);

    back.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onBackPressed();
        }
    });
getData();
    }


    public void getData(){
        final android.app.AlertDialog loading = new ProgressDialog(Chat_Activity.this);
        loading.setMessage("Checking...");
        loading.setCancelable(false);
        loading.show();

        JsonObjectRequest jsonRequest=new JsonObjectRequest(Request.Method.GET,
                Constant.Base_url_chatlist + id, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Boolean status;
                String data;
                try {

                    status=response.getBoolean("response");
                    if(status){
                        loading.cancel();
                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<Chat_model>>() {
                        }.getType();

                        chat_models = gson.fromJson(response.getString("chats"), listType);


                        chat_adapter = new Chat_Adapter(chat_models);
                        chat.setAdapter(chat_adapter);
                        chat_adapter.notifyDataSetChanged();


                    }else {
                        data=response.getString("error");
                        Toast.makeText(Chat_Activity.this, data, LENGTH_SHORT).show();
                    }

                }catch (Exception e){

                    loading.dismiss();
                    Toast.makeText(Chat_Activity.this,"Something Went Wrong", LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                Toast.makeText(Chat_Activity.this, "Something went wrong" + error, Toast.LENGTH_LONG).show();

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
