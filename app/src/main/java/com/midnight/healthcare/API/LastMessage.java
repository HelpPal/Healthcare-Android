package com.midnight.healthcare.API;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by TUSK.ONE on 9/8/16.
 */
public class LastMessage implements Serializable {
    @SerializedName("text")
    String text;
    @SerializedName("time")
    String time;

    @SerializedName("user")
    String user;

    public String getUser() {
        return user;
    }

    public String getText() {
        return text;
    }

    public String getTime() {
        return time;
    }
}
