package com.example.buy4all4;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class Update extends AppCompatActivity {
    private Button mail, username, password, back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        mail = findViewById(R.id.mail);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        back = findViewById(R.id.BackButupdate);

        mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Update.this, ChangeMail.class);
                startActivity(intent);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Update.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

        username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Update.this, ChangeUsername.class);
                startActivity(intent);
            }
        });

        password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Update.this, ChangePassword.class);
                startActivity(intent);
            }
        });

    }
}
