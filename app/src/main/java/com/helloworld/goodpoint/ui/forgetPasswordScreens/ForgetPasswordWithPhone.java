package com.helloworld.goodpoint.ui.forgetPasswordScreens;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.helloworld.goodpoint.R;

public class ForgetPasswordWithPhone extends AppCompatActivity {

    private Button next_btn;
    private ImageView back_btn;
    private EditText phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password_with_phone);
        next_btn = (Button) findViewById(R.id.Next_btn);
        back_btn = (ImageView) findViewById(R.id.back_btn);
        phone = (EditText) findViewById(R.id.Phone);
        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneInput = phone.getText().toString().trim();
                if (phoneInput.isEmpty()) {
                    phone.setError("Field can't be empty");
                    phone.requestFocus();
                } else if (phone.length() < 11) {
                    phone.setError("Please enter a valid phone number");
                    phone.requestFocus();
                } else {
                    startActivity(new Intent(ForgetPasswordWithPhone.this, VerifiyCode.class));

                }
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