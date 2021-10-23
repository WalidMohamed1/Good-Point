package com.helloworld.goodpoint.pojo;

import android.graphics.Bitmap;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class User {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("birthdate")
    @Expose
    private String birthdate;
    @SerializedName("profile_pic")
    @Expose
    private String profile_pic;
    @SerializedName("id_card_pic")
    @Expose
    private String id_card_pic;
    @SerializedName("losts")
    @Expose
    List<Integer>losts;
    @SerializedName("founds")
    @Expose
    List<Integer>founds;
    private Bitmap profile_bitmap;

    List<FoundItem> foundItem;
    private static User user;
    public static User getUser()
    {
        if (user == null) {
            user = new User();
            user.losts = new ArrayList<>();
            user.founds = new ArrayList<>();
            user.foundItem = new ArrayList<>();
        }
        return user;
    }

    public static void userLogout(){
        user = null;
    }

    public List<Integer> getLosts() {
        return losts;
    }

    public List<Integer> getFounds() {
        return founds;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) { this.birthdate = birthdate; }

    public String getProfile_pic() { return profile_pic; }

    public void setProfile_pic(String profile_pic) { this.profile_pic = profile_pic; }

    public Bitmap getProfile_bitmap() {
        return profile_bitmap;
    }

    public void setProfile_bitmap(Bitmap profile_bitmap) {
        this.profile_bitmap = profile_bitmap;
    }

    public String getId_card_pic() {
        return id_card_pic;
    }

    public void setId_card_pic(String id_card_pic) {
        this.id_card_pic = id_card_pic;
    }
}
