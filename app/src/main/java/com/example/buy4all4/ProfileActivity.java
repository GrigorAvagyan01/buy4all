package com.example.buy4all4;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {
    private Button update, history, myanouncments, home, service, favorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        history = findViewById(R.id.HistoryBut);
        update = findViewById(R.id.UpdateBut);
        myanouncments = findViewById(R.id.MyAnouncmentsBut);
        home = findViewById(R.id.Homebutprof);
        service = findViewById(R.id.serviceButprof);
        favorite = findViewById(R.id.FavButprof);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, HomePage.class);
                startActivity(intent);
            }
        });

        service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, ServiceActivity.class);
                startActivity(intent);
            }
        });

        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, FavoritesActivity.class);
                startActivity(intent);
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(ProfileActivity.this, com.example.buy4all4.Update.class);
                startActivity(intent);
            }
        });
        myanouncments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(ProfileActivity.this, com.example.buy4all4.MyAnouncments.class);
                startActivity(intent);
            }
        });
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, History.class);
                startActivity(intent);
            }
        });
    }
}

