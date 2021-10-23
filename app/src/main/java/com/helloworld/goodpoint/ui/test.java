package com.helloworld.goodpoint.ui;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;
import com.helloworld.goodpoint.R;
import com.helloworld.goodpoint.pojo.FoundPerson;
import com.helloworld.goodpoint.pojo.LostPerson;
import com.helloworld.goodpoint.pojo.User;
import com.helloworld.goodpoint.retrofit.ApiClient;
import com.helloworld.goodpoint.retrofit.ApiInterface;
import com.helloworld.goodpoint.ui.lostFoundObject.FoundObjectActivity;
import com.helloworld.goodpoint.ui.lostFoundObject.LostObjectDetailsActivity;
import com.shashank.sony.fancytoastlib.FancyToast;

import org.json.JSONException;
import org.json.JSONObject;

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

public class test extends AppCompatActivity  {


    subAdapter recyclerViewAdapter;
    RecyclerView rvItem;
    List<Bitmap> mfaces;
    Button Save_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.r);
        rvItem = findViewById(R.id.q);
        mfaces = GlobalVar.FinialFacesThatWillGoToDataBase;
        createCard();
        Save_btn = (Button) findViewById(R.id.Save_btn);
        Save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(GlobalVar.flag==1)
                    FoundPerson();
                else if(GlobalVar.flag==2)
                    LostPerson();
                //FancyToast.makeText(test.this, "Done", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();

            }
        });
    }
    private void createCard() {

        LinearLayoutManager layoutManager = new LinearLayoutManager(
                rvItem.getContext(),
                LinearLayoutManager.HORIZONTAL,
                false
        );
        recyclerViewAdapter = new subAdapter(buildSubItemList(mfaces), this);
        rvItem.setAdapter(recyclerViewAdapter);
        rvItem.setLayoutManager(layoutManager);

    }

    public Uri getImageUri(Bitmap bitmap_Image) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap_Image.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap_Image, (System.currentTimeMillis()%1000)+"", null);
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

    public void FoundPerson()
    {

        String Datee = FoundPerson.getFoundPerson().getDate();
        ApiInterface apiInterface = ApiClient.getApiClient(new PrefManager(getApplicationContext()).getNGROKLink()).create(ApiInterface.class);

        Uri imageURI = getImageUri(GlobalVar.FinialFacesThatWillGoToDataBase.get(0));
        File file = new File(getRealPathFromURI(imageURI));
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part Pimage = MultipartBody.Part.createFormData("image", file.getName(), requestBody);

        String PName = FoundPerson.getFoundPerson().getName();
        String City = FoundPerson.getFoundPerson().getCity();
        double Longitude = FoundPerson.getFoundPerson().getLongitude();
        double Latitude = FoundPerson.getFoundPerson().getLatitude();

        Call<JsonObject> call = apiInterface.storeFoundPerson(User.getUser().getId(), Datee, City, Longitude , Latitude, PName, Pimage);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.isSuccessful()) {
                    FancyToast.makeText(test.this, "The data has been saved successfully", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();
                    try {
                        String id = new JSONObject(response.body().toString()).getString("id");
                    } catch (JSONException e) {
                        Log.e("TAG", "onResponse: "+e.getMessage());
                    }
                    startActivity(new Intent(test.this, HomeActivity.class));
                    finish();
                }else
                    Toast.makeText(test.this, "The object is not posted.", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(test.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


    }

    public void LostPerson()  {

        String Datee = LostPerson.getLostPerson().getDate();
        ApiInterface apiInterface = ApiClient.getApiClient(new PrefManager(getApplicationContext()).getNGROKLink()).create(ApiInterface.class);

        Uri imageURI = getImageUri(GlobalVar.FinialFacesThatWillGoToDataBase.get(0));
        File file = new File(getRealPathFromURI(imageURI));
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part Pimage = MultipartBody.Part.createFormData("image", file.getName(), requestBody);
        //MultipartBody.Part image = MultipartBody.Part.createFormData("images", file.getName(), requestBody);
        //Pimages.add(image);

        String PName = LostPerson.getLostPerson().getName();
        String City = LostPerson.getLostPerson().getCity();

        Call<JsonObject> call = apiInterface.storeLostPerson(Datee,City,User.getUser().getId(),PName,Pimage);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.isSuccessful()) {
                    FancyToast.makeText(test.this, "The data has been saved successfully", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();
                    try {
                        String id = new JSONObject(response.body().toString()).getString("id");
                    } catch (JSONException e) {
                        Log.e("TAG", "onResponse: "+e.getMessage());
                    }
                    startActivity(new Intent(test.this, HomeActivity.class));
                    finish();
                }else {
                    try {
                        Log.e("onResponse: ", response.errorBody().string());
                    } catch (IOException e) {
                        Log.e("onResponse: ", e.getMessage());
                    }
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(test.this, t.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("onFailure: ", t.getMessage());
            }
        });

    }

    private List<sub> buildSubItemList(List<Bitmap> faces) {
        List<sub> subItemList = new ArrayList<>();
        for (int i = 0; i < faces.size(); i++) {
            sub subItem = new sub(faces.get(i), i);
            subItemList.add(subItem);
        }

        return subItemList;
    }


}
