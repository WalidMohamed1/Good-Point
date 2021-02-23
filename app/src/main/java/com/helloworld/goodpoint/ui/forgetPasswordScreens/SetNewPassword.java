package com.helloworld.goodpoint.ui.forgetPasswordScreens;

import androidx.appcompat.app.AppCompatActivity;

import com.helloworld.goodpoint.R;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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
    private EditText pass1 ,pass2;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_new_password);
        ok_btn = (Button) findViewById(R.id.ok_btn);
        back_btn = (ImageView) findViewById(R.id.back_btn);
        pass1= (EditText) findViewById(R.id.pass1);
        pass2=(EditText)findViewById(R.id.pass2);
        ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String password = pass1.getText().toString().trim();
                String confirm = pass2.getText().toString().trim();


               if (password.isEmpty()) {
                    pass1.setError("Please enter Password");
                    pass1.requestFocus();
                }
                if (confirm.isEmpty()) {
                    pass2.setError("Please confirm Password");
                    pass2.requestFocus();
                }

                else if (password.length() < 6) {
                    pass1.setError("Password must contain 6 characters");
                    pass1.requestFocus();
                }

                else if (!PASSWORD_PATTERN.matcher(password).matches()) {
                    pass1.setError("password to weak!");
                    pass1.requestFocus();
                }
                else if(!password.equals(confirm)) {
                    pass2.setError("Password Not matching");
                    pass2.requestFocus();
                }
                else
                    {
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