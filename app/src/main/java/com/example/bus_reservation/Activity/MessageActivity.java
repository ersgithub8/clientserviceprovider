package com.example.bus_reservation.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.bus_reservation.Adapter.Message_Adapter;
import com.example.bus_reservation.Constant;
import com.example.bus_reservation.Model.Message_Model;
import com.example.bus_reservation.R;
import com.example.bus_reservation.volley.CustomRequest;
import com.example.bus_reservation.volley.VolleyMultipartRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;

public class MessageActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ImageView imageView;
    TextView back,name,send,media;
    EditText editText;
    ArrayList<Message_Model> model;
    String id,provider_id;
    Uri uri;
    Bitmap bitmap;
    Message_Adapter adapter;
    int totallength =0;
    Handler handler = new Handler();
    Runnable runnable;
    int delay = 1500;
    String MY_PREFS_NAME = "MyPrefsFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        model=new ArrayList<>();
        recyclerView=findViewById(R.id.rec_chat);
        back=findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        String Name = getIntent().getStringExtra("name");
        String image = getIntent().getStringExtra("image");
        provider_id = getIntent().getStringExtra("pid");

        SharedPreferences editors = getSharedPreferences(MY_PREFS_NAME,MODE_PRIVATE);
        id = editors.getString("id","Null");

        editText=findViewById(R.id.edit_id);
        imageView=findViewById(R.id.imagee);
        name=findViewById(R.id.g_name);
        send=findViewById(R.id.sender_id);
        media=findViewById(R.id.gallery_id);

        name.setText(Name);
        Picasso.with(MessageActivity.this).load(image).into(imageView);

        getData(provider_id,id);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editText.getText().toString().equals("")){
                    makeText(MessageActivity.this,"Please Enter Text", LENGTH_SHORT).show();
                }
                else {

                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(send.getWindowToken(),
                            InputMethodManager.RESULT_UNCHANGED_SHOWN);

                    insertdata(provider_id, id, editText.getText().toString().trim());

                }
            }
        });
        media.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //showPictureDialog();
                Intent pickImage = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickImage, 0);
            }
        });
    }

    private void insertdata(String id, String provider_id, String trim) {
//        final AlertDialog loading = new ProgressDialog(chat_screen.this);
//        loading.setMessage("Sending...");
//        loading.show();

        Map<String, String> params = new Hashtable<String, String>();
        params.put("add_chat", "true");
        params.put("client_id", provider_id);
        params.put("provider_id", id);
        params.put("msg", trim);
        params.put("image_flag", "0");
        editText.setText("");

        CustomRequest jsonRequest = new CustomRequest(Request.Method.POST, Constant.Base_url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Boolean status = null;
                try {
                    status = response.getBoolean("response");

                    if (status) {

//                        loading.dismiss();

                    } else {
//                        loading.dismiss();
                        makeText(MessageActivity.this, "Message not send", LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
//                    loading.dismiss();
                    makeText(MessageActivity.this, "Connection Error", LENGTH_SHORT).show();
                }

            }
        }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //               loading.dismiss();
                makeText(MessageActivity.this, "Something went wrong " + error, LENGTH_LONG).show();
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
        RequestQueue queue = Volley.newRequestQueue(MessageActivity.this);
        queue.add(jsonRequest);
    }
    private void getData(String provider_id, String id) {
        final android.app.AlertDialog loading = new ProgressDialog(MessageActivity.this);
        loading.setMessage("Please Wait...");
        loading.show();
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, Constant.Base_url_get_conversation+"&client_id="+id+"&provider_id="+provider_id, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Boolean status = null;
                try {
                    status = response.getBoolean("response");
                    if (status) {
                        JSONArray jsonArray = response.getJSONArray("conversation");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            Message_Model value=new Message_Model();
                            value.setId(object.getString("id"));
                            value.setChat_id(object.getString("chat_id"));
                            value.setDate(object.getString("date"));
                            value.setImage_flag(object.getString("image_flag"));
                            value.setMsg(object.getString("msg"));
                            value.setSender_id(object.getString("sender_id"));
                            model.add(value);
                        }

                        LinearLayoutManager layoutManager = new LinearLayoutManager(MessageActivity.this);
                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.setAdapter(new Message_Adapter(MessageActivity.this, model));
                        recyclerView.scrollToPosition(jsonArray.length() - 1);
                        loading.dismiss();
                    }
                    else {
                        loading.dismiss();
//                        makeText(MessageActivity.this,"No Data Found", LENGTH_SHORT).show();
                    }
                }
                catch (Exception e){
                    loading.dismiss();
                    makeText(MessageActivity.this,"Error Connenction", LENGTH_SHORT).show();
                }
            }
        }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                makeText(MessageActivity.this, "Connection Error", LENGTH_LONG).show();
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

        RequestQueue queue = Volley.newRequestQueue(MessageActivity.this);
        queue.add(jsonRequest);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 0) {
                uri = data.getData();
                try {

                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);

                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    byte[] imageInByte = stream.toByteArray();

                    long lengthbmp = imageInByte.length;

                    uploadbitmap(provider_id,bitmap,id,lengthbmp);

                } catch (IOException e) {
                    makeText(MessageActivity.this,"Error Loading Image", LENGTH_SHORT).show();
                }
            }
            else {

            }
        }
    }

    private void uploadbitmap(final String client_id, final Bitmap bitmap, final String id, final long lengthbmp) {
        final AlertDialog loading = new ProgressDialog(MessageActivity.this);
        loading.setMessage("Sending Image...");
        loading.setCancelable(false);
        loading.show();
        VolleyMultipartRequest jsonRequest = new VolleyMultipartRequest(Request.Method.POST, Constant.Base_url, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {

                try {

                    JSONObject obj = new JSONObject(new String(response.data));
                    String status = obj.getString("response");
                    if (status.equals("1")){
                        loading.dismiss();
                    }
                    else {
                        loading.dismiss();
//                            String msg = obj.getString("msg");
//                            Toast.makeText(chat_screen.this,msg, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    loading.dismiss();
                    makeText(MessageActivity.this,"Connection Error", LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                makeText(MessageActivity.this, "Something went wrong" + error, LENGTH_LONG).show();
            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new Hashtable<String, String>();
                params.put("add_chat", "true");
                params.put("client_id", client_id);
                params.put("provider_id", id);
                params.put("image_flag", "1");
                return params;
            }
            @Override
            protected Map<String, DataPart> getByteData() {
                int quality;
                Map<String, DataPart> params = new HashMap<>();
                long ima = System.currentTimeMillis();
                String imagename2 = String.valueOf(ima).concat(".jpg");

                if (lengthbmp > 1000000){
                    quality = 50;
                }
                else {
                    quality = 80;
                }
                params.put("image", new DataPart(imagename2, getFileDataFromDrawable(bitmap,quality)));
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

    public byte[] getFileDataFromDrawable(Bitmap bitmap, int quality) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }
    //update
    @Override
    protected void onResume() {
        //start handler as activity become visible

        handler.postDelayed( runnable = new Runnable() {
            public void run() {
                //do something
                handler.postDelayed(runnable, delay);
                getChatTimer(provider_id,id);
                //getStatus(rec_id);
            }
        }, delay);
        super.onResume();
    }

    private void getChatTimer(String provider_id, String id) {
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, Constant.Base_url_get_conversation+"&client_id="+id+"&provider_id="+provider_id, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Boolean status = null;
                try {

                    status = response.getBoolean("response");
                    if (status) {
                        JSONArray jsonArray = response.getJSONArray("conversation");
                        if (jsonArray.length() > totallength) {
                            model.clear();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                Message_Model value=new Message_Model();
                                value.setId(object.getString("id"));
                                value.setChat_id(object.getString("chat_id"));
                                value.setDate(object.getString("date"));
                                value.setImage_flag(object.getString("image_flag"));
                                value.setMsg(object.getString("msg"));
                                value.setSender_id(object.getString("sender_id"));
                                model.add(value);
                            }

                            totallength = jsonArray.length();
                            if (adapter != null) {
                                adapter.notifyDataSetChanged();
                                recyclerView.scrollToPosition(jsonArray.length() - 1);
                            }
                            else {
                                adapter = new Message_Adapter(MessageActivity.this, model);
                                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                                recyclerView.setLayoutManager(mLayoutManager);
                                recyclerView.scrollToPosition(jsonArray.length() - 1);
                                recyclerView.setAdapter(adapter);
                            }
                        } else {
//                        if (single_chat_adapter != null) {
//                            single_chat_adapter.notifyDataSetChanged();
//                        }
//                        else {
//                            single_chat_adapter= new Single_chat_adapter(Single_Activity.this, model);
//                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
//                            recyclerView.setLayoutManager(mLayoutManager);
//                            recyclerView.setAdapter(single_chat_adapter);
//                            recyclerView.scrollToPosition(jsonArray2.length() - 1);
//                        }
                        }

                    }
                    else {
                        delay = 5000000;
//                        Toast.makeText(chat_screen.this,"No Data Found", Toast.LENGTH_SHORT).show();
                    }
                }
                catch (Exception e){
//                    Toast.makeText(chat_screen.this,"Error Connenction", Toast.LENGTH_SHORT).show();
                }
            }
        }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(chat_screen.this, "Connection Error", Toast.LENGTH_LONG).show();
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

        RequestQueue queue = Volley.newRequestQueue(MessageActivity.this);
        queue.add(jsonRequest);

    }

}
