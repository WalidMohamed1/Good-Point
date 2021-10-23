package com.helloworld.goodpoint.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RegUser {

    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("first_name")
    @Expose
    private String first_name;
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


    public RegUser(String username, String password, String first_name, String phone, String city, String birthdate, String profile_pic) {
        this.username = username;
        this.password = password;
        this.first_name = first_name;
        this.phone = phone;
        this.city = city;
        this.birthdate = birthdate;
        this.profile_pic = profile_pic;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
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

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    /*public String getProfile_pic() { return profile_pic; }

    public void setProfile_pic(String profile_pic) { this.profile_pic = profile_pic; }*/
}
