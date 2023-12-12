package com.example.fatawa;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 1000;

    Animation middleAnimation;
    TextView middleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        middleAnimation = AnimationUtils.loadAnimation(this, R.anim.middle_animation);

        middleView = findViewById(R.id.middleView);
        middleView.setAnimation(middleAnimation);

        final Intent intent = new Intent(this, MainActivity2.class);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(intent);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}