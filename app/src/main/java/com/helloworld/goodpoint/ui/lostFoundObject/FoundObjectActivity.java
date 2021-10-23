package com.helloworld.goodpoint.ui.lostFoundObject;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.util.SparseArray;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.app.ActivityCompat;
import androidx.loader.content.CursorLoader;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;
import com.google.gson.JsonObject;
import com.helloworld.goodpoint.R;
import com.helloworld.goodpoint.pojo.FoundItem;
import com.helloworld.goodpoint.pojo.FoundPerson;
import com.helloworld.goodpoint.pojo.LostItem;
import com.helloworld.goodpoint.pojo.NotificationItem;
import com.helloworld.goodpoint.pojo.User;
import com.helloworld.goodpoint.retrofit.ApiClient;
import com.helloworld.goodpoint.retrofit.ApiInterface;
import com.helloworld.goodpoint.ui.Alert;
import com.helloworld.goodpoint.ui.GlobalVar;
import com.helloworld.goodpoint.ui.PrefManager;
import com.helloworld.goodpoint.ui.prepareList;
import com.shashank.sony.fancytoastlib.FancyToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FoundObjectActivity extends AppCompatActivity implements View.OnClickListener, objectDataType {
    private TextView DateFound;
    private EditText Location;
    private DatePickerDialog.OnDateSetListener DateSet;
    private int year, month, Day;
    private Button Person;
    private Button Object;
    private Button MatchFound;
    private prepareList List;
    private List<String> listColor;
    private Fragment PersonF, ObjectF;
    private String location, City;
    private String ObjectColor, Serial, brand, textArea_information, Type;
    private String PName;
    private ProgressBar progressbar;
    private WifiManager wifiManager;
    private final static int PLACE_PICKER_REQUEST = 999;
    private List<Bitmap> Person_Images;
    double Latitude;
    double Longitude;
    private FaceDetector faceDetector;
    FusedLocationProviderClient fusedLocationProviderClient;
    private boolean flagPerson, flagObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_found_object);
        Calendar cal = Calendar.getInstance();//To get today's date
        inti();
        if (savedInstanceState != null) {
            year = savedInstanceState.getInt("year");
            month = savedInstanceState.getInt("month");
            Day = savedInstanceState.getInt("Day");
            flagPerson = savedInstanceState.getBoolean("flagPerson");
            flagObject = savedInstanceState.getBoolean("flagObject");
            if (flagPerson == true) {
                Person.setTextColor(0xFFF38E3A);
                Object.setTextColor(Color.BLACK);
            } else if (flagObject == true) {
                Object.setTextColor(0xFFF38E3A);
                Person.setTextColor(Color.BLACK);
            }
        } else {
            year = cal.get(Calendar.YEAR);
            month = cal.get(Calendar.MONTH);
            Day = cal.get(Calendar.DAY_OF_MONTH);
        }
        //String todayDate = year + "/" + (month + 1) + "/" + Day;
        String todayDate = year + "-" + (month + 1) + "-" + Day;
        DateFound.setText(todayDate);

        DateSet = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int y, int m, int d) {
                m++;
                if (y > year || (m - 1 > month && y >= year) || (d > Day && m - 1 >= month && y >= year)) {
                    FancyToast.makeText(FoundObjectActivity.this, "Invalid date", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                    //String todayDate = year + "/" + (month + 1) + "/" + Day;
                    String todayDate = year + "-" + (month + 1) + "-" + Day;
                    DateFound.setText(todayDate);
                } else {
                    year = y;
                    month = m - 1;
                    Day = d;
                    //String Date = y + "/" + m + "/" + d;
                    String Date = y + "-" + m + "-" + d;
                    DateFound.setText(Date);
                }
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(FoundObjectActivity.this);
        if (ActivityCompat.checkSelfPermission(FoundObjectActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(FoundObjectActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(FoundObjectActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 12);
        } else {
            getCurrentLocation();
        }
    }

    @Override
    public void onClick(View view) {
        FragmentManager FM = getFragmentManager();
        FragmentTransaction FT = FM.beginTransaction();
        switch (view.getId()) {
            case R.id.DateFound:
                DatePickerDialog dialog = new DatePickerDialog(
                        FoundObjectActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        DateSet,
                        year, month, Day
                );
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
                break;
            case R.id.FoundLocatin:
                PopupMenu popupMenu = new PopupMenu(this, view);
                popupMenu.getMenuInflater().inflate(R.menu.choose_location, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (ActivityCompat.checkSelfPermission(FoundObjectActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                                && ActivityCompat.checkSelfPermission(FoundObjectActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(FoundObjectActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 12);
                        } else {
                            switch (item.getItemId()) {
                                case R.id.TakeCurrLocation:
                                    isInternetAvailable Available = new isInternetAvailable();
                                    Available.execute();
                                    CurrentLocation Locate = new CurrentLocation();
                                    Locate.execute();
                                    break;
                                case R.id.DeteLocation:
                                    boolean flag = false;
                                    wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                                    if (wifiManager.isWifiEnabled())
                                        wifiManager.setWifiEnabled(false);
                                    else {
                                        flag = true;
                                    }
                                    PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                                    try {
                                        Intent intent = builder.build(FoundObjectActivity.this);
                                        startActivityForResult(intent, PLACE_PICKER_REQUEST);
                                        if (!flag) wifiManager.setWifiEnabled(true);
                                    } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                                        Toast.makeText(FoundObjectActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                    } catch (Exception e) {
                                        Toast.makeText(FoundObjectActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                    break;
                            }
                        }
                        return true;
                    }
                });
                popupMenu.show();
                break;
            case R.id.PersonFound:
                flagPerson = true;
                flagObject = false;
                FT.replace(R.id.FragmentFoundID, PersonF, null);
                Person.setTextColor(0xFFF38E3A);
                Object.setTextColor(Color.BLACK);

                FT.commit();
                break;
            case R.id.ObjectFound:
                flagObject = true;
                flagPerson = false;
                FT.replace(R.id.FragmentFoundID, ObjectF, null);
                Object.setTextColor(0xFFF38E3A);
                Person.setTextColor(Color.BLACK);

                FT.commit();
                break;
            case R.id.MatchFound:
                GlobalVar.allFaces.clear();
                if (!flagObject && !flagPerson) {
                    FancyToast.makeText(this, "Specify the type of the missing object", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                } else if (flagObject && CheckMatchObject()) {
                    FoundItems();
                    FoundItem item =new FoundItem(Type,Serial,brand,ObjectColor);
                    getItems(item,getApplicationContext());
                    FancyToast.makeText(this, "The data has been saved successfully", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();
                    //finish();
                } else if (flagPerson && CheckMatchPerson()) {
                    faceDetector = new FaceDetector.Builder(this)
                            .setTrackingEnabled(false)
                            .setLandmarkType(FaceDetector.ALL_LANDMARKS)
                            .setMode(FaceDetector.FAST_MODE).build();
                    if (!faceDetector.isOperational()) {
                        Toast.makeText(this, "Face Detection can't be setup", Toast.LENGTH_SHORT).show();
                    }
                    checkFaces N = new checkFaces(this);
                    N.execute();
                }
                //FoundItem item=new FoundItem(Type,Serial,brand,ObjectColor);
                //getItems(item,this);
                break;
        }
    }

    private boolean CheckMatchPerson() {
        EditText PersonName = PersonF.getView().findViewById(R.id.PersonName);
        PName = PersonName.getText().toString();
        location = Location.getText().toString();
        if (location.isEmpty()) {
            FancyToast.makeText(this, "Specify where you found this object", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
            return false;
        } else if (Person_Images.size() == 0) {
            FancyToast.makeText(this, "You must put at least one picture!", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
            return false;
        }
        return true;
    }

    class checkFaces extends AsyncTask<Void, Void, Void> {
        AlertDialog.Builder builder;
        AlertDialog dialog;
        Context context;

        private checkFaces(Context context) {
            this.context = context.getApplicationContext();
            builder = new AlertDialog.Builder(FoundObjectActivity.this);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            builder.setCancelable(false);
            View view = getLayoutInflater().inflate(R.layout.progress_bar_alert, null);
            builder.setView(view);
            dialog = builder.create();
            dialog.show();
        }

        @Override
        protected void onPostExecute(Void a) {
            super.onPostExecute(a);
            Log.e("img", "onPostExecute: " + GlobalVar.ImgThatHaveMoreThanOneFace.size() + "  " + GlobalVar.FinialFacesThatWillGoToDataBase.size());
            if (GlobalVar.allFaces.size() > 0) {
                FoundPerson.getFoundPerson().setName(PName);
                FoundPerson.getFoundPerson().setDate(DateFound.getText().toString().trim());
                FoundPerson.getFoundPerson().setCity(City);
                FoundPerson.getFoundPerson().setLongitude(Longitude);
                FoundPerson.getFoundPerson().setLatitude(Latitude);
                GlobalVar.flag=1;
                startActivity(new Intent(FoundObjectActivity.this, Alert.class));
                finish();

            } else {
                FoundPerson();
            }
            dialog.dismiss();

        }

        @Override
        protected Void doInBackground(Void... voids) {
            GlobalVar.ImgThatHaveMoreThanOneFace.clear();
            GlobalVar.FinialFacesThatWillGoToDataBase.clear();
            GlobalVar.allFaces.clear();
            boolean flag = false;
            for (int i = 0; i < Person_Images.size(); i++) {
                Bitmap My = Person_Images.get(i);
                Bitmap faceBitmap;
                List<Bitmap> faces = new ArrayList<>();//In one Img;
                Frame frame = new Frame.Builder().setBitmap(My).build();
                SparseArray<Face> sparseArray = faceDetector.detect(frame);
                for (int j = 0; j < sparseArray.size(); j++) {
                    flag = false;
                    Face face = sparseArray.valueAt(j);
                    if (((int) face.getPosition().y + (int) face.getHeight()) > My.getHeight()) {
                        int H = My.getHeight() - (int) face.getPosition().y;
                        faceBitmap = Bitmap.createBitmap(My, (int) face.getPosition().x, (int) face.getPosition().y, (int) face.getWidth(), H);
                    } else if (((int) face.getPosition().x + (int) face.getWidth()) > My.getWidth()) {
                        int W = My.getWidth() - (int) face.getPosition().x;
                        faceBitmap = Bitmap.createBitmap(My, (int) face.getPosition().x, (int) face.getPosition().y, W, (int) face.getHeight());
                    } else if ((((int) face.getPosition().x + (int) face.getWidth()) > My.getWidth()) && (((int) face.getPosition().y + (int) face.getHeight()) > My.getHeight())) {
                        int H = My.getHeight() - (int) face.getPosition().y;
                        int W = My.getWidth() - (int) face.getPosition().x;
                        faceBitmap = Bitmap.createBitmap(My, (int) face.getPosition().x, (int) face.getPosition().y, W, H);
                    } else {
                        faceBitmap = Bitmap.createBitmap(My, (int) face.getPosition().x, (int) face.getPosition().y, (int) face.getWidth(), (int) face.getHeight());

                    }
                    if (sparseArray.size() == 1) {
                        GlobalVar.FinialFacesThatWillGoToDataBase.add(faceBitmap);
                        flag = true;
                    } else {
                        faces.add(faceBitmap);
                    }
                }
                if (!flag) {
                    GlobalVar.ImgThatHaveMoreThanOneFace.add(My);
                    GlobalVar.allFaces.add(faces);
                }

            }
            return null;
        }
    }

    private boolean CheckMatchObject() {
        location = Location.getText().toString();
        AutoCompleteTextView V = ObjectF.getView().findViewById(R.id.ColorOfObject);
        EditText serialObject = ObjectF.getView().findViewById(R.id.Serial);
        EditText brandObject = ObjectF.getView().findViewById(R.id.brand);
        EditText textArea_informationObject = ObjectF.getView().findViewById(R.id.textArea_information);
        EditText TypeObject;

        ObjectColor = V.getText().toString();
        Serial = serialObject.getText().toString();
        brand = brandObject.getText().toString();
        textArea_information = textArea_informationObject.getText().toString();
        if (location.isEmpty()) {
            FancyToast.makeText(this, "Specify where you found this object", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
            return false;
        } else if (Type.equals("Type")) {
            FancyToast.makeText(this, "You must Choose the Type!", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
            return false;
        } else if (Type.equals("Others")) {
            TypeObject = ObjectF.getView().findViewById(R.id.Other);
            if (TypeObject.getText().toString().isEmpty()) {
                TypeObject.setError("Field can't be empty");
                return false;
            } else {
                Type = TypeObject.getText().toString();
            }
        } else if (brand.isEmpty()) {
            brandObject.setError("Field can't be empty");
            return false;
        } else if (ObjectColor.isEmpty()) {
            V.setError("Field can't be empty");
            return false;
        } else if (!listColor.contains(ObjectColor.trim())) {
            V.setError("Color isn't known!");
            return false;
        } else if (textArea_information.isEmpty()) {
            textArea_informationObject.setError("Field can't be empty");
            return false;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_PICKER_REQUEST && resultCode == RESULT_OK) {
            Place place = PlacePicker.getPlace(data, this);
            StringBuilder stringBuilder = new StringBuilder();
            Latitude = place.getLatLng().latitude;
            Longitude = place.getLatLng().longitude;
            isInternetAvailable Available = new isInternetAvailable();
            Available.execute();
            CurrentLocation Locate = new CurrentLocation();
            Locate.execute();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 12 && (grantResults.length > 0) &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation();
        } else if (requestCode == 12 && (grantResults.length > 0) &&
                grantResults[0] == PackageManager.PERMISSION_DENIED) {

            FancyToast.makeText(this, "Permission denied", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
        } else if (requestCode == 11 && (grantResults.length > 0) &&
                grantResults[0] == PackageManager.PERMISSION_DENIED) {
            FancyToast.makeText(this, "Permission denied", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
        }

    }

    @SuppressLint("MissingPermission")
    private void getCurrentLocation() {
        LocationManager locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        ) {
            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @SuppressLint("MissingPermission")
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    Location location = task.getResult();
                    if (location != null) {
                        Longitude = location.getLongitude();
                        Latitude = location.getLatitude();
                    } else {
                        @SuppressLint("RestrictedApi") LocationRequest locationRequest = new LocationRequest()
                                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                                .setInterval(10000)
                                .setFastestInterval(1000)
                                .setNumUpdates(1);
                        LocationCallback locationCallback = new LocationCallback() {
                            @Override
                            public void onLocationResult(LocationResult locationResult) {
                                Location location1 = locationResult.getLastLocation();
                                Longitude = location1.getLongitude();
                                Latitude = location1.getLatitude();
                            }
                        };
                        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());

                    }
                }
            });
        } else {
            //when location servies is not enabled
            //open location setting
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }

    class CurrentLocation extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressbar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String Locate) {
            super.onPostExecute(Locate);
            if (Locate.isEmpty())
                FancyToast.makeText(FoundObjectActivity.this, "An error has occurred , please try again", FancyToast.LENGTH_LONG, FancyToast.WARNING, false).show();
            Location.setText(Locate);
            progressbar.setVisibility(View.GONE);
        }

        @Override
        protected String doInBackground(Void... voids) {
            Geocoder geocoder = new Geocoder(FoundObjectActivity.this, new Locale("en"));
            String Locate = "";
            try {
                List<Address> addresses = geocoder.getFromLocation(Latitude, Longitude, 1);
                String Country = addresses.get(0).getCountryName();
                String CityG = addresses.get(0).getAdminArea();
                int index = CityG.lastIndexOf(' ');
                if(index == -1)
                    City = CityG;
                else
                    City = CityG.substring(0, index);
                String area = addresses.get(0).getLocality();
                Locate = area + "," + CityG + "," + Country + ".";
            } catch (IOException e) {
                Toast.makeText(FoundObjectActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            return Locate;
        }

    }

    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nInfo = cm.getActiveNetworkInfo();
        boolean connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
        return connected;
    }

    class isInternetAvailable extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressbar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            progressbar.setVisibility(View.GONE);
            if (!aBoolean)
                FancyToast.makeText(FoundObjectActivity.this, "No Internet Connection", FancyToast.LENGTH_LONG, FancyToast.WARNING, false).show();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            Boolean flag;
            try {
                String command = "ping -c 1 google.com";
                flag = (Runtime.getRuntime().exec(command).waitFor() == 0);
            } catch (Exception e) {
                flag = false;
            }
            return flag;
        }
    }

    protected void inti() {

        DateFound = findViewById(R.id.DateFound);
        progressbar = findViewById(R.id.ProgressBar);
        Button foundLocatin = findViewById(R.id.FoundLocatin);
        Person = findViewById(R.id.PersonFound);
        Object = findViewById(R.id.ObjectFound);
        MatchFound = findViewById(R.id.MatchFound);
        Location = findViewById(R.id.Location);
        DateFound.setOnClickListener(this);
        foundLocatin.setOnClickListener(this);
        Person.setOnClickListener(this);
        Object.setOnClickListener(this);
        MatchFound.setOnClickListener(this);
        PersonF = new PersonFragment();
        ObjectF = new ObjectFragment();
        List = new prepareList();
        listColor = List.prepareListColor(this);

    }

    @Override
    public void getType(String T) {
        Type = T;
    }

    @Override
    public void getImageCheck(Boolean check) {
    }

    @Override
    public void getBitmap_Image(Bitmap Bitmap_Image) {
    }

    @Override
    public void getBitmap_ImagePersonImages(List<Bitmap> PImages) {
        Person_Images = PImages;
        Log.e("img", "getBitmap_ImagePersonImages: Hi " + Person_Images.size());
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("year", year);
        outState.putInt("month", month);
        outState.putInt("Day", Day);
        outState.putBoolean("flagPerson", flagPerson);
        outState.putBoolean("flagObject", flagObject);
    }


    public void FoundItems() {

        String Datee = DateFound.getText().toString().trim();
        ApiInterface apiInterface = ApiClient.getApiClient(new PrefManager(getApplicationContext()).getNGROKLink()).create(ApiInterface.class);

        Call<JsonObject> call = apiInterface.storeFoundObj(User.getUser().getId(), Datee, City, Longitude , Latitude);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.d("e","responce="+response.body());
                if (response.isSuccessful()) {

                    try {
                        Log.d("e","found="+response.body().toString());
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        String id = jsonObject.getString("id");
                        Toast.makeText(FoundObjectActivity.this, "Object is posted.", Toast.LENGTH_SHORT).show();

                        Call<FoundItem> call2 = apiInterface.storeFoundItem(id, Type, Serial, brand, ObjectColor, textArea_information);
                        call2.enqueue(new Callback<FoundItem>() {
                            @Override
                            public void onResponse(Call<FoundItem> call, Response<FoundItem> response) {
                                if (response.isSuccessful()) {
                                    Toast.makeText(FoundObjectActivity.this, "Item is posted.", Toast.LENGTH_SHORT).show();
                                    User.getUser().getFounds().add(Integer.parseInt(id));
                                    finish();
                                } else {
                                    Toast.makeText(FoundObjectActivity.this, "The item is not posted.", Toast.LENGTH_SHORT).show();
                                }
                            }
                            @Override
                            public void onFailure(Call<FoundItem> call, Throwable t) {
                                Toast.makeText(FoundObjectActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                    } catch (JSONException e) {
                        Toast.makeText(FoundObjectActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                } else
                        Toast.makeText(FoundObjectActivity.this, "Object is not posted.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(FoundObjectActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

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

        String Datee = DateFound.getText().toString().trim();
        ApiInterface apiInterface = ApiClient.getApiClient(new PrefManager(getApplicationContext()).getNGROKLink()).create(ApiInterface.class);

        Uri imageURI = getImageUri(GlobalVar.FinialFacesThatWillGoToDataBase.get(0));
        File file = new File(getRealPathFromURI(imageURI));
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part Pimage = MultipartBody.Part.createFormData("image", file.getName(), requestBody);

        Call<JsonObject> call = apiInterface.storeFoundPerson(User.getUser().getId(), Datee, City, Longitude , Latitude, PName, Pimage);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.isSuccessful()) {
                    FancyToast.makeText(FoundObjectActivity.this, "The data has been saved successfully", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();
                    try {
                        String id = new JSONObject(response.body().toString()).getString("id");
                        } catch (JSONException e) {
                        Log.e("TAG", "onResponse: "+e.getMessage());
                    }
                    finish();
                }else
                    Toast.makeText(FoundObjectActivity.this, "The object is not posted.", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(FoundObjectActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


    }

    public void getItems(FoundItem item, Context context) {
        ApiInterface apiInterface = ApiClient.getApiClient(new PrefManager(context).getNGROKLink()).create(ApiInterface.class);
        Call<List<LostItem>> call = apiInterface.getLItem(item.getType());
        Log.d("tes=",Type);
        GlobalVar.type=Type;
        call.enqueue(new Callback<List<LostItem>>() {
            @Override
            public void onResponse(Call<List<LostItem>> call, Response<List<LostItem>> response) {
                GlobalVar.lostList = new ArrayList<>();
                GlobalVar.lostList = response.body();
                GlobalVar.percentList = new ArrayList<>();
                if (response.body()!=null&&GlobalVar.lostList.size()!=0) {
                    storeCandidatesNotifictation() ;
                    for (int i = 0; i < GlobalVar.lostList.size(); i++) {
                        GlobalVar.percentList.add(MatchItems(item, GlobalVar.lostList.get(i)));
                   }
                } else
                    Toast.makeText(context, "There is no items can be candidates !", Toast.LENGTH_SHORT).show();
            }


            @Override
            public void onFailure(Call<java.util.List<LostItem>> call, Throwable t) {
                Toast.makeText(FoundObjectActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    public String MatchItems(FoundItem item1, LostItem item2) {
        int percentage =20;
        if (item1.getBrand().equals(item2.getBrand()))
            percentage += 20;
        if (item1.getColor().equals(item2.getColor()))
            percentage +=20;
        if (item1.getSerial_number().equals(item2.getSerial_number())) {
         return "100%";
        }
        String p=percentage+"%";
        return p;
    }

public void storeCandidatesNotifictation(){
    ApiInterface apiInterface = ApiClient.getApiClient(new PrefManager(getApplicationContext()).getNGROKLink()).create(ApiInterface.class);
    Call<NotificationItem> call = apiInterface.storeNotification(User.getUser().getId(),"Candidate Items","There are Candidates founded",3);
    call.enqueue(new Callback<NotificationItem>() {
        @Override
        public void onResponse(Call<NotificationItem> call, Response<NotificationItem> response) {
            if (response.isSuccessful()) {
                Toast.makeText(FoundObjectActivity.this, "Notification is posted.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(FoundObjectActivity.this, "The Notification is not posted.", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onFailure(Call<NotificationItem> call, Throwable t) {
            Toast.makeText(FoundObjectActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();

        }
        });
}
}