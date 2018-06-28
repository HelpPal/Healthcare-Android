package com.midnight.healthcare.API;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by TUSK.ONE on 9/6/16.
 */
public class JobLocation implements Serializable {
    @SerializedName("city")
    private String city;
    @SerializedName("state")
    private String state;

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setState(String state) {
        this.state = state;
    }
}
