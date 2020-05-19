package com.example.narva;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private Button button, Register,Anonymous;
    private EditText email,password;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener mfirebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        Window g = getWindow();
        g.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.TYPE_STATUS_BAR);
        button = findViewById(R.id.button2);
        firebaseAuth = FirebaseAuth.getInstance();
        email = findViewById(R.id.editText4);
        password = findViewById(R.id.editText5);
        mfirebaseAuth = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mFirebaseUser = firebaseAuth.getCurrentUser();
                if( mFirebaseUser != null ){
                    Toast.makeText(MainActivity.this,"You are logged in",Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(MainActivity.this, Main.class);
                    startActivity(i);
                }
                else{
                    Toast.makeText(MainActivity.this,"Please Login",Toast.LENGTH_SHORT).show();
                }
            }
        };
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Email = email.getText().toString();
                String Password = password.getText().toString();
                if(Email.isEmpty()){
                    email.setError("Please enter email id");
                    email.requestFocus();
                }
                else  if(Password.isEmpty()){
                    password.setError("Please enter your password");
                    password.requestFocus();
                }
                else  if(Email.isEmpty() && Password.isEmpty()){
                    Toast.makeText(MainActivity.this,"Fields Are Empty!",Toast.LENGTH_SHORT).show();
                }
                else  if(!(Email.isEmpty() && Password.isEmpty())){
                    firebaseAuth.signInWithEmailAndPassword(Email, Password).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                Toast.makeText(MainActivity.this,"Login Error, Please Login Again",Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Intent intToHome = new Intent(MainActivity.this,Main.class);
                                startActivity(intToHome);
                            }
                        }
                    });
                }
                else{
                    Toast.makeText(MainActivity.this,"Error Occurred!",Toast.LENGTH_SHORT).show();

                }
            }
        });
        Register = findViewById(R.id.buttonRegister);
        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRegister();
            }
        });
        Anonymous = findViewById(R.id.Anonymous);
        Anonymous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSlider();
            }
        });


    }
    public void openSlider(){
        Intent intent = new Intent(this, Slider.class);
        startActivity(intent);
    }
    public void openRegister(){
        Intent intent = new Intent(this, Register.class);
        startActivity(intent);
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
    protected void onStop() {
        super.onStop();
    }
    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(mfirebaseAuth);
    }
}
