package com.example.bus_reservation.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.bus_reservation.R;

public class SkipActivity extends AppCompatActivity {
    TextView join;
    TextView skip;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skip);
        skip = findViewById(R.id.skip);
        join = findViewById(R.id.join);


        // Set up the user interaction to manually show or hide the system UI.
        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SkipActivity.this, Login.class));
                finish();
            }
        });

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SkipActivity.this,Menu.class));
                finish();
            }
        });
    }
}
