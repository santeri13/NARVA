package com.example.narva;
import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.ARN.Narva.UnityPlayerActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class Gps extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,TaskLoadedCallback{
    private static final int REQUEST_CHECK_SETTINGS = 0x1;
    private static GoogleApiClient mGoogleApiClient;
    private static final int ACCESS_FINE_LOCATION_INTENT_ID = 3;
    private GoogleMap mMap;
    LocationRequest mLocationRequest;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    LatLng latLng,lion,prom,plats;
    MarkerOptions lionmarker,prommarker,platsmarker, markerOptions1;
    private String locastion;
    private Polyline currentPolyline;
    double latitude, longtitude;
    double end_latitude,end_longtitude;
    TextView textTitle,points;
    ArrayList arrayList;
    DatabaseReference mreference;
    String title;
    RecyclerView recyclerView;
    private List<PointReader> artistList;
    private PointAdapter adapter;
    Context context;
    String uid;
    DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);
        Window g = getWindow();
        g.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.TYPE_STATUS_BAR);
        Intent i = getIntent();
        points = (TextView)findViewById(R.id.points);
        title = i.getStringExtra("title");
        textTitle = findViewById(R.id.tour_text);
        textTitle.setText(title);
        mreference = FirebaseDatabase.getInstance().getReference().child("Tours").child(title).child("MainCoordinates");
        mreference.keepSynced(true);
        mreference.addValueEventListener(valueEventListener);
        recyclerView = (RecyclerView)findViewById(R.id.setpath);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager ilm = new LinearLayoutManager(this);
        ilm.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(ilm);
        artistList = new ArrayList<>();
        adapter = new PointAdapter(this,artistList);
        recyclerView.setAdapter(adapter);
        lionmarker = new MarkerOptions().position(new LatLng(59.373015, 28.200559)).title("Swedish lion statue in Narva");
        prommarker = new MarkerOptions().position(new LatLng(59.377580, 28.203154)).title("Narva Promenaad");
        platsmarker = new MarkerOptions().position(new LatLng(59.379110, 28.198908)).title("Raekoja plats");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        initGoogleAPIClient();
        checkPermissions();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();
        database = FirebaseDatabase.getInstance().getReference();

        if (user.isAnonymous()) {
            points.setText(0);
        }
        else{
            database.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Long user1 = dataSnapshot.child("user").child(uid).child("points").getValue(Long.class);
                    String user2 = user1.toString().trim();
                    points.setText(user2);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    ValueEventListener valueEventListener = (new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            artistList.clear();
            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    PointReader artist = snapshot.getValue(PointReader.class);
                    artistList.add(artist);
                }
                adapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    });
    public void Unity() {
        Intent intent = new Intent(this, UnityPlayerActivity.class);
        startActivity(intent);
    }
    /* Initiate Google API Client  */
    private void initGoogleAPIClient() {
        //Without Google API Client Auto Location Dialog will not work
        mGoogleApiClient = new GoogleApiClient.Builder(Gps.this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    /* Check Location Permission for Marshmallow Devices */
    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(Gps.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)
            requestLocationPermission();
        else
            showSettingDialog();

    }

    /*  Show Popup to access User Permission  */
    private void requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(Gps.this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
            ActivityCompat.requestPermissions(Gps.this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    ACCESS_FINE_LOCATION_INTENT_ID);

        } else {
            ActivityCompat.requestPermissions(Gps.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    ACCESS_FINE_LOCATION_INTENT_ID);
        }
    }

    /* Show Location Access Dialog */
    private void showSettingDialog() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);//Setting priotity of Location request to high
        locationRequest.setInterval(30 * 1000);
        locationRequest.setFastestInterval(5 * 1000);//5 sec Time interval for location update
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true); //this is the key ingredient to show dialog always when GPS is off

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can initialize location
                        // requests here.
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied. But could be fixed by showing the user
                        // a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(Gps.this, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            e.printStackTrace();
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.
                        break;
                }
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case RESULT_OK:
                        Log.e("Settings", "Result OK");
                        //startLocationUpdates();
                        break;
                    case RESULT_CANCELED:
                        Log.e("Settings", "Result Cancel");
                        break;
                }
                break;
        }
    }

    /* Broadcast receiver to check status of GPS */


    /* On Request permission method to check the permisison is granted or not for Marshmallow+ Devices  */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    //Main method for GoogleMap to manipulate with information which you see on map
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        try{
            boolean success = googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this,R.raw.arn_map));

        }catch(Resources.NotFoundException e){
            Log.e("Gps","Cant find style");
        }
        mMap.setMaxZoomPreference(17.0f);
            lion = new LatLng(59.372404, 28.200153);
            prom = new LatLng(59.377580, 28.203154);
            end_latitude = prom.latitude;
            end_longtitude = prom.longitude;
            plats = new LatLng(59.379110, 28.198908);
            mMap.addMarker(lionmarker);
            mMap.addMarker(prommarker);
            mMap.addMarker(platsmarker);
            mMap.addPolyline(new PolylineOptions().add(
                    lion,
                    new LatLng(59.373015, 28.200559),

                    new LatLng(59.372404, 28.200153),

                    new LatLng(59.372351, 28.200274),

                    new LatLng(59.372324, 28.200365),

                    new LatLng(59.372296, 28.200473),

                    new LatLng(59.372293, 28.200580),

                    new LatLng(59.372283, 28.200752),

                    new LatLng(59.372422, 28.200693),

                    new LatLng(59.372624, 28.200773),

                    new LatLng(59.372884, 28.201009),

                    new LatLng(59.373212, 28.201108),

                    new LatLng(59.374442, 28.201466),

                    new LatLng(59.375224, 28.202039),

                    new LatLng(59.377358, 28.202929),

                    prom)

                    .width(10)

                    .color(Color.MAGENTA)
            );
            mMap.addPolyline(new PolylineOptions().add(

                    prom,
                    new LatLng(59.377358, 28.202929),

                    new LatLng(59.376870, 28.202709),

                    new LatLng(59.376916, 28.202183),

                    new LatLng(59.377263, 28.202210),

                    new LatLng(59.377345, 28.202505),

                    new LatLng(59.377683, 28.202811),

                    new LatLng(59.377802, 28.202639),

                    new LatLng(59.378003, 28.202776),

                    new LatLng(59.378019, 28.202711),

                    new LatLng(59.378291, 28.201186),

                    new LatLng(59.378353, 28.199758),

                    new LatLng(59.378395, 28.198703),

                    new LatLng(59.378708, 28.198875),

                    plats)

                    .width(10)

                    .color(Color.BLUE)



            );
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(59.373062, 28.200594), 15),5000,null);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            //Location Permission already granted
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        } else {
            //Request Location Permission
            checkLocationPermission();
        }

    }

    //Put information on GoogleMap which we use in omMapReady method.
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    //Each 2-3 seconds method activate to change information on map.
    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        TextView textView = findViewById(R.id.meters);
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }
        //Place current location marker
        latLng = new LatLng(location.getLatitude(), location.getLongitude());
        latitude = location.getLatitude();
        longtitude = location.getLongitude();
        markerOptions1 = new MarkerOptions().position( new LatLng (location.getLatitude(), location.getLongitude())).title("Current Position");
        markerOptions1.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        new FetchURL(Gps.this).execute(getUrl(markerOptions1.getPosition(), lionmarker.getPosition(), "walking"), "walking");
        float result[] = new float[1];
        Location.distanceBetween(latitude,longtitude,end_latitude,end_longtitude,result);
        for(int i = 0, n = result.length; i <n ;i++){
            textView.setText((int) result[i]+"m");
            if(result[i]<=50){
                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        Unity();
                        return false;
                    }
                });
            }
        }

    }
    private String getUrl(LatLng origin, LatLng dest, String directionMode) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Mode
        String mode = "mode=" + directionMode;
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getString(R.string.google_maps_API__key);
        return url;
    }
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(Gps.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION );
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION );
            }
        }
    }
    //If you go to another app on phone then it stop work but not close.
    @Override
    public void onPause() {
        super.onPause();

        //stop location updates when Activity is no longer active
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    @Override
    public void onTaskDone(Object... values) {
        if (currentPolyline != null)
            currentPolyline.remove();
        currentPolyline = mMap.addPolyline((PolylineOptions) values[0]);
    }

}
