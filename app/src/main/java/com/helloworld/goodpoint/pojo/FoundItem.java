package com.helloworld.goodpoint.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FoundItem {
    public FoundItem(String type, String serial_number, String brand, String color) {
        this.type = type;
        this.serial_number = serial_number;
        this.brand = brand;
        this.color = color;
    }

    @SerializedName("user_id")
    @Expose
    private String user_id;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("serial_number")
    @Expose
    private String serial_number;
    @Expose
    @SerializedName("brand")
    private String brand;
    @SerializedName("color")
    @Expose
    private String color;
    @SerializedName("description")
    @Expose
    private String description;


   /* public FoundItem(int user_id, int id, String date, String city, String type, String serial_number, String brand, String color, String description) {
        this.id = id;
        this.user_id = user_id;
        this.date = date;
        this.city = city;
        this.type = type;
        this.serial_number = serial_number;
        this.brand = brand;
        this.color = color;
        this.description = description;
    }*/


    private static FoundItem FoundItem;
    public static FoundItem getFoundItem()
    {
        if (FoundItem == null) {
            FoundItem = new FoundItem();

        }
        return FoundItem;
    }
    private FoundItem()
    {

    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() { return date; }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSerial_number() {
        return serial_number;
    }

    public void setSerial_number(String serial_number) {
        this.serial_number = serial_number;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
