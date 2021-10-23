package com.helloworld.goodpoint.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.util.Size;
import android.view.TextureView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.common.MlKitException;
import com.google.mlkit.common.model.LocalModel;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.common.internal.ImageConvertUtils;
import com.google.mlkit.vision.objects.DetectedObject;
import com.google.mlkit.vision.objects.ObjectDetection;
import com.google.mlkit.vision.objects.ObjectDetector;
import com.google.mlkit.vision.objects.custom.CustomObjectDetectorOptions;
import com.helloworld.goodpoint.R;
import com.helloworld.goodpoint.utils.Draw;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ID_cardDetection extends AppCompatActivity {
private ObjectDetector objectDetector;
private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
private androidx.camera.view.PreviewView previewView;
private RelativeLayout relativeLayout;
private ImageButton IDCard;
private boolean flag = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_i_d_card_detection);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED)
        {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA},20);
        }
        previewView = findViewById(R.id.PV);
        relativeLayout = findViewById(R.id.ParentLayout);
        IDCard = findViewById(R.id.IDCard);
        cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                bindPreview(cameraProvider);
            } catch (ExecutionException | InterruptedException e) {
                Log.e("ESRAA", "ExecutionException: "+e.getMessage());
            }
        }, ContextCompat.getMainExecutor(this));
        LocalModel localModel = new LocalModel.Builder()
                .setAssetFilePath("objectDetection.tflite").build();
        CustomObjectDetectorOptions customObjectDetectorOptions = new CustomObjectDetectorOptions.Builder(localModel)
                .setDetectorMode(CustomObjectDetectorOptions.STREAM_MODE)
                .enableClassification()
                .setClassificationConfidenceThreshold(0.5f)
                .setMaxPerObjectLabelCount(1)
                .build();
        objectDetector = ObjectDetection.getClient(customObjectDetectorOptions);

    }
    private void bindPreview(@NonNull ProcessCameraProvider cameraProvider) {
        Log.e("ESRAA", "IamHERE");
        Preview preview = new Preview.Builder().build();
        TextureView mTextureView = new TextureView(this);
        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();
        preview.setSurfaceProvider(previewView.getSurfaceProvider());
        ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                .setTargetResolution(new Size(480,480))
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST).build();
        imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(this),
                image -> {
                    int rotationDegrees = image.getImageInfo().getRotationDegrees();

                    @SuppressLint("UnsafeOptInUsageError") Image im= image.getImage();
                    if(im != null)
                    {
                        InputImage processImage= InputImage.fromMediaImage(im,rotationDegrees);
                        InputImage i = InputImage.fromMediaImage(im, image.getImageInfo().getRotationDegrees());
                        try {
                            GlobalVar.realcameraIdCard = ImageConvertUtils.getInstance().getUpRightBitmap(i);
                        } catch (MlKitException e) {
                            e.printStackTrace();
                        }
                        objectDetector.process(processImage)
                                .addOnSuccessListener(detectedObjects -> {
                                    if(detectedObjects.size() == 0)
                                    {
                                        if (relativeLayout.getChildCount() > 1)
                                            relativeLayout.removeViewAt(1);
                                        String text ="There's no card found";
                                        Draw D = new Draw(ID_cardDetection.this,text);
                                        D.init(2);
                                        relativeLayout.addView(D);
                                        IDCard.setImageResource(R.drawable.ic_baseline_photo_camera_24);
                                        flag = false;
                                    }
                                    else {
                                        for (DetectedObject detectedObject : detectedObjects) {
                                            String text;
                                            detectedObject.getLabels().contains("Driver's license");
                                            for (DetectedObject.Label label : detectedObject.getLabels()) {
                                                if (relativeLayout.getChildCount() > 1)
                                                    relativeLayout.removeViewAt(1);
                                                if (label.getText().equals("Driver's license")) {
                                                    text = "There's card found";
                                                    Draw D = new Draw(ID_cardDetection.this, text);
                                                    D.init(1);
                                                    relativeLayout.addView(D);
                                                    Log.e("ESRAA", "Object detected: " + text + "; ");
                                                    IDCard.setImageResource(R.drawable.ic_realcam);
                                                    flag = true;
                                                } else {
                                                    text = "There's no card found";
                                                    Draw D = new Draw(ID_cardDetection.this, text);
                                                    D.init(2);
                                                    relativeLayout.addView(D);
                                                    IDCard.setImageResource(R.drawable.ic_baseline_photo_camera_24);
                                                    flag = false;
                                                }
                                            }
                                        }
                                    }
                                    image.close();
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.e("ESRAA", "onFailure: "+e.getMessage());
                                        image.close();
                                    }
                                });
                    }
                }
        );
        cameraProvider.bindToLifecycle((LifecycleOwner)this, cameraSelector,imageAnalysis, preview);
    }

    public void check_there(View view) {
           if(flag)
           {
               Intent My = new Intent();
               setResult(RESULT_OK,My);
               finish();
           }
           else {
               Toast.makeText(this,"There's no card found",Toast.LENGTH_SHORT).show();
           }
    }
}