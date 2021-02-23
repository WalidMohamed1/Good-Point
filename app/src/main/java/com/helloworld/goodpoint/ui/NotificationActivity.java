package com.helloworld.goodpoint.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.helloworld.goodpoint.R;
import com.helloworld.goodpoint.adapter.NotificationListAdapter;
import com.helloworld.goodpoint.pojo.NotificationItem;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class NotificationActivity extends AppCompatActivity {

    List<NotificationItem> list;
    ListView listView;
    TextView noNotification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        init();
        createList();
        showView();
    }

    private void createList() {
        ///*
        NotificationItem item = new NotificationItem("item matched", new Date(2019-1900,12-1,25,10,55), "your laptop is found by Ahmed Mostafa",null);
        item.setRead(true);
        list.add(item);
        item = new NotificationItem("person matched", new Date(2020-1900,2-1,1,0,0), "Abdelrahman Mamdouh is found by Walid Mohamed",null);
        list.add(item);
        item = new NotificationItem("item matched", new Date(2020-1900,3-1,25,10,55), "your wallet is found by Dina Mishahed",null);
        item.setRead(true);
        list.add(item);
        item = new NotificationItem("item matched", new Date(2020-1900,4-1,17,15,0), "your money is found by Esraa Sayed",null);
        list.add(item);
        item = new NotificationItem("person matched", new Date(2020-1900,12-1,25,10,55), "a relative of Folan Elfolany was found",null);
        list.add(item);
        item = new NotificationItem("candidate item", Calendar.getInstance().getTime(), "there are 3 candidate phones for matching",null);
        list.add(item);//*/
    }

    private void showView() {
        if(list.isEmpty()){
            noNotification.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        }else {
            listView.setAdapter(new NotificationListAdapter(this, 0, list));
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    startActivity(new Intent(NotificationActivity.this,DetailsActivity.class));
                    NotificationListAdapter adapter = (NotificationListAdapter)listView.getAdapter();
                    adapter.getItem(list.size()-i-1).setRead(true);
                    adapter.notifyDataSetChanged();
                }
            });

        }
    }

    private void init() {
        list = new ArrayList<>();
        listView = findViewById(R.id.notification_listview);
        noNotification = findViewById(R.id.no_notification);
    }
}