package com.midnight.healthcare.API;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TUSK.ONE on 9/3/16.
 */
public class User {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("address_lat")
    @Expose
    private String addressLat;
    @SerializedName("address_long")
    @Expose
    private String addressLong;
    @SerializedName("experience")
    @Expose
    private String experience;
    @SerializedName("available_time")
    @Expose
    private String availableTime;
    @SerializedName("price_min")
    @Expose
    private String priceMin;
    @SerializedName("price_max")
    @Expose
    private String priceMax;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("birthday")
    @Expose
    private String birthday;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("profile_img")
    @Expose
    private String profileImg;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("available")
    @Expose
    private String available;
    @SerializedName("years")
    @Expose
    private Integer years;
    @SerializedName("distance")
    @Expose
    private String distance;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("zip")
    @Expose
    private String zip;
    @SerializedName("address")
    @Expose
    private String address;

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getZip() {
        return zip;
    }

    public String getAddress() {
        return address;
    }

    public JobLocation getLocation() {
        return location;
    }

    @SerializedName("location")
    @Expose
    private JobLocation location;
    @SerializedName("skills")
    @Expose
    private List<Skill> skills = new ArrayList<Skill>();

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getType() {
        return type;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getAddressLat() {
        return addressLat;
    }

    public String getAddressLong() {
        return addressLong;
    }

    public String getExperience() {
        return experience;
    }

    public String getAvailableTime() {
        return availableTime;
    }

    public String getPriceMin() {
        return priceMin;
    }

    public String getPriceMax() {
        return priceMax;
    }

    public String getPhone() {
        return phone;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getDescription() {
        return description;
    }

    public String getProfileImg() {
        return profileImg;
    }

    public String getGender() {
        return gender;
    }

    public String getAvailable() {
        return available;
    }

    public Integer getYears() {
        return years;
    }

    public String getDistance() {
        return distance;
    }

    public List<Skill> getSkills() {
        return skills;
    }

}
