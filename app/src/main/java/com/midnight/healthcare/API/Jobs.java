package com.midnight.healthcare.API;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by TUSK.ONE on 9/5/16.
 */
public class Jobs implements Serializable {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("lat")
    @Expose
    private String lat;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("hours")
    @Expose
    private String hours;
    @SerializedName("min_price")
    @Expose
    private String minPrice;
    @SerializedName("max_price")
    @Expose
    private String maxPrice;
    @SerializedName("min_year")
    @Expose
    private String minYear;
    @SerializedName("max_year")
    @Expose
    private String maxYear;
    @SerializedName("repate")
    @Expose
    private String repate;
    @SerializedName("avalable")
    @Expose
    private String avalable;
    @SerializedName("information")
    @Expose
    private String information;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("hidden")
    @Expose
    private String hidden;
    @SerializedName("byUser")
    @Expose
    private String byUser;
    @SerializedName("time_desc")
    @Expose
    private String time_desc;
    @SerializedName("distance")
    @Expose
    private String distance;

    @SerializedName("myRating")
    @Expose
    private Float myRating;
    @SerializedName("userRating")
    @Expose
    private Float userRating;

    public Float getMyRating() {
        return myRating;
    }

    public Float getUserRating() {
        return userRating;
    }

    public String getTime_desc() {
        return time_desc;
    }

    public String getDistance() {
        return distance;
    }

    public JobLocation getLocation() {
        return location;
    }

    @SerializedName("location")
    @Expose
    private JobLocation location;



    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getLat() {
        return lat;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getHours() {
        return hours;
    }

    public String getMinPrice() {
        return minPrice;
    }

    public String getMaxPrice() {
        return maxPrice;
    }

    public String getMinYear() {
        return minYear;
    }

    public String getMaxYear() {
        return maxYear;
    }

    public String getRepate() {
        return repate;
    }

    public String getAvalable() {
        return avalable;
    }

    public String getInformation() {
        return information;
    }

    public String getDate() {
        return date;
    }

    public String getHidden() {
        return hidden;
    }

    public String getByUser() {
        return byUser;
    }

    @SerializedName("days")
    @Expose

    private List<Day> days = new ArrayList<Day>();

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }

    public void setMinPrice(String minPrice) {
        this.minPrice = minPrice;
    }

    public void setMaxPrice(String maxPrice) {
        this.maxPrice = maxPrice;
    }

    public void setMinYear(String minYear) {
        this.minYear = minYear;
    }

    public void setMaxYear(String maxYear) {
        this.maxYear = maxYear;
    }

    public void setRepate(String repate) {
        this.repate = repate;
    }

    public void setAvalable(String avalable) {
        this.avalable = avalable;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setHidden(String hidden) {
        this.hidden = hidden;
    }

    public void setByUser(String byUser) {
        this.byUser = byUser;
    }

    public void setTime_desc(String time_desc) {
        this.time_desc = time_desc;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public void setLocation(JobLocation location) {
        this.location = location;
    }

    public List<Day> getDays() {
        return days;
    }

    public void setDays(List<Day> days) {
        this.days = days;
    }
}
