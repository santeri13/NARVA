package com.example.narva;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Slider extends AppCompatActivity {

    private ViewPager mPager;
    private int[] layouts = {R.layout.plan, R.layout.discovery,R.layout.connect};
    private SliderAdapter slider;
    private LinearLayout mDotsLayout;
    private TextView[]mDots;
    private Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slider);
        Window g = getWindow();
        g.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.TYPE_STATUS_BAR);
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
            Intent intent = new Intent(this, Main.class);
            startActivity(intent);
            finish();

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}