package com.midnight.healthcare.API;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by TUSK.ONE on 9/8/16.
 */
public class MessegesResponse  {


    @SerializedName("response")
    List<Messegess> response;


    public List<Messegess> getResponse() {
        return response;
    }
}
