package com.midnight.healthcare.API;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by TUSK.ONE on 9/5/16.
 */
public class Day implements Serializable{
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("day")
    @Expose
    private String day;
    @SerializedName("job_id")
    @Expose
    private String jobId;

    public String getId() {
        return id;
    }

    public String getDay() {
        return day;
    }

    public String getJobId() {
        return jobId;
    }

    public void setDay(String day) {
        this.day = day;
    }
}
