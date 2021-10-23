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

public class ForgetPasswordWithEmail extends AppCompatActivity {
    private Button next_btn;
    private EditText email;
    private ImageView back_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        next_btn = (Button) findViewById(R.id.Next_btn);
        back_btn = (ImageView) findViewById(R.id.back_btn);
        email = (EditText) findViewById(R.id.Email);
        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailInput = email.getText().toString().trim();
                if (emailInput.isEmpty()) {
                    email.setError("Field can't be empty");
                    email.requestFocus();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
                    email.setError("Please enter a valid email address");
                    email.requestFocus();
                } else {
                    startActivity(new Intent(ForgetPasswordWithEmail.this, VerifiyCode.class));

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