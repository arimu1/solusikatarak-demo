package com.solusikatarak.solusikatarak.models;

import com.google.gson.annotations.SerializedName;

public class ResponseAPI {
    @SerializedName("code")
    String code;
    @SerializedName("message")
    String message;

    public String getCode() {
        return code;
    }

    public void setKode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setPesan(String Message) {
        this.message = message;
    }
}
