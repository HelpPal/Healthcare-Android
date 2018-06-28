package com.midnight.healthcare.API;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by TUSK.ONE on 9/3/16.
 */
public class NurseResponse {

    @SerializedName("response")
    List<Nurse> response;


    public List<Nurse> getResponse() {
        return response;
    }
}
