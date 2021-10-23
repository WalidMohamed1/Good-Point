package com.helloworld.goodpoint.ui;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.SparseArray;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.app.ActivityCompat;
import androidx.loader.content.CursorLoader;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.google.gson.JsonObject;
import com.helloworld.goodpoint.R;
import com.helloworld.goodpoint.adapter.NotificationListAdapter;
import com.helloworld.goodpoint.pojo.NotificationItem;
import com.helloworld.goodpoint.pojo.Token;
import com.helloworld.goodpoint.pojo.User;
import com.helloworld.goodpoint.retrofit.ApiClient;
import com.helloworld.goodpoint.retrofit.ApiInterface;
import com.helloworld.goodpoint.ui.candidate.CandidatePage;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationActivity extends AppCompatActivity {

    List<NotificationItem> list;
    ListView listView;
    TextView noNotification;
    Bitmap img;
    AlertDialog dialog;
    AlertDialog.Builder builder;
    View view;
    Uri photoFromGallery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        init();
        createList();
    }

    private void createList() {
        ApiInterface apiInterface = ApiClient.getApiClient(new PrefManager(getApplicationContext()).getNGROKLink()).create(ApiInterface.class);
        Call<List<NotificationItem>>call = apiInterface.getNotification(User.getUser().getId());
        call.enqueue(new Callback<List<NotificationItem>>() {
            @Override
            public void onResponse(Call<List<NotificationItem>> call, Response<List<NotificationItem>> response) {
                list = response.body();
                if(list == null)
                    list = new ArrayList<>();
                showView();
            }

            @Override
            public void onFailure(Call<List<NotificationItem>> call, Throwable t) {
                Log.e("TAG", "onFailure: "+t.getMessage());
            }
        });
    }

    private void showView() {
        if(list.isEmpty()){
            noNotification.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        }else {
            listView.setAdapter(new NotificationListAdapter(this, 0, list));
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View v, int i, long l) {
                    int pos = list.size()-i-1;
                    NotificationListAdapter adapter = (NotificationListAdapter)listView.getAdapter();
                    adapter.getItem(pos).setRead(true);
                    adapter.notifyDataSetChanged();
                    ApiInterface apiInterface = ApiClient.getApiClient(new PrefManager(getApplicationContext()).getNGROKLink()).create(ApiInterface.class);
                    Call<JsonObject>call = apiInterface.updateRead(list.get(pos).getId(), true);
                    call.enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                            if(response.isSuccessful())
                            {
                                Log.e("TAG", "onResponse: "+response.body().toString());
                            }
                           else {
                                try {
                                    Log.e("TAG", "onFailure2: "+response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<JsonObject> call, Throwable t) {
                            Log.e("TAG", "onFailure: "+t.getMessage());
                        }
                    });
                    if(list.get(pos).getType()==3){
                        //intent to candidate
                        startActivity(new Intent(NotificationActivity.this, CandidatePage.class));
                        return;
                    }
                    else if(list.get(pos).getType() == 1 || list.get(pos).getType() == 4){
                        //lost person and lost item
                        if(User.getUser().getId_card_pic().isEmpty()){
                            view = getLayoutInflater().inflate(R.layout.alert_id_card, null);
                            Button Choose = view.findViewById(R.id.Id_card);
                            Choose.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (ActivityCompat.checkSelfPermission(getApplicationContext(),  Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                                            && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                        ActivityCompat.requestPermissions(NotificationActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 11);
                                    }
                                    else{
                                        Intent I = new Intent(NotificationActivity.this,ID_cardDetection.class);
                                        startActivityForResult(I, 10);
                                       /* PopupMenu popupMenu = new PopupMenu(NotificationActivity.this, view);
                                        popupMenu.getMenuInflater().inflate(R.menu.choose_photo, popupMenu.getMenu());
                                        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                                            @Override
                                            public boolean onMenuItemClick(MenuItem item) {
                                                switch (item.getItemId()) {
                                                    case R.id.TakePhoto:
                                                       Intent I = new Intent(NotificationActivity.this,ID_cardDetection.class);
                                                        startActivityForResult(I, 10);
                                                        break;
                                                    case R.id.Gallery:
                                                        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                                                        startActivityForResult(gallery, 11);
                                                        break;
                                                }
                                                return true;
                                            }
                                        });
                                        popupMenu.show();*/
                                    }
                                }
                            });

                            builder = new AlertDialog.Builder(NotificationActivity.this);
                            builder.setMessage("Please , Upload an image of your id_card");
                            builder.setView(view);
                            dialog = builder.create();
                            dialog.show();
                            return;
                        }
                    }
                    Intent intent = new Intent(NotificationActivity.this,DetailsActivity.class);
                    intent.putExtra("id",list.get(pos).getId());
                    intent.putExtra("type",list.get(pos).getType());
                    startActivity(intent);
                }
            });

        }
    }

    public Uri getImageUri(Bitmap bitmap_Image) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap_Image.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap_Image, "id card", null);
        return Uri.parse(path);
    }

    private String getRealPathFromURI(Uri imageUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(this, imageUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK&&data!=null)
        {
            switch(requestCode) {
                case 10: {
                    img =(Bitmap) GlobalVar.realcameraIdCard;
                    photoFromGallery = getImageUri(img);
                }
                break;
                /*case 11:
                    photoFromGallery = data.getData();
                    try {
                        img = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoFromGallery);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;*/
            }
            checkVaildIdcard();
        }
    }
    private void checkVaildIdcard() {
        TextView messageforuser = view.findViewById(R.id.messageforuser);
        boolean flag_not_idCard = false;
        FaceDetector faceDetector = new FaceDetector.Builder(this)
                .setTrackingEnabled(false)
                .setLandmarkType(FaceDetector.ALL_LANDMARKS)
                .setMode(FaceDetector.FAST_MODE).build();
        if (!faceDetector.isOperational()) {
            Toast.makeText(this, "Face Detection can't be setup", Toast.LENGTH_SHORT).show();
        }
        else {
            Frame frame = new Frame.Builder().setBitmap(img).build();
            SparseArray<Face> sparseArray = faceDetector.detect(frame);
            if(sparseArray.size() != 1)
            {
                flag_not_idCard = true;

            }
            TextRecognizer textRecognizer  = new TextRecognizer.Builder(this).build();
            if(!textRecognizer.isOperational())
                Toast.makeText(this, "textRecognizer can't be setup", Toast.LENGTH_SHORT).show();
            else
            {
                Frame frametext = new Frame.Builder().setBitmap(img).build();
                SparseArray<TextBlock> sparseArraytext = textRecognizer.detect(frame);
                TextBlock item;
                if(sparseArraytext.size()>4 || sparseArraytext.size()<1)
                {
                    flag_not_idCard = true;
                }
                else if(sparseArraytext.size()>=1) {
                    item = sparseArraytext.valueAt(sparseArraytext.size() - 1);
                    if(item.getValue().length() != 9)
                        flag_not_idCard = true;
                }

                Log.e("img", "num of img : "+sparseArray.size()+"  num of text " +sparseArraytext.size());
            }
            if(flag_not_idCard)
            {
                messageforuser.setTextColor(0xFFB80D0D);
                messageforuser.setText("Error, The card picture cannot be recognized\nplease upload your id card");
            }
            else{
                dialog.dismiss();
                builder = new AlertDialog.Builder(NotificationActivity.this);
                builder.setMessage("Your id_card has been successfully taken").setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ApiInterface apiInterface = ApiClient.getApiClient(new PrefManager(getApplicationContext()).getNGROKLink()).create(ApiInterface.class);
                        Call<Token> call = apiInterface.refresh(Token.getToken().getRefresh());
                        call.enqueue(new Callback<Token>() {
                            @Override
                            public void onResponse(Call<Token> call, Response<Token> response) {
                                File file = new File(getRealPathFromURI(photoFromGallery));
                                RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                                MultipartBody.Part Pimage = MultipartBody.Part.createFormData("id_card_pic", file.getName(), requestBody);
                                Call<JsonObject>idcardCall = apiInterface.setIdCard(response.body().getAccess(), Pimage);
                                idcardCall.enqueue(new Callback<JsonObject>() {
                                    @Override
                                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                        Log.d("TAG", "onResponse: Success");
                                    }

                                    @Override
                                    public void onFailure(Call<JsonObject> call, Throwable t) {
                                        Log.e("TAG", "onFailure: "+t.getMessage());
                                    }
                                });
                            }

                            @Override
                            public void onFailure(Call<Token> call, Throwable t) {
                                Log.e("TAG", "onFailure: "+t.getMessage());
                            }
                        });
                    }});
                dialog = builder.create();
                dialog.show();
            }

        }
    }
    private void init() {
        list = new ArrayList<>();
        listView = findViewById(R.id.notification_listview);
        noNotification = findViewById(R.id.no_notification);
        if(User.getUser().getId() == null || User.getUser().getId().isEmpty())
            User.getUser().setId(getIntent().getExtras().getString("ID"));
        if(Token.getToken().getRefresh() == null || Token.getToken().getRefresh().isEmpty())
            Token.getToken().setRefresh(new PrefManager(this).isLoginned());
    }
}