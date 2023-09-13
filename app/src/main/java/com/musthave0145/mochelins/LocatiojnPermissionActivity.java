package com.musthave0145.mochelins;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class LocatiojnPermissionActivity extends AppCompatActivity {
    LocationManager locationManager;
    LocationListener locationListener;

    double lat;
    double lng;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locatiojn_permission);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        // 로케이션 리스터를 만든다.
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                // 여러분들의 로직 작성.

                // 위도 가져오는 코드.
                // location.getLatitude();
                // 경도 가져오는 코드.
                // location.getLongitude();

                lat = location.getLatitude();
                lng = location.getLongitude();

                Intent intent = new Intent(LocatiojnPermissionActivity.this, MainActivity.class);
                startActivity(intent);

                finish();

            }
        };

        if( ActivityCompat.checkSelfPermission(LocatiojnPermissionActivity.this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED ){

            ActivityCompat.requestPermissions(LocatiojnPermissionActivity.this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION} ,
                    100);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 100){

            if( ActivityCompat.checkSelfPermission(LocatiojnPermissionActivity.this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED ){

                ActivityCompat.requestPermissions(LocatiojnPermissionActivity.this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION} ,
                        100);
                return;
            }

            // 위치기반 허용하였으므로,
            // 로케이션 매니저에, 리스너를 연결한다. 그러면 동작한다.
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    3000,
                    -1,
                    locationListener);

            Intent intent = new Intent(LocatiojnPermissionActivity.this, MainActivity.class);
            startActivity(intent);

            finish();

        }

    }
}