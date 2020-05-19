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
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {

    Button button;
    private EditText email,password,nickname;
    private FirebaseAuth firebaseAuth;
    DatabaseReference databsereference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Window g = getWindow();
        g.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.TYPE_STATUS_BAR);
        button = (Button) findViewById(R.id.Enter);
        firebaseAuth = FirebaseAuth.getInstance();
        databsereference = FirebaseDatabase.getInstance().getReference("user");
        email = findViewById(R.id.e_mail);
        password = findViewById(R.id.password);
        nickname = findViewById(R.id.name_enter);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Email = email.getText().toString();
                String Password = password.getText().toString();
                String Name = nickname.getText().toString();
                if(Email.isEmpty()){
                    email.setError("Please enter email id");
                    email.requestFocus();
                }
                else  if(Password.isEmpty()){
                    password.setError("Please enter your password");
                    password.requestFocus();
                }
                else  if(Email.isEmpty() && Password.isEmpty()){
                    Toast.makeText(Register.this,"Fields Are Empty!",Toast.LENGTH_SHORT).show();
                }
                else  if(!(Email.isEmpty() && Password.isEmpty())){
                    firebaseAuth.createUserWithEmailAndPassword(Email, Password).addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                Toast.makeText(Register.this,"SignUp Unsuccessful, Please Try Again",Toast.LENGTH_SHORT).show();
                            }
                            else {
                                addUser();
                                startActivity(new Intent(Register.this,Slider.class));
                            }
                        }

                    });
                }
                else{
                    Toast.makeText(Register.this,"Error Occurred!",Toast.LENGTH_SHORT).show();

                }
            }
        });
    }
    private void addUser(){
        String Email = email.getText().toString();
        String Name = nickname.getText().toString();
        String Password = password.getText().toString();
        String id = databsereference.push().getKey();
        RegisterDatabase registerDatabase = new RegisterDatabase(id,Name,Email,Password);
        databsereference.child(id).setValue(registerDatabase);
    }
}
