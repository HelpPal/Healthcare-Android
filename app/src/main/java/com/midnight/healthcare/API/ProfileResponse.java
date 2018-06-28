package com.midnight.healthcare.API;

import com.google.gson.annotations.SerializedName;

/**
 * Created by tolea on 16.10.16.
 */

public class ProfileResponse {

    @SerializedName("response")
    private Nurse response;

    public Nurse getResponse() {
        return response;
    }
}
