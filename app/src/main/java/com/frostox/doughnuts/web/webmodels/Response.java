package com.frostox.doughnuts.web.webmodels;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Roger Cores on 1/8/16.
 */
public class Response {
    private Integer code;

    private String error;

    @SerializedName("error_description")
    private String errorDescription;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }
}
