package com.helloworld.goodpoint.ui.select_multiple_faces;


import android.graphics.Bitmap;

import java.util.List;

public class ItemList {

    private Bitmap ItemImage;
    private List<SubItemList> subItemList;

    public ItemList(Bitmap ItemImage, List<SubItemList> subItemList) {
        this.subItemList = subItemList;
        this.ItemImage = ItemImage;
    }


    public List<SubItemList> getSubItemList() {
        return subItemList;
    }

    public void setSubItemList(List<SubItemList> subItemList) {
        this.subItemList = subItemList;
    }

    public Bitmap getItemImage() {
        return ItemImage;
    }

    public void setItemImage(Bitmap itemImage) {
        ItemImage = itemImage;
    }

}
