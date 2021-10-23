package com.helloworld.goodpoint.ui;

import android.graphics.Bitmap;

import com.helloworld.goodpoint.pojo.LostItem;
import com.helloworld.goodpoint.ui.candidate.SubItem;
import com.helloworld.goodpoint.ui.select_multiple_faces.SubItemList;

import java.util.ArrayList;
import java.util.List;

public class GlobalVar {
    public static  List<List<Bitmap>> allFaces = new ArrayList<>();
    public static  Bitmap realcameraImage ;
    public static  Bitmap realcameraIdCard ;
    public static  List<Bitmap> ImgThatHaveMoreThanOneFace = new ArrayList<>();
    public static  List<Bitmap> FinialFacesThatWillGoToDataBase = new ArrayList<>();
    public static  int flag;
    public static  List<SubItemList> slist;
    public static  List<SubItem> sublist;
    public static  int position;
    public static  int p;
    public static  List<String> losts;
    public static  List<String> founds;
    public static  List<LostItem> lostList;
    public static  List<String> percentList;
    public static  String type;


}
