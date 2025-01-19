package com.example.buy4all4;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MyAnouncments extends AppCompatActivity {
    private Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_anouncments);

        back = findViewById(R.id.BackButanounc);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyAnouncments.this, com.example.buy4all4.ProfileActivity.class);
                startActivity(intent);
            }
        });
    }
}
