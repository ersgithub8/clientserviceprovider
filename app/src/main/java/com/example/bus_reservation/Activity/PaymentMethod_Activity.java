package com.example.bus_reservation.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.bus_reservation.R;
import com.skydoves.elasticviews.ElasticButton;

public class PaymentMethod_Activity extends AppCompatActivity {
    ElasticButton paypal,stripe;
    String Paymenttype,Totalprice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_method_);
    paypal=findViewById(R.id.paypalpm);
    stripe=findViewById(R.id.stripepm);

    Paymenttype=getIntent().getStringExtra("paymenttype");
    Totalprice=getIntent().getStringExtra("price");



    paypal.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    });


    stripe.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent=new Intent(PaymentMethod_Activity.this,Payumoney.class);
            intent.putExtra("paymenttype",Paymenttype);
            intent.putExtra("price",Totalprice);
            startActivity(intent);
        }
    });
    }
}
