package com.example.runcatch;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class CollectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);

        // 홈
        Button homeNavButton = findViewById(R.id.homeNavButton);

        homeNavButton.setOnClickListener(v -> {
            Intent intent = new Intent(CollectionActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        // 러닝
        Button runningNavButton = findViewById(R.id.runningNavButton);

        runningNavButton.setOnClickListener(v -> {
            Intent intent = new Intent(CollectionActivity.this, RunningActivity.class);
            startActivity(intent);
        });

        // 도감
        Button collectionNavButton = findViewById(R.id.collectionNavButton);

        collectionNavButton.setOnClickListener(v -> {
            // 현재 이미 도감 화면이므로 아무것도 하지 않음
        });

        // My
        Button myNavButton = findViewById(R.id.myNavButton);

        myNavButton.setOnClickListener(v -> {
            Intent intent = new Intent(CollectionActivity.this, MyActivity.class);
            startActivity(intent);
            finish();
        });
    }
}