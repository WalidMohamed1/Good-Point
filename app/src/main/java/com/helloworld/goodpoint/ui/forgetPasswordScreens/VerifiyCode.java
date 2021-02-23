package com.helloworld.goodpoint.ui.forgetPasswordScreens;

import androidx.appcompat.app.AppCompatActivity;

import com.chaos.view.PinView;
import com.helloworld.goodpoint.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class VerifiyCode extends AppCompatActivity {
private Button verify_btn;
private ImageView back_btn;
private PinView code;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verifiy_code);
        verify_btn=(Button)findViewById(R.id.verify_btn);
        back_btn=(ImageView)findViewById(R.id.back_btn);
        code= (PinView)findViewById(R.id.otp_view);
        verify_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Code = code.getText().toString().trim();
                if (Code.isEmpty()) {
                    code.setError("Please enter the code");
                    code.requestFocus();
                    Toast.makeText(VerifiyCode.this, "Invalid Code", Toast.LENGTH_SHORT).show();
                }
                else if (Code.length()<6){
                    code.setError("Complete the code");
                    code.requestFocus();
                    Toast.makeText(VerifiyCode.this, "Invalid Code", Toast.LENGTH_SHORT).show();
                }
                else {
                    startActivity(new Intent(VerifiyCode.this, SetNewPassword.class));
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