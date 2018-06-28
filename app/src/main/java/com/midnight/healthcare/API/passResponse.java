package com.midnight.healthcare.API;

import com.google.gson.annotations.SerializedName;

/**
 * Created by tolea on 19.10.16.
 */

public class passResponse {

    @SerializedName("response")
    private String response;

    @SerializedName("error")
    private String error;


    public String getResponse() {
        return response;
    }

    public String getError() {
        return error;
    }
}
