package com.frostox.doughnuts.web.webmodels;

import com.google.gson.annotations.SerializedName;

/**
 * Created by bloss on 2/8/16.
 */
public class CreatedResponse {


    @SerializedName("_id")
    private String id;

    @SerializedName("__v")
    private int version;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }


}
