package com.helloworld.goodpoint.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.JsonObject;
import com.helloworld.goodpoint.R;
import com.helloworld.goodpoint.pojo.Token;
import com.helloworld.goodpoint.pojo.User;
import com.helloworld.goodpoint.retrofit.ApiClient;
import com.helloworld.goodpoint.retrofit.ApiInterface;
import com.helloworld.goodpoint.ui.forgetPasswordScreens.MakeSelection;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SigninActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText Email, Pass;
    private TextInputLayout tilUserName, tilEmail, tilPassword, tilCity, tilPhone;
    private TextView ForgetPass;
    private CheckBox RememberMe;
    private Button Sigin, CreateNewAccount;
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[0-9])" +         //at least 1 digit
                    "(?=.*[a-z])" +         //at least 1 lower case letter
                    "(?=.*[A-Z])" +         //at least 1 upper case letter
                    // "(?=.*[a-zA-Z])" +      //any letter
                    // "(?=.*[@#$%^&+=])" +    //at least 1 special character
                    // "(?=\\S+$)" +           //no white spaces
                    ".{8,}" +               //at least 8 characters
                    "$");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        //getActionBar().hide();
        setContentView(R.layout.activity_signin);
        inti();
        String f;

    }

    protected void inti() {
        Email = findViewById(R.id.email);
        Pass = findViewById(R.id.pass);
        tilPassword = findViewById(R.id.tilPass);
        ForgetPass = findViewById(R.id.forgetPass);
        Sigin = findViewById(R.id.signin);
        CreateNewAccount = findViewById(R.id.NewAccount);
        RememberMe = findViewById(R.id.checkbox);
        Sigin.setOnClickListener(this);
        CreateNewAccount.setOnClickListener(this);
        ForgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), MakeSelection.class);
                startActivityForResult(myIntent, 0);
            }
        });
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.signin:
                if (validAccount() && validatePassword()) {
                        loginUser(RememberMe.isChecked());
                        //startActivity(new Intent(SigninActivity.this, HomeActivity.class));
                } else
                    Toast.makeText(this, "Invalid account", Toast.LENGTH_SHORT).show();
                    //startActivity(new Intent(SigninActivity.this, HomeActivity.class));
                break;
            case R.id.NewAccount:
                startActivity(new Intent(this, SignupActivity.class));
                break;
        }
    }

    private boolean validatePassword() {
        String passwordInput = Pass.getText().toString().trim();
        if (passwordInput.isEmpty()) {
            tilPassword.setError("Field can't be empty");
            return false;
        } else if (!PASSWORD_PATTERN.matcher(passwordInput).matches()) {
            tilPassword.setError("Must contains digits, lower&upper case letters and length > 8");
            /*if(!passwordInput.matches("[0-9]+"))
                Pass.setError("must contain at least 1 digit");
            else if(!passwordInput.matches("[a-z]+"))
                Pass.setError("must contain at least 1 lower case letter");
            else if(!passwordInput.matches("[A-Z]+"))
                Pass.setError("must contain at least 1 upper case letter");
            else
                Pass.setError("must contain at least 8 characters");*/
            return false;
        } else {
            tilPassword.setError(null);
            return true;
        }
    }

    private boolean validateEmail() {
        String emailInput = Email.getText().toString().trim();
        if (emailInput.isEmpty()) {
            Email.setError("Field can't be empty");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            Email.setError("Please enter a valid email address");
            return false;
        } else {
            Email.setError(null);
            return true;
        }
    }

    private boolean validAccount() {
        //check validation
        if (validateEmail()) return true;
        else return false;
    }


    public void loginUser(boolean Remember) {
        String emailInput = Email.getText().toString().trim();
        String passwordInput = Pass.getText().toString().trim();

        ApiInterface apiInterface = ApiClient.getApiClient(new PrefManager(getApplicationContext()).getNGROKLink()).create(ApiInterface.class);
        Call<Token> call = apiInterface.getToken(emailInput, passwordInput);

        call.enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {
                if (response.isSuccessful()) {
                    String token = response.body().getAccess();
                    Token.getToken().setAccess(response.body().getAccess());
                    Token.getToken().setRefresh(response.body().getRefresh());
                    if (Remember) {
                        new PrefManager(getApplicationContext()).setLogin(response.body().getRefresh());
                    }

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
                            if(User.getUser().getProfile_pic() != null && !User.getUser().getProfile_pic().isEmpty()) {
                                String dnsLink = new PrefManager(SigninActivity.this).getNGROKLink();
                                DownloadProfilePic download = new DownloadProfilePic();
                                download.execute(dnsLink+User.getUser().getProfile_pic()+"/");
                            }else{
                                Intent intent = new Intent(SigninActivity.this, HomeActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        } catch (Exception e) {
                            Log.e("TAG", "onResponse: "+e.getMessage());
                            Toast.makeText(SigninActivity.this, "Error in entering", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        Toast.makeText(SigninActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
                else
                    Toast.makeText(SigninActivity.this, "Invalid account.", Toast.LENGTH_SHORT).show();
            }

                @Override
            public void onFailure(Call<Token> call, Throwable t) {
                    Toast.makeText(SigninActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }

    class DownloadProfilePic extends AsyncTask<String,Void, Bitmap> {
        AlertDialog.Builder builder;
        AlertDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            builder = new AlertDialog.Builder(SigninActivity.this);
            builder.setCancelable(false);
            View view = getLayoutInflater().inflate(R.layout.progress_bar_alert, null);
            builder.setView(view);
            dialog = builder.create();
            dialog.show();
        }

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
            dialog.dismiss();
            if(bitmap!=null)
                User.getUser().setProfile_bitmap(bitmap);
            Intent intent = new Intent(SigninActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
    }
}