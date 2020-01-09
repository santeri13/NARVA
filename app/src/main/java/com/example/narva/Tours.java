package com.example.narva;
import androidx.appcompat.app.AppCompatActivity;
import android.Manifest;
import android.os.Bundle;


import com.google.android.gms.maps.GoogleMap;



public class Tours extends AppCompatActivity {

    private static final String TAG = "Tours";

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCAL_PERMISSION_REQUEST_CODE = 1234;

    private Boolean mLocationPermissionGranted = false;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tours);
    }
}
