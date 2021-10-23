package com.helloworld.goodpoint.ui.lostFoundObject;

import android.graphics.Bitmap;
import android.net.Uri;

import java.util.List;

public interface objectDataType {
    public void getType(String Type);
    public void getImageCheck(Boolean check);
    public void getBitmap_Image(Bitmap Bitmap_Image);
    public void getBitmap_ImagePersonImages(List<Bitmap> Bitmap_Images);
}
