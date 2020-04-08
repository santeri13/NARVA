package com.example.narva;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Slider extends AppCompatActivity {

    private ViewPager mPager;
    private int[] layouts = {R.layout.plan, R.layout.discovery,R.layout.connect};
    private SliderAdapter slider;
    private LinearLayout mDotsLayout;
    private TextView[]mDots;
    private Button button;
    SharedPreferences sharedPreferences;
    Boolean firstTime;
    String FILENAME = "Name";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slider);
        Window g = getWindow();
        g.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.TYPE_STATUS_BAR);
        try{
            FileOutputStream fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
            fos.close();
        }catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }
        sharedPreferences = getSharedPreferences("MyPrefs",MODE_PRIVATE);
        firstTime = sharedPreferences.getBoolean("firstTime",true);
        //if (firstTime){
            //new Handler().postDelayed(new Runnable() {
                //@Override
                //public void run() {

                    //SharedPreferences.Editor editor = sharedPreferences.edit();
                    //firstTime = false;
                    //editor.putBoolean("firstTime",firstTime);
                    //editor.apply();

                    //Intent i  = new Intent(Slider.this,Main.class);
                    //startActivity(i);
                    //finish();
                //}
            //}, 5000);
        //}
        mPager = (ViewPager)findViewById(R.id.viewPager);
        mDotsLayout = (LinearLayout) findViewById(R.id.dotsLayout);
        slider = new SliderAdapter (layouts,this);
        mPager.setAdapter(slider);
        addDotsIndicator(0);
        mPager.addOnPageChangeListener(viewListener);
        button = findViewById(R.id.button7);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMain();
            }
        });
    }
    public void addDotsIndicator(int  posistion){
        mDots = new TextView[3];
        mDotsLayout.removeAllViews();
        for (int i= 0; i < mDots.length; i++){
            mDots[i] = new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226;",1));
            mDots[i].setTextSize(35);
            mDots[i].setTextColor(Color.parseColor("#00ffffff"));
            mDots[i].setBackground(getResources().getDrawable(R.drawable.ring));

            mDotsLayout.addView(mDots[i]);
        }
        if (mDots.length > 0){
            mDots[posistion].setTextColor(Color.parseColor("#ffffff"));
        }
    }
    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }
        @Override
        public void onPageSelected(int position) {
            addDotsIndicator(position);
        }
        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
    public void openMain(){
            Intent intent = new Intent(this, Register.class);
            startActivity(intent);
            finish();

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}