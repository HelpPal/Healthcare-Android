package com.midnight.healthcare.API;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by TUSK.ONE on 9/3/16.
 */
public class SkillsResponse {
    @SerializedName("response")
    List<Skill> response;

    public List<Skill> getResponse() {
        return response;
    }
}
