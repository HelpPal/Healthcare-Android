package com.midnight.healthcare.API;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by TUSK.ONE on 9/7/16.
 */
public class ApplicationsIndResponse {
    @SerializedName("response")
    List<AplictaionInd> response;

    public List<AplictaionInd> getResponse() {
        return response;
    }
}
