package com.midnight.healthcare.Models;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TUSK.ONE on 8/29/16.
 */
public class RegisterModel {
    int type;
    String password = "";
    String email = "";
    String firstName = "";
    String lastName = "";


    String lat = "";
    String longitudine = "";
    private String address = "";
    private String addressToShow = "";
    private String city = "";
    private String state = "";
    private String zipToShow = "";

    public List<String> getSkilssid() {
        return skilssid;
    }

    public void setSkilssid(List<String> skilssid) {
        this.skilssid = skilssid;
    }

    List<String> skilss = new ArrayList<>();
    List<String> skilssid = new ArrayList<>();
    List<String> otherSkills = new ArrayList<>();

    int expirience = 1;
    int avalable = 1;
    String price_min = "";
    String price_max = "";
    String phone = "";
    int gender = 0;
    Bitmap profile_img;


    Drawable drawableCache;
    String description = "";
    String birthday = "";

    public List<String> getOtherSkills() {
        return otherSkills;
    }

    public void setOtherSkills(List<String> otherSkills) {
        this.otherSkills = otherSkills;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLongitudine() {
        return longitudine;
    }

    public void setLongitudine(String longitudine) {
        this.longitudine = longitudine;
    }

    public List<String> getSkilss() {
        return skilss;
    }

    public void setSkilss(List<String> skilss) {
        this.skilss = skilss;
    }

    public int getExpirience() {
        return expirience;
    }

    public void setExpirience(int expirience) {
        this.expirience = expirience;
    }

    public int getAvalable() {
        return avalable;
    }

    public void setAvalable(int avalable) {
        this.avalable = avalable;
    }

    public String getPrice_min() {
        return price_min;
    }

    public void setPrice_min(String price_min) {
        this.price_min = price_min;
    }

    public String getPrice_max() {
        return price_max;
    }

    public void setPrice_max(String price_max) {
        this.price_max = price_max;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public Bitmap getProfile_img() {
        return profile_img;
    }

    public void setProfile_img(Bitmap profile_img) {
        this.profile_img = profile_img;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddressToShow() {
        return addressToShow;
    }

    public void setAddressToShow(String addressToShow) {
        this.addressToShow = addressToShow;
    }

    public String getZipToShow() {
        return zipToShow;
    }

    public void setZipToShow(String zipToShow) {
        this.zipToShow = zipToShow;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Drawable getDrawableCache() {
        return drawableCache;
    }

    public void setDrawableCache(Drawable drawableCache) {
        this.drawableCache = drawableCache;
    }
}
