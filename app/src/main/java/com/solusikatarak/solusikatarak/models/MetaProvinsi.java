package com.solusikatarak.solusikatarak.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MetaProvinsi {
    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("semuaprovinsi")
    @Expose
    private List<Semuaprovinsi> semuaprovinsi = null;

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Semuaprovinsi> getSemuaprovinsi() {
        return semuaprovinsi;
    }

    public void setSemuaprovinsi(List<Semuaprovinsi> semuaprovinsi) {
        this.semuaprovinsi = semuaprovinsi;
    }
}
