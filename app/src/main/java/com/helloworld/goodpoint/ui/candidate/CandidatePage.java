package com.helloworld.goodpoint.ui.candidate;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.helloworld.goodpoint.R;
import com.helloworld.goodpoint.pojo.LostItem;
import com.helloworld.goodpoint.pojo.LostObject;
import com.helloworld.goodpoint.pojo.NotificationItem;
import com.helloworld.goodpoint.retrofit.ApiClient;
import com.helloworld.goodpoint.retrofit.ApiInterface;
import com.helloworld.goodpoint.ui.GlobalVar;
import com.helloworld.goodpoint.ui.PrefManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CandidatePage extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private static final String TAG = "CandidatePage";
    //vars
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mImageUrls = new ArrayList<>();
    RadioButton rb;
    Button Done_btn;
    SubItemAdapter recyclerViewAdapter;
    TextView type;
    List<LostItem> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_candidate_page);
        type = findViewById(R.id.tv_item_title1);
        Done_btn = findViewById(R.id.d_btn);
        RecyclerView rvItem = findViewById(R.id.rv_sub_item1);


        LostItem item = new LostItem("Phone", "123456788", "Lenovo", "White", "details");

        if (GlobalVar.lostList == null && GlobalVar.percentList == null) {
            GlobalVar.lostList = new ArrayList<>();
            GlobalVar.percentList = new ArrayList<>();
            GlobalVar.lostList.add(item);
            GlobalVar.lostList.add(item);
            GlobalVar.lostList.add(item);
            GlobalVar.percentList.add("60%");
            GlobalVar.percentList.add("50%");
            GlobalVar.percentList.add("40%");
            GlobalVar.type="Phone";
        }
        type.setText(GlobalVar.type);
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                rvItem.getContext(),
                LinearLayoutManager.VERTICAL,
                false
        );
        recyclerViewAdapter = new SubItemAdapter(buildSubItemList(GlobalVar.lostList), this);
        recyclerViewAdapter.setOnItemClickListener(this);
        rvItem.setAdapter(recyclerViewAdapter);
        rvItem.setLayoutManager(layoutManager);
        Done_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SubItem list = GlobalVar.sublist.get(GlobalVar.p);
                storeCandidatesNotifictation(list);
                finish();
            }
        });

    }

    private List<com.helloworld.goodpoint.ui.candidate.SubItem> buildSubItemList(List<LostItem> items) {
        List<com.helloworld.goodpoint.ui.candidate.SubItem> subItemList = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            com.helloworld.goodpoint.ui.candidate.SubItem subItem = new com.helloworld.goodpoint.ui.candidate.SubItem(items.get(i).getType(), items.get(i).getDescription(), GlobalVar.percentList.get(i), i, items.get(i).getId());
            subItemList.add(subItem);
        }
        GlobalVar.sublist = subItemList;
        return subItemList;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        SubItem list = GlobalVar.sublist.get(position);
        int i = list.getPos();
        i += 1;
        GlobalVar.p = position;
        Toast.makeText(this, "Item (" + i
                + ") is selected", Toast.LENGTH_SHORT).show();


    }

    public void storeCandidatesNotifictation(SubItem item) {

        ApiInterface apiInterface = ApiClient.getApiClient(new PrefManager(getApplicationContext()).getNGROKLink()).create(ApiInterface.class);
        Call<LostObject> call = apiInterface.getObject(item.getId());
        call.enqueue(new Callback<LostObject>() {

            @Override
            public void onResponse(Call<LostObject> call, Response<LostObject> response) {
                LostObject obj = response.body();
                if(response.body()!=null) {
                    Call<NotificationItem> call2 = apiInterface.storeNotification(obj.getUser_id(), "Item matched", "Your " + item.getSubItemTitle() + " is Found ", 4);
                    call2.enqueue(new Callback<NotificationItem>() {
                        @Override
                        public void onResponse(Call<NotificationItem> call, Response<NotificationItem> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(CandidatePage.this, "Notification is posted.", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(CandidatePage.this, "The Notification is not posted.", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<NotificationItem> call, Throwable t) {
                            Toast.makeText(CandidatePage.this, t.getMessage(), Toast.LENGTH_LONG).show();

                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<LostObject> call, Throwable t) {
                Toast.makeText(CandidatePage.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }
}

