package com.midnight.healthcare.API;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by TUSK.ONE on 9/3/16.
 */
public class SubscribeAddResponse {
    @SerializedName("response")
    @Expose
    String type;

    @SerializedName("error")
    @Expose
    private String error;

    public String getUser() {
        return type;
    }

    public String getError() {
        return error;
    }
}
