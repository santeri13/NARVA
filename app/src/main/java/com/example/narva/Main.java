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
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.ARN.Narva.UnityPlayerActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Main extends AppCompatActivity{

    private TextView textView,name;
    private Button button1,button2;
    private static final String TAG = "Main";
    private static final int ERROR_DIALOG_REQUEST = 9001;
    private ImageView imageButton;
    String uid;
    DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Window g = getWindow();
        g.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.TYPE_STATUS_BAR);
        textView = (TextView) findViewById(R.id.textView4);
        name = (TextView)findViewById(R.id.name);
        imageButton = findViewById(R.id.logoutimage);
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
                textView.setText("");
            }

        }
        if (isServiceOk()) {
            init();
        }
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intToMain = new Intent(Main.this, MainActivity.class);
                startActivity(intToMain);
            }
        });
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();
        database = FirebaseDatabase.getInstance().getReference();

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String user1 = dataSnapshot.child("user").child(uid).child("username").getValue(String.class);
                name.setText(user1);
                Log.d("TAG", "This is text");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    public void openTours() {
        Intent intent = new Intent(this, nav.class);
        startActivity(intent);
    }
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
    private void init(){
        button1 = findViewById(R.id.button3);
        button2=findViewById(R.id.button5);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTours();
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Unity();
            }
        });
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    public void Unity() {
        Intent intent = new Intent(this, UnityPlayerActivity.class);
        startActivity(intent);
    }
}
