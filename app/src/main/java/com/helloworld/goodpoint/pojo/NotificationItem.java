package com.helloworld.goodpoint.pojo;

import android.content.Intent;
import android.graphics.Bitmap;

import java.util.Date;

public class NotificationItem {
    String title;
    Date date;
    String description;
    Bitmap image;
    boolean read;
    Intent intent;

    public NotificationItem(String title, Date date, String description, Bitmap image) {
        this.title = title;
        this.date = date;
        this.description = description;
        this.image = image;
        this.read = false;
    }

    public String getTitle() {
        return title;
    }

    public Date getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public Bitmap getImage() {
        return image;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public Intent getIntent() {
        return intent;
    }
}
