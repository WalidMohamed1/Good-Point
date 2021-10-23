package com.helloworld.goodpoint.ui.candidate;


import java.util.List;

public class Item {

    private String itemTitle;
    private List<SubItem> subItem;

    public String getItemTitle() {
        return itemTitle;
    }

    public void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
    }

    public List<SubItem> getSubItem() {
        return subItem;
    }

    public void setSubItem(List<SubItem> subItem) {
        this.subItem = subItem;
    }

    public Item(String itemTitle, List<SubItem> subItem) {
        this.itemTitle = itemTitle;
        this.subItem = subItem;
    }
}
