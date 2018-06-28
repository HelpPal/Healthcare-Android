package com.midnight.healthcare.API;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by TUSK.ONE on 9/3/16.
 */
public class Skill implements Serializable{
    @SerializedName("name")
    String name;

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    @SerializedName("id")
    String id;

    public boolean isOtherSkill() {
        return otherSkill;
    }

    public void setOtherSkill(boolean otherSkill) {
        this.otherSkill = otherSkill;
    }

    boolean otherSkill;
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
