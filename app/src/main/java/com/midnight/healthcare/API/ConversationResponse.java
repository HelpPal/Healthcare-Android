package com.midnight.healthcare.API;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by TUSK.ONE on 9/8/16.
 */
public class ConversationResponse {

    @SerializedName("response")
    List<LastMessage> response;

    public List<LastMessage> getResponse() {
        return response;
    }
}
