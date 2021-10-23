package com.helloworld.goodpoint.ui.forgetPasswordScreens;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.helloworld.goodpoint.R;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Pattern;

public class SetNewPassword extends AppCompatActivity {

    private Button ok_btn;
    private ImageView back_btn;
    private EditText pass1, pass2;
    private TextInputLayout errorPass,errorConfirm;
    boolean isEmailValid, isPasswordValid, isPasswordVisible;
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[0-9])" +         //at least 1 digit
                    "(?=.*[a-z])" +         //at least 1 lower case letter
                    "(?=.*[A-Z])" +         //at least 1 upper case letter
                    // "(?=.*[a-zA-Z])" +      //any letter
                    // "(?=.*[@#$%^&+=])" +    //at least 1 special character
                    // "(?=\\S+$)" +           //no white spaces
                    ".{8,}" +               //at least 8 characters
                    "$");

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_new_password);
        ok_btn = (Button) findViewById(R.id.ok_btn);
        back_btn = (ImageView) findViewById(R.id.back_btn);
        pass1 = (EditText) findViewById(R.id.pass1);
        pass2 = (EditText) findViewById(R.id.pass2);
        errorPass = (TextInputLayout) findViewById(R.id.errorPass);
        errorConfirm = (TextInputLayout) findViewById(R.id.errorConfirm);

        ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String password = pass1.getText().toString().trim();
                String confirm = pass2.getText().toString().trim();
                errorPass.setError(null);
                errorConfirm.setError(null);
                if (password.isEmpty()) {
                    errorPass.setError("Please enter Password");
                    errorPass.setErrorIconDrawable(null);

                }
                if (confirm.isEmpty()) {
                    errorConfirm.setError("Please confirm Password");
                    errorConfirm.setErrorIconDrawable(null);

                } else if (password.length() < 6) {
                    errorPass.setError("Password must contain 6 characters");
                    errorPass.setErrorIconDrawable(null);

                } else if (!PASSWORD_PATTERN.matcher(password).matches()) {
                    errorPass.setError("password to weak!");
                    errorPass.setErrorIconDrawable(null);

                } else if (!password.equals(confirm)) {
                    errorConfirm.setError("Password Not matching");
                    errorConfirm.setErrorIconDrawable(null);
                } else {

                    startActivity(new Intent(SetNewPassword.this, ForgetPassSuccessMessage.class));
                    finish();
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