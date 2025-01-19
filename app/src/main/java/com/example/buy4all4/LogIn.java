package com.example.buy4all4;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class LogIn extends AppCompatActivity {
    private Button signupbut, signin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        signupbut = findViewById(R.id.SignUpBut);
        signin = findViewById(R.id.signInButton);
    signupbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(LogIn.this, com.example.buy4all4.SignUp.class);
                startActivity(intent);
            }
        });
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LogIn.this, HomePage.class);
                startActivity(intent);
            }
        });
    }
}

