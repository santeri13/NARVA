package com.example.narva;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Choose_Location extends AppCompatActivity {
    //List of code words for different tours
    private static final String[] TOWNS = new String[]{
            "Narva"
    };

    private static final String TAG = "Choose_Location";

    private static final int ERROR_DIALOG_REQUEST = 9001;

    private static String location;

    Button buttonNext;
    public static String getLocation(){
        return location;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_location);
        Window g = getWindow();
        g.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.TYPE_STATUS_BAR);
        init();


    }
    //Move to Gps class when user click on button
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
}
