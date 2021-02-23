package com.helloworld.goodpoint.ui.lostFoundObject;

import android.Manifest;
import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.google.android.material.textfield.TextInputLayout;
import com.helloworld.goodpoint.R;
import com.helloworld.goodpoint.ui.prepareList;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.io.IOException;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class ObjectFragment extends Fragment implements AdapterView.OnItemSelectedListener, View.OnClickListener {
   private Spinner spinner;
   public List<String> list;
   private AutoCompleteTextView autoCom;
   private TextInputLayout other;
   private ImageButton objectImageView;
   private  Bitmap Bitmap_Image ;
   private CheckBox checkIcon;
   private Uri imageUri;
   private prepareList List;
   private String Type ;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActivity() instanceof LostObjectDetailsActivity) {
            if (ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 11);

            }
        }
        List= new prepareList();
        list = List.prepareListColor(getActivity().getApplicationContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_object, container, false);
        spinner = v.findViewById(R.id.spinner);
        autoCom = v.findViewById(R.id.ColorOfObject);
        other = v.findViewById(R.id.other);
        checkIcon = v.findViewById(R.id.checkIcon);
        objectImageView = v.findViewById(R.id.objectImageView);
        objectImageView.setOnClickListener(this);

        if (getActivity() instanceof LostObjectDetailsActivity) {
            checkIcon.setVisibility(View.VISIBLE);
            checkIcon.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    //is chkIos checked?
                    if (((CheckBox) v).isChecked()) {
                        objectImageView.setVisibility(View.VISIBLE);
                        ((objectDataType)getActivity()).getImageCheck(true);
                    } else {
                        objectImageView.setVisibility(View.GONE);
                        ((objectDataType)getActivity()).getImageCheck(false);
                    }

                }
            });
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getActivity().getApplicationContext(),
                R.layout.spinner_item,
                getResources().getStringArray(R.array.Types));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        adapter = new ArrayAdapter<>(getActivity().getApplicationContext(),R.layout.spinner_item , list);
        autoCom.setThreshold(1);//start working from first char
        autoCom.setAdapter(adapter);
        // Inflate the layout for this fragment
        return v;
    }
    @Override
    public void onClick(View v) {
        if(v == objectImageView) {
            if (ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 11);
            } else {
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                gallery.setType("image/*");
                if (gallery.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivityForResult(gallery, 1);
                } else
                    FancyToast.makeText(getActivity().getApplicationContext(),"Error",FancyToast.LENGTH_LONG, FancyToast.ERROR,false).show();

            }
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            if(requestCode == 1) {
                    imageUri = data.getData();
                    try {
                        Bitmap_Image = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                        ((objectDataType)getActivity()).getBitmap_Image( Bitmap_Image);
                    } catch (IOException e) {
                        FancyToast.makeText(getActivity().getApplicationContext(),"Error",FancyToast.LENGTH_LONG, FancyToast.ERROR,false).show();
                        e.printStackTrace();
                    }
                    objectImageView.setImageURI(imageUri);
            }
        }
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        int posit = parent.getSelectedItemPosition();
       Type = parent.getItemAtPosition(position).toString();
       ((objectDataType)getActivity()).getType(Type);
        if(posit == 9)
        {
            other.setVisibility(View.VISIBLE);
        }
        else
        {
            other.setVisibility(View.GONE);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

}