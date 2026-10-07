package com.example.runcatch;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        // 홈
        Button homeNavButton = findViewById(R.id.homeNavButton);

        homeNavButton.setOnClickListener(v -> {
            Intent intent = new Intent(MyActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        // 러닝
        Button runningNavButton = findViewById(R.id.runningNavButton);

        runningNavButton.setOnClickListener(v -> {
            Intent intent = new Intent(MyActivity.this, RunningActivity.class);
            startActivity(intent);
        });

        // 도감
        Button collectionNavButton = findViewById(R.id.collectionNavButton);

        collectionNavButton.setOnClickListener(v -> {
            Intent intent = new Intent(MyActivity.this, CollectionActivity.class);
            startActivity(intent);
            finish();
        });

        // My
        Button myNavButton = findViewById(R.id.myNavButton);

        myNavButton.setOnClickListener(v -> {
            // 현재 이미 My 화면이므로 아무것도 하지 않음
        });
    }
}