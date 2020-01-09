package com.example.narva;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

public class Choose_Location extends AppCompatActivity {
    private static final String[] COUNTRIES = new String[]{
            "Narva"
    };

    private static final String TAG = "Choose_Location";

    private static final int ERROR_DIALOG_REQUEST = 9001;

    AutoCompleteTextView textIn;
    RelativeLayout container;
    private static String location;

    private static final String[] Places = new String[] {

    };
    Button buttonAdd;
    Button buttonNext;
    public static String getLocation(){
        return location;
    }

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
        final EditText editText = findViewById(R.id.autoCompleteTextView);
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                location = editText.getText().toString().trim();
                if (location.equals("Narva")) {
                    Intent intent = new Intent(Choose_Location.this, Gps.class);
                    startActivity(intent);
                }else{
                    Context context = getApplicationContext();
                    CharSequence text = "Sorry but this code not right!";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
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
