package com.example.narva;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;

public class nav extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static String field1;
    String uid;
    DatabaseReference database;
    TextView point, username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav);
        Window g = getWindow();
        g.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.TYPE_STATUS_BAR);
        final DrawerLayout drawerLayout = findViewById(R.id.drawerlayout);
        findViewById(R.id.imageMenu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        username = (TextView)findViewById(R.id.username);
        NavigationView navigation = findViewById(R.id.navigationView);
        navigation.setNavigationItemSelectedListener(this);
        NavController navController = Navigation.findNavController(this, R.id.navHostFragment);
        Navigation.setViewNavController(navigation,navController);
        EditText field1 = (EditText)findViewById(R.id.search);
        field1.setSelection(0);
        field1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Search newFragment = new Search();
                Tours newsecondFragment = new Tours();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                if(field1.length() >= 1){
                    addtown(field1);
                    transaction.replace(R.id.navHostFragment,newFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
                else{
                    transaction.replace(R.id.navHostFragment,newsecondFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    private void addtown(EditText editext){
        field1 = editext.getText().toString();
    }

    public static String getField1() {
        return field1;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.tours:
                getSupportFragmentManager().beginTransaction().replace(R.id.tours, new Tours());
                break;
            case R.id.profile:
                Intent intent2 = new Intent(this,Main.class);
                startActivity(intent2);
                break;
            case R.id.favourites:
                Toast.makeText(this,"Sorry but this function is come in next update",Toast.LENGTH_SHORT).show();
                break;
            case R.id.tourcode:
                Toast.makeText(this,"Sorry but this function is come in next update",Toast.LENGTH_SHORT).show();
                break;
            case R.id.mypoints:
                Toast.makeText(this,"Sorry but this function is come in next update",Toast.LENGTH_SHORT).show();
                break;
            case R.id.help:
                Toast.makeText(this,"Sorry but this function is come in next update",Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }
}
