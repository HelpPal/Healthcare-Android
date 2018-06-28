package com.midnight.healthcare.API;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by TUSK.ONE on 9/7/16.
 */
public class Applications {
    @SerializedName("id")
    @Expose
    private String id;

    public String getId() {
        return id;
    }

    public String getUserid() {
        return userid;
    }

    public String getJobId() {
        return jobId;
    }

    public String getType() {
        return type;
    }

    public String getResult() {
        return result;
    }

    public String getDate() {
        return date;
    }

    @SerializedName("userid")

    @Expose
    private String userid;
    @SerializedName("job_id")
    @Expose
    private String jobId;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("result")
    @Expose
    private String result;
    @SerializedName("date")
    @Expose
    private String date;
}
