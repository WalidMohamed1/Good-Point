package com.helloworld.goodpoint.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.helloworld.goodpoint.R;
import com.helloworld.goodpoint.ui.candidate.CandidatePage;
import com.helloworld.goodpoint.ui.forgetPasswordScreens.ForgetPassSuccessMessage;
import com.helloworld.goodpoint.ui.forgetPasswordScreens.ForgetPasswordWithEmail;
import com.helloworld.goodpoint.ui.forgetPasswordScreens.MakeSelection;
import com.helloworld.goodpoint.ui.forgetPasswordScreens.VerifiyCode;

import java.util.Calendar;
import java.util.regex.Pattern;

public class SigninActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText Email,Pass;
    private TextView ForgetPass;
    private CheckBox RememberMe;
    private Button Sigin ,CreateNewAccount;
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
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
       //getActionBar().hide();
        setContentView(R.layout.activity_signin);
        inti();

    }

    protected void inti() {
        Email = findViewById(R.id.email);
        Pass =findViewById(R.id.pass);
        ForgetPass = findViewById(R.id.forgetPass);
        Sigin =findViewById(R.id.signin);
        CreateNewAccount = findViewById(R.id.NewAccount);
        RememberMe = findViewById(R.id.checkbox);
        Sigin.setOnClickListener(this);
        CreateNewAccount.setOnClickListener(this);
        ForgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), MakeSelection.class);
                startActivityForResult(myIntent, 0);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.signin:
                if(validAccount()) {
                    if(RememberMe.isChecked())
                        new PrefManager(getApplicationContext()).setLogin("Token");
                    startActivity(new Intent(this, HomeActivity.class));
                    finish();
                }else
                    Toast.makeText(this, "Invalid account", Toast.LENGTH_SHORT).show();
                break;
            case R.id.NewAccount:
                    startActivity(new Intent(this, SignupActivity.class));
                    break;
        }
    }
    private boolean validatePassword() {
        String passwordInput = Pass.getText().toString().trim();
        if (passwordInput.isEmpty()) {
            Pass.setError("Field can't be empty");
            return false;
        } else if (!PASSWORD_PATTERN.matcher(passwordInput).matches()) {
            Pass.setError("password to weak!");
            /*if(!passwordInput.matches("[0-9]+"))
                Pass.setError("must contain at least 1 digit");
            else if(!passwordInput.matches("[a-z]+"))
                Pass.setError("must contain at least 1 lower case letter");
            else if(!passwordInput.matches("[A-Z]+"))
                Pass.setError("must contain at least 1 upper case letter");
            else
                Pass.setError("must contain at least 8 characters");*/
            return false;
        } else {
            Pass.setError(null);
            return true;
        }
    }
    private boolean validateEmail() {
        String emailInput = Email.getText().toString().trim();
        if (emailInput.isEmpty()) {
            Email.setError("Field can't be empty");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            Email.setError("Please enter a valid email address");
            return false;
        } else {
            Email.setError(null);
            return true;
        }
    }
    private boolean validAccount() {
        //check validation
        if(validateEmail()) return true;
        else return false;
    }
}