package com.helloworld.goodpoint.ui.lostFoundObject;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.widget.PopupMenu;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;
import com.helloworld.goodpoint.R;
import com.helloworld.goodpoint.detection.DetectorActivity;
import com.helloworld.goodpoint.ui.GlobalVar;
import com.shashank.sony.fancytoastlib.FancyToast;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class PersonFragment extends Fragment implements View.OnClickListener {
    private ImageButton imageView;
    private ImageView imageView2;
    private List<Bitmap> bitmap  = new ArrayList<>();
    private LinearLayout linearLayout,ADDP;
    private LayoutInflater inflater2;
    private Uri photoFromGallery;
    private View rootView;
    private Button Close,add_new__photo;
    private ProgressBar CheckImages;
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
        CheckImages = rootView.findViewById(R.id.CheckImages);
        CheckImages.setVisibility(View.GONE);
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
                                Intent i = new Intent(getActivity().getApplicationContext(), DetectorActivity.class);
                               startActivityForResult(i,10);
                               /* Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                if (i.resolveActivity(getActivity().getPackageManager()) != null) {
                                    startActivityForResult(i, 10);
                                } else
                                    FancyToast.makeText(getActivity().getApplicationContext(),"Error",FancyToast.LENGTH_LONG, FancyToast.ERROR,false).show();*/
                                break;
                            case R.id.Gallery:

                                   Intent pickPhoto = new Intent(Intent.ACTION_GET_CONTENT,
                                           MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                   pickPhoto.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                                   pickPhoto.setType("image/*");//accept any type of images
                                   if (pickPhoto.resolveActivity(getActivity().getPackageManager()) != null) {
                                       startActivityForResult(pickPhoto, 1);
                                   } else
                                       FancyToast.makeText(getActivity().getApplicationContext(), "Error", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
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
    int NumOfImgSelected ;
   int BitMapSize = bitmap.size();
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        BitMapSize = bitmap.size();
        ADDP.setVisibility(View.VISIBLE);
        if(resultCode==RESULT_OK&&data!=null)
        {
            switch(requestCode) {
                case 10: {
                    if (bitmap.size() >= 10) {
                        FancyToast.makeText(getActivity().getApplicationContext(), "You cannot choose more than 10 images", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                    }
                    else {
                        if(GlobalVar.realcameraImage != null)
                        { bitmap.add(GlobalVar.realcameraImage) ;  NumOfImgSelected = 1;}

                    }
                break;
                  }
                case 1:
                    try {
                        ClipData clipData = data.getClipData();
                        if(clipData != null)
                        {
                            NumOfImgSelected = clipData.getItemCount();
                            for (int i = 0; i < clipData.getItemCount(); i++) {
                                photoFromGallery = clipData.getItemAt(i).getUri();
                                if (bitmap.size() >= 10) {
                                    Toast toast = FancyToast.makeText(getActivity().getApplicationContext(), "You cannot choose more than 10 images", FancyToast.LENGTH_LONG, FancyToast.ERROR, false);
                                    toast.setGravity(Gravity.BOTTOM, 0, 0);
                                    toast.show();

                                    break;
                                }
                                bitmap.add(MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), photoFromGallery));
                            }
                        }
                        else{
                            NumOfImgSelected = 1;
                            photoFromGallery = data.getData();
                            if(bitmap.size()>=10) {
                                Toast toast =  FancyToast.makeText(getActivity().getApplicationContext(),"You cannot choose more than 10 images",FancyToast.LENGTH_LONG, FancyToast.ERROR,false);
                                toast.setGravity(Gravity.BOTTOM,0,0);
                                toast.show();
                            }
                            else {
                                bitmap.add(MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), photoFromGallery));
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
            ((objectDataType)getActivity()).getBitmap_ImagePersonImages(bitmap);
            if(BitMapSize != bitmap.size()) {
                checkIfAllImagesContainFacesOrNot N = new checkIfAllImagesContainFacesOrNot();
                N.execute(bitmap);
            }
        }
    }
    class checkIfAllImagesContainFacesOrNot extends AsyncTask<List<Bitmap>,Void, List<Bitmap>>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            CheckImages.setVisibility(View.VISIBLE);
            add_new__photo.setVisibility(View.GONE);
        }
        @Override
        protected void onPostExecute(List<Bitmap> ImgNotHaveFaces) {
            super.onPostExecute(ImgNotHaveFaces);
            CheckImages.setVisibility(View.GONE);
            add_new__photo.setVisibility(View.VISIBLE);
            if(ImgNotHaveFaces.size()>0) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setCancelable(false);
                @SuppressLint({"NewApi", "LocalSuppress"})
                View view = getLayoutInflater().inflate(R.layout.images_be_removed, null);
                LinearLayout RemovedImg = view.findViewById(R.id.RemovedImg);
                if (ImgNotHaveFaces.size() > 1)
                    builder.setMessage("These " + ImgNotHaveFaces.size() + " images do not contain any faces so they will be removed");
                else
                    builder.setMessage("This image do not contain any faces so it will be removed");

                for (int i = 0; i < ImgNotHaveFaces.size(); i++) {
                    @SuppressLint({"NewApi", "LocalSuppress"})
                    View view2 = getLayoutInflater().inflate(R.layout.images, null);
                    (view2.findViewById(R.id.imageView2)).setVisibility(View.GONE);
                    (view2.findViewById(R.id.Close)).setVisibility(View.GONE);
                    ImageView imageView = view2.findViewById(R.id.faces);
                    imageView.setImageBitmap(ImgNotHaveFaces.get(i));
                    RemovedImg.addView(view2);
                }
                builder.setView(view).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.e("img","  Finished I Now Have "+ImgNotHaveFaces.size() +" img ");
                        for (int i = linearLayout.getChildCount(); i < bitmap.size(); i++) {
                            View view = inflater2.inflate(R.layout.images, linearLayout, false);
                            imageView2 = view.findViewById(R.id.imageView2);
                            Close =   (Button)view.findViewById(R.id.Close);
                            Close.setBackgroundColor(0x80F38E3A);
                            imageView2.setImageBitmap(bitmap.get(i));
                            linearLayout.addView(view);

                        }
                        setOnClickListeners();
                        ImgNotHaveFaces.clear();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();

            }
            else
            {
                for (int i = linearLayout.getChildCount(); i < bitmap.size(); i++) {
                    View view = inflater2.inflate(R.layout.images, linearLayout, false);
                    imageView2 = view.findViewById(R.id.imageView2);
                    Close =   (Button)view.findViewById(R.id.Close);
                    Close.setBackgroundColor(0x80F38E3A);
                    imageView2.setImageBitmap(bitmap.get(i));
                    linearLayout.addView(view);

                }
                setOnClickListeners();
            }

        }
        @Override
        protected List<Bitmap> doInBackground(List<Bitmap>... bitmap) {
            List<Bitmap> ImgNotHaveFaces = new ArrayList<>();
            int counter = NumOfImgSelected;
            int index = bitmap[0].size();
            while(counter > 0 && index > 0) {
                Bitmap My = bitmap[0].get(--index);
                FaceDetector faceDetector = new FaceDetector.Builder(getActivity())
                        .setTrackingEnabled(false)
                        .setLandmarkType(FaceDetector.ALL_LANDMARKS)
                        .setMode(FaceDetector.FAST_MODE).build();
                if (!faceDetector.isOperational()) {
                    Toast.makeText(getActivity().getApplicationContext(), "Face Detection can't be setup", Toast.LENGTH_SHORT).show();
                }
                else {
                    Frame frame = new Frame.Builder().setBitmap(My).build();
                    SparseArray<Face> sparseArray = faceDetector.detect(frame);
                    Log.e("Camera", "doInBackground22: Esraa " + sparseArray.size());
                    if(sparseArray.size()==0) {

                        ImgNotHaveFaces.add(bitmap[0].get(index));
                        bitmap[0].remove(index);
                        Log.e("img", "I removed Image number " + (index) );
                    }
                }
                counter--;

            }
            ((objectDataType)getActivity()).getBitmap_ImagePersonImages(bitmap[0]);
            return ImgNotHaveFaces;
        }
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