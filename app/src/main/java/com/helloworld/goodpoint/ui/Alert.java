package com.helloworld.goodpoint.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.helloworld.goodpoint.R;
import com.helloworld.goodpoint.ui.select_multiple_faces.Selection;

public class Alert extends AppCompatActivity {
    private Button next_btn;
    private TextView text;
    int i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alert);
        next_btn = (Button) findViewById(R.id.login_btn);
        text=(TextView)findViewById(R.id.text);
        i=GlobalVar.ImgThatHaveMoreThanOneFace.size();
        text.setText("There are ("+ i+ ") images that have multiple faces so press next to select only face ");

        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Alert.this, Selection.class));
                finish();

            }
        });

    }
}