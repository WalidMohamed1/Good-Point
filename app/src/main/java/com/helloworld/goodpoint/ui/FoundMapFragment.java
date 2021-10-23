package com.helloworld.goodpoint.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.helloworld.goodpoint.R;
import com.helloworld.goodpoint.pojo.ObjectLocation;
import com.helloworld.goodpoint.pojo.RegUser;
import com.helloworld.goodpoint.pojo.User;
import com.helloworld.goodpoint.pojo.UserMap;
import com.helloworld.goodpoint.retrofit.ApiClient;
import com.helloworld.goodpoint.retrofit.ApiInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.graphics.Color.BLUE;

public class FoundMapFragment extends Fragment implements GoogleMap.OnMarkerClickListener, View.OnClickListener {

    private static final int MAP_CODE = 1;
    private static final int CALL_CODE = 2;
    private static final int CHECK_LOCATION_ENABLED_CODE = 3;
    SupportMapFragment mapFragment;
    FusedLocationProviderClient client;
    Location curLocation;
    String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    List<ObjectLocation> list;
    Map<Marker,Integer>marker_id;
    AlertDialog.Builder dialog;
    TextView name,email,phone;
    Button call,mail;
    String emailAddress;

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        @SuppressLint("MissingPermission")
        @Override
        public void onMapReady(GoogleMap googleMap) {
            LatLng curLatLng = new LatLng(curLocation.getLatitude(), curLocation.getLongitude());

            googleMap.setMyLocationEnabled(true);
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(curLatLng, 15));

            //list = getLocations();

            for(ObjectLocation object: list) {
                Marker marker = googleMap.addMarker(new MarkerOptions().position(new LatLng(object.getLatitude(), object.getLongitude())));
                marker.setIcon(BitmapDescriptorFactory.defaultMarker(30));
                marker_id.put(marker,object.getUserId());
            }

            googleMap.setOnMarkerClickListener(FoundMapFragment.this);

        }
    };

    /*
    private List<ObjectLocation> getLocations() {
        List<ObjectLocation>ret = new ArrayList<>();
        Random random = new Random();
        for(int i=0;i<100;i++)
            ret.add(new ObjectLocation(25+random.nextDouble()*10,22+random.nextDouble()*10,i));
        return ret;
    }
    */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getPoints();

    }

    public boolean locationEnable() {
        final LocationManager manager = (LocationManager) getContext().getSystemService(getContext().LOCATION_SERVICE);

        return manager.isProviderEnabled(LocationManager.GPS_PROVIDER) || manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS),CHECK_LOCATION_ENABLED_CODE);
                        //startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    private void runGoogleMap() {
        if (ActivityCompat.checkSelfPermission(getContext(), permissions[0]) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(), permissions[1]) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(permissions, MAP_CODE);
        }else if(!locationEnable()){
            buildAlertMessageNoGps();
        }else {
            init();
            client.getLastLocation()/*.addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(@NonNull Location location) {
                    curLocation = location;
                    if (mapFragment != null) {
                        mapFragment.getMapAsync(callback);
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            })*/.addOnCompleteListener(new OnCompleteListener<Location>() {
                @SuppressLint("MissingPermission")
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    Location location = task.getResult();
                    if(location != null) {
                        curLocation = location;
                        if (mapFragment != null) {
                            mapFragment.getMapAsync(callback);
                        }
                    }else{
                        LocationRequest locationRequest = new LocationRequest()
                                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                                .setInterval(10000)
                                .setFastestInterval(1000)
                                .setNumUpdates(1);
                        LocationCallback locationCallback = new LocationCallback(){
                            @Override
                            public void onLocationResult(LocationResult locationResult) {
                                Location location = locationResult.getLastLocation();
                                curLocation = location;
                                if (mapFragment != null) {
                                    mapFragment.getMapAsync(callback);
                                }
                            }
                        };
                        client.requestLocationUpdates(locationRequest,locationCallback, Looper.myLooper());
                    }
                }
            });
        }
    }

    private void init() {
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        client = LocationServices.getFusedLocationProviderClient(getContext());
        marker_id = new HashMap<>();
        dialog = new AlertDialog.Builder(getContext());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode){
            case MAP_CODE:
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    runGoogleMap();
                }else {
                    Toast.makeText(getContext(), "You should allow to access location", Toast.LENGTH_SHORT).show();
                }
                break;
            case CALL_CODE:
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    callHim();
                }else {
                    Toast.makeText(getContext(), "You should allow to access call", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        int id = marker_id.get(marker);
        getUserMap(id);
        Log.e("MYTAG",id+"");
        return false;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.call_button:
                callHim();
                break;
            case R.id.mail_button:
                sendEmail();
                break;
        }
    }

    private void callHim() {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:"+phone.getText().toString()));
        if(ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, CALL_CODE);
            return;
        }
        startActivity(callIntent);
    }

    private void sendEmail() {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:?subject=Lost object&to="+emailAddress));
        try {
            startActivity(Intent.createChooser(emailIntent,"Send mail..."));
        }catch (ActivityNotFoundException e){
            Log.e("MY_TAG",e.getMessage());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case CHECK_LOCATION_ENABLED_CODE:
                runGoogleMap();
                break;
        }
    }

    public void getPoints()
    {

        ApiInterface apiInterface = ApiClient.getApiClient(new PrefManager(getContext()).getNGROKLink()).create(ApiInterface.class);
        Call<List<ObjectLocation>> call = apiInterface.getPoint();
        call.enqueue(new Callback<List<ObjectLocation>>() {
            @Override
            public void onResponse(Call<List<ObjectLocation>> call, Response<List<ObjectLocation>> response) {
                list = response.body();
                runGoogleMap();
//                Toast.makeText(getContext(), ""+response.body().get(0).getLatitude(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<List<ObjectLocation>> call, Throwable t) {

            }
        });
    }

    public  void getUserMap(int id)
    {
        ApiInterface apiInterface = ApiClient.getApiClient(new PrefManager(getContext()).getNGROKLink()).create(ApiInterface.class);
        Call<UserMap> call2 = apiInterface.getUserMap(id);
        call2.enqueue(new Callback<UserMap>() {
            @Override
            public void onResponse(Call<UserMap> call2, Response<UserMap> response) {

                LayoutInflater inflater = LayoutInflater.from(getContext());
                View v = inflater.inflate(R.layout.custom_map_dialog,null);
                name = v.findViewById(R.id.name_of_founder);
                name.setText(response.body().getName());
                email = v.findViewById(R.id.mail_of_founder);
                email.setText(response.body().getEmail());
                emailAddress = email.getText().toString();
                phone = v.findViewById(R.id.phone_of_founder);
                phone.setText(response.body().getPhone());
                call = v.findViewById(R.id.call_button);
                call.setOnClickListener(FoundMapFragment.this);
                mail = v.findViewById(R.id.mail_button);
                mail.setOnClickListener(FoundMapFragment.this);
                dialog.setView(v);
                dialog.setCancelable(true);
                dialog.create().show();


//                Toast.makeText(getContext(), response.body().getName(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<UserMap> call, Throwable t) {

            }
        });
    }
}