package com.helloworld.goodpoint.ui;

import android.graphics.Bitmap;

import java.io.Serializable;

public class sub implements Serializable {
    private Bitmap subItemImage;

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    private int pos;


    public sub(Bitmap subItemImage,int pos) {
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
