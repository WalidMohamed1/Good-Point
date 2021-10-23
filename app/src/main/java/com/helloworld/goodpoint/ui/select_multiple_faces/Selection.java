package com.helloworld.goodpoint.ui.select_multiple_faces;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.helloworld.goodpoint.R;
import com.helloworld.goodpoint.ui.GlobalVar;
import com.helloworld.goodpoint.ui.test;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.List;

public class Selection extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private static final String TAG = "Selection";
    //vars
    //private ArrayList<String> mNames = new ArrayList<>();
    //private ArrayList<String> mImageUrls = new ArrayList<>();
    RadioButton rb;
    Button Done_btn;
    ImageView imageView;
    SubItemListAdapter recyclerViewAdapter;
    RecyclerView rvItem;
    List<List<Bitmap>> faces;
    List<Bitmap> mfaces;

    boolean click = false;
    int k = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.multiple_faces_selection);
        rvItem = findViewById(R.id.recycler_view_sub_items);
        imageView = findViewById(R.id.ItemImage);
        rb = (RadioButton) findViewById(R.id.radioButton);
        faces = GlobalVar.allFaces;
        mfaces = GlobalVar.ImgThatHaveMoreThanOneFace;
        List<Bitmap> mfaces = GlobalVar.ImgThatHaveMoreThanOneFace;
        click = false;
        createCard(k);
        Done_btn = (Button) findViewById(R.id.Done_btn);
        if (k == mfaces.size() - 1) {
            Done_btn.setText("Done");
        }
            Done_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (click) {
                    SubItemList list = GlobalVar.slist.get(GlobalVar.position);
                    GlobalVar.FinialFacesThatWillGoToDataBase.add(list.getSubItemImage());
                    if (k == mfaces.size() - 1) {
                        startActivity(new Intent(Selection.this, test.class));
                        finish();
                    } else {
                        k++;
                        createCard(k);
                        click = false;
                        if (k == mfaces.size() - 1) {
                            Done_btn.setText("Done");
                        }

                    }
                } else
                    FancyToast.makeText(Selection.this, "Specify the face of the missing person", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();


//
            }
        });


    }

    private void createCard(int i) {
        imageView.setImageBitmap(mfaces.get(i));

        LinearLayoutManager layoutManager = new LinearLayoutManager(
                rvItem.getContext(),
                LinearLayoutManager.HORIZONTAL,
                false
        );
        recyclerViewAdapter = new SubItemListAdapter(buildSubItemList(faces.get(i)), this);
        recyclerViewAdapter.setOnItemClickListener(this);
        rvItem.setAdapter(recyclerViewAdapter);
        rvItem.setLayoutManager(layoutManager);

    }


    private List<com.helloworld.goodpoint.ui.select_multiple_faces.SubItemList> buildSubItemList(List<Bitmap> faces) {
        List<com.helloworld.goodpoint.ui.select_multiple_faces.SubItemList> subItemList = new ArrayList<>();
        for (int i = 0; i < faces.size(); i++) {
            com.helloworld.goodpoint.ui.select_multiple_faces.SubItemList subItem = new com.helloworld.goodpoint.ui.select_multiple_faces.SubItemList(faces.get(i), i);
            subItemList.add(subItem);
        }
        GlobalVar.slist = subItemList;

        return subItemList;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        SubItemList list = GlobalVar.slist.get(position);
        click = true;
        int i = list.getPos();
        i += 1;
        GlobalVar.position=position;

        Toast.makeText(this, "Face number (" + i
                + ") is selected", Toast.LENGTH_SHORT).show();
    }
}
