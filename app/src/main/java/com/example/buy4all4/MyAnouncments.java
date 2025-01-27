package com.example.buy4all4;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class MyAnouncments extends AppCompatActivity {
    private ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_anouncments);

        back = findViewById(R.id.backma);

        if (back != null) {
            back.setOnClickListener(v -> {
                Intent intent = new Intent(MyAnouncments.this, ProfileFragment.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                finish();
            });
        }
    }
}
