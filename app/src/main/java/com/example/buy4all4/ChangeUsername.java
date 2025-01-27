package com.example.buy4all4;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ChangeUsername extends AppCompatActivity {
    private Button updateusername, backusername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_username);

        updateusername = findViewById(R.id.uschen);
        //backusername = findViewById(R.id.backbutuser);

        updateusername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ChangeUsername.this, "Username updated successfully!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ChangeUsername.this, ProfileFragment.class);
                startActivity(intent);
            }
        });

//        backusername.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//           }
//       });
    }
}