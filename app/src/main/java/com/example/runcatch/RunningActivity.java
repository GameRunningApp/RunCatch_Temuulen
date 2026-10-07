package com.example.runcatch;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;

public class RunningActivity extends AppCompatActivity {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 100;

    private TextView statusText;
    private TextView runningDistanceText;
    private Button startRunningButton;
    private Button pauseRunningButton;

    private boolean isRunning = false;
    private boolean isPaused = false;

    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationCallback locationCallback;
    private LocationRequest locationRequest;
    private Location previousLocation;

    private float totalDistance = 0;
    private long startTime;
    private long pauseStartTime;
    private long totalPausedTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_running);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        locationRequest = new LocationRequest.Builder(
                Priority.PRIORITY_HIGH_ACCURACY, 2000
        ).setMinUpdateIntervalMillis(1000).build();

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult){
                for(Location location : locationResult.getLocations()){
                    if(previousLocation != null){
                        float distance = previousLocation.distanceTo(location);

                        totalDistance += distance;

                        runningDistanceText.setText(
                                String.format("%.2f Km", totalDistance / 1000)
                        );
                    }

                    previousLocation = location;
                }
            }
        };

        statusText = findViewById(R.id.statusText);
        runningDistanceText = findViewById(R.id.runningDistanceText);
        startRunningButton = findViewById(R.id.startRunningButton);
        pauseRunningButton = findViewById(R.id.pauseRunningButton);

        pauseRunningButton.setVisibility(View.GONE);

        startRunningButton.setOnClickListener(v -> {

            if (!isRunning) {

                // GPS 권한 확인
                if (ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(
                            this,
                            new String[]{
                                    Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_COARSE_LOCATION
                            },
                            LOCATION_PERMISSION_REQUEST_CODE
                    );

                    return;
                }

                fusedLocationProviderClient.requestLocationUpdates(
                        locationRequest,
                        locationCallback,
                        getMainLooper()
                );
                //시작 시간 측정 시작
                startTime = System.currentTimeMillis();

                // GPS 권한이 이미 있는 경우
                isRunning = true;

                statusText.setText("러닝 중");
                startRunningButton.setText("러닝 종료");

                //일시정지 버튼 보이기
                pauseRunningButton.setVisibility(View.VISIBLE);

            } else {

                //Gps 위치 측정 종료
                fusedLocationProviderClient.removeLocationUpdates(locationCallback);

                //끝나는 시간 계산
                long endTime = System.currentTimeMillis();
                //일시정지 상태일 때 러닝 종료시 마지막  일시정지 시간을 더함
                if(isPaused){
                    totalPausedTime += endTime - pauseStartTime;
                }
                //걸린 시간 계산
                long elapsedTime = endTime - startTime - totalPausedTime;

                // 러닝 종료
                isRunning = false;

                //결과 화면으로 이동
                Intent intent = new Intent(RunningActivity.this, ResultActivity.class);

                //이동한 거리 전달
                intent.putExtra("distance", totalDistance / 1000);
                //걸린 시간 전달
                intent.putExtra("elapsedTime", elapsedTime);

                startActivity(intent);

                //현재 화면 종료
                finish();
            }
        });
        pauseRunningButton.setOnClickListener(v -> {
            if(!isPaused){

                isPaused = true;

                pauseStartTime = System.currentTimeMillis();

                //Gps 중지
                fusedLocationProviderClient.removeLocationUpdates(locationCallback);

                pauseRunningButton.setText("계속하기");
            }
            else{

                isPaused = false;

                long pauseDuration = System.currentTimeMillis() - pauseStartTime;
                totalPausedTime += pauseDuration;

                //Gps 재개
                fusedLocationProviderClient.requestLocationUpdates(
                        locationRequest,
                        locationCallback,
                        getMainLooper()
                );

                pauseRunningButton.setText("일시정지");
            }
        });
    }
}