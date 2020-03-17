package com.example.bus_reservation.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bus_reservation.R;

import java.util.ArrayList;

public class price_detail extends AppCompatActivity {

    CheckBox checkBox;
    TextView back;
    TextView Price,Tprice,Seat,Pickup,Dropup,Route;
    Button button;
    ArrayList<String> temp,name,number;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_price_detail);

        checkBox=findViewById(R.id.check_id);
        back=findViewById(R.id.back);
        button=findViewById(R.id.next);
        Route=findViewById(R.id.route_id);
        Dropup=findViewById(R.id.dropup);
        Pickup=findViewById(R.id.pickup);
        Seat=findViewById(R.id.seats);
        Tprice=findViewById(R.id.total_price);
        Price=findViewById(R.id.price_id);

        temp = new ArrayList<String>();
        name = new ArrayList<String>();
        number = new ArrayList<String>();


        temp = this.getIntent().getStringArrayListExtra("seat");
        name= this.getIntent().getStringArrayListExtra("Name");
        number= this.getIntent().getStringArrayListExtra("Number");

        final String price = getIntent().getStringExtra("price");
        final String pick = getIntent().getStringExtra("pick");
        final String drop = getIntent().getStringExtra("drop");
        final String routn = getIntent().getStringExtra("routn");

        final String rout_id = getIntent().getStringExtra("rout_id");
        final String booking_date = getIntent().getStringExtra("booking_date");
        final String trip_id = getIntent().getStringExtra("trip_id");
        final String fleet_id = getIntent().getStringExtra("fleet_id");

        Route.setText(routn);

        Price.setText(price);
        Dropup.setText(drop);
        Pickup.setText(pick);
        int a = Integer.parseInt(price);
        int b = temp.size();
        final int z = a*b;
        Tprice.setText(String.valueOf(z));
        Seat.setText(String.valueOf(b));

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (checkBox.isChecked()) {

                    int size = temp.size();

                    Intent i = new Intent(price_detail.this, Payumoney.class);
                    i.putExtra("size",String.valueOf(size));
                    i.putExtra("price",String.valueOf(z));
                    i.putExtra("pick",pick);
                    i.putExtra("drop",drop);
                    i.putExtra("routn",routn);
                    i.putStringArrayListExtra("seat",temp);
                    i.putStringArrayListExtra("Name",name);
                    i.putStringArrayListExtra("Number",number);
                    i.putExtra("rout_id",rout_id);
                    i.putExtra("fleet_id",fleet_id);
                    i.putExtra("trip_id",trip_id);
                    i.putExtra("booking_date",booking_date);
                    startActivity(i);
                }
                else {
                    Toast.makeText(price_detail.this,"Please Check Agree And Continue",Toast.LENGTH_SHORT).show();
                }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name.clear();
                number.clear();
                finish();
            }
        });
    }
    public void onBackPressed(){
        name.clear();
        number.clear();
        finish();
    }

}
