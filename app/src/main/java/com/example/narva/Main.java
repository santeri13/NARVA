package com.example.narva;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

//import com.DefaultCompany.Android.UnityPlayerActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Main extends AppCompatActivity implements LocationListener {

    private TextView textView;
    private Button button1;
    private static final String TAG = "Main";
    private static final int ERROR_DIALOG_REQUEST = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        textView = (TextView) findViewById(R.id.textView4);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1000);
        } else {
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            try {
                String city = citylocation(location.getLatitude(), location.getLongitude());
                String country = countrylocation(location.getLatitude(), location.getLongitude());
                textView.setText(city + " , " + country);
            } catch (Exception e) {
                textView.setText("Not found");
            }

        }
        if (isServiceOk()) {
            init();
        }
    }


    //move to Choose_Location class
    public void openTours() {
        Intent intent = new Intent(this, Choose_Location.class);
        startActivity(intent);
    }
    //if you like to test  unity then uncommit 71-74 rows and 24 row(remove this //), then in Gradle Script => build.gradle (Module:app) uncommit 43-39 rows and uncommit build.gradle(Project: arcoreclient) 19-21
    //public void openUnity(){
        //Intent intent = new Intent(this, UnityPlayerActivity.class);
        //startActivity(intent);
    //}

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1000: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    Activity#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for Activity#requestPermissions for more details.
                        return;
                    }
                    Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    try{
                        String city = citylocation(location.getLatitude(),location.getLongitude());
                        String country = countrylocation(location.getLatitude(),location.getLongitude());
                        textView.setText(city + ","+country);
                    }catch(Exception e){
                        textView.setText("Not found");
                    }
                }
                else{
                    Toast.makeText(this,"Permission not granted", Toast.LENGTH_LONG).show();
                }
                break;
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
    //Search city
    private String citylocation(double latitude, double longtitude){
        String cityName = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses;
        try{
            addresses = geocoder.getFromLocation(latitude,longtitude,10);
            if(addresses.size()>0){
                for(Address adr: addresses){
                    if(adr.getLocality() !=null && adr.getLocality().length()>0){
                        cityName = adr.getLocality();
                        break;
                    }
                }
            }
        } catch (IOException e){
            textView.setText("Location not found");
        }
        return cityName;
    }
    //Search country
    private String countrylocation(double latitude, double longtitude){
        String countryName = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address>addresses;
        try{
            addresses = geocoder.getFromLocation(latitude,longtitude,10);
            if(addresses.size()>0){
                for(Address adr: addresses){
                    if(adr.getCountryName() !=null && adr.getCountryName().length()>0){
                        countryName = adr.getCountryName();
                        break;
                    }
                }
            }
        } catch (IOException e){
            textView.setText("Location not found");
        }
        return countryName;
    }
    //Control if all services is ok
    public boolean isServiceOk(){
        Log.d(TAG, "isServiceOK: checking google service version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(Main.this);

        if(available == ConnectionResult.SUCCESS){
            Log.d(TAG, "isServiceOK: Google Play Services is working");
            return true;
        }
        else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            Log.d(TAG,"isServiceOK: an error occured but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(Main.this,available, ERROR_DIALOG_REQUEST);
        }
        else{
            Toast.makeText(this, "You can't make app request", Toast.LENGTH_SHORT).show();
        }
        return false;
    }
    //On button click move to openTours method
    private void init(){
        button1 = findViewById(R.id.button3);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTours();
            }
        });
    }
}
