package com.helloworld.goodpoint.ui;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.helloworld.goodpoint.R;

public class MainActivity extends AppCompatActivity {

    PrefManager prefManager;
    ImageView splash;
    Thread t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        if (prefManager.isFirstTimeLaunch()) {
            prefManager.setFirstTimeLaunch(true);
            startActivity(new Intent(MainActivity.this, WelcomeActivity.class));
            finish();
        }else if(!prefManager.isLoginned().isEmpty()) {
            rotateSplash();
            t = startApp();
        }else {

            startActivity(new Intent(this,SigninActivity.class));
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        t.start();
    }

    @Override
    protected void onPause() {
        t.interrupt();
        super.onPause();
    }

    private void init() {
        prefManager = new PrefManager(getApplicationContext());
        splash = findViewById(R.id.splash_icon);
    }

    private void rotateSplash() {
        Animation animation = AnimationUtils.loadAnimation(this,R.anim.rotate_splash);
        splash.startAnimation(animation);
    }

    private Thread startApp() {
        return new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}