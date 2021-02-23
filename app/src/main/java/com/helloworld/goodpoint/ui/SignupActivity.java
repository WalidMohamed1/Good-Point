package com.helloworld.goodpoint.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.helloworld.goodpoint.R;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.io.IOException;
import java.io.ObjectInputValidation;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Pattern;
import android.util.Patterns;

import org.intellij.lang.annotations.RegExp;

public class SignupActivity extends AppCompatActivity {
    private TextView DateT;
    private TextInputLayout tilUserName, tilEmail, tilPassword, tilCity, tilPhone;
    private EditText UserName, Email, Password, Phone;
    AutoCompleteTextView city;
    private DatePickerDialog.OnDateSetListener DateSet;
    private int year, month, Day;
    private ImageView image;
    Button CreateAccount;
    List<String> list;
    Bitmap Bitmap_Image ; Uri imageUri;
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[0-9])" +         //at least 1 digit
                    "(?=.*[a-z])" +         //at least 1 lower case letter
                    "(?=.*[A-Z])" +         //at least 1 upper case letter
                    //"(?=.*[a-zA-Z])" +      //any letter
                    //"(?=.*[@#$%^&+=])" +    //at least 1 special character
                    //"(?=\\S+$)" +           //no white spaces
                    ".{8,}" +               //at least 4 characters
                    "$");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        //getSupportActionBar().setTitle("Sing up");
        inti();
        if(savedInstanceState != null )
        {
            Bitmap_Image = savedInstanceState.getParcelable("BitmapImage");
            if(Bitmap_Image != null){
                image.setImageBitmap(Bitmap_Image);
            }
        }
        DateT.setOnClickListener(new View.OnClickListener() {
            // @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                DatePickerDialog dialog = new DatePickerDialog(
                        SignupActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        DateSet,
                        year, month, Day
                );
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }

        });
        DateSet = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int y, int m, int d) {
                m++;
                if (y > year || (y == year && m - 1 > month)|| (y == year && m - 1 == month && d > Day) ) {
                    String Date = d + "/" + m + "/" + y;
                    //Toast.makeText(SignupActivity.this, Date, Toast.LENGTH_SHORT).show();
                    String todayDate = Day + "/" + (month + 1) + "/" + year;
                    DateT.setText(todayDate);
                    FancyToast.makeText(SignupActivity.this,"Invalid date",FancyToast.LENGTH_LONG, FancyToast.ERROR,false).show();
                }
                else {
                    //Toast.makeText(SignupActivity.this, "hi", Toast.LENGTH_SHORT).show();
                    String Date = d + "/" + m + "/" + y;
                    DateT.setText(Date);
                }
            }
        };
    CreateAccount.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
           // Toast.makeText(SignupActivity.this, "", Toast.LENGTH_SHORT).show();
            if(!confirmInput(view) ){
                startActivity(new Intent(SignupActivity.this,check_registration.class));
                finish();
            }
        }
    });

    }



    protected void inti() {
        UserName = findViewById(R.id.edName);
        Email = findViewById(R.id.edEmail);
        Password = findViewById(R.id.edPass);
        city = findViewById(R.id.edCity);
        Phone =findViewById(R.id.edPhone);

        tilUserName = findViewById(R.id.tilName);
        tilEmail = findViewById(R.id.tilEmail);
        tilPassword = findViewById(R.id.tilPass);
        tilCity = findViewById(R.id.tilcity);
        tilPhone =findViewById(R.id.tilPhone);

        DateT = findViewById(R.id.Date);
        image = findViewById(R.id.im);
        registerForContextMenu(image);
        CreateAccount = findViewById(R.id.createAccount);
        Calendar cal = Calendar.getInstance();//To get today's date
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH);
        Day = cal.get(Calendar.DAY_OF_MONTH);
        String TodayDate = Day + "/" + (month + 1) + "/" + year;
        DateT.setText(TodayDate);/**/
        prepareList List = new prepareList();
        list = List.prepareList(this);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
        city.setThreshold(1);
        city.setAdapter(adapter);
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.menu_main, menu);
        Toast.makeText(this, "onCreateContextMenu", Toast.LENGTH_SHORT);
        //menu.getItem(2).setEnabled(false);
        menu.findItem(R.id.Delete).setVisible(false);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_capture:
                //Toast.makeText(this,"Hello!!",Toast.LENGTH_SHORT).show();
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (cameraIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(cameraIntent, 10);
                }
                break;
            case R.id.action_choose:
                //Toast.makeText(this,"Hello",Toast.LENGTH_SHORT).show();
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(gallery, 11);
                break;
        }

        return super.onContextItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10 && resultCode == RESULT_OK) {
            Bitmap_Image = (Bitmap) data.getExtras().get("data");
            image.setImageBitmap(Bitmap_Image);
        }
        if (requestCode == 11 && resultCode == RESULT_OK) {
            imageUri = data.getData();
            try {
                Bitmap_Image = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
            } catch (IOException e) {
                Toast.makeText(this,"Dina",Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
            image.setImageURI(imageUri);
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
    public boolean isAlpha(String name) {
        char[] chars = name.toCharArray();

        for (char c : chars) {
            if(!Character.isLetter(c) | c !=' ' ) {
                return false;
            }
        }

        return true;
    }/**/
    private boolean validateUsername() {
        String usernameInput = UserName.getText().toString().trim();
        if (usernameInput.isEmpty()) {
            UserName.setError("Field can't be empty");
            return false;
        } else if (usernameInput.length() > 15) {
            UserName.setError("Username too long");
            return false;
        }else if (isAlpha(usernameInput)) {
            UserName.setError("Using only Letters");
            return false;
        } else {
            UserName.setError(null);
            return true;
        }
    }
    private boolean find_Digit(String s){
        String n ;

        for(int i = 1; i < s.length(); i++){
            n = s.substring(i-1,i);
            if(n.matches("[0-9]"))
                return true;
        }

        return false;
    }
    private boolean validatePassword() {
        String passwordInput = Password.getText().toString().trim();
        if (passwordInput.isEmpty()) {
            tilPassword.setError("Field can't be empty");
            return false;
        } else if (!PASSWORD_PATTERN.matcher(passwordInput).matches()) {
            /*if(!find_Digit(passwordInput) )
                tilPassword.setError("must contain at least 1 digit");
            else if(!passwordInput.matches("[a-z]"))
                tilPassword.setError("must contain at least 1 lower case letter");
            else if(!passwordInput.matches("[A-Z]"))
                tilPassword.setError("must contain at least 1 upper case letter");
            else*/
            tilPassword.setError("Must contains digits, lower&upper case letters and length > 8");
            return false;
        } else {
            tilPassword.setError(null);
            return true;
        }
    }
    private boolean validateCity() {
        String cityInput = city.getText().toString().trim();
        if (cityInput.isEmpty()) {
            city.setError("Field can't be empty");
            return false;
        } else if (!list.contains(cityInput)) {
            city.setError("Please Enter valid city!");
            return false;
        } else {
            city.setError(null);
            return true;
        }
    }//
    private boolean validatePhone() {
        String pInput = Phone.getText().toString().trim();
        if (pInput.isEmpty()) {
            Phone.setError("Field can't be empty");
            return false;
        } else if (pInput.length() != 11) {
            Phone.setError("Please enter a valid phone number");
            return false;
        } else {
            /*try{
                int i = Integer.parseInt(pInput);
                tilPhone.setError("Please enter a valid phone number");
            }
            catch (Exception e){
                tilPhone.setError(null);
            }*/
            Phone.setError(null);
            return true;
        }
    }//| !validatePhone()
    public boolean confirmInput(View v) {
        if (!validateEmail() | !validateUsername() | !validatePassword() | !validatePhone() |!validateCity()) {
            return true;
        }
        /*String input = "Email: " + Email.getText().toString();
        input += "\n";
        input += "Username: " + UserName.getText().toString();
        input += "\n";
        input += "Password: " + Password.getText().toString();
        Toast.makeText(this, input, Toast.LENGTH_SHORT).show();*/
        return false;
    }/**/


    protected void prepareList() {
        list = new ArrayList<>();
        list.add(getString(R.string.Cairo));
        list.add(getString(R.string.Alexandria));
        list.add(getString(R.string.ShubraElKheima));
        list.add(getString(R.string.Giza));
        list.add(getString(R.string.PortSaid));
        list.add(getString(R.string.Suez));
        list.add(getString(R.string.ElMahallaElKubra));
        list.add(getString(R.string.Luxor));
        list.add(getString(R.string.Mansoura));
        list.add(getString(R.string.Tanta));
        list.add(getString(R.string.Asyut));
        list.add(getString(R.string.Ismailia));
        list.add(getString(R.string.Faiyum));
        list.add(getString(R.string.Zagazig));
        list.add(getString(R.string.Damietta));
        list.add(getString(R.string.Aswan));
        list.add(getString(R.string.Minya));
        list.add(getString(R.string.BeniSuef));
        list.add(getString(R.string.Hurghada));
        list.add(getString(R.string.Qena));
        list.add(getString(R.string.Sohag));
        list.add(getString(R.string.ShibinElKom));
        list.add(getString(R.string.Banha));
        list.add(getString(R.string.Arish));

    }

    public void onSaveInstanceState(@NonNull Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putParcelable("BitmapImage",Bitmap_Image);
    }


}