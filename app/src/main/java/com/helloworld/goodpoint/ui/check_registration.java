package com.helloworld.goodpoint.ui;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;

import android.content.Intent;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.helloworld.goodpoint.R;

public class check_registration extends AppCompatActivity {
    ImageView done;
    Button gotoLogin;
    AnimatedVectorDrawableCompat avd;
    AnimatedVectorDrawable  avd2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_registration);
        gotoLogin = findViewById(R.id.goto_login);
        done =  findViewById(R.id.done);
        Drawable drawable = done.getDrawable();
        if(drawable instanceof AnimatedVectorDrawableCompat){
            avd =(AnimatedVectorDrawableCompat) drawable;
            avd.start();
        }
        else  if(drawable instanceof  AnimatedVectorDrawable){
            avd2 = (AnimatedVectorDrawable) drawable;
            avd2.start();
        }
        gotoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(check_registration.this,SigninActivity.class));
                finish();
            }
        });

    }
}