package com.midnight.healthcare.API;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by TUSK.ONE on 9/6/16.
 */
public class JobResponse {
    @SerializedName("response")
    List<Jobs> list;


    public List<Jobs> getList() {
        return list;
    }
}
