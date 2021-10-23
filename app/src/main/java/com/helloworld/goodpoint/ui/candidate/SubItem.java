package com.helloworld.goodpoint.ui.candidate;

public class SubItem {
    private String subItemTitle;
    private String subItemDes;
    private String persent;
    private int pos;
    private String id;

    public SubItem(String subItemTitle, String subItemDes, String persent, int pos, String id) {
        this.subItemTitle = subItemTitle;
        this.subItemDes = subItemDes;
        this.persent = persent;
        this.pos = pos;
        this.id = id;
    }

    public String getSubItemTitle() {
        return subItemTitle;
    }

    public void setSubItemTitle(String subItemTitle) {
        this.subItemTitle = subItemTitle;
    }

    public String getSubItemDes() {
        return subItemDes;
    }

    public void setSubItemDes(String subItemDes) {
        this.subItemDes = subItemDes;
    }

    public String getPersent() {
        return persent;
    }

    public void setPersent(String persent) {
        this.persent = persent;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
