package com.example.buy4all4;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ChangeMail extends AppCompatActivity {
    private Button updatemail, backmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_mail);

        updatemail = findViewById(R.id.mailchen);
        updatemail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ChangeMail.this, "Mail updated successfully!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ChangeMail.this, ProfileFragment.class);
                startActivity(intent);
            }
        });

        backmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}

