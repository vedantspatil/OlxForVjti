package com.example.vedan.olxforvjti;

import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        ImageView imageView = findViewById(R.id.splashim);
        TextView textView = findViewById(R.id.splashtext);
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.splashanimation);
        imageView.startAnimation(animation);


        Thread t = new Thread(){
            @Override
            public void run() {
                try {
                    sleep(3000);
                    Intent intent = new Intent(SplashScreen.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                    super.run();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        };

        t.start();
    }
}
