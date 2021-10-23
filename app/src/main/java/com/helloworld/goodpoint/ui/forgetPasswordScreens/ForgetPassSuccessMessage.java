package com.helloworld.goodpoint.ui.forgetPasswordScreens;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.helloworld.goodpoint.R;
import com.helloworld.goodpoint.ui.MainActivity;
import com.helloworld.goodpoint.ui.SigninActivity;

public class ForgetPassSuccessMessage extends AppCompatActivity {
    private Button login_btn;
    private ImageView back_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pass_success_message);
        login_btn = (Button) findViewById(R.id.login_btn);
        back_btn = (ImageView) findViewById(R.id.back_btn);

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SigninActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}