package com.midnight.healthcare.API;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by TUSK.ONE on 9/3/16.
 */
public class LoginResponse {
    @SerializedName("response")
    @Expose
    User user;

    @SerializedName("error")
    @Expose
    private String error;

    public User getUser() {
        return user;
    }

    public String getError() {
        return error;
    }
}
