package com.example.bus_reservation.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bus_reservation.R;
import com.skydoves.elasticviews.ElasticButton;

public class PaymentMethod_Activity extends AppCompatActivity {
    ElasticButton paypal,stripe,confirm;
    String Paymenttype,Totalprice;
    TextView back;
    RadioButton paypalr,striper;
    RadioGroup radioGroup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_method_);
    paypal=findViewById(R.id.paypalpm);
    stripe=findViewById(R.id.stripepm);
    confirm=findViewById(R.id.confirm);
    back=findViewById(R.id.backpm);
    radioGroup=findViewById(R.id.radigp);
    paypalr=findViewById(R.id.paypalrb);
    striper=findViewById(R.id.striperb);



    back.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onBackPressed();
        }
    });

    Paymenttype=getIntent().getStringExtra("paymenttype");
    Totalprice=getIntent().getStringExtra("price");



    paypal.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    });


    confirm.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(paypalr.isChecked()){

            }else if(striper.isChecked()){

                Intent intent=new Intent(PaymentMethod_Activity.this,Payumoney.class);
                intent.putExtra("paymenttype",Paymenttype);
                intent.putExtra("price",Totalprice);
                startActivity(intent);
            }else {
                Toast.makeText(PaymentMethod_Activity.this, "Select a Payment Method.", Toast.LENGTH_SHORT).show();
            }
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


    radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch(checkedId){


                case R.id.paypalrb:
                    confirm.setClickable(true);
                    break;

                case R.id.striperb:
                    confirm.setClickable(true);
                    break;
                default:
                    break;
            }
        }
    });
    }
}
