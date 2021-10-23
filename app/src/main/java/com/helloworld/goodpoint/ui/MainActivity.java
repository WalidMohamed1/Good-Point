package com.helloworld.goodpoint.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonObject;
import com.helloworld.goodpoint.R;
import com.helloworld.goodpoint.pojo.Token;
import com.helloworld.goodpoint.pojo.User;
import com.helloworld.goodpoint.retrofit.ApiClient;
import com.helloworld.goodpoint.retrofit.ApiInterface;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    PrefManager prefManager;
    ImageView splash;
    Thread t;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        if (prefManager.isFirstTimeLaunch()) {
            prefManager.setFirstTimeLaunch(true);
            startActivity(new Intent(MainActivity.this, WelcomeActivity.class));
            finish();
        }else if(!prefManager.isLoginned().isEmpty()) {
            rotateSplash();
            t = startApp();
        }else {

            startActivity(new Intent(this,SigninActivity.class));
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        t.start();
    }

    @Override
    protected void onPause() {
        t.interrupt();
        super.onPause();
    }

    private void init() {
        prefManager = new PrefManager(getApplicationContext());
        splash = findViewById(R.id.splash_icon);
    }

    private void rotateSplash() {
        Animation animation = AnimationUtils.loadAnimation(this,R.anim.rotate_splash);
        splash.startAnimation(animation);
    }

    private Thread startApp() {
        return new Thread(new Runnable() {
            @Override
            public void run() {
                //try {
                    //Thread.sleep(3000);
                    ApiInterface apiInterface = ApiClient.getApiClient(new PrefManager(getApplicationContext()).getNGROKLink()).create(ApiInterface.class);
                    Call<Token> call = apiInterface.refresh(new PrefManager(getApplicationContext()).isLoginned());
                    Token.getToken().setRefresh(new PrefManager(getApplicationContext()).isLoginned());
                    call.enqueue(new Callback<Token>() {
                        @Override
                        public void onResponse(Call<Token> call, Response<Token> response) {
                            if(response.isSuccessful()) {
                                String token = response.body().getAccess();

                                Call<JsonObject> call2 = apiInterface.getData("Bearer " + token);
                                call2.enqueue(new Callback<JsonObject>() {
                                    @Override
                                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                                        try {
                                            Log.d("e","res="+response.body().toString());
                                            JSONObject jsonObject = new JSONObject(response.body().toString()).getJSONObject("user");
                                            String id = jsonObject.getString("id");
                                            String name = jsonObject.getString("username");
                                            String email = jsonObject.getString("email");
                                            String phone = jsonObject.getString("phone");
                                            String city = jsonObject.getString("city");
                                            String birthdate = jsonObject.getString("birthdate");
                                            String Userimage = jsonObject.getString("profile_pic");
                                            String idcardimage = jsonObject.getString("id_card_pic");
                                            JSONArray jsonArray = jsonObject.getJSONArray("losts");
                                            Log.e("blabla", jsonArray.length() + "");
                                            for(int i=0;i<jsonArray.length();i++)
                                                User.getUser().getLosts().add(jsonArray.getJSONObject(i).getInt("id"));
                                            jsonArray = jsonObject.getJSONArray("founds");
                                            for(int i=0;i<jsonArray.length();i++)
                                                User.getUser().getFounds().add(jsonArray.getJSONObject(i).getInt("id"));

                                            User.getUser().setId(id);
                                            User.getUser().setUsername(name);
                                            User.getUser().setEmail(email);
                                            User.getUser().setPhone(phone);
                                            User.getUser().setCity(city);
                                            User.getUser().setBirthdate(birthdate);
                                            User.getUser().setProfile_pic(Userimage);
                                            User.getUser().setId_card_pic(idcardimage);
                                            if(User.getUser().getProfile_pic() != null &&!User.getUser().getProfile_pic().isEmpty() && User.getUser().getProfile_bitmap() == null) {
                                                String dnsLink = new PrefManager(MainActivity.this).getNGROKLink();
                                                DownloadProfilePic download = new DownloadProfilePic();
                                                download.execute(dnsLink+User.getUser().getProfile_pic()+"/");
                                            }else{
                                                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                                                startActivity(intent);
                                                finish();
                                            }


                                        } catch (Exception e) {
                                            Log.e("Error: ", e.getMessage());
                                        }
                                    }

                                @Override
                                public void onFailure(Call<JsonObject> call, Throwable t) {
                                    Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(MainActivity.this, SigninActivity.class));
                                    finish();
                                }
                            });
                        }else{
                            startActivity(new Intent(MainActivity.this, SigninActivity.class));
                            finish();
                        }
                    }

                        @Override
                        public void onFailure(Call<Token> call, Throwable t) {
                            Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                            startActivity(new Intent(MainActivity.this, SigninActivity.class));
                            finish();
                        }
                    });

                //} catch (InterruptedException e) {
                  //  Log.e("InterruptedException: ", e.getMessage());
                //}
            }
        });
    }

    class DownloadProfilePic extends AsyncTask<String,Void, Bitmap> {

        private Bitmap download(String urlLink) throws IOException {
            Bitmap bitmap = null;
            URL url = null;
            HttpURLConnection httpConn;
            InputStream is = null;
            Log.e("ProfilePic", urlLink);
            try {
                url = new URL(urlLink);
                httpConn = (HttpURLConnection) url.openConnection();
                httpConn.connect();
                is = httpConn.getInputStream();
                bitmap = BitmapFactory.decodeStream(is);
            }catch (MalformedURLException e){
                Log.e("DownloadProfilePic", "download: "+e.getMessage());
            }
            return  bitmap;
        }

        @Override
        protected Bitmap doInBackground(String... urls) {
            try {

                return download(urls[0]);
            } catch (IOException e) {
                Log.e("TAG", "doInBackground: "+e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if(bitmap!=null)
                User.getUser().setProfile_bitmap(bitmap);
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
    }



}