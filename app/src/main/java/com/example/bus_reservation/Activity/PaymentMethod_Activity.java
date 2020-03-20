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
    ElasticButton confirm;
    String Paymenttype,Totalprice;
    TextView back;
    RadioButton paypalr,striper;
    RadioGroup radioGroup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_method_);
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


        paypalr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                striper.setChecked(false);
            }
        });
    striper.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            paypalr.setChecked(false);
        }
    });

    radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch(checkedId){

                case R.id.paypalrb:
                    striper.setChecked(false);
                    break;

                case R.id.striperb:
                    paypalr.setChecked(false);
                    break;
                default:
                    break;
            }
        }
    });
    }
}
