package com.helloworld.goodpoint.ui.candidate;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.helloworld.goodpoint.R;

import java.util.ArrayList;
import java.util.List;

public class CandidatePage extends AppCompatActivity {

    private static final String TAG = "CandidatePage";
    //vars
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mImageUrls = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_candidate_page);
        getImages();

        RecyclerView rvItem = findViewById(R.id.rv_item);
        LinearLayoutManager layoutManager = new LinearLayoutManager(com.helloworld.goodpoint.ui.candidate.CandidatePage.this);
        ItemAdapter itemAdapter = new ItemAdapter(buildItemList());
        rvItem.setAdapter(itemAdapter);
        rvItem.setLayoutManager(layoutManager);

    }

    private List<com.helloworld.goodpoint.ui.candidate.Item> buildItemList() {
        List<com.helloworld.goodpoint.ui.candidate.Item> itemList = new ArrayList<>();
        for (int i=0; i<9; i++) {
            com.helloworld.goodpoint.ui.candidate.Item item = new com.helloworld.goodpoint.ui.candidate.Item("Item "+i, buildSubItemList());
            itemList.add(item);
        }
        return itemList;
    }

    private List<com.helloworld.goodpoint.ui.candidate.SubItem> buildSubItemList() {
        List<com.helloworld.goodpoint.ui.candidate.SubItem> subItemList = new ArrayList<>();
        for (int i=0; i<3; i++) {
            com.helloworld.goodpoint.ui.candidate.SubItem subItem = new com.helloworld.goodpoint.ui.candidate.SubItem("Sub Item "+i);
            subItemList.add(subItem);
        }
        return subItemList;
    }
    private void getImages(){
        Log.d(TAG, "initImageBitmaps: preparing bitmaps.");

        mImageUrls.add("https://c1.staticflickr.com/5/4636/25316407448_de5fbf183d_o.jpg");
        mNames.add("Havasu Falls");

        mImageUrls.add("https://i.redd.it/tpsnoz5bzo501.jpg");
        mNames.add("Trondheim");

        mImageUrls.add("https://i.redd.it/qn7f9oqu7o501.jpg");
        mNames.add("Portugal");

        mImageUrls.add("https://i.redd.it/j6myfqglup501.jpg");
        mNames.add("Rocky Mountain National Park");


        mImageUrls.add("https://i.redd.it/0h2gm1ix6p501.jpg");
        mNames.add("Mahahual");

        mImageUrls.add("https://i.redd.it/k98uzl68eh501.jpg");
        mNames.add("Frozen Lake");


        mImageUrls.add("https://i.redd.it/glin0nwndo501.jpg");
        mNames.add("White Sands Desert");

        mImageUrls.add("https://i.redd.it/obx4zydshg601.jpg");
        mNames.add("Austrailia");

        mImageUrls.add("https://i.imgur.com/ZcLLrkY.jpg");
        mNames.add("Washington");



    }


}
