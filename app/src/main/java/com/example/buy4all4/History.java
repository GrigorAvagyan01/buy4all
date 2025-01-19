package com.example.buy4all4;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class History extends AppCompatActivity {
    private Button backhis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        backhis = findViewById(R.id.BackButhis);

        backhis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(History.this, com.example.buy4all4.ProfileActivity.class);
                startActivity(intent);
            }
        });

    }
}

