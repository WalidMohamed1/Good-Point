package com.helloworld.goodpoint.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.helloworld.goodpoint.R;

public class ExternalActivity extends AppCompatActivity {

    EditText et;
    Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_external);
        et = findViewById(R.id.ngrok_link);
        btn = findViewById(R.id.submit_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PrefManager p = new PrefManager(ExternalActivity.this);
                p.setNGROKLink("https://"+et.getText().toString()+".ngrok.io");
            }
        });
    }
}