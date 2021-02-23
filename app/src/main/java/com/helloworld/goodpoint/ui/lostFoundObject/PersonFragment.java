package com.helloworld.goodpoint.ui.lostFoundObject;

import android.Manifest;
import android.app.Fragment;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.app.ActivityCompat;

import com.helloworld.goodpoint.R;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class PersonFragment extends Fragment implements View.OnClickListener {
    private ImageButton imageView;
    private ImageView imageView2;
    private List<Bitmap> bitmap  = new ArrayList<>();
    private Uri photoFromGallery;
    private LinearLayout linearLayout,ADDP;
    private LayoutInflater inflater2;
    private View rootView;
    private Button Close,add_new__photo;
    private int nmberOfImageSelected;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},11);

        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((objectDataType)getActivity()).getBitmap_ImagePersonImages(bitmap);
        rootView = inflater.inflate(R.layout.fragment_person, container, false);
        imageView = rootView.findViewById(R.id.imageView);
        ADDP = rootView.findViewById(R.id.ADDP);
        add_new__photo= rootView.findViewById(R.id.add_new__photo);
        linearLayout = (LinearLayout) rootView.findViewById(R.id.Gallery2);
        inflater2 = LayoutInflater.from(getActivity());
        add_new__photo.setOnClickListener(this);
        return rootView;

    }
    @Override
    public void onClick(View view) {
        if(view == add_new__photo) {
            if (ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(),  Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 11);
            }
            else{
            if (getActivity() instanceof FoundObjectActivity) {
                PopupMenu popupMenu = new PopupMenu(getActivity(), view);
                popupMenu.getMenuInflater().inflate(R.menu.choose_photo, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.TakePhoto:
                                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                if (i.resolveActivity(getActivity().getPackageManager()) != null) {
                                    startActivityForResult(i, 10);
                                } else
                                    FancyToast.makeText(getActivity().getApplicationContext(),"Error",FancyToast.LENGTH_LONG, FancyToast.ERROR,false).show();
                                break;
                            case R.id.Gallery:
                                Intent pickPhoto = new Intent(Intent.ACTION_GET_CONTENT,
                                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                pickPhoto.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                                pickPhoto.setType("image/*");//accept any type of images
                                if (pickPhoto.resolveActivity(getActivity().getPackageManager()) != null) {
                                    startActivityForResult(pickPhoto, 1);
                                } else
                                    FancyToast.makeText(getActivity().getApplicationContext(),"Error",FancyToast.LENGTH_LONG, FancyToast.ERROR,false).show();

                                break;

                        }
                        return true;
                    }
                });
                popupMenu.show();
            } else if (getActivity() instanceof LostObjectDetailsActivity) {
                Intent pickPhoto = new Intent(Intent.ACTION_GET_CONTENT,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickPhoto.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                pickPhoto.setType("image/*");//accept any type of images
                if (pickPhoto.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivityForResult(pickPhoto, 1);
                } else
                    FancyToast.makeText(getActivity().getApplicationContext(),"Error",FancyToast.LENGTH_LONG, FancyToast.ERROR,false).show();
            }
        }
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ADDP.setVisibility(View.VISIBLE);
        if(resultCode==RESULT_OK&&data!=null)
        {
            switch(requestCode) {
                case 10:
                    bitmap.add((Bitmap) data.getExtras().get("data"));
                    imageView.setImageBitmap(bitmap.get(bitmap.size()-1));
                    ((objectDataType)getActivity()).getBitmap_ImagePersonImages(bitmap);
                    break;
                case 1:
                    try {
                        ClipData clipData = data.getClipData();
                        if(clipData != null)
                        {
                            if(clipData.getItemCount()>10)
                            {
                                Toast toast =  FancyToast.makeText(getActivity().getApplicationContext(),"You cannot choose more than 10 images",FancyToast.LENGTH_LONG, FancyToast.ERROR,false);
                                toast.setGravity(Gravity.BOTTOM,0,0);
                                toast.show();
                                for(int i = 0; i<10;i++)
                                {
                                    photoFromGallery = clipData.getItemAt(i).getUri();
                                    bitmap.add(MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), photoFromGallery));
                                }
                            }
                            else {
                                for (int i = 0; i < clipData.getItemCount(); i++) {
                                    photoFromGallery = clipData.getItemAt(i).getUri();
                                    bitmap.add(MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), photoFromGallery));
                                }
                            }
                        }
                        else{
                            photoFromGallery = data.getData();
                            bitmap.add(MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), photoFromGallery));
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
            }
            ((objectDataType)getActivity()).getBitmap_ImagePersonImages(bitmap);
            if(linearLayout.getChildCount() == 0) {
                for (int i = 0; i < bitmap.size(); i++) {
                    View view = inflater2.inflate(R.layout.images, linearLayout, false);
                    imageView2 = (ImageView) view.findViewById(R.id.imageView2);
                    Close =  (Button)view.findViewById(R.id.Close);
                    Close.setBackgroundColor(0x80F38E3A);
                    imageView2.setImageBitmap(bitmap.get(i));
                    linearLayout.addView(view);
                }
            }
            else
            {
                if((linearLayout.getChildCount()+bitmap.size())>10)
                {
                   Toast toast = FancyToast.makeText(getActivity().getApplicationContext(),"You cannot choose more than 10 images",FancyToast.LENGTH_LONG, FancyToast.ERROR,false);
                   toast.setGravity(Gravity.BOTTOM,0,0);
                   toast.show();
                    for (int i = linearLayout.getChildCount(); i < 10; i++) {
                        View view = inflater2.inflate(R.layout.images, linearLayout, false);
                        imageView2 = view.findViewById(R.id.imageView2);
                        Close =   (Button)view.findViewById(R.id.Close);
                        Close.setBackgroundColor(0x80F38E3A);
                        imageView2.setImageBitmap(bitmap.get(i));
                        linearLayout.addView(view);

                    }
                }
                else {
                    for (int i = linearLayout.getChildCount(); i < bitmap.size(); i++) {
                        View view = inflater2.inflate(R.layout.images, linearLayout, false);
                        imageView2 = view.findViewById(R.id.imageView2);
                        Close = (Button) view.findViewById(R.id.Close);
                        Close.setBackgroundColor(0x80F38E3A);
                        imageView2.setImageBitmap(bitmap.get(i));
                        linearLayout.addView(view);

                    }
                }
            }
        }
        setOnClickListeners();
    }
    private void setOnClickListeners() {
        for (int index = 0; index < linearLayout.getChildCount(); index++) {
            final int finalIndex1 = index;
            View view = linearLayout.getChildAt(index);
            Close = view.findViewById(R.id.Close);
            imageView2 = view.findViewById(R.id.imageView2);
            Close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    bitmap.remove(finalIndex1);
                    ((objectDataType)getActivity()).getBitmap_ImagePersonImages(bitmap);
                    if(nmberOfImageSelected == finalIndex1)
                    {
                        imageView.setVisibility(View.GONE);
                        imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_gallery_24));
                    }
                    linearLayout.removeViewAt(finalIndex1);
                    setOnClickListeners();
                }
            });
            imageView2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    imageView2 = view.findViewById(R.id.imageView2);
                    imageView.setVisibility(View.VISIBLE);
                    imageView.setImageDrawable(imageView2.getDrawable());
                    nmberOfImageSelected = finalIndex1;
                }
            });

         }
    }
}