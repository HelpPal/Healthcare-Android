package com.midnight.healthcare.API;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by TUSK.ONE on 9/7/16.
 */
public class AplictaionInd {
    @SerializedName("job")
    @Expose
    private Jobs job;
    @SerializedName("application")
    @Expose
    private Applications application;
    @SerializedName("user")
    @Expose
    private Nurse user;

    public Jobs getJob() {
        return job;
    }

    public Applications getApplication() {
        return application;
    }

    public Nurse getUser() {
        return user;
    }
}
