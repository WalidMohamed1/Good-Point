package com.helloworld.goodpoint.ui.select_multiple_faces;

import android.graphics.Bitmap;

import java.io.Serializable;

public class SubItemList implements Serializable {
    private Bitmap subItemImage;

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    private int pos;


    public SubItemList(Bitmap subItemImage,int pos) {
        this.subItemImage = subItemImage;
        this.pos=pos;
    }


    public Bitmap getSubItemImage() {
        return subItemImage;
    }

    public void setSubItemImage(Bitmap subItemImage) {
        this.subItemImage = subItemImage;
    }

}
