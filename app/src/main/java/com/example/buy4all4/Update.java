package com.example.buy4all4;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class Update extends AppCompatActivity {
    private Button mail, username, password;
    private ImageButton back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        mail = findViewById(R.id.mail);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        back = findViewById(R.id.backupd);

        if (mail != null) mail.setOnClickListener(v -> navigateTo(ChangeMail.class));
        if (username != null) username.setOnClickListener(v -> navigateTo(ChangeUsername.class));
        if (password != null) password.setOnClickListener(v -> navigateTo(ChangePassword.class));
        if (back != null) {
            back.setOnClickListener(v -> {
                navigateTo(ProfileActivity.class);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            });
        }
    }

    private void navigateTo(Class<?> destination) {
        Intent intent = new Intent(Update.this, destination);
        startActivity(intent);
        finish();
    }
}
