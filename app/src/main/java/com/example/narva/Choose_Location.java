package com.example.narva;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

public class Choose_Location extends AppCompatActivity {

    private static final String TAG = "Choose_Location";

    private static final int ERROR_DIALOG_REQUEST = 9001;

    AutoCompleteTextView textIn;
    RelativeLayout container;

    private static final String[] Places = new String[] {

    };
    Button buttonAdd;
    Button buttonNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_location);

        if (isServiceOK()){
            init();
        }

    }
    private void init(){
        buttonNext = (Button) findViewById(R.id.button7);
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Choose_Location.this, Gps.class);
                startActivity(intent);
            }
        });
    }
    public boolean isServiceOK(){
        Log.d(TAG,"isServiceOK: checking google services version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(Choose_Location.this);
        if(available == ConnectionResult.SUCCESS){
            Log.d(TAG,"isServiceOK: Google Play Service is working");
            return true;
        }
        else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            Log.d(TAG,"isServicesOK: an error occured but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(Choose_Location.this,available,ERROR_DIALOG_REQUEST);
            dialog.show();
        }else{
            Toast.makeText(this, "You cant make map request", Toast.LENGTH_SHORT).show();
        }
        return false;
    }
}
