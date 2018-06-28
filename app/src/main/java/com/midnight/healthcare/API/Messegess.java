package com.midnight.healthcare.API;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by TUSK.ONE on 9/8/16.
 */
public class Messegess implements Serializable {
    @SerializedName("partner")
    private String partner;
    @SerializedName("parner-name")
    String partner_name;
    @SerializedName("last_message")
    LastMessage lastMessage;

    public String getPartner_name() {
        return partner_name;
    }

    @SerializedName("id")

    String id;

    public String getId() {
        return id;
    }

    public String getPartner() {
        return partner;
    }

    public LastMessage getLastMessage() {
        return lastMessage;
    }

    public void setPartner(String partner) {
        this.partner = partner;
    }
}
