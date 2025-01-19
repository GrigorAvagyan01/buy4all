package com.example.buy4all4;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.Firebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity {
    TextInputLayout regUsername, regMail, regPassword, regPassword2;
    private Button loginbut, signup;
    FirebaseDatabase rootNode;
    DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        loginbut = findViewById(R.id.loginbut);
        regMail = findViewById(R.id.email);
        regUsername = findViewById(R.id.username);
        regPassword = findViewById(R.id.password);
        regPassword2 = findViewById(R.id.password2);
        signup = findViewById(R.id.signupButton);


        loginbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rootNode = FirebaseDatabase.getInstance();
                reference = rootNode.getReference("user");
                String username = regUsername.getEditText().getText().toString();
                String mail = regMail.getEditText().getText().toString();
                String password = regPassword.getEditText().getText().toString();
                String password2 = regPassword2.getEditText().getText().toString();


                UserHellperClass hellperClass = new UserHellperClass();
                Intent intent = new Intent(SignUp.this, LogIn.class);
                startActivity(intent);
                reference.setValue("Change the Course data");
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUp.this, LogIn.class);
                startActivity(intent);
            }
        });

    }
}