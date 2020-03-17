package com.example.bus_reservation;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Environment;
import android.provider.MediaStore;
import android.provider.SyncStateContract;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.bus_reservation.Activity.Register;
import com.example.bus_reservation.volley.CustomRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;
import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;
import static com.example.bus_reservation.Activity.Login.MY_PREFS_NAME;

public class Update extends Fragment {

    Uri uri;
    private String Id;
    String code = "91";
    Button update;
    EditText first, last, phone,dob,pincode;
    ImageView image;
    private static int RESULT_LOAD_IMAGE = 1;
    RadioGroup radioGroup;
    String gender;

    RadioButton r1,r2,r3;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_update, container, false);
        r1=view.findViewById(R.id.male);
        r2=view.findViewById(R.id.Female);
        r3=view.findViewById(R.id.others);
        image = view.findViewById(R.id.image_id);
        first = (EditText) view.findViewById(R.id.first);
        last = view.findViewById(R.id.last);
        phone = view.findViewById(R.id.phone);
        dob = view.findViewById(R.id.dob);
        pincode = view.findViewById(R.id.pincode);
        radioGroup=(RadioGroup)view.findViewById(R.id.gender_radio);
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
                        gender="other";

                        break;
                }
            }
        });


        update = view.findViewById(R.id.update);

        SharedPreferences editors = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        final String client_id = editors.getString("id", "Null");
        showData(client_id);
//        image.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent i = new Intent(Intent.ACTION_PICK,
//                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                startActivityForResult(i, RESULT_LOAD_IMAGE);
//          }
//     });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(first.getText().toString())) {
                    makeText(getContext(), " Name is required", LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(last.getText().toString())) {
                    makeText(getContext(), "User Name is required", LENGTH_SHORT).show();
                }  else if (TextUtils.isEmpty(phone.getText().toString())) {
                    makeText(getContext(), "Phone Number is required", LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(dob.getText().toString())) {
                    makeText(getContext(), "Date of birth is required", LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(pincode.getText().toString())) {
                    makeText(getContext(), "pincode is required", LENGTH_SHORT).show();
                } else {

                    getData( first.getText().toString(), last.getText().toString(), phone.getText().toString().trim(), pincode.getText().toString(), dob.getText().toString(), gender, client_id);

                }
            }
        });

        return view;
    }

    private void getData( final String First, final String Last, final String Phone, final String pincode, final String dob, final String gender2,final String client) {

        final AlertDialog loading = new ProgressDialog(getContext());
        loading.setMessage("Checking...");
        loading.show();

        Map<String, String> params = new Hashtable<String, String>();
        params.put("update_profile","true");
        params.put("name", First);
        params.put("username", Last);
        params.put("phone", Phone);
        params.put("pin", pincode);
        params.put("dob", dob);
        params.put("gender", gender2);
        params.put("client_id", client);

        CustomRequest jsonRequest = new CustomRequest(Request.Method.POST, Constant.Base_url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Boolean status = null;
                String msg = "";

                try {
                    status = response.getBoolean("resoponse");
                    Toast.makeText(getActivity(),String.valueOf(status), LENGTH_SHORT).show();
                    if (status) {
                        loading.dismiss();
                        msg = response.getString("data");

                        SharedPreferences.Editor editors = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                        editors.putString("firstname", First);
                        editors.putString("username", Last);
                        editors.putString("phone", Phone);
                        editors.putString("pincode", pincode);
                        editors.putString("dob", dob);
                        editors.putString("gender", gender2);
                        editors.apply();

                        makeText(getContext(), msg, LENGTH_SHORT).show();

                        Fragment fragment = new Dashboard();
                        FragmentTransaction transaction2 = getActivity().getSupportFragmentManager().beginTransaction();
                        transaction2.replace(R.id.nav_fragment, fragment);
                        transaction2.commit();

                    } else {
                        loading.dismiss();
                        String error = response.getString("error");
                        Toast.makeText(getContext(),error, LENGTH_SHORT).show();                    }
                } catch (JSONException e) {
                    loading.dismiss();
                    makeText(getActivity(), "Something Went Wrong", LENGTH_SHORT).show();
                }

            }
        }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                makeText(getContext(), "Something went wrong" + error, LENGTH_LONG).show();
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
    private void showData( final String ID) {
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, Constant.Base_url+"?client_id="+ID+"&profile_data=true", new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Boolean status = null;
                String msg = "";

                try {
                    status = response.getBoolean("response");
                    if(status){

                        JSONArray jsonArray = response.getJSONArray("data");
//                        Toast.makeText(Provider_Detail.this, response.getString("data"), LENGTH_SHORT).show();
                        for (int i=0;i<jsonArray.length();i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            first.setText(object.getString("name"));
                            last.setText(object.getString("username"));
                            phone.setText(object.getString("phone"));
                            dob.setText(object.getString("dob"));
                            pincode.setText(object.getString("pin_code"));
                            String gender1=object.getString("gender");
                            if (gender1.equalsIgnoreCase("male")){
                                r1.setChecked(true);
                            }else if (gender1.equalsIgnoreCase("female"))
                            {
                                r2.setChecked(true);
                            }else if (gender1.equalsIgnoreCase("others"))
                            {
                                r3.setChecked(true);
                            }

                        }

                    } else {

                        String error = response.getString("error");
                        Toast.makeText(getContext(),error, LENGTH_SHORT).show();                    }
                } catch (JSONException e) {

                    makeText(getActivity(), "Something Went Wrong", LENGTH_SHORT).show();
                }

            }
        }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                makeText(getContext(), "Something went wrong" + error, LENGTH_LONG).show();
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


//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (resultCode == RESULT_OK) {
//            if (requestCode == RESULT_LOAD_IMAGE) {
//                Picasso.with(getContext()).load(data.getData()).noPlaceholder()
//                        .into(image);
//                uri = data.getData();
//
//            }
//        }
//    }
}
