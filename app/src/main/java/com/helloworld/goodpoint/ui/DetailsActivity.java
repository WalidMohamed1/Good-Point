package com.helloworld.goodpoint.ui;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.helloworld.goodpoint.R;

public class DetailsActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int CALL_CODE = 1;
    TextView main,sub,name,brand,color,addr,dlost,dfound,mname,mail,phone;
    LinearLayout subLayout,nameLayout,brandLayout,colorLayout,addrLayout,matchLayout;
    ImageView img;
    Button call,sendMail;
    private int id;
    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        marginOrientation();
        init();
       // updateTable();
        sendMail.setOnClickListener(this);
        subLayout.setVisibility(View.GONE);
        colorLayout.setVisibility(View.GONE);
        brandLayout.setVisibility(View.GONE);

        call.setOnClickListener(this);
    }

    private void updateTable() {
        switch (type){
            case 1:
                subLayout.setVisibility(View.GONE);
                brandLayout.setVisibility(View.GONE);
                colorLayout.setVisibility(View.GONE);
                
                break;
            case 2:

                break;
            case 3:

                break;
            case 4:

                break;
            default:

        }
    }

    private void init() {
        main = findViewById(R.id.main_cat);
        sub = findViewById(R.id.item_cat);
        name = findViewById(R.id.lost_name);
        brand = findViewById(R.id.brand_name);
        color = findViewById(R.id.color_item);
        addr = findViewById(R.id.address);
        dlost = findViewById(R.id.date_of_lost);
        dfound = findViewById(R.id.date_of_found);
        mname = findViewById(R.id.name_of_matching);
        mail = findViewById(R.id.mail_of_matching);
        phone = findViewById(R.id.phone_of_matching);

        subLayout = findViewById(R.id.sub_cat);
        nameLayout = findViewById(R.id.person_name);
        brandLayout = findViewById(R.id.brand_detail);
        colorLayout = findViewById(R.id.color_detail);
        addrLayout = findViewById(R.id.addr_loc);
        matchLayout = findViewById(R.id.matched_detail);

        img = findViewById(R.id.img_detail);
        call = findViewById(R.id.call_btn);
        sendMail = findViewById(R.id.mail_btn);

       // id = getIntent().getIntExtra("id",0);
       // type = getIntent().getIntExtra("type",0);
    }

    private void marginOrientation() {
        int orientation = getResources().getConfiguration().orientation;
        if(orientation == Configuration.ORIENTATION_LANDSCAPE){
            LinearLayout layout = findViewById(R.id.parent_table_details);
            LinearLayout.MarginLayoutParams layoutParams = (LinearLayout.MarginLayoutParams) layout.getLayoutParams();
            float ver = getResources().getDimension(R.dimen._5sdp);
            float hor = getResources().getDimension(R.dimen._15sdp);
            int marginVer = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,ver,getResources().getDisplayMetrics());
            int marginHor = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,hor,getResources().getDisplayMetrics());
            layoutParams.setMargins(marginHor,marginVer,marginHor,marginVer);
            layout.requestLayout();
        }
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.call_btn:
                callHim();
                break;
            case R.id.mail_btn:
                sendEmail();
                break;
        }
    }

    private void callHim() {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:"+phone.getText().toString()));
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CALL_PHONE},CALL_CODE);
            return;
        }
        startActivity(callIntent);
    }

    private void sendEmail() {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:?subject=Matching item&to="+mail.getText().toString()));
        try {
            startActivity(Intent.createChooser(emailIntent,"Send mail..."));
        }catch (ActivityNotFoundException e){
            Log.e("MY_TAG",e.getMessage());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case CALL_CODE:
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    callHim();
                else
                    Toast.makeText(this, "You should allow to access call", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}