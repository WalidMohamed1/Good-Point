package com.helloworld.goodpoint.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
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
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.helloworld.goodpoint.R;
import com.helloworld.goodpoint.pojo.ObjectLocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class FoundMapFragment extends Fragment implements GoogleMap.OnMarkerClickListener, View.OnClickListener {

    private static final int MAP_CODE = 1;
    private static final int CALL_CODE = 2;
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

            list = getLocations();

            for(ObjectLocation object: list) {
                Marker marker = googleMap.addMarker(new MarkerOptions().position(object.getLatLng()));
                marker_id.put(marker,object.getUserId());
            }

            googleMap.setOnMarkerClickListener(FoundMapFragment.this);

        }
    };

    private List<ObjectLocation> getLocations() {
        List<ObjectLocation>ret = new ArrayList<>();
        Random random = new Random();
        for(int i=0;i<100;i++)
            ret.add(new ObjectLocation(25+random.nextDouble()*10,22+random.nextDouble()*10,i));
        return ret;
    }

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

        runGoogleMap();

    }

    private void runGoogleMap() {
        if (ActivityCompat.checkSelfPermission(getContext(), permissions[0]) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(), permissions[1]) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(permissions, MAP_CODE);
        }else {
            init();
            client.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
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

                break;
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        int id = marker_id.get(marker);
        Log.e("MYTAG",id+"");
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View v = inflater.inflate(R.layout.custom_map_dialog,null);
        name = v.findViewById(R.id.name_of_founder);
        email = v.findViewById(R.id.mail_of_founder);
        emailAddress = email.getText().toString();
        phone = v.findViewById(R.id.phone_of_founder);
        call = v.findViewById(R.id.call_button);
        call.setOnClickListener(this);
        mail = v.findViewById(R.id.mail_button);
        mail.setOnClickListener(this);
        dialog.setView(v);
        dialog.setCancelable(true);
        dialog.create().show();
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
}