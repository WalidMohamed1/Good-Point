package com.helloworld.goodpoint.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.helloworld.goodpoint.R;
import com.helloworld.goodpoint.ui.lostFoundObject.LostObjectDetailsActivity;

public class ActionActivity extends AppCompatActivity {
    Button lost;
    Button found;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lost = (Button) findViewById(R.id.button_lost);
        found = (Button) findViewById(R.id.button_found);

    }
    public void lost_btn(View v){

        startActivity(new Intent(ActionActivity.this, LostObjectDetailsActivity.class));
    }
    public void found_btn(View v){
        startActivity(new Intent(ActionActivity.this, LostObjectDetailsActivity.class));
    }


}