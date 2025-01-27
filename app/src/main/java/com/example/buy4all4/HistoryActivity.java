package com.example.buy4all4;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class HistoryActivity extends AppCompatActivity {
    private ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        back = findViewById(R.id.backhis);

        if (back != null) {
            back.setOnClickListener(v -> {
                Intent intent = new Intent(HistoryActivity.this, ProfileFragment.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                finish();
            });
        }
    }
}
